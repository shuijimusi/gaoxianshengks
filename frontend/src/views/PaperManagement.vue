<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { getPaperList, deletePaper, startExam } from '../api/paper';
import { getTeacherCourses } from '../api/course';
import type { ExamPaper } from '../types';
import type { Course } from '../api/course';
import { ElMessage, ElMessageBox, ElPagination, ElDialog, ElRadioGroup, ElRadioButton, ElDatePicker, ElSelect, ElOption } from 'element-plus';
import Sidebar from '../components/Sidebar.vue';
import { useUserStore } from '../stores/user';

const router = useRouter();
const userStore = useUserStore();
const papers = ref<ExamPaper[]>([]);
const loading = ref(false);
const pageNum = ref(1);
const pageSize = ref(10);
const total = ref(0);

const dialogVisible = ref(false);
const currentPaperId = ref(0);
const startMode = ref(0);
const selectedTime = ref('');
const selectedCourseId = ref<number | undefined>(undefined);
const courses = ref<Course[]>([]);

onMounted(async () => {
  await loadPapers();
  if (userStore.isAdmin || userStore.isTeacher) {
    await loadCourses();
  }
});

async function loadPapers() {
  loading.value = true;
  const res = await getPaperList({ pageNum: pageNum.value, pageSize: pageSize.value });
  if (res.code === 200 && res.data) {
    papers.value = res.data.rows || res.data.records || [];
    total.value = Number(res.data.total) || 0;
  } else {
    papers.value = [];
    total.value = 0;
  }
  loading.value = false;
}

async function loadCourses() {
  const res = await getTeacherCourses();
  if (res.code === 200 && res.data) {
    courses.value = res.data;
  }
}

function handlePageChange(page: number) {
  pageNum.value = page;
  loadPapers();
}

function handlePageSizeChange(size: number) {
  pageSize.value = size;
  pageNum.value = 1;
  loadPapers();
}

async function handleDelete(id: number) {
  await ElMessageBox.confirm('确定删除该试卷？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning',
  });
  
  const res = await deletePaper(id);
  if (res.code === 200) {
    ElMessage.success('删除成功');
    await loadPapers();
  } else {
    ElMessage.error(res.msg || res.message || '删除失败');
  }
}

function openStartExamDialog(paperId: number) {
  currentPaperId.value = paperId;
  startMode.value = 0;
  selectedTime.value = '';
  selectedCourseId.value = undefined;
  dialogVisible.value = true;
}

async function confirmStartExam() {
  const paper = papers.value.find(p => p.id === currentPaperId.value);
  if (!paper) {
    ElMessage.error('试卷不存在');
    return;
  }

  if (!selectedCourseId.value) {
    ElMessage.error('请选择班级');
    return;
  }

  const startTime = startMode.value === 0 ? null : Date.parse(selectedTime.value);

  const res = await startExam({
    paperId: currentPaperId.value,
    duration: paper.duration,
    startTime,
    courseId: selectedCourseId.value,
  });

  if (res.code === 200) {
    ElMessage.success('考试已发布');
    dialogVisible.value = false;
    await loadPapers();
  } else {
    ElMessage.error(res.msg || res.message || '发布考试失败');
  }
}

function viewDetail(paperId: number) {
  router.push(`/papers/${paperId}`);
}
</script>

<template>
  <div class="min-h-screen bg-gray-100">
    <Sidebar />
    
    <main class="ml-64 p-8">
      <div class="flex items-center justify-between mb-8">
        <div>
          <h1 class="text-3xl font-bold text-gray-800">试卷管理</h1>
          <p class="text-gray-600 mt-2">管理和查看所有试卷</p>
        </div>
        <button
          v-if="userStore.isAdmin || userStore.isTeacher"
          @click="router.push('/papers/generate')"
          class="px-6 py-3 bg-blue-600 text-white font-medium rounded-lg hover:bg-blue-700 transition-colors shadow-lg"
        >
          智能组卷
        </button>
      </div>

      <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        <div
          v-for="paper in papers"
          :key="paper.id"
          class="bg-white rounded-xl shadow-lg overflow-hidden hover:shadow-xl transition-shadow"
        >
          <div class="p-6">
            <div class="mb-4">
              <h3 class="text-lg font-semibold text-gray-800">{{ paper.title }}</h3>
            </div>
            
            <div v-if="paper.description" class="text-sm text-gray-500 mb-4 line-clamp-2">
              {{ paper.description }}
            </div>
            
            <div class="space-y-2 text-sm text-gray-600 mb-6">
              <p class="flex items-center">
                <svg class="w-4 h-4 mr-2 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z" />
                </svg>
                {{ paper.duration }}分钟
              </p>
              <p class="flex items-center">
                <svg class="w-4 h-4 mr-2 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z" />
                </svg>
                {{ paper.questionCount }}道题目
              </p>
              <p class="flex items-center">
                <svg class="w-4 h-4 mr-2 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 19v-6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2a2 2 0 002-2zm0 0V9a2 2 0 012-2h2a2 2 0 012 2v10m-6 0a2 2 0 002 2h2a2 2 0 002-2m0 0V5a2 2 0 012-2h2a2 2 0 012 2v14a2 2 0 01-2 2h-2a2 2 0 01-2-2z" />
                </svg>
                总分：{{ paper.totalScore }}分
              </p>
            </div>

            <div class="flex space-x-3">
              <button
                @click="viewDetail(paper.id)"
                class="flex-1 px-4 py-2 border border-gray-300 text-gray-700 rounded-lg hover:bg-gray-50 transition-colors"
              >
                查看详情
              </button>
              <button
                @click="openStartExamDialog(paper.id)"
                class="flex-1 px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors"
              >
                发布考试
              </button>
              <button
                v-if="userStore.isAdmin || userStore.isTeacher"
                @click="handleDelete(paper.id)"
                class="p-2 text-red-600 hover:bg-red-50 rounded-lg transition-colors"
              >
                <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" />
                </svg>
              </button>
            </div>
          </div>
        </div>

        <div
          v-if="papers.length === 0"
          class="bg-white/50 rounded-xl border-2 border-dashed border-gray-300 p-12 text-center"
        >
          <svg class="w-16 h-16 text-gray-400 mx-auto mb-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M7 21h10a2 2 0 002-2V9.414a1 1 0 00-.293-.707l-5.414-5.414A1 1 0 0012.586 3H7a2 2 0 00-2 2v14a2 2 0 002 2z" />
          </svg>
          <p class="text-gray-500">暂无试卷</p>
          <button
            v-if="userStore.isAdmin || userStore.isTeacher"
            @click="router.push('/papers/generate')"
            class="mt-4 px-6 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors"
          >
            智能组卷
          </button>
        </div>
      </div>

      <div v-if="total > 0" class="mt-8 flex justify-center">
        <ElPagination
          @size-change="handlePageSizeChange"
          @current-change="handlePageChange"
          :current-page="pageNum"
          :page-sizes="[10, 20, 50]"
          :page-size="pageSize"
          :total="total"
          layout="total, sizes, prev, pager, next, jumper"
        />
      </div>
    </main>

    <ElDialog title="发布考试" v-model="dialogVisible" width="500px">
      <div class="space-y-6">
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-2">开始方式</label>
          <ElRadioGroup v-model="startMode">
            <ElRadioButton :label="0">现在开始</ElRadioButton>
            <ElRadioButton :label="1">选定时间</ElRadioButton>
          </ElRadioGroup>
        </div>

        <div v-if="startMode === 1">
          <label class="block text-sm font-medium text-gray-700 mb-2">开始时间</label>
          <ElDatePicker
            v-model="selectedTime"
            type="datetime"
            placeholder="请选择开始时间"
            style="width: 100%"
          />
        </div>

        <div>
          <label class="block text-sm font-medium text-gray-700 mb-2">选择班级</label>
          <ElSelect v-model="selectedCourseId" placeholder="请选择班级" style="width: 100%">
            <ElOption
              v-for="course in courses"
              :key="course.id"
              :label="course.courseName"
              :value="course.id"
            />
          </ElSelect>
        </div>
      </div>

      <template #footer>
        <button
          @click="dialogVisible = false"
          class="px-6 py-2 border border-gray-300 text-gray-700 rounded-lg hover:bg-gray-50 transition-colors"
        >
          取消
        </button>
        <button
          @click="confirmStartExam"
          class="px-6 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors"
        >
          确定发布
        </button>
      </template>
    </ElDialog>
  </div>
</template>