<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { useUserStore } from '../stores/user';
import { getQuestionList } from '../api/question';
import { getPaperList } from '../api/paper';
import { getExamRecords } from '../api/exam';
import type { Question, ExamPaper, ExamRecord } from '../types';
import { QUESTION_TYPES, DIFFICULTY_LEVELS, ROLES } from '../types';
import Sidebar from '../components/Sidebar.vue';

const router = useRouter();
const userStore = useUserStore();

const questions = ref<Question[]>([]);
const papers = ref<ExamPaper[]>([]);
const records = ref<ExamRecord[]>([]);

const stats = ref({
  totalQuestions: 0,
  totalPapers: 0,
  totalExams: 0,
  avgScore: 0,
});

onMounted(async () => {
  const [qRes, pRes, rRes] = await Promise.all([
    getQuestionList({ pageNum: 1, pageSize: 100 }),
    getPaperList({ pageNum: 1, pageSize: 100 }),
    getExamRecords(userStore.user?.id),
  ]);
  
  questions.value = qRes.data?.records || [];
  papers.value = pRes.data?.rows || pRes.data?.records || [];
  records.value = rRes.data || [];
  
  stats.value = {
    totalQuestions: questions.value.length,
    totalPapers: papers.value.length,
    totalExams: records.value.length,
    avgScore: records.value.length > 0 
      ? Math.round(records.value.reduce((sum, r) => sum + (r.score || 0), 0) / records.value.length)
      : 0,
  };
});

function startExam(paperId: number) {
  router.push(`/exam/${paperId}`);
}

function viewResult(recordId: number) {
  router.push(`/results/${recordId}`);
}
</script>

<template>
  <div class="min-h-screen bg-gray-100">
    <Sidebar />
    
    <main class="ml-64 p-8">
      <div class="mb-8">
        <h1 class="text-3xl font-bold text-gray-800">欢迎回来，{{ userStore.user?.realName }}</h1>
        <p class="text-gray-600 mt-2">今天是 {{ new Date().toLocaleDateString('zh-CN') }}</p>
      </div>

      <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-8">
        <div class="bg-white rounded-xl shadow-lg p-6 border-l-4 border-blue-500">
          <div class="flex items-center justify-between">
            <div>
              <p class="text-gray-500 text-sm">题目总数</p>
              <p class="text-3xl font-bold text-gray-800 mt-1">{{ stats.totalQuestions }}</p>
            </div>
            <div class="w-12 h-12 bg-blue-100 rounded-lg flex items-center justify-center">
              <svg class="w-6 h-6 text-blue-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z" />
              </svg>
            </div>
          </div>
        </div>

        <div class="bg-white rounded-xl shadow-lg p-6 border-l-4 border-green-500">
          <div class="flex items-center justify-between">
            <div>
              <p class="text-gray-500 text-sm">试卷总数</p>
              <p class="text-3xl font-bold text-gray-800 mt-1">{{ stats.totalPapers }}</p>
            </div>
            <div class="w-12 h-12 bg-green-100 rounded-lg flex items-center justify-center">
              <svg class="w-6 h-6 text-green-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M7 21h10a2 2 0 002-2V9.414a1 1 0 00-.293-.707l-5.414-5.414A1 1 0 0012.586 3H7a2 2 0 00-2 2v14a2 2 0 002 2z" />
              </svg>
            </div>
          </div>
        </div>

        <div class="bg-white rounded-xl shadow-lg p-6 border-l-4 border-purple-500">
          <div class="flex items-center justify-between">
            <div>
              <p class="text-gray-500 text-sm">已参加考试</p>
              <p class="text-3xl font-bold text-gray-800 mt-1">{{ stats.totalExams }}</p>
            </div>
            <div class="w-12 h-12 bg-purple-100 rounded-lg flex items-center justify-center">
              <svg class="w-6 h-6 text-purple-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z" />
              </svg>
            </div>
          </div>
        </div>

        <div class="bg-white rounded-xl shadow-lg p-6 border-l-4 border-orange-500">
          <div class="flex items-center justify-between">
            <div>
              <p class="text-gray-500 text-sm">平均成绩</p>
              <p class="text-3xl font-bold text-gray-800 mt-1">{{ stats.avgScore }}分</p>
            </div>
            <div class="w-12 h-12 bg-orange-100 rounded-lg flex items-center justify-center">
              <svg class="w-6 h-6 text-orange-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 19v-6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2a2 2 0 002-2zm0 0V9a2 2 0 012-2h2a2 2 0 012 2v10m-6 0a2 2 0 002 2h2a2 2 0 002-2m0 0V5a2 2 0 012-2h2a2 2 0 012 2v14a2 2 0 01-2 2h-2a2 2 0 01-2-2z" />
              </svg>
            </div>
          </div>
        </div>
      </div>

      <div class="grid grid-cols-1 lg:grid-cols-2 gap-6">
        <div class="bg-white rounded-xl shadow-lg p-6">
          <div class="flex items-center justify-between mb-6">
            <h2 class="text-xl font-semibold text-gray-800">最近考试</h2>
            <button @click="router.push('/papers')" class="text-blue-600 hover:text-blue-700 text-sm font-medium">
              查看全部
            </button>
          </div>
          <div class="space-y-4">
            <div
              v-for="paper in papers.slice(0, 3)"
              :key="paper.id"
              class="p-4 border border-gray-200 rounded-lg hover:border-blue-400 hover:shadow-md transition-all cursor-pointer"
              @click="startExam(paper.id)"
            >
              <div class="flex items-center justify-between">
                <div>
                  <h3 class="font-medium text-gray-800">{{ paper.title }}</h3>
                  <p class="text-sm text-gray-500 mt-1">
                    {{ paper.totalScore }}分 · {{ paper.duration }}分钟 · {{ paper.questionCount }}题
                  </p>
                </div>
                <button class="px-4 py-2 bg-blue-600 text-white text-sm rounded-lg hover:bg-blue-700 transition-colors">
                  开始考试
                </button>
              </div>
            </div>
            <div v-if="papers.length === 0" class="text-center py-8 text-gray-500">
              暂无试卷
            </div>
          </div>
        </div>

        <div class="bg-white rounded-xl shadow-lg p-6">
          <div class="flex items-center justify-between mb-6">
            <h2 class="text-xl font-semibold text-gray-800">考试记录</h2>
            <button @click="router.push('/results')" class="text-blue-600 hover:text-blue-700 text-sm font-medium">
              查看全部
            </button>
          </div>
          <div class="space-y-4">
            <div
              v-for="record in records.slice(0, 3)"
              :key="record.id"
              class="p-4 border border-gray-200 rounded-lg hover:border-blue-400 hover:shadow-md transition-all cursor-pointer"
              @click="viewResult(record.id)"
            >
              <div class="flex items-center justify-between">
                <div>
                  <h3 class="font-medium text-gray-800">
                    {{ papers.find(p => p.id === record.paperId)?.title || '未知试卷' }}
                  </h3>
                  <p class="text-sm text-gray-500 mt-1">
                    {{ record.submitTime }} · 得分: {{ record.score }}/{{ record.totalScore }}
                  </p>
                </div>
                <span
                  :class="[
                    'px-3 py-1 rounded-full text-sm font-medium',
                    record.score >= (record.totalScore * 0.6)
                      ? 'bg-green-100 text-green-700'
                      : 'bg-red-100 text-red-700'
                  ]"
                >
                  {{ record.score >= (record.totalScore * 0.6) ? '及格' : '未及格' }}
                </span>
              </div>
            </div>
            <div v-if="records.length === 0" class="text-center py-8 text-gray-500">
              暂无考试记录
            </div>
          </div>
        </div>
      </div>
    </main>
  </div>
</template>