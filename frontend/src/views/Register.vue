<script setup lang="ts">
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { useUserStore } from '../stores/user';
import { ElMessage } from 'element-plus';

const router = useRouter();
const userStore = useUserStore();

const form = ref({
  account: '',
  password: '',
  confirmPassword: '',
  email: '',
  permission: undefined as number | undefined,
});

const loading = ref(false);

async function handleRegister() {
  if (!form.value.account || !form.value.password || !form.value.email) {
    ElMessage.warning('请填写账号、密码和邮箱');
    return;
  }

  if (form.value.password !== form.value.confirmPassword) {
    ElMessage.error('两次密码不一致');
    return;
  }

  if (form.value.permission === undefined) {
    ElMessage.warning('请选择身份（学生或老师）');
    return;
  }

  loading.value = true;
  const success = await userStore.handleRegister({
    account: form.value.account,
    password: form.value.password,
    email: form.value.email,
    permission: form.value.permission,
  });
  loading.value = false;

  if (success) {
    ElMessage.success('注册成功，请登录');
    router.push('/login');
  } else {
    ElMessage.error('注册失败');
  }
}

function goToLogin() {
  router.push('/login');
}
</script>

<template>
  <div class="min-h-screen bg-gradient-to-br from-blue-900 via-blue-800 to-blue-900 flex items-center justify-center p-4">
    <div class="w-full max-w-md">
      <div class="bg-white/10 backdrop-blur-lg rounded-2xl shadow-2xl p-8 border border-white/20">
        <div class="text-center mb-8">
          <div class="w-16 h-16 bg-gradient-to-r from-green-500 to-green-600 rounded-xl flex items-center justify-center mx-auto mb-4">
            <svg class="w-8 h-8 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M18 9v3m0 0v3m0-3h3m-3 0h-3m-2-5a4 4 0 11-8 0 4 4 0 018 0zM3 20a6 6 0 0112 0v1H3v-1z" />
            </svg>
          </div>
          <h1 class="text-3xl font-bold text-white">用户注册</h1>
          <p class="text-blue-200 mt-2">创建您的考试系统账号</p>
        </div>

        <form @submit.prevent="handleRegister" class="space-y-5">
          <div>
            <label class="block text-blue-100 text-sm font-medium mb-2">账号</label>
            <input
              v-model="form.account"
              type="text"
              placeholder="请输入账号"
              class="w-full px-4 py-3 bg-white/10 border border-white/20 rounded-xl text-white placeholder-blue-300 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-all"
            />
          </div>
          <div>
            <label class="block text-blue-100 text-sm font-medium mb-2">邮箱</label>
            <input
              v-model="form.email"
              type="email"
              placeholder="请输入邮箱"
              class="w-full px-4 py-3 bg-white/10 border border-white/20 rounded-xl text-white placeholder-blue-300 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-all"
            />
          </div>
          <div>
            <label class="block text-blue-100 text-sm font-medium mb-2">密码</label>
            <input
              v-model="form.password"
              type="password"
              placeholder="请输入密码"
              class="w-full px-4 py-3 bg-white/10 border border-white/20 rounded-xl text-white placeholder-blue-300 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-all"
            />
          </div>
          <div>
            <label class="block text-blue-100 text-sm font-medium mb-2">确认密码</label>
            <input
              v-model="form.confirmPassword"
              type="password"
              placeholder="请再次输入密码"
              class="w-full px-4 py-3 bg-white/10 border border-white/20 rounded-xl text-white placeholder-blue-300 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-all"
            />
          </div>
          <div>
            <label class="block text-blue-100 text-sm font-medium mb-3">身份选择</label>
            <div class="flex space-x-6">
              <label class="flex items-center space-x-2 cursor-pointer">
                <input
                  v-model="form.permission"
                  type="radio"
                  :value="2"
                  class="w-4 h-4 text-blue-500 border-gray-600 bg-gray-700 focus:ring-blue-500"
                />
                <span class="text-gray-300">我是学生</span>
              </label>
              <label class="flex items-center space-x-2 cursor-pointer">
                <input
                  v-model="form.permission"
                  type="radio"
                  :value="1"
                  class="w-4 h-4 text-blue-500 border-gray-600 bg-gray-700 focus:ring-blue-500"
                />
                <span class="text-gray-300">我是老师</span>
              </label>
            </div>
          </div>
          <button
            type="submit"
            :loading="loading"
            class="w-full py-3 bg-gradient-to-r from-green-500 to-green-600 text-white font-semibold rounded-xl hover:from-green-600 hover:to-green-700 focus:outline-none focus:ring-2 focus:ring-green-500 focus:ring-offset-2 focus:ring-offset-blue-900 transition-all shadow-lg hover:shadow-xl"
          >
            {{ loading ? '注册中...' : '注册' }}
          </button>
        </form>

        <div class="mt-6 text-center">
          <span class="text-blue-200">已有账号？</span>
          <button
            @click="goToLogin"
            class="ml-2 text-blue-400 hover:text-blue-300 font-medium transition-colors"
          >
            立即登录
          </button>
        </div>
      </div>
    </div>
  </div>
</template>