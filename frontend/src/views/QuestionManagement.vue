<script setup lang="ts">
import { ref, computed, onMounted, watch } from 'vue';
import { getQuestionList, addQuestion, updateQuestion, deleteQuestion, getQuestionByImage } from '../api/question';
import type { Question, QuestionRequest } from '../types';
import { QUESTION_TYPES, DIFFICULTY_LEVELS, CHAPTERS } from '../types';
import { ElMessage, ElMessageBox, ElTable, ElTableColumn, ElPagination, ElInput, ElSelect, ElOption, ElLoading } from 'element-plus';
import Sidebar from '../components/Sidebar.vue';

const questions = ref<Question[]>([]);
const loading = ref(false);

const showDialog = ref(false);
const editMode = ref(false);
const form = ref<Partial<Question>>({
  type: 1,
  difficulty: 2,
});

// 选项列表（用于选择题）
const optionsList = ref<string[]>(['', '', '', '']);

const searchForm = ref({
  content: '',
  chapter: '',
  difficulty: undefined as number | undefined,
});

const pagination = ref({
  pageNum: 1,
  pageSize: 10,
  total: 0,
  current: 1,
  size: 10,
});

// 是否为选择题（单选或复选）
const isChoiceQuestion = computed(() => form.value.type === 1 || form.value.type === 2);
// 是否为判断题
const isTrueFalseQuestion = computed(() => form.value.type === 3);

onMounted(async () => {
  await loadQuestions();
});

// 监听题型变化，重置选项和答案
watch(() => form.value.type, (newType) => {
  if (newType === 1 || newType === 2) {
    // 选择题：如果选项为空，初始化4个空选项
    if (optionsList.value.length === 0) {
      optionsList.value = ['', '', '', ''];
    }
  } else if (newType === 3) {
    // 判断题：默认答案为"对"
    form.value.answer = '对';
  }
});

async function loadQuestions() {
  loading.value = true;
  const res = await getQuestionList({
    pageNum: pagination.value.pageNum,
    pageSize: pagination.value.pageSize,
    content: searchForm.value.content || undefined,
    chapter: searchForm.value.chapter || undefined,
    difficulty: searchForm.value.difficulty,
  });
  if (res.code === 200 && res.data) {
    questions.value = res.data.rows || res.data.records || [];
    pagination.value.total = Number(res.data.total) || 0;
    pagination.value.current = res.data.current || res.data.pageNum || 1;
    pagination.value.size = res.data.size || res.data.pageSize || 10;
  }
  loading.value = false;
}

function handlePageChange(pageNum: number) {
  pagination.value.pageNum = pageNum;
  loadQuestions();
}

function handlePageSizeChange(pageSize: number) {
  pagination.value.pageSize = pageSize;
  pagination.value.pageNum = 1;
  loadQuestions();
}

function handleSearch() {
  pagination.value.pageNum = 1;
  loadQuestions();
}

function handleReset() {
  searchForm.value = { content: '', chapter: '', difficulty: undefined };
  pagination.value.pageNum = 1;
  loadQuestions();
}

function openAddDialog() {
  editMode.value = false;
  form.value = { type: 1, difficulty: 2 };
  optionsList.value = ['', '', '', ''];
  showDialog.value = true;
}

async function openEditDialog(row: any) {
  const question = row as Question;
  editMode.value = true;
  
  const res = await getQuestionByImage(question.id);
  if (res.code === 200 && res.data) {
    form.value = { ...res.data };
    // 解析选项
    if (form.value.options) {
      try {
        const opts = typeof form.value.options === 'string' ? JSON.parse(form.value.options) : form.value.options;
        optionsList.value = Array.isArray(opts) ? [...opts] : [];
      } catch {
        optionsList.value = [];
      }
    } else {
      optionsList.value = [];
    }
  } else {
    form.value = { ...question };
    ElMessage.warning('获取题目详情失败，使用缓存数据');
  }
  
  showDialog.value = true;
}

function addOption() {
  optionsList.value.push('');
}

function removeOption(index: number) {
  if (optionsList.value.length <= 2) {
    ElMessage.warning('至少需要保留2个选项');
    return;
  }
  optionsList.value.splice(index, 1);
}

