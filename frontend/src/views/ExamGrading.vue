<script setup lang="ts">
import { ref, watch } from 'vue';
import { useRoute } from 'vue-router';
import { ElSelect, ElOption, ElMessage, ElPagination } from 'element-plus';
import Sidebar from '../components/Sidebar.vue';
import { searchGradingClass, getGradingExamList, getGradingStudentList, type ClassItem } from '../api/paper';
import type { ExamPaper } from '../types';

const route = useRoute();
const examId = route.params.examId ? Number(route.params.examId) : null;

const courses = ref<ClassItem[]>([]);
const selectedCourseId = ref<number | null>(null);
const exams = ref<ExamPaper[]>([]);
const selectedExam = ref<ExamPaper | null>(null);
const loading = ref(false);
const students = ref<any[]>([]);
const studentLoading = ref(false);
const studentPageNum = ref(1);
const studentPageSize = ref(10);
const studentTotal = ref(0);

if (examId) {
  selectedExam.value = { id: examId, title: '加载中...' } as ExamPaper;
  loadStudents();
}

async function handleSearch(query: string) {
  loading.value = true;
  try {
    const res = await searchGradingClass({ classname: query, pageNum: 1, pageSize: 100 });
    if (res.code === 200) {
      const rows = res.data?.rows || res.data?.records || [];
      courses.value = rows.map((item: any) => typeof item === 'string' ? { id: parseInt(item, 10) || 0, courseName: item } : item);
    }
  } catch (error) {
    ElMessage.error('搜索班级失败');
  } finally {
    loading.value = false;
  }
}

function loadExams() {
  if (!selectedCourseId.value) { exams.value = []; return; }
  const course = courses.value.find(function(c) { return c.id === selectedCourseId.value; });
  if (!course) { exams.value = []; return; }
  loading.value = true;
  getGradingExamList({ courseName: course.courseName, pageNum: 1, pageSize: 100 })
    .then(res => {
      if (res.code === 200) {
        exams.value = (res.data?.rows || res.data?.records || []).map((item: any) => ({ id: Number(item.examId) || 0, title: item.examName }));
        if (exams.value.length === 0) {
          ElMessage.info('该班级暂无考试');
        }
      }
    })
    .catch(() => ElMessage.error('获取考试列表失败'))
    .finally(() => { loading.value = false; });
}

function selectExam(exam: ExamPaper) {
  selectedExam.value = exam;
  studentPageNum.value = 1;
  loadStudents();
}

async function loadStudents() {
  if (!selectedExam.value) return;
  studentLoading.value = true;
  try {
    const res = await getGradingStudentList(selectedExam.value.id, studentPageNum.value, studentPageSize.value);
    if (res.code === 200) {
      students.value = res.data?.rows || res.data?.records || [];
      studentTotal.value = parseInt(String(res.data?.total || '0'));
    }
  } catch (error) {
    ElMessage.error('获取学生列表失败');
  } finally {
    studentLoading.value = false;
  }
}

function handleStudentPageChange(page: number) {
  studentPageNum.value = page;
  loadStudents();
}

function handleStudentPageSizeChange(size: number) {
  studentPageSize.value = size;
  studentPageNum.value = 1;
  loadStudents();
}

function gradeStudent(student: any) {
  ElMessage.info('批改 ' + student.username + ' 的试卷');
}
</script>

<template>
  <div class="min-h-screen bg-gray-100">
    <Sidebar />
    <main class="ml-64 p-8">
      <div class="mb-8">
        <h1 class="text-3xl font-bold text-gray-800">试卷批改</h1>
        <p class="text-gray-600 mt-2">批改学生试卷</p>
      </div>

     <div v-if="!selectedExam" class="space-y-6">
        <div class="flex gap-6">
          <div class="flex-1 bg-white rounded-xl shadow-lg p-6">
         <label class="block text-sm font-medium text-gray-700 mb-2">选择班级</label>
         <ElSelect
           v-model="selectedCourseId"
           placeholder="请输入班级名称搜索"
           filterable
           remote
           :remote-method="handleSearch"
           :loading="loading"
           clearable
           @change="loadExams"
            style="width: 100%"
         >
           <ElOption v-for="course in courses" :key="course.id" :label="course.courseName" :value="course.id" />
         </ElSelect>
         </div>

          <div class="flex-1 bg-white rounded-xl shadow-lg p-6">
            <label class="block text-sm font-medium text-gray-700 mb-2">选择考试</label>
            <ElSelect
              v-model="selectedExam"
              placeholder="请先选择班级"
              placement="bottom"
              :disabled="!selectedCourseId || exams.length === 0"
              style="width: 100%"
              @change="selectExam"
            >
              <ElOption
                v-for="exam in exams"
                :key="exam.id"
                :label="exam.title + ' 编号' + exam.id"
                :value="exam"
              />
            </ElSelect>
          </div>
        </div>
      </div>
      <div v-if="selectedExam" class="bg-white rounded-xl shadow-lg p-6">
        <div class="flex items-center justify-between mb-6">
          <h2 class="text-xl font-semibold text-gray-800">{{ selectedExam.title }}</h2>
          <button
            v-if="!examId"
            @click="selectedExam = null; selectedCourseId = null; exams = []"
            class="px-4 py-2 border border-gray-300 text-gray-700 rounded-lg hover:bg-gray-50 text-sm"
          >
            返回选择
          </button>
        </div>
        <!-- 学生列表 -->
        <div v-if="studentLoading" class="text-center py-8">
          <p class="text-gray-500">加载中...</p>
        </div>
        <div v-else-if="students.length === 0" class="text-center py-8">
          <p class="text-gray-500">暂无学生提交</p>
        </div>
        <div v-else>
          <div class="divide-y divide-gray-200">
            <div
              v-for="student in students"
              :key="student.stuId"
              class="flex items-center justify-between py-4"
            >
              <div class="flex items-center space-x-4">
                <div class="w-10 h-10 bg-gray-200 rounded-full flex items-center justify-center">
                  <svg class="w-5 h-5 text-gray-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
                  </svg>
                </div>
                <div>
                  <p class="font-medium text-gray-800">{{ student.username }}</p>
                  <p class="text-sm text-gray-500">{{ student.submitTime }}</p>
                </div>
              </div>
              <div>
                <span
                  v-if="student.stuStatus === 3"
                  class="px-3 py-1 rounded-full text-sm bg-green-100 text-green-700"
                >
                  已批改
                </span>
                <button
                  v-else
                  @click="gradeStudent(student)"
                  class="px-4 py-2 bg-blue-600 text-white text-sm rounded-lg hover:bg-blue-700 transition-colors"
                >
                  批改
                </button>
              </div>
            </div>
          </div>

          <div class="flex justify-center mt-6">
            <ElPagination
              v-model:current-page="studentPageNum"
              v-model:page-size="studentPageSize"
              :total="studentTotal"
              :page-sizes="[5, 10, 20]"
              layout="total, prev, pager, next"
              @current-change="handleStudentPageChange"
              @size-change="handleStudentPageSizeChange"
            />
          </div>
        </div>
      </div>
    </main>
  </div>
</template>
