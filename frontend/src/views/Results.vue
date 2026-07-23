<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { getExamRecords } from '../api/exam';
import { getPaperList } from '../api/paper';
import type { ExamRecord, ExamPaper } from '../types';
import Sidebar from '../components/Sidebar.vue';
import { useUserStore } from '../stores/user';

const router = useRouter();
const userStore = useUserStore();
const records = ref<ExamRecord[]>([]);
const papers = ref<ExamPaper[]>([]);
const loading = ref(false);

onMounted(async () => {
  await loadData();
});

async function loadData() {
  loading.value = true;
  const [rRes, pRes] = await Promise.all([
    getExamRecords(userStore.user?.id),
    getPaperList({ pageNum: 1, pageSize: 100 }),
  ]);
  records.value = rRes.data || [];
  papers.value = pRes.data?.rows || pRes.data?.records || [];
  loading.value = false;
}

function getPaperTitle(paperId: number) {
  return papers.value.find(p => p.id === paperId)?.title || '未知试卷';
}

function getScorePercent(score: number, totalScore: number) {
  return Math.round((score / totalScore) * 100);
}

function getScoreClass(score: number, totalScore: number) {
  const percent = getScorePercent(score, totalScore);
  if (percent >= 90) return 'bg-green-500';
  if (percent >= 80) return 'bg-blue-500';
  if (percent >= 60) return 'bg-yellow-500';
  return 'bg-red-500';
}

function viewDetail(recordId: number) {
  router.push(`/results/${recordId}`);
}
</script>

<template>
  <div class="min-h-screen bg-gray-100">
    <Sidebar />
    
    <main class="ml-64 p-8">
      <div class="mb-8">
        <h1 class="text-3xl font-bold text-gray-800">成绩查询</h1>
        <p class="text-gray-600 mt-2">查看您的考试记录和成绩</p>
      </div>

      <div class="bg-white rounded-xl shadow-lg overflow-hidden">
        <div class="overflow-x-auto">
          <table class="w-full">
            <thead class="bg-gray-50">
              <tr>
                <th class="px-6 py-4 text-left text-xs font-semibold text-gray-500 uppercase tracking-wider">试卷名称</th>
                <th class="px-6 py-4 text-left text-xs font-semibold text-gray-500 uppercase tracking-wider">得分</th>
                <th class="px-6 py-4 text-left text-xs font-semibold text-gray-500 uppercase tracking-wider">正确率</th>
                <th class="px-6 py-4 text-left text-xs font-semibold text-gray-500 uppercase tracking-wider">用时</th>
                <th class="px-6 py-4 text-left text-xs font-semibold text-gray-500 uppercase tracking-wider">提交时间</th>
                <th class="px-6 py-4 text-left text-xs font-semibold text-gray-500 uppercase tracking-wider">操作</th>
              </tr>
            </thead>
            <tbody class="divide-y divide-gray-200" v-loading="loading">
              <tr v-for="record in records" :key="record.id" class="hover:bg-gray-50">
                <td class="px-6 py-4">
                  <span class="font-medium text-gray-800">{{ getPaperTitle(record.paperId) }}</span>
                </td>
                <td class="px-6 py-4">
                  <div class="flex items-center space-x-3">
                    <span
                      :class="[
                        'text-2xl font-bold',
                        record.score >= (record.totalScore * 0.6) ? 'text-green-600' : 'text-red-600'
                      ]"
                    >
                      {{ record.score }}
                    </span>
                    <span class="text-gray-500">/ {{ record.totalScore }}分</span>
                  </div>
                </td>
                <td class="px-6 py-4">
                  <div class="flex items-center space-x-3">
                    <div class="w-24 h-2 bg-gray-200 rounded-full overflow-hidden">
                      <div
                        :class="['h-full rounded-full', getScoreClass(record.score, record.totalScore)]"
                        :style="{ width: `${getScorePercent(record.score, record.totalScore)}%` }"
                      ></div>
                    </div>
                    <span class="text-sm text-gray-600">{{ getScorePercent(record.score, record.totalScore) }}%</span>
                  </div>
                </td>
                <td class="px-6 py-4">
                  <span class="text-sm text-gray-600">{{ record.duration }}分钟</span>
                </td>
                <td class="px-6 py-4">
                  <span class="text-sm text-gray-600">{{ record.submitTime }}</span>
                </td>
                <td class="px-6 py-4">
                  <button
                    @click="viewDetail(record.id)"
                    class="px-4 py-2 bg-blue-600 text-white text-sm rounded-lg hover:bg-blue-700 transition-colors"
                  >
                    查看详情
                  </button>
                </td>
              </tr>
            </tbody>
          </table>
        </div>

        <div v-if="records.length === 0" class="p-12 text-center">
          <svg class="w-16 h-16 text-gray-400 mx-auto mb-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 19v-6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2a2 2 0 002-2zm0 0V9a2 2 0 012-2h2a2 2 0 012 2v10m-6 0a2 2 0 002 2h2a2 2 0 002-2m0 0V5a2 2 0 012-2h2a2 2 0 012 2v14a2 2 0 01-2 2h-2a2 2 0 01-2-2z" />
          </svg>
          <p class="text-gray-500">暂无考试记录</p>
          <button
            @click="router.push('/papers')"
            class="mt-4 px-6 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors"
          >
            开始考试
          </button>
        </div>
      </div>
    </main>
  </div>
</template>