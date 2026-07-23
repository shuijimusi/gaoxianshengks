<script setup lang="ts">import { ref, onMounted, computed, onUnmounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { getExamProcess, submitExamProcess, cacheQuestion } from '../api/paper';
import type { Question } from '../types';
import { QUESTION_TYPES } from '../types';
import { ElMessage, ElMessageBox } from 'element-plus';
const route = useRoute();
const router = useRouter();
const examId = Number(route.params.id);
const endTime = route.query.endTime as string | undefined;

const questions = ref<Question[]>([]);
const answers = ref<Record<number, string>>({});
const currentIndex = ref(0);
const timeRemaining = ref(0);
const timer = ref<number | null>(null);
const cacheTimer = ref<number | null>(null);
const isSubmitted = ref(false);
const saving = ref(false);
const totalScore = computed(() => {
  return questions.value.reduce((sum, q) => sum + Number(q.score || 0), 0);
});

const showAll = ref(false);

function finishAnswering() {
  showAll.value = true;
}
onMounted(async () => {
  const res = await getExamProcess(examId);
  if (res.code !== 200) {
    ElMessage.error(res.msg || '无法进入考试');
    router.push('/myexam');
    return;
  }

  questions.value = res.data?.questions || [];
  // 加载草稿答案
  const drafts = res.data?.examStudentAnswers || [];
  drafts.forEach((item: any) => {
    if (item.questionId && item.studentAnswer != null) {
      // 兼容旧格式（A,B,C → ABC）
      if (typeof item.studentAnswer === 'string') {
        item.studentAnswer = item.studentAnswer.replace(/,/g, '');
      }
      answers.value[item.questionId] = item.studentAnswer;
    }
  });
  if (endTime) {
    timeRemaining.value = Math.floor((new Date(endTime).getTime() - Date.now()) / 1000);
    if (timeRemaining.value < 0) timeRemaining.value = 0;
  } else {
    timeRemaining.value = 90 * 60;
  }
  startTimer();
  // 自动缓存答案（每30秒）
  cacheTimer.value = window.setInterval(() => {
    cacheQuestion(getCacheData());
  }, 30000);
});

onUnmounted(() => {
  if (timer.value) {
    clearInterval(timer.value);
  }
  if (cacheTimer.value) {
    clearInterval(cacheTimer.value);
  }
});
function startTimer() {
  timer.value = window.setInterval(() => {
    if (timeRemaining.value > 0) {
      timeRemaining.value--;
    } else {
      handleSubmit();
    }
  }, 1000);
}
function formatTime(seconds: number) {
  const h = Math.floor(seconds / 3600);
  const m = Math.floor((seconds % 3600) / 60);
  const s = seconds % 60;
  if (h > 0) {
    return `${h}:${m.toString().padStart(2, '0')}:${s.toString().padStart(2, '0')}`;
  }
  return `${m.toString().padStart(2, '0')}:${s.toString().padStart(2, '0')}`;
}
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
function selectAnswer(questionId: number, answer: string) {
  answers.value[questionId] = answer;
}
function selectMultipleAnswer(questionId: number, option: string) {
  const current = answers.value[questionId] || '';
  const options = current.split('').filter(o => o);
  if (options.includes(option)) {
    answers.value[questionId] = options.filter(o => o !== option).join('');
  } else {
    options.push(option);
    answers.value[questionId] = options.sort().join('');
  }
}
function goToQuestion(index: number) {
  if (index >= 0 && index < questions.value.length) {
    currentIndex.value = index;
  }
}
function prevQuestion() {
  goToQuestion(currentIndex.value - 1);
}
function nextQuestion() {
  goToQuestion(currentIndex.value + 1);
}
const answeredCount = computed(() => {
  return Object.keys(answers.value).filter(key => answers.value[key]).length;
});
async function handleSubmit() {
  if (timer.value) {
    clearInterval(timer.value);
  }
  await ElMessageBox.confirm(`您已完成 ${answeredCount.value}/${questions.value.length} 道题，确定提交？`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '继续答题',
    type: 'warning',
  });
  isSubmitted.value = true;
  const res = await submitExamProcess(getCacheData());
  if (res.code === 200) {
    ElMessage.success('提交成功！');
    setTimeout(() => {
      router.push('/myexam');
    }, 1500);
  } else {
    ElMessage.error(res.msg || '提交失败');
  }
}

function getCacheData(): Record<string, string> {
  const data: Record<string, string> = { examId: String(examId) };
  for (const key in answers.value) {
    data['qs' + key] = typeof answers.value[key] === 'string' ? answers.value[key].replace(/,/g, '') : answers.value[key];
  }
  return data;
}

async function saveAnswers() {
  if (saving.value) return;
  saving.value = true;
  try {
    const res = await cacheQuestion(getCacheData());
    if (res.code === 200) {
      ElMessage.success('答案已保存');
    } else {
      ElMessage.error(res.msg || '保存失败');
    }
  } catch (error) {
    ElMessage.error('保存失败');
  } finally {
    saving.value = false;
  }
}

function getQuestionClass(index: number) {
  const classes = ['w-full h-1 rounded-full transition-all'];
  const question = questions.value[index];
  if (answers.value[question.id]) {
    classes.push('bg-green-500');
  } else {
    classes.push('bg-gray-300');
  }
  if (index === currentIndex.value) {
    classes.push('ring-2 ring-blue-500');
  }
  return classes.join(' ');
}
</script>

<template>
  <div class="min-h-screen bg-gray-100">
    <div class="bg-white shadow-md sticky top-0 z-10">
      <div class="max-w-6xl mx-auto px-6 py-4">
        <div class="flex items-center justify-between">
          <div>
            <h1 class="text-xl font-semibold text-gray-800">{{ '考试' }}</h1>
            <p class="text-sm text-gray-600">{{ questions.length }}道题 · {{ totalScore }}分</p>
          </div>
          <div class="flex items-center space-x-6">
            <div
              :class="[
                'flex items-center space-x-2 px-4 py-2 rounded-lg',
                timeRemaining < 300 ? 'bg-red-100 text-red-700' : 'bg-blue-100 text-blue-700'
              ]"
            >
              <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z" />
              </svg>
              <span class="font-mono font-bold text-lg">{{ formatTime(timeRemaining) }}</span>
            </div>
            <button
              @click="saveAnswers"
              :disabled="saving"
              class="px-4 py-2 border border-gray-300 text-gray-700 rounded-lg hover:bg-gray-50 transition-colors disabled:opacity-50"
            >
              {{ saving ? '保存中...' : '保存答案' }}
            </button>
            <button
              @click="handleSubmit"
              class="px-6 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors disabled:opacity-50"
            >
              提交试卷
            </button>
          </div>
        </div>
      </div>
    </div>

    <div class="max-w-6xl mx-auto px-6 py-6">
      <div class="flex gap-6">
        <div class="flex-1">
          <div v-if="!showAll && questions.length > 0" class="bg-white rounded-xl shadow-lg p-8">
            <div class="flex items-center justify-between mb-6">
              <div class="flex items-center space-x-4">
                <span class="text-2xl font-bold text-blue-600">{{ currentIndex + 1 }}</span>
                <span class="text-gray-500">/ {{ questions.length }}</span>
                <span
                  :class="[
                    'px-3 py-1 rounded-full text-sm font-medium',
                    questions[currentIndex]?.type === 1 ? 'bg-blue-100 text-blue-700' :
                    questions[currentIndex]?.type === 2 ? 'bg-purple-100 text-purple-700' :
                    questions[currentIndex]?.type === 3 ? 'bg-green-100 text-green-700' :
                    'bg-orange-100 text-orange-700'
                  ]"
                >
                  {{ getTypeName(questions[currentIndex]?.type || 0) }}
                </span>
                <span class="text-gray-600">{{ questions[currentIndex]?.score }}分</span>
              </div>
            </div>

            <div class="mb-8">
              <p class="text-lg text-gray-800 leading-relaxed">{{ questions[currentIndex]?.content }}</p>
            </div>

            <div v-if="questions[currentIndex]?.type === 1" class="space-y-3">
              <label
                v-for="(option, idx) in getOptions(questions[currentIndex])"
                :key="idx"
                :class="[
                  'flex items-center p-4 rounded-lg border-2 cursor-pointer transition-all',
                  answers[questions[currentIndex].id] === String.fromCharCode(65 + idx)
                    ? 'border-blue-500 bg-blue-50'
                    : 'border-gray-200 hover:border-blue-300'
                ]"
              >
                <input
                  type="radio"
                  :value="String.fromCharCode(65 + idx)"
                  :checked="answers[questions[currentIndex].id] === String.fromCharCode(65 + idx)"
                  @change="selectAnswer(questions[currentIndex].id, String.fromCharCode(65 + idx))"
                  class="w-5 h-5 text-blue-600"
                />
                <span class="ml-3 text-gray-800">{{ String.fromCharCode(65 + idx) }}. {{ option }}</span>
              </label>
            </div>

            <div v-else-if="questions[currentIndex]?.type === 2" class="space-y-3">
              <label
                v-for="(option, idx) in getOptions(questions[currentIndex])"
                :key="idx"
                :class="[
                  'flex items-center p-4 rounded-lg border-2 cursor-pointer transition-all',
                  (answers[questions[currentIndex].id] || '').split('').includes(String.fromCharCode(65 + idx))
                    ? 'border-purple-500 bg-purple-50'
                    : 'border-gray-200 hover:border-purple-300'
                ]"
              >
                <input
                  type="checkbox"
                  :checked="(answers[questions[currentIndex].id] || '').split('').includes(String.fromCharCode(65 + idx))"
                  @change="selectMultipleAnswer(questions[currentIndex].id, String.fromCharCode(65 + idx))"
                  class="w-5 h-5 text-purple-600"
                />
                <span class="ml-3 text-gray-800">{{ String.fromCharCode(65 + idx) }}. {{ option }}</span>
              </label>
              <p class="text-sm text-gray-500 mt-2">（多选）</p>
            </div>

            <div v-else-if="questions[currentIndex]?.type === 3" class="flex space-x-4">
              <label
                :class="[
                  'flex-1 flex items-center justify-center p-4 rounded-lg border-2 cursor-pointer transition-all',
                  answers[questions[currentIndex].id] === '对'
                    ? 'border-green-500 bg-green-50'
                    : 'border-gray-200 hover:border-green-300'
                ]"
              >
                <input
                  type="radio"
                  value="对"
                  :checked="answers[questions[currentIndex].id] === '对'"
                  @change="selectAnswer(questions[currentIndex].id, '对')"
                  class="w-5 h-5 text-green-600"
                />
                <span class="ml-3 text-lg font-medium text-gray-800">正确</span>
              </label>
              <label
                :class="[
                  'flex-1 flex items-center justify-center p-4 rounded-lg border-2 cursor-pointer transition-all',
                  answers[questions[currentIndex].id] === '错'
                    ? 'border-red-500 bg-red-50'
                    : 'border-gray-200 hover:border-red-300'
                ]"
              >
                <input
                  type="radio"
                  value="错"
                  :checked="answers[questions[currentIndex].id] === '错'"
                  @change="selectAnswer(questions[currentIndex].id, '错')"
                  class="w-5 h-5 text-red-600"
                />
                <span class="ml-3 text-lg font-medium text-gray-800">错误</span>
              </label>
            </div>

            <div v-else class="space-y-4">
              <textarea
                v-model="answers[questions[currentIndex].id]"
                rows="4"
                placeholder="请输入答案"
                class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 resize-none"
              ></textarea>
            </div>

            <div class="flex items-center justify-between mt-8 pt-6 border-t border-gray-200">
              <button
                @click="prevQuestion"
                :disabled="currentIndex === 0"
                class="px-6 py-2 border border-gray-300 text-gray-700 rounded-lg hover:bg-gray-50 transition-colors disabled:opacity-50 disabled:cursor-not-allowed"
              >
                上一题
              </button>
              <button
                v-if="currentIndex < questions.length - 1"
                @click="nextQuestion"
                class="px-6 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors"
              >
                下一题
              </button>
              <button
                v-else
                @click="finishAnswering"
                class="px-6 py-2 bg-green-600 text-white rounded-lg hover:bg-green-700 transition-colors"
              >
                完成答题
              </button>
            </div>
          </div>

          <!-- 全部题目展示 -->
          <div v-if="showAll" class="space-y-6">
            <div
              v-for="(question, index) in questions"
              :key="question.id"
              class="bg-white rounded-xl shadow-lg p-8"
            >
              <div class="flex items-center justify-between mb-6">
                <div class="flex items-center space-x-4">
                  <span class="text-2xl font-bold text-blue-600">{{ index + 1 }}</span>
                  <span class="text-gray-500">/ {{ questions.length }}</span>
                  <span
                    :class="['px-3 py-1 rounded-full text-sm font-medium',
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
              </div>

              <div class="mb-8">
                <p class="text-lg text-gray-800 leading-relaxed">{{ question.content }}</p>
              </div>

              <!-- 单选题 -->
              <div v-if="question.type === 1" class="space-y-3">
                <label
                  v-for="(option, idx) in getOptions(question)"
                  :key="idx"
                  :class="[
                    'flex items-center p-4 rounded-lg border-2 cursor-pointer transition-all',
                    answers[question.id] === String.fromCharCode(65 + idx)
                      ? 'border-blue-500 bg-blue-50'
                      : 'border-gray-200 hover:border-blue-300'
                  ]"
                >
                  <input
                    type="radio"
                    :value="String.fromCharCode(65 + idx)"
                    :checked="answers[question.id] === String.fromCharCode(65 + idx)"
                    @change="selectAnswer(question.id, String.fromCharCode(65 + idx))"
                    class="w-5 h-5 text-blue-600"
                  />
                  <span class="ml-3 text-gray-800">{{ String.fromCharCode(65 + idx) }}. {{ option }}</span>
                </label>
              </div>

              <!-- 多选题 -->
              <div v-else-if="question.type === 2" class="space-y-3">
                <label
                  v-for="(option, idx) in getOptions(question)"
                  :key="idx"
                  :class="[
                    'flex items-center p-4 rounded-lg border-2 cursor-pointer transition-all',
                    (answers[question.id] || '').split('').includes(String.fromCharCode(65 + idx))
                      ? 'border-purple-500 bg-purple-50'
                      : 'border-gray-200 hover:border-purple-300'
                  ]"
                >
                  <input
                    type="checkbox"
                    :checked="(answers[question.id] || '').split('').includes(String.fromCharCode(65 + idx))"
                    @change="selectMultipleAnswer(question.id, String.fromCharCode(65 + idx))"
                    class="w-5 h-5 text-purple-600"
                  />
                  <span class="ml-3 text-gray-800">{{ String.fromCharCode(65 + idx) }}. {{ option }}</span>
                </label>
                <p class="text-sm text-gray-500 mt-2">（多选）</p>
              </div>

              <!-- 判断题 -->
              <div v-else-if="question.type === 3" class="flex space-x-4">
                <label
                  :class="[
                    'flex-1 flex items-center justify-center p-4 rounded-lg border-2 cursor-pointer transition-all',
                    answers[question.id] === '对'
                      ? 'border-green-500 bg-green-50'
                      : 'border-gray-200 hover:border-green-300'
                  ]"
                >
                  <input type="radio" value="对" :checked="answers[question.id] === '对'" @change="selectAnswer(question.id, '对')" class="w-5 h-5 text-green-600" />
                  <span class="ml-3 text-lg font-medium text-gray-800">正确</span>
                </label>
                <label
                  :class="[
                    'flex-1 flex items-center justify-center p-4 rounded-lg border-2 cursor-pointer transition-all',
                    answers[question.id] === '错'
                      ? 'border-red-500 bg-red-50'
                      : 'border-gray-200 hover:border-red-300'
                  ]"
                >
                  <input type="radio" value="错" :checked="answers[question.id] === '错'" @change="selectAnswer(question.id, '错')" class="w-5 h-5 text-red-600" />
                  <span class="ml-3 text-lg font-medium text-gray-800">错误</span>
                </label>
              </div>

              <!-- 填空/简答/编程 -->
              <div v-else class="space-y-4">
                <textarea
                  v-model="answers[question.id]"
                  rows="4"
                  placeholder="请输入答案"
                  class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 resize-none"
                ></textarea>
              </div>
            </div>

            <div class="flex justify-center pt-4 pb-8">
              <button
                @click="handleSubmit"
                :disabled="isSubmitted"
                class="px-8 py-3 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors disabled:opacity-50"
              >
                提交试卷
              </button>
            </div>
          </div>
        </div>

        <div v-if="!showAll" class="w-64">
          <div class="bg-white rounded-xl shadow-lg p-6 sticky top-20">
            <h3 class="text-lg font-semibold text-gray-800 mb-4">题目导航</h3>
            <p class="text-sm text-gray-500 mb-4">已答：{{ answeredCount }} / {{ questions.length }}</p>
            <div class="grid grid-cols-5 gap-2">
              <button
                v-for="(question, index) in questions"
                :key="question.id"
                @click="goToQuestion(index)"
                :class="[
                  'w-10 h-10 rounded-lg text-sm font-medium transition-all',
                  index === currentIndex ? 'bg-blue-600 text-white' :
                  answers[question.id] ? 'bg-green-500 text-white' :
                  'bg-gray-100 text-gray-600 hover:bg-gray-200'
                ]"
              >
                {{ index + 1 }}
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
