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
});

const loading = ref(false);

async function handleLogin() {
  if (!form.value.account || !form.value.password) {
    ElMessage.warning('请输入账号和密码');
    return;
  }

  loading.value = true;
  const success = await userStore.handleLogin(form.value);
  loading.value = false;

  if (success) {
    ElMessage.success('登录成功');
    router.push('/home');
  } else {
    ElMessage.error('账号或密码错误');
  }
}

function goToRegister() {
  router.push('/register');
}
</script>

<template>
  <div class="min-h-screen bg-gradient-to-br from-blue-900 via-blue-800 to-blue-900 flex items-center justify-center p-4">
    <div class="w-full max-w-md">
      <div class="bg-white/10 backdrop-blur-lg rounded-2xl shadow-2xl p-8 border border-white/20">
        <div class="text-center mb-8">
          <div class="w-16 h-16 bg-gradient-to-r from-blue-500 to-blue-600 rounded-xl flex items-center justify-center mx-auto mb-4">
            <svg class="w-8 h-8 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m5.618-4.016A11.955 11.955 0 0112 2.944a11.955 11.955 0 01-8.618 3.04A12.02 12.02 0 003 9c0 5.591 3.824 10.29 9 11.622 5.176-1.332 9-6.03 9-11.622 0-1.042-.133-2.052-.382-3.016z" />
            </svg>
          </div>
          <h1 class="text-3xl font-bold text-white">在线考试系统</h1>
          <p class="text-blue-200 mt-2">数据结构课程考试平台</p>
        </div>

        <form @submit.prevent="handleLogin" class="space-y-6">
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
            <label class="block text-blue-100 text-sm font-medium mb-2">密码</label>
            <input
              v-model="form.password"
              type="password"
              placeholder="请输入密码"
              class="w-full px-4 py-3 bg-white/10 border border-white/20 rounded-xl text-white placeholder-blue-300 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-all"
            />
          </div>
          <button
            type="submit"
            :loading="loading"
            class="w-full py-3 bg-gradient-to-r from-blue-500 to-blue-600 text-white font-semibold rounded-xl hover:from-blue-600 hover:to-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2 focus:ring-offset-blue-900 transition-all shadow-lg hover:shadow-xl"
          >
            {{ loading ? '登录中...' : '登录' }}
          </button>
        </form>

        <div class="mt-6 text-center">
          <span class="text-blue-200">还没有账号？</span>
          <button
            @click="goToRegister"
            class="ml-2 text-blue-400 hover:text-blue-300 font-medium transition-colors"
          >
            立即注册
          </button>
        </div>

        <div class="mt-8 pt-6 border-t border-white/10">
          <p class="text-center text-blue-300 text-sm">
            测试账号：admin/123456 | teacher/123456 | student/123456
          </p>
        </div>
      </div>
    </div>
  </div>
</template>