function getOptionLabel(index: number) {
  return String.fromCharCode(65 + index);
}

async function handleSubmit() {
  if (!form.value.content || !form.value.score) {
    ElMessage.warning('请填写题目内容和分数');
    return;
  }

  let questionData: any = { ...form.value };

  // 选择题：将选项数组转为JSON字符串
  if (isChoiceQuestion.value) {
    // 过滤掉空选项
    const validOptions = optionsList.value.filter(opt => opt.trim() !== '');
    if (validOptions.length < 2) {
      ElMessage.warning('请至少填写2个选项');
      return;
    }
    questionData.options = JSON.stringify(validOptions);
  } else if (isTrueFalseQuestion.value) {
    // 判断题选项固定
    questionData.options = JSON.stringify(['对', '错']);
  } else {
    // 其他题型不需要选项
    questionData.options = form.value.options || '';
  }
  
  if (editMode.value && form.value.id) {
    await updateQuestion(questionData as QuestionRequest);
    ElMessage.success('更新成功');
  } else {
    await addQuestion(questionData as QuestionRequest);
    ElMessage.success('添加成功');
  }
  
  showDialog.value = false;
  await loadQuestions();
}

async function handleDelete(id: number) {
  await ElMessageBox.confirm('确定删除该题目？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning',
  });
  
  await deleteQuestion(id);
  ElMessage.success('删除成功');
  await loadQuestions();
}

function getTypeName(type: number) {
  return QUESTION_TYPES[type] || type;
}

function getDifficultyName(difficulty: number) {
  return DIFFICULTY_LEVELS[difficulty] || difficulty;
}

function getDifficultyClass(difficulty: number) {
  const classes: Record<number, string> = {
    1: 'bg-green-100 text-green-700',
    2: 'bg-yellow-100 text-yellow-700',
    3: 'bg-red-100 text-red-700',
  };
  return classes[difficulty] || 'bg-gray-100 text-gray-700';
}
</script>

<template>
  <div class="min-h-screen bg-gray-100">
    <Sidebar />
    
    <main class="ml-64 p-8">
      <div class="flex items-center justify-between mb-8">
        <div>
          <h1 class="text-3xl font-bold text-gray-800">题库管理</h1>
          <p class="text-gray-600 mt-2">管理考试系统的所有题目</p>
        </div>
        <button
          @click="openAddDialog"
          class="px-6 py-3 bg-blue-600 text-white font-medium rounded-lg hover:bg-blue-700 transition-colors shadow-lg"
        >
          添加题目
        </button>
      </div>

      <div class="bg-white rounded-xl shadow-lg overflow-hidden">
        <div class="p-6 border-b border-gray-200">
          <div class="flex gap-4">
              <div class="flex-1">
                <label class="block text-sm font-medium text-gray-700 mb-2">题目内容</label>
                <ElInput
                  v-model="searchForm.content"
                  placeholder="搜索题目内容..."
                  clearable
                  @keyup.enter="handleSearch"
                />
              </div>
              <div class="w-64">
                <label class="block text-sm font-medium text-gray-700 mb-2">章节</label>
                <ElSelect
                  v-model="searchForm.chapter"
                  placeholder="选择章节"
                  clearable
                >
                  <ElOption v-for="chapter in CHAPTERS" :key="chapter" :label="chapter" :value="chapter" />
                </ElSelect>
              </div>
              <div class="w-48">
                <label class="block text-sm font-medium text-gray-700 mb-2">难度</label>
                <ElSelect
                  v-model="searchForm.difficulty"
                  placeholder="选择难度"
                  clearable
                >
                  <ElOption label="简单" :value="1" />
                  <ElOption label="中等" :value="2" />
                  <ElOption label="困难" :value="3" />
                </ElSelect>
              </div>
              <div class="flex items-end gap-2">
                <button
                  @click="handleSearch"
                  class="px-6 py-3 bg-blue-600 text-white font-medium rounded-lg hover:bg-blue-700 transition-colors"
                >
                  搜索
                </button>
                <button
                  @click="handleReset"
                  class="px-6 py-3 bg-gray-200 text-gray-700 font-medium rounded-lg hover:bg-gray-300 transition-colors"
                >
                  重置
                </button>
              </div>
            </div>
        </div>

        <div class="overflow-x-auto">
          <ElTable :data="questions" border v-loading="loading" stripe>
            <ElTableColumn prop="content" label="题目" min-width="200">
              <template #default="{ row }">
                <p class="text-sm text-gray-800 line-clamp-2">{{ row.content }}</p>
              </template>
            </ElTableColumn>
            <ElTableColumn prop="chapter" label="章节" width="180">
              <template #default="{ row }">
                <span class="px-3 py-1 bg-purple-100 text-purple-700 text-sm rounded-full">
                  {{ row.chapter }}
                </span>
              </template>
            </ElTableColumn>
            <ElTableColumn prop="type" label="类型" width="100">
              <template #default="{ row }">
                <span class="px-3 py-1 bg-blue-100 text-blue-700 text-sm rounded-full">
                  {{ getTypeName(row.type) }}
                </span>
              </template>
            </ElTableColumn>
            <ElTableColumn prop="difficulty" label="难度" width="100">
              <template #default="{ row }">
                <span :class="['px-3 py-1 text-sm rounded-full', getDifficultyClass(row.difficulty)]">
                  {{ getDifficultyName(row.difficulty) }}
                </span>
              </template>
            </ElTableColumn>
            <ElTableColumn prop="score" label="分数" width="80">
              <template #default="{ row }">
                <span class="text-sm font-medium text-gray-800">{{ row.score }}分</span>
              </template>
            </ElTableColumn>
            <ElTableColumn label="操作" width="160" fixed="right">
              <template #default="{ row }">
                <div class="flex items-center space-x-3">
                  <button
                    @click="openEditDialog(row)"
                    class="text-blue-600 hover:text-blue-700"
                  >
                    <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z" />
                    </svg>
                  </button>
                  <button
                    @click="handleDelete(row.id)"
                    class="text-red-600 hover:text-red-700"
                  >
                    <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" />
                    </svg>
                  </button>
                </div>
              </template>
            </ElTableColumn>
          </ElTable>
        </div>

        <div class="p-6 border-t border-gray-200">
          <ElPagination
            @size-change="handlePageSizeChange"
            @current-change="handlePageChange"
            :current-page="pagination.pageNum"
            :page-sizes="[10, 20, 50, 100]"
            :page-size="pagination.pageSize"
            :total="pagination.total"
            layout="total, sizes, prev, pager, next, jumper"
          />
        </div>

        <div v-if="questions.length === 0 && !loading" class="p-12 text-center">
          <svg class="w-16 h-16 text-gray-400 mx-auto mb-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z" />
          </svg>
          <p class="text-gray-500">暂无题目</p>
        </div>
      </div>

      <div
        v-if="showDialog"
        class="fixed inset-0 bg-black/50 flex items-center justify-center z-50"
        @click.self="showDialog = false"
      >
        <div class="bg-white rounded-xl shadow-2xl w-full max-w-2xl p-6 max-h-[90vh] overflow-y-auto">
          <h3 class="text-xl font-semibold text-gray-800 mb-6">
            {{ editMode ? '编辑题目' : '添加题目' }}
          </h3>
          
          <form @submit.prevent="handleSubmit" class="space-y-5">
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-2">题目类型</label>
              <select
                v-model="form.type"
                class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
              >
                <option v-for="(label, value) in QUESTION_TYPES" :key="value" :value="Number(value)">
                  {{ label }}
                </option>
              </select>
            </div>

            <div>
              <label class="block text-sm font-medium text-gray-700 mb-2">所属章节</label>
              <select
                v-model="form.chapter"
                class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
              >
                <option value="" disabled>请选择章节</option>
                <option v-for="chapter in CHAPTERS" :key="chapter" :value="chapter">
                  {{ chapter }}
                </option>
              </select>
            </div>

            <div>
              <label class="block text-sm font-medium text-gray-700 mb-2">难度等级</label>
              <select
                v-model="form.difficulty"
                class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
              >
                <option v-for="(label, value) in DIFFICULTY_LEVELS" :key="value" :value="Number(value)">
                  {{ label }}
                </option>
              </select>
            </div>

            <div>
              <label class="block text-sm font-medium text-gray-700 mb-2">题目内容</label>
              <textarea
                v-model="form.content"
                rows="4"
                placeholder="请输入题目内容"
                class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 resize-none"
              ></textarea>
            </div>

            <!-- 选择题选项 -->
            <div v-if="isChoiceQuestion">
              <div class="flex items-center justify-between mb-2">
                <label class="block text-sm font-medium text-gray-700">选项</label>
                <button
                  type="button"
                  @click="addOption"
                  class="text-sm text-blue-600 hover:text-blue-700 flex items-center"
                >
                  <svg class="w-4 h-4 mr-1" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4" />
                  </svg>
                  添加选项
                </button>
              </div>
              <div class="space-y-3">
                <div
                  v-for="(option, index) in optionsList"
                  :key="index"
                  class="flex items-center gap-3"
                >
                  <span class="flex-shrink-0 w-8 h-8 bg-blue-100 text-blue-700 rounded-full flex items-center justify-center font-medium text-sm">
                    {{ getOptionLabel(index) }}
                  </span>
                  <input
                    v-model="optionsList[index]"
                    type="text"
                    :placeholder="`选项${getOptionLabel(index)}内容`"
                    class="flex-1 px-4 py-2.5 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                  />
                  <button
                    type="button"
                    @click="removeOption(index)"
                    class="flex-shrink-0 text-red-500 hover:text-red-700 p-1"
                  >
                    <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" />
                    </svg>
                  </button>
                </div>
              </div>
              <p class="text-xs text-gray-500 mt-2">提示：单选题答案填A/B/C/D，多选题答案填如"ABC"</p>
            </div>

            <!-- 判断题答案 -->
            <div v-if="isTrueFalseQuestion">
              <label class="block text-sm font-medium text-gray-700 mb-2">正确答案</label>
              <div class="flex gap-4">
                <label
                  :class="[
                    'flex-1 flex items-center justify-center px-6 py-3 rounded-lg border-2 cursor-pointer transition-all',
                    form.answer === '对' ? 'border-green-500 bg-green-50 text-green-700' : 'border-gray-200 hover:border-green-300'
                  ]"
                >
                  <input
                    v-model="form.answer"
                    type="radio"
                    value="对"
                    class="hidden"
                  />
                  <span class="font-medium">对</span>
                </label>
                <label
                  :class="[
                    'flex-1 flex items-center justify-center px-6 py-3 rounded-lg border-2 cursor-pointer transition-all',
                    form.answer === '错' ? 'border-red-500 bg-red-50 text-red-700' : 'border-gray-200 hover:border-red-300'
                  ]"
                >
                  <input
                    v-model="form.answer"
                    type="radio"
                    value="错"
                    class="hidden"
                  />
                  <span class="font-medium">错</span>
                </label>
              </div>
            </div>

            <!-- 其他题型答案 -->
            <div v-if="!isChoiceQuestion && !isTrueFalseQuestion">
              <label class="block text-sm font-medium text-gray-700 mb-2">正确答案</label>
              <textarea
                v-model="form.answer"
                rows="2"
                placeholder="请输入正确答案"
                class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 resize-none"
              ></textarea>
            </div>

            <!-- 选择题答案 -->
            <div v-if="isChoiceQuestion">
              <label class="block text-sm font-medium text-gray-700 mb-2">正确答案</label>
              <input
                v-model="form.answer"
                type="text"
                :placeholder="form.type === 1 ? '如：A' : '如：ABC（多选）'"
                class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
              />
            </div>

            <div>
              <label class="block text-sm font-medium text-gray-700 mb-2">分数</label>
              <input
                v-model.number="form.score"
                type="number"
                min="1"
                placeholder="请输入分数"
                class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
              />
            </div>

            <div class="flex space-x-4 pt-4">
              <button
                type="button"
                @click="showDialog = false"
                class="flex-1 px-6 py-3 bg-gray-200 text-gray-700 font-medium rounded-lg hover:bg-gray-300 transition-colors"
              >
                取消
              </button>
              <button
                type="submit"
                class="flex-1 px-6 py-3 bg-blue-600 text-white font-medium rounded-lg hover:bg-blue-700 transition-colors"
              >
                {{ editMode ? '更新' : '添加' }}
              </button>
            </div>
          </form>
        </div>
      </div>
    </main>
  </div>
</template>