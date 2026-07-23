<script setup lang="ts">
import { ref, computed } from 'vue';
import { useRouter } from 'vue-router';
import { getQuestionList } from '../api/question';
import { generatePaper } from '../api/paper';
import type { Question } from '../types';
import { QUESTION_TYPES, DIFFICULTY_LEVELS } from '../types';
import { ElMessage } from 'element-plus';
import Sidebar from '../components/Sidebar.vue';

const router = useRouter();

const form = ref({
  title: '',
  duration: 90,
  totalScore: 100,
  questionCount: 20,
  typeDistribution: {
    1: 10,
    2: 5,
    3: 3,
    4: 2,
  },
  difficultyDistribution: {
    1: 40,
    2: 40,
    3: 20,
  },
});

const questions = ref<Question[]>([]);
const loading = ref(false);
const generating = ref(false);

const typeOptions = Object.entries(QUESTION_TYPES).map(([value, label]) => ({ value, label }));
const difficultyOptions = Object.entries(DIFFICULTY_LEVELS).map(([value, label]) => ({ value, label }));

const totalQuestionsByType = computed(() => {
  return Object.values(form.value.typeDistribution).reduce((sum, val) => sum + val, 0);
});

const totalDifficultyPercent = computed(() => {
  return Object.values(form.value.difficultyDistribution).reduce((sum, val) => sum + val, 0);
});

async function handleGenerate() {
  if (!form.value.title) {
    ElMessage.warning('请输入试卷标题');
    return;
  }

  if (totalQuestionsByType.value !== form.value.questionCount) {
    ElMessage.warning('各题型数量之和必须等于总题数');
    return;
  }

  if (totalDifficultyPercent.value !== 100) {
    ElMessage.warning('难度比例之和必须等于100%');
    return;
  }

  generating.value = true;
  
  try {
    const res = await generatePaper(form.value);
    
    if (res.success) {
      ElMessage.success('试卷生成成功');
      router.push('/papers');
    } else {
      ElMessage.error(res.message || '试卷生成失败');
    }
  } finally {
    generating.value = false;
  }
}

function cancel() {
  router.push('/papers');
}
</script>

<template>
  <div class="min-h-screen bg-gray-100">
    <Sidebar />
    
    <main class="ml-64 p-8">
      <div class="mb-8">
        <h1 class="text-3xl font-bold text-gray-800">智能组卷</h1>
        <p class="text-gray-600 mt-2">根据约束条件自动生成试卷</p>
      </div>

      <div class="max-w-4xl mx-auto">
        <div class="bg-white rounded-xl shadow-lg p-8">
          <div class="mb-8">
            <h2 class="text-lg font-semibold text-gray-800 mb-4">基本信息</h2>
            <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">试卷标题</label>
                <input
                  v-model="form.title"
                  type="text"
                  placeholder="请输入试卷标题"
                  class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                />
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">考试时长（分钟）</label>
                <input
                  v-model.number="form.duration"
                  type="number"
                  min="30"
                  placeholder="请输入考试时长"
                  class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                />
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">总分</label>
                <input
                  v-model.number="form.totalScore"
                  type="number"
                  min="50"
                  placeholder="请输入总分"
                  class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                />
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">总题数</label>
                <input
                  v-model.number="form.questionCount"
                  type="number"
                  min="5"
                  placeholder="请输入总题数"
                  class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                />
              </div>
            </div>
          </div>

          <div class="mb-8">
            <h2 class="text-lg font-semibold text-gray-800 mb-4">题型分布</h2>
            <p class="text-sm text-gray-500 mb-4">各题型数量之和：{{ totalQuestionsByType }} / {{ form.questionCount }}</p>
            <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div v-for="(label, value) in QUESTION_TYPES" :key="value" class="flex items-center space-x-4">
                <label class="flex-1 text-sm font-medium text-gray-700">{{ label }}</label>
                <input
                  v-model.number="form.typeDistribution[value as keyof typeof form.typeDistribution]"
                  type="number"
                  min="0"
                  class="w-24 px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 text-center"
                />
                <span class="text-sm text-gray-500">题</span>
              </div>
            </div>
          </div>

          <div class="mb-8">
            <h2 class="text-lg font-semibold text-gray-800 mb-4">难度分布</h2>
            <p class="text-sm text-gray-500 mb-4">各难度比例之和：{{ totalDifficultyPercent }}%</p>
            <div class="space-y-4">
              <div v-for="(label, value) in DIFFICULTY_LEVELS" :key="value" class="flex items-center space-x-4">
                <label class="w-20 text-sm font-medium text-gray-700">{{ label }}</label>
                <input
                  v-model.number="form.difficultyDistribution[value as keyof typeof form.difficultyDistribution]"
                  type="range"
                  min="0"
                  max="100"
                  class="flex-1 h-2 bg-gray-200 rounded-lg appearance-none cursor-pointer accent-blue-600"
                />
                <input
                  v-model.number="form.difficultyDistribution[value as keyof typeof form.difficultyDistribution]"
                  type="number"
                  min="0"
                  max="100"
                  class="w-20 px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 text-center"
                />
                <span class="text-sm text-gray-500">%</span>
              </div>
            </div>
          </div>

          <div class="bg-gradient-to-r from-blue-50 to-purple-50 rounded-xl p-6 mb-8">
            <h3 class="text-sm font-semibold text-gray-800 mb-3">算法说明</h3>
            <ul class="text-sm text-gray-600 space-y-2">
              <li>• <strong>多约束条件随机抽样优化算法</strong>：在满足题型数量、难度分布等约束条件下，从题库中随机抽取题目</li>
              <li>• <strong>约束条件检查</strong>：确保各题型数量之和等于总题数，难度比例之和等于100%</li>
              <li>• <strong>题目去重</strong>：保证每张试卷中的题目不重复</li>
              <li>• <strong>分数自动分配</strong>：根据题目难度和题型自动分配分数</li>
            </ul>
          </div>

          <div class="flex space-x-4">
            <button
              @click="cancel"
              class="flex-1 px-6 py-3 bg-gray-200 text-gray-700 font-medium rounded-lg hover:bg-gray-300 transition-colors"
            >
              取消
            </button>
            <button
              @click="handleGenerate"
              :loading="generating"
              class="flex-1 px-6 py-3 bg-gradient-to-r from-blue-600 to-purple-600 text-white font-medium rounded-lg hover:from-blue-700 hover:to-purple-700 transition-colors shadow-lg"
            >
              {{ generating ? '生成中...' : '智能生成试卷' }}
            </button>
          </div>
        </div>
      </div>
    </main>
  </div>
</template>