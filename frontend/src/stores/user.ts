import { defineStore } from 'pinia';
import { ref, computed } from 'vue';
import type { LoginRequest, LoginResponse } from '../types';
import { login, register, getPermission } from '../api/user';
import { ElMessage } from 'element-plus';

export const useUserStore = defineStore('user', () => {
  const token = ref(localStorage.getItem('token') || '');
  const user = ref<LoginResponse['user'] | null>(null);
  const isLoggedIn = computed(() => !!token.value && !!user.value);
  const isAdmin = computed(() => user.value?.role === 0);
  const isTeacher = computed(() => user.value?.role === 1);
  const isStudent = computed(() => user.value?.role === 2);

  async function fetchUserPermission() {
    try {
      const response = await getPermission();
      if (response.code === 200 && response.data !== undefined && response.data !== null) {
        const data = response.data as any;
        if (typeof data === 'object' && data.role !== undefined) {
          user.value = {
            id: data.id || 0,
            username: data.account || data.username || '',
            realName: data.realName || '',
            role: data.role,
          };
        } else {
          user.value = {
            id: 0,
            username: '',
            realName: '',
            role: data,
          };
        }
        localStorage.setItem('user', JSON.stringify(user.value));
        return true;
      }
    } catch (error) {
      console.error('获取权限失败:', error);
    }
    logout();
    return false;
  }

  async function handleLogin(data: LoginRequest) {
    const response = await login(data);
    if (response.code === 200) {
      let tokenValue = (response as any).msg || (response as any).data || response.message;
      if (typeof tokenValue !== 'string') {
        tokenValue = `temp-token-${Date.now()}`;
      }
      token.value = tokenValue;
      localStorage.setItem('token', tokenValue);
      
      const success = await fetchUserPermission();
      if (success) {
        return true;
      }
    }
    return false;
  }

  async function handleRegister(data: { account: string; password: string; email: string; permission: number }) {
    const response = await register(data);
    return response.code === 200;
  }

  function logout() {
    token.value = '';
    user.value = null;
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    window.location.href = '/login';
  }

  async function initUser() {
    const savedToken = localStorage.getItem('token');
    const savedUser = localStorage.getItem('user');
    
    if (savedToken) {
      token.value = savedToken;
      if (savedUser) {
        try {
          user.value = JSON.parse(savedUser);
          return;
        } catch {
          user.value = null;
        }
      }
      await fetchUserPermission();
    }
  }

  function setRole(role: number) {
    if (user.value) {
      user.value.role = role;
      localStorage.setItem('user', JSON.stringify(user.value));
    }
  }

  return {
    token,
    user,
    isLoggedIn,
    isAdmin,
    isTeacher,
    isStudent,
    handleLogin,
    handleRegister,
    logout,
    initUser,
    setRole,
    fetchUserPermission,
  };
});