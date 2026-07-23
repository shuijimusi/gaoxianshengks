<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { getPaperById } from '../api/paper';
import type { ExamPaperDetail } from '../types';
import Sidebar from '../components/Sidebar.vue';

const route = useRoute();
const router = useRouter();
const paperId = Number(route.params.id);
const paper = ref<ExamPaperDetail | null>(null);
const loading = ref(false);

onMounted(async () => {
  loading.value = true;
  const res = await getPaperById(paperId);
  paper.value = res.data;
  loading.value = false;
});

function startExam() {
  router.push(`/exam/${paperId}`);
}
</script>

<template>
  <div class="min-h-screen bg-gray-100">
    <Sidebar />
    
    <main class="ml-64 p-8">
      <div class="mb-8">
        <div class="flex items-center justify-between">
          <div>
            <h1 class="text-3xl font-bold text-gray-800">{{ paper?.title }}</h1>
            <p class="text-gray-600 mt-2">试卷详情</p>
          </div>
          <button
            @click="startExam"
            class="px-6 py-3 bg-blue-600 text-white font-medium rounded-lg hover:bg-blue-700 transition-colors shadow-lg"
          >
            开始考试
          </button>
        </div>
      </div>

      <div class="grid grid-cols-1 lg:grid-cols-4 gap-6 mb-8">
        <div class="bg-white rounded-xl shadow-lg p-6">
          <div class="flex items-center justify-between">
            <div>
              <p class="text-gray-500 text-sm">考试时长</p>
              <p class="text-2xl font-bold text-gray-800 mt-1">{{ paper?.duration }}分钟</p>
            </div>
            <div class="w-12 h-12 bg-blue-100 rounded-lg flex items-center justify-center">
              <svg class="w-6 h-6 text-blue-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z" />
              </svg>
            </div>
          </div>
        </div>

        <div class="bg-white rounded-xl shadow-lg p-6">
          <div class="flex items-center justify-between">
            <div>
              <p class="text-gray-500 text-sm">题目数量</p>
              <p class="text-2xl font-bold text-gray-800 mt-1">{{ paper?.questionCount }}道</p>
            </div>
            <div class="w-12 h-12 bg-green-100 rounded-lg flex items-center justify-center">
              <svg class="w-6 h-6 text-green-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z" />
              </svg>
            </div>
          </div>
        </div>

        <div class="bg-white rounded-xl shadow-lg p-6">
          <div class="flex items-center justify-between">
            <div>
              <p class="text-gray-500 text-sm">总分</p>
              <p class="text-2xl font-bold text-gray-800 mt-1">{{ paper?.totalScore }}分</p>
            </div>
            <div class="w-12 h-12 bg-purple-100 rounded-lg flex items-center justify-center">
              <svg class="w-6 h-6 text-purple-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 19v-6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2a2 2 0 002-2zm0 0V9a2 2 0 012-2h2a2 2 0 012 2v10m-6 0a2 2 0 002 2h2a2 2 0 002-2m0 0V5a2 2 0 012-2h2a2 2 0 012 2v14a2 2 0 01-2 2h-2a2 2 0 01-2-2z" />
              </svg>
            </div>
          </div>
        </div>

        <div class="bg-white rounded-xl shadow-lg p-6">
          <div class="flex items-center justify-between">
            <div>
              <p class="text-gray-500 text-sm">及格分</p>
              <p class="text-2xl font-bold text-gray-800 mt-1">{{ paper?.passScore }}分</p>
            </div>
            <div class="w-12 h-12 bg-yellow-100 rounded-lg flex items-center justify-center">
              <svg class="w-6 h-6 text-yellow-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" />
              </svg>
            </div>
          </div>
        </div>
      </div>

      <div class="bg-white rounded-xl shadow-lg p-6 mb-8">
        <h2 class="text-xl font-semibold text-gray-800 mb-4">试卷描述</h2>
        <p class="text-gray-600">{{ paper?.description || '暂无描述' }}</p>
      </div>

      <div class="bg-white rounded-xl shadow-lg p-6">
        <h2 class="text-xl font-semibold text-gray-800 mb-4">详细信息</h2>
        <div class="grid grid-cols-2 md:grid-cols-4 gap-4">
          <div class="space-y-1">
            <p class="text-sm text-gray-500">创建时间</p>
            <p class="text-gray-800">{{ paper?.createTime }}</p>
          </div>
          <div class="space-y-1">
            <p class="text-sm text-gray-500">更新时间</p>
            <p class="text-gray-800">{{ paper?.updateTime }}</p>
          </div>
          <div class="space-y-1">
            <p class="text-sm text-gray-500">创建者ID</p>
            <p class="text-gray-800">{{ paper?.creatorId }}</p>
          </div>
          <div class="space-y-1">
            <p class="text-sm text-gray-500">试卷ID</p>
            <p class="text-gray-800">{{ paper?.id }}</p>
          </div>
        </div>
      </div>
    </main>
  </div>
</template>