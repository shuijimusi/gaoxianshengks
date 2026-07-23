<script setup lang="ts">
import { ref, computed } from 'vue';
import { useRouter } from 'vue-router';
import { getQuestionList, getQuestionForSelector, createQuestion, addQuestion } from '../api/question';
import { createPaper } from '../api/paper';
import type { Question, QuestionRequest } from '../types';
import { QUESTION_TYPES, DIFFICULTY_LEVELS, CHAPTERS } from '../types';
import { ElMessage, ElDialog, ElTable, ElTableColumn, ElPagination, ElInput, ElSelect, ElOption, ElCheckbox, ElButton } from 'element-plus';
import Sidebar from '../components/Sidebar.vue';
import { usePaperCreateStore } from '../stores/paperCreate';
import { storeToRefs } from 'pinia';

const router = useRouter();
const paperCreateStore = usePaperCreateStore();
const { form, selectedChapters, questionTypes, selectedQuestions, totalScore, totalQuestions } = storeToRefs(paperCreateStore);

const showQuestionSelector = ref(false);
const selectorType = ref<number | null>(null);
const selectorQuestions = ref<Question[]>([]);
const selectorPage = ref(1);
const selectorPageSize = ref(10);
const selectorTotal = ref(0);
const selectorLoading = ref(false);
const selectedForAdd = ref<number[]>([]);
const selectorChapter = ref<string | undefined>(undefined);
const selectorDifficulty = ref<number | undefined>(undefined);

const showWriteDialog = ref(false);
const writeForm = ref({
  type: 1,
  difficulty: 2,
  content: '',
  options: '',
  answer: '',
  analysis: '',
  score: 2,
  tags: '',
  chapter: '',
});

const showSmartSelect = ref(false);

async function loadQuestionsForSelector(type: number) {
  selectorLoading.value = true;
  selectorType.value = type;
  selectedForAdd.value = [];

  const res = await getQuestionForSelector({
    pageNum: selectorPage.value,
    pageSize: selectorPageSize.value,
    chapter: selectorChapter.value,
    difficulty: selectorDifficulty.value,
    type,
  });

  if (res.code === 200 && res.data) {
    selectorQuestions.value = res.data.rows || res.data.records || [];
    selectorTotal.value = Number(res.data.total) || 0;
  } else {
    selectorQuestions.value = [];
    selectorTotal.value = 0;
  }
  selectorLoading.value = false;
}

function openQuestionSelector(type: number) {
  selectorPage.value = 1;
  loadQuestionsForSelector(type);
  showQuestionSelector.value = true;
}

function toggleSelectQuestion(id: number) {
  const index = selectedForAdd.value.indexOf(id);
  if (index > -1) {
    selectedForAdd.value.splice(index, 1);
  } else {
    selectedForAdd.value.push(id);
  }
}

function confirmAddSelected() {
  const toAdd = selectorQuestions.value.filter(q => selectedForAdd.value.includes(q.id));
  paperCreateStore.addQuestions(toAdd);
  showQuestionSelector.value = false;
  selectedForAdd.value = [];
  ElMessage.success(`已添加 ${toAdd.length} 道题目`);
}

function isQuestionAdded(id: number) {
  return selectedQuestions.value.some(q => q.id === id);
}

function isSelectable(row: any) {
  return !isQuestionAdded(row.id);
}

function openWriteDialog(type: number) {
  writeForm.value = { 
    type, 
    difficulty: 2, 
    score: questionTypes.value.find(t => t.type === type)?.score || 2,
    content: '',
    options: '',
    answer: '',
    analysis: '',
    tags: '',
    chapter: '',
  };
  showWriteDialog.value = true;
}

async function saveWrittenQuestion() {
  if (!writeForm.value.content || !writeForm.value.answer) {
    ElMessage.warning('请填写题目内容和答案');
    return;
  }
  
  const questionData = {
    ...writeForm.value,
    chapter: writeForm.value.chapter || selectedChapters.value[0] || CHAPTERS[0],
  } as QuestionRequest;
  
  const res = await createQuestion(questionData);
  if (res.code === 200 && res.data) {
    paperCreateStore.addQuestions([res.data]);
    ElMessage.success('题目创建成功并已添加到试卷');
    showWriteDialog.value = false;
    writeForm.value = { 
      type: 1, 
      difficulty: 2,
      content: '',
      options: '',
      answer: '',
      analysis: '',
      score: 2,
      tags: '',
      chapter: '',
    };
  } else {
    ElMessage.error('创建失败');
  }
}

function openSmartSelect() {
  showSmartSelect.value = true;
}

async function smartSelectQuestions() {
  const requirements: Record<number, { count: number; score: number }> = {};
  questionTypes.value.forEach(t => {
    if (t.count > 0) {
      requirements[t.type] = { count: t.count, score: t.score };
    }
  });
  
  if (Object.keys(requirements).length === 0) {
    ElMessage.warning('请先设置各题型数量');
    return;
  }
  
  const allQuestions: Question[] = [];
  let pageNum = 1;
  
  while (pageNum <= 10) {
    const res = await getQuestionList({
      pageNum,
      pageSize: 50,
    });
    
    if (res.code === 200 && res.data) {
      const questions = res.data.rows || res.data.records || [];
      if (questions.length === 0) break;
      
      const filtered = questions.filter((q: Question) => {
        if (selectedChapters.value.length > 0 && !selectedChapters.value.includes(q.chapter)) {
          return false;
        }
        return requirements[q.type] !== undefined;
      });
      
      filtered.forEach(q => {
        if (!allQuestions.find(aq => aq.id === q.id)) {
          allQuestions.push(q);
        }
      });
      
      if (allQuestions.length >= 500) break;
    } else {
      break;
    }
    pageNum++;
  }
  
  const selected: Question[] = [];
  Object.entries(requirements).forEach(([typeStr, req]) => {
    const type = parseInt(typeStr);
    const pool = allQuestions.filter(q => q.type === type);
    const shuffled = pool.sort(() => Math.random() - 0.5);
    const selectedForType = shuffled.slice(0, req.count);
    
    selectedForType.forEach(q => {
      const newQ = { ...q, score: req.score };
      if (!selected.find(sq => sq.id === q.id)) {
        selected.push(newQ);
      }
    });
  });
  
  selectedQuestions.value = [...selectedQuestions.value, ...selected];
  showSmartSelect.value = false;
  ElMessage.success(`智能选题成功，添加了 ${selected.length} 道题目`);
}

function removeQuestion(id: number) {
  paperCreateStore.removeQuestion(id);
  ElMessage.success('已移除');
}

function clearAllQuestions() {
  paperCreateStore.clearAll();
  ElMessage.success('已清空所有题目');
}

async function handleSubmit() {
  if (!form.value.title) {
    ElMessage.warning('请输入试卷标题');
    return;
  }
  
  if (selectedQuestions.value.length === 0) {
    ElMessage.warning('请添加至少一道题目');
    return;
  }
  
  const questionIdScore = selectedQuestions.value.map(q => ({
    questionId: q.id,
    questionType: q.type,
    questionScore: q.score,
  }));
  
  const res = await createPaper({
    title: form.value.title,
    description: form.value.description || '',
    totalScore: totalScore.value,
    duration: form.value.duration || 90,
    passScore: Math.round(totalScore.value * 0.6),
    questionCount: selectedQuestions.value.length,
    questionIdScore,
  });
  
  if (res.code === 200) {
    ElMessage.success('试卷创建成功');
    paperCreateStore.reset();
    router.push('/papers');
  } else {
    ElMessage.error(res.msg || res.message || '创建失败');
  }
}

function getTypeName(type: number) {
  return QUESTION_TYPES[type] || type;
}

function getDifficultyName(difficulty: number) {
  return DIFFICULTY_LEVELS[difficulty] || difficulty;
}
</script>

<template>
  <div class="min-h-screen bg-gray-100">
    <Sidebar />
    
    <main class="ml-64 p-8">
      <div class="flex items-center justify-between mb-8">
        <div>
          <h1 class="text-3xl font-bold text-gray-800">创建试卷</h1>
          <p class="text-gray-600 mt-2">选择章节和题目，定制专属试卷</p>
        </div>
        <div class="flex items-center space-x-4">
          <div class="bg-blue-100 px-6 py-3 rounded-lg">
            <span class="text-blue-800 font-medium">总分：</span>
            <span class="text-blue-600 text-xl font-bold">{{ totalScore }}</span>
            <span class="text-blue-800 font-medium ml-2">分</span>
          </div>
          <div class="bg-green-100 px-6 py-3 rounded-lg">
            <span class="text-green-800 font-medium">题数：</span>
            <span class="text-green-600 text-xl font-bold">{{ totalQuestions }}</span>
            <span class="text-green-800 font-medium ml-2">道</span>
          </div>
        </div>
      </div>

      <div class="grid grid-cols-3 gap-6">
        <div class="col-span-1 space-y-6">
          <div class="bg-white rounded-xl shadow-lg p-6">
            <h2 class="text-lg font-semibold text-gray-800 mb-4">试卷信息</h2>
            <div class="space-y-4">
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">试卷标题 *</label>
                <ElInput v-model="form.title" placeholder="请输入试卷标题" />
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">试卷描述</label>
                <ElInput v-model="form.description" type="textarea" :rows="3" placeholder="请输入试卷描述" />
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">考试时长（分钟）</label>
                <ElInput v-model.number="form.duration" type="number" placeholder="90" />
              </div>
            </div>
          </div>

          <div class="bg-white rounded-xl shadow-lg p-6">
            <h2 class="text-lg font-semibold text-gray-800 mb-4">选择章节</h2>
            <ElCheckboxGroup v-model="selectedChapters">
              <div class="space-y-2">
                <ElCheckbox
                  v-for="chapter in CHAPTERS"
                  :key="chapter"
                  :label="chapter"
                />
              </div>
            </ElCheckboxGroup>
          </div>

          <div class="bg-white rounded-xl shadow-lg p-6">
            <h2 class="text-lg font-semibold text-gray-800 mb-4">题型设置</h2>
            <div class="space-y-4">
              <div
                v-for="qType in questionTypes"
                :key="qType.type"
                class="flex items-center justify-between p-3 bg-gray-50 rounded-lg"
              >
                <div class="flex items-center space-x-3">
                  <span class="font-medium text-gray-800">{{ qType.name }}</span>
                </div>
                <div class="flex items-center space-x-3">
                  <div class="flex items-center">
                    <span class="text-sm text-gray-500 mr-2">数量</span>
                    <ElInput v-model.number="qType.count" type="number" :min="0" :max="qType.maxCount" class="w-20" />
                  </div>
                  <div class="flex items-center">
                    <span class="text-sm text-gray-500 mr-2">每题</span>
                    <ElInput v-model.number="qType.score" type="number" :min="1" class="w-20" />
                    <span class="text-sm text-gray-500 ml-1">分</span>
                  </div>
                  <div class="flex space-x-2">
                    <button
                      @click="openQuestionSelector(qType.type)"
                      class="px-3 py-1 bg-blue-500 text-white text-sm rounded hover:bg-blue-600"
                    >
                      题库选
                    </button>
                    <button
                      @click="openWriteDialog(qType.type)"
                      class="px-3 py-1 bg-green-500 text-white text-sm rounded hover:bg-green-600"
                    >
                      现场写
                    </button>
                  </div>
                </div>
              </div>
            </div>
            <button
              @click="openSmartSelect"
              class="w-full mt-4 py-3 bg-purple-500 text-white font-medium rounded-lg hover:bg-purple-600 transition-colors"
            >
              🤖 智能选题
            </button>
          </div>
        </div>

        <div class="col-span-2">
          <div class="bg-white rounded-xl shadow-lg">
            <div class="p-6 border-b border-gray-200 flex items-center justify-between">
              <h2 class="text-xl font-semibold text-gray-800">已选题目</h2>
              <button
                v-if="selectedQuestions.length > 0"
                @click="clearAllQuestions"
                class="text-red-600 hover:text-red-700 text-sm"
              >
                清空所有
              </button>
            </div>

            <div v-if="selectedQuestions.length === 0" class="p-12 text-center">
              <div class="text-gray-400 mb-4">
                <svg class="w-16 h-16 mx-auto" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5" d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z" />
                </svg>
              </div>
              <p class="text-gray-500">暂无题目，请从左侧添加</p>
            </div>

            <div v-else class="overflow-x-auto">
              <ElTable :data="selectedQuestions" border>
                <ElTableColumn prop="id" label="ID" width="60" />
                <ElTableColumn prop="type" label="类型" width="100">
                  <template #default="{ row }">
                    <span class="px-2 py-1 bg-blue-100 text-blue-700 text-sm rounded">
                      {{ getTypeName(row.type) }}
                    </span>
                  </template>
                </ElTableColumn>
                <ElTableColumn prop="chapter" label="章节" width="150" />
                <ElTableColumn prop="difficulty" label="难度" width="80">
                  <template #default="{ row }">
                    <span class="px-2 py-1 text-sm rounded" :class="row.difficulty === 1 ? 'bg-green-100 text-green-700' : row.difficulty === 2 ? 'bg-yellow-100 text-yellow-700' : 'bg-red-100 text-red-700'">
                      {{ getDifficultyName(row.difficulty) }}
                    </span>
                  </template>
                </ElTableColumn>
                <ElTableColumn prop="content" label="题目内容" min-width="300">
                  <template #default="{ row }">
                    <p class="text-sm text-gray-800 line-clamp-2">{{ row.content }}</p>
                  </template>
                </ElTableColumn>
                <ElTableColumn prop="score" label="分数" width="80">
                  <template #default="{ row }">
                    <span class="font-medium text-gray-800">{{ row.score }}分</span>
                  </template>
                </ElTableColumn>
                <ElTableColumn label="操作" width="80">
                  <template #default="{ row }">
                    <button @click="removeQuestion(row.id)" class="text-red-600 hover:text-red-700">
                      <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" />
                      </svg>
                    </button>
                  </template>
                </ElTableColumn>
              </ElTable>
            </div>

            <div class="p-6 border-t border-gray-200 flex items-center justify-end space-x-4">
              <button @click="router.push('/papers')" class="px-6 py-3 bg-gray-200 text-gray-700 font-medium rounded-lg hover:bg-gray-300 transition-colors">
                返回
              </button>
              <button
                @click="handleSubmit"
                class="px-6 py-3 bg-blue-600 text-white font-medium rounded-lg hover:bg-blue-700 transition-colors shadow-lg"
              >
                创建试卷
              </button>
            </div>
          </div>
        </div>
      </div>
    </main>

    <ElDialog title="从题库选择题目" v-model="showQuestionSelector" width="900px">
      <div class="mb-4 flex gap-4">
        <span class="text-sm text-gray-600">当前选择：{{ getTypeName(selectorType || 0) }}</span>
        <div class="flex-1"></div>
        <div class="w-48">
          <ElSelect
            v-model="selectorChapter"
            placeholder="选择章节"
            clearable
            @change="() => { selectorPage = 1; loadQuestionsForSelector(selectorType || 1); }"
          >
            <ElOption v-for="chapter in CHAPTERS" :key="chapter" :label="chapter" :value="chapter" />
          </ElSelect>
        </div>
        <div class="w-36">
          <ElSelect
            v-model="selectorDifficulty"
            placeholder="选择难度"
            clearable
            @change="() => { selectorPage = 1; loadQuestionsForSelector(selectorType || 1); }"
          >
            <ElOption label="简单" :value="1" />
            <ElOption label="中等" :value="2" />
            <ElOption label="困难" :value="3" />
          </ElSelect>
        </div>
      </div>
      <ElTable :data="selectorQuestions" border v-loading="selectorLoading" @selection-change="(val: any[]) => selectedForAdd = val.map(v => v.id)">
        <ElTableColumn type="selection" width="55" :selectable="isSelectable" />
        <ElTableColumn prop="id" label="ID" width="60" />
        <ElTableColumn prop="chapter" label="章节" width="150" />
        <ElTableColumn prop="difficulty" label="难度" width="80">
          <template #default="{ row }">
            <span class="px-2 py-1 text-sm rounded" :class="row.difficulty === 1 ? 'bg-green-100 text-green-700' : row.difficulty === 2 ? 'bg-yellow-100 text-yellow-700' : 'bg-red-100 text-red-700'">
              {{ getDifficultyName(row.difficulty) }}
            </span>
          </template>
        </ElTableColumn>
        <ElTableColumn prop="content" label="题目内容">
          <template #default="{ row }">
            <p class="text-sm text-gray-800 line-clamp-2">{{ row.content }}</p>
          </template>
        </ElTableColumn>
        <ElTableColumn prop="score" label="分数" width="80" />
        <ElTableColumn label="状态" width="80">
          <template #default="{ row }">
            <span v-if="isQuestionAdded(row.id)" class="px-2 py-1 bg-gray-100 text-gray-500 text-xs rounded">已添加</span>
          </template>
        </ElTableColumn>
      </ElTable>
      <ElPagination
        @size-change="(size: number) => { selectorPageSize = size; selectorPage = 1; loadQuestionsForSelector(selectorType || 1); }"
        @current-change="(page: number) => { selectorPage = page; loadQuestionsForSelector(selectorType || 1); }"
        :current-page="selectorPage"
        :page-sizes="[10, 20, 50]"
        :page-size="selectorPageSize"
        :total="selectorTotal"
        layout="total, sizes, prev, pager, next, jumper"
        class="mt-4"
      />
      <template #footer>
        <div class="flex justify-end space-x-2">
          <ElButton @click="showQuestionSelector = false">取消</ElButton>
          <ElButton type="primary" @click="confirmAddSelected">确认添加（{{ selectedForAdd.length }}）</ElButton>
        </div>
      </template>
    </ElDialog>

    <ElDialog title="现场编写题目" v-model="showWriteDialog" width="600px">
      <div class="space-y-4">
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-2">题目类型</label>
          <span class="px-3 py-1 bg-blue-100 text-blue-700 rounded">{{ getTypeName(writeForm.type || 1) }}</span>
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-2">章节</label>
          <ElSelect v-model="writeForm.chapter" placeholder="选择章节">
            <ElOption v-for="chapter in CHAPTERS" :key="chapter" :label="chapter" :value="chapter" />
          </ElSelect>
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-2">难度</label>
          <ElSelect v-model="writeForm.difficulty" placeholder="选择难度">
            <ElOption label="简单" :value="1" />
            <ElOption label="中等" :value="2" />
            <ElOption label="困难" :value="3" />
          </ElSelect>
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-2">题目内容 *</label>
          <ElInput v-model="writeForm.content" type="textarea" :rows="4" placeholder="请输入题目内容" />
        </div>
        <div v-if="[1, 2].includes(writeForm.type || 0)">
          <label class="block text-sm font-medium text-gray-700 mb-2">选项（每行一个）</label>
          <ElInput v-model="writeForm.options" type="textarea" :rows="4" placeholder="选项A&#10;选项B&#10;选项C&#10;选项D" />
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-2">答案 *</label>
          <ElInput v-model="writeForm.answer" placeholder="请输入答案" />
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-2">解析</label>
          <ElInput v-model="writeForm.analysis" type="textarea" :rows="3" placeholder="请输入解析" />
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-2">分数</label>
          <ElInput v-model.number="writeForm.score" type="number" placeholder="请输入分数" />
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-2">标签</label>
          <ElInput v-model="writeForm.tags" placeholder="多个标签用逗号分隔" />
        </div>
      </div>
      <template #footer>
        <div class="flex justify-end space-x-2">
          <ElButton @click="showWriteDialog = false">取消</ElButton>
          <ElButton type="primary" @click="saveWrittenQuestion">保存并添加</ElButton>
        </div>
      </template>
    </ElDialog>

    <ElDialog title="智能选题" v-model="showSmartSelect" width="500px">
      <div class="space-y-4">
        <p class="text-gray-600">智能选题将根据您设置的题型数量和章节，从题库中随机抽取题目。</p>
        <div class="bg-gray-50 p-4 rounded-lg">
          <h4 class="font-medium text-gray-800 mb-2">选题配置：</h4>
          <div class="space-y-2">
            <div
              v-for="qType in questionTypes.filter(t => t.count > 0)"
              :key="qType.type"
              class="flex justify-between text-sm"
            >
              <span class="text-gray-600">{{ qType.name }}</span>
              <span class="font-medium">
                {{ qType.count }} 道 × {{ qType.score }} 分 = {{ qType.count * qType.score }} 分
              </span>
            </div>
          </div>
        </div>
        <div v-if="selectedChapters.length > 0" class="bg-blue-50 p-3 rounded-lg">
          <span class="text-sm text-blue-700">已选择章节：{{ selectedChapters.join(', ') }}</span>
        </div>
      </div>
      <template #footer>
        <div class="flex justify-end space-x-2">
          <ElButton @click="showSmartSelect = false">取消</ElButton>
          <ElButton type="primary" @click="smartSelectQuestions">开始选题</ElButton>
        </div>
      </template>
    </ElDialog>
  </div>
</template>