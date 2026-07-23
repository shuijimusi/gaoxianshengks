<script setup lang="ts">
import { ref } from 'vue';
import { ElMessage } from 'element-plus';
import { joinCourse } from '../api/course';
import Sidebar from '../components/Sidebar.vue';

const courseCode = ref('');
const loading = ref(false);

async function handleJoin() {
  if (!courseCode.value.trim()) {
    ElMessage.warning('请输入课程码');
    return;
  }

  loading.value = true;
  try {
    const res = await joinCourse(courseCode.value.trim());
    if (res.code === 200) {
      ElMessage.success('加入班级成功');
      courseCode.value = '';
    } else {
      ElMessage.error(res.msg || '加入班级失败');
    }
  } catch (error) {
    ElMessage.error('加入班级失败');
  } finally {
    loading.value = false;
  }
}
</script>

<template>
  <div class="min-h-screen bg-gray-100">
    <Sidebar />
    
    <main class="ml-64 p-8">
      <div class="max-w-2xl mx-auto">
        <div class="mb-8">
          <h1 class="text-3xl font-bold text-gray-800">加入班级</h1>
          <p class="text-gray-600 mt-2">输入教师提供的课程码加入班级</p>
        </div>

        <div class="bg-white rounded-xl shadow-lg p-8">
          <div class="text-center mb-8">
            <div class="w-16 h-16 bg-gradient-to-r from-blue-500 to-blue-600 rounded-xl flex items-center justify-center mx-auto mb-4">
              <svg class="w-8 h-8 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0zm6 3a2 2 0 11-4 0 2 2 0 014 0zM7 10a2 2 0 11-4 0 2 2 0 014 0z" />
              </svg>
            </div>
            <p class="text-gray-600">请输入教师提供的课程码</p>
          </div>

          <div class="space-y-6">
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-2">课程码</label>
              <div class="flex gap-3">
                <input
                  v-model="courseCode"
                  type="text"
                  placeholder="请输入课程码"
                  class="flex-1 px-4 py-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-all"
                  @keyup.enter="handleJoin"
                />
                <button
                  @click="handleJoin"
                  :disabled="loading"
                  class="px-6 py-3 bg-blue-600 text-white font-medium rounded-lg hover:bg-blue-700 transition-colors disabled:opacity-50"
                >
                  {{ loading ? '加入中...' : '加入班级' }}
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </main>
  </div>
</template>
