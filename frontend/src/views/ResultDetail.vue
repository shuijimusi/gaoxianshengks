<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { getExamRecordById } from '../api/exam';
import { getPaperWithQuestions } from '../api/paper';
import type { ExamRecord, ExamPaper, Question } from '../types';
import { QUESTION_TYPES } from '../types';

const route = useRoute();
const router = useRouter();
const recordId = Number(route.params.id);
const record = ref<ExamRecord | null>(null);
const paper = ref<ExamPaper | null>(null);
const questions = ref<Question[]>([]);
const loading = ref(false);

onMounted(async () => {
  loading.value = true;
  const rRes = await getExamRecordById(recordId);
  const pRes = await getPaperWithQuestions(rRes.data?.paperId || 0);
  record.value = rRes.data;
  paper.value = pRes.data;
  questions.value = pRes.data?.questions || [];
  loading.value = false;
});

function getTypeName(type: number) {
  return QUESTION_TYPES[type] || type;
}

function getOptions(question: Question): string[] {
  try {
    return typeof question.options === 'string' ? JSON.parse(question.options) : question.options || [];
  } catch {
    return [];
  }
}

function getUserAnswer(questionId: number): string {
  return record.value?.answers[questionId] || '未作答';
}

function isCorrect(question: Question): boolean {
  const userAnswer = getUserAnswer(question.id);
  return userAnswer === question.answer;
}

function getScorePercent() {
  if (!record.value) return 0;
  return Math.round((record.value.score / record.value.totalScore) * 100);
}

function goBack() {
  router.push('/results');
}
</script>

<template>
  <div class="min-h-screen bg-gray-100 py-8">
    <div class="max-w-4xl mx-auto px-6">
      <div class="bg-white rounded-xl shadow-lg overflow-hidden">
        <div class="bg-gradient-to-r from-blue-600 to-blue-700 p-8 text-white">
          <div class="flex items-center justify-between">
            <div>
              <h1 class="text-2xl font-bold">{{ paper?.title }}</h1>
              <p class="text-blue-100 mt-2">考试结果</p>
            </div>
            <button
              @click="goBack"
              class="px-4 py-2 bg-white/20 hover:bg-white/30 rounded-lg transition-colors"
            >
              返回列表
            </button>
          </div>
        </div>

        <div class="p-8">
          <div class="grid grid-cols-1 md:grid-cols-4 gap-6 mb-8">
            <div class="text-center p-4 bg-gray-50 rounded-lg">
              <p class="text-gray-500 text-sm">得分</p>
              <p
                :class="[
                  'text-4xl font-bold mt-2',
                  record?.score >= (record?.totalScore || 100) * 0.6 ? 'text-green-600' : 'text-red-600'
                ]"
              >
                {{ record?.score }}
              </p>
              <p class="text-gray-500 text-sm">/ {{ record?.totalScore }}分</p>
            </div>

            <div class="text-center p-4 bg-gray-50 rounded-lg">
              <p class="text-gray-500 text-sm">正确率</p>
              <p class="text-4xl font-bold mt-2 text-blue-600">{{ getScorePercent() }}%</p>
            </div>

            <div class="text-center p-4 bg-gray-50 rounded-lg">
              <p class="text-gray-500 text-sm">用时</p>
              <p class="text-4xl font-bold mt-2 text-purple-600">{{ record?.duration }}</p>
              <p class="text-gray-500 text-sm">分钟</p>
            </div>

            <div class="text-center p-4 bg-gray-50 rounded-lg">
              <p class="text-gray-500 text-sm">提交时间</p>
              <p class="text-lg font-bold mt-2 text-gray-800">{{ record?.submitTime }}</p>
            </div>
          </div>

          <div class="mb-6">
            <h2 class="text-xl font-semibold text-gray-800">答题详情</h2>
          </div>

          <div class="space-y-6" v-loading="loading">
            <div
              v-for="(question, index) in questions"
              :key="question.id"
              :class="[
                'p-6 rounded-lg border-2',
                isCorrect(question) ? 'border-green-200 bg-green-50' : 'border-red-200 bg-red-50'
              ]"
            >
              <div class="flex items-start justify-between mb-4">
                <div class="flex items-center space-x-4">
                  <span class="text-lg font-bold text-blue-600">{{ index + 1 }}.</span>
                  <span
                    :class="[
                      'px-3 py-1 rounded-full text-sm font-medium',
                      question.type === 1 ? 'bg-blue-100 text-blue-700' :
                      question.type === 2 ? 'bg-purple-100 text-purple-700' :
                      question.type === 3 ? 'bg-green-100 text-green-700' :
                      'bg-orange-100 text-orange-700'
                    ]"
                  >
                    {{ getTypeName(question.type) }}
                  </span>
                  <span class="text-gray-600">{{ question.score }}分</span>
                </div>
                <span
                  :class="[
                    'px-4 py-2 rounded-full font-medium',
                    isCorrect(question) ? 'bg-green-500 text-white' : 'bg-red-500 text-white'
                  ]"
                >
                  {{ isCorrect(question) ? '正确' : '错误' }}
                </span>
              </div>

              <div class="ml-8 mb-4">
                <p class="text-gray-800">{{ question.content }}</p>
              </div>

              <div class="ml-8 space-y-2" v-if="question.options">
                <div
                  v-for="(option, idx) in getOptions(question)"
                  :key="idx"
                  :class="[
                    'flex items-center p-3 rounded-lg',
                    question.answer === String.fromCharCode(65 + idx)
                      ? 'bg-green-200 text-green-800'
                      : getUserAnswer(question.id).split(',').includes(String.fromCharCode(65 + idx)) && !isCorrect(question)
                      ? 'bg-red-200 text-red-800'
                      : 'bg-gray-100 text-gray-600'
                  ]"
                >
                  <span class="w-6 h-6 rounded-full bg-white/50 flex items-center justify-center text-sm font-medium mr-3">
                    {{ String.fromCharCode(65 + idx) }}
                  </span>
                  <span>{{ option }}</span>
                  <span v-if="question.answer === String.fromCharCode(65 + idx)" class="ml-auto text-sm font-medium">
                    正确答案
                  </span>
                </div>
              </div>

              <div class="ml-8 mt-4 pt-4 border-t border-gray-200">
                <div class="flex items-center space-x-6">
                  <div>
                    <span class="text-sm font-medium text-gray-700 mr-2">您的答案：</span>
                    <span
                      :class="[
                        'text-sm font-medium',
                        isCorrect(question) ? 'text-green-600' : 'text-red-600'
                      ]"
                    >
                      {{ getUserAnswer(question.id) }}
                    </span>
                  </div>
                  <div>
                    <span class="text-sm font-medium text-gray-700 mr-2">正确答案：</span>
                    <span class="text-sm font-medium text-green-600">{{ question.answer }}</span>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>