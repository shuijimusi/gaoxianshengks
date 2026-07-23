﻿<script setup lang="ts">
import { ref, watch } from 'vue';
import { useRouter } from 'vue-router';
import { ElSelect, ElOption, ElMessage, ElPagination } from 'element-plus';
import Sidebar from '../components/Sidebar.vue';
import { searchClass, getExamByCourse, checkExamStatus, type ClassItem } from '../api/paper';
import type { ExamPaper } from '../types';

const router = useRouter();

const courses = ref<ClassItem[]>([]);
const selectedCourseId = ref<number | null>(null);
const exams = ref<ExamPaper[]>([]);
const loading = ref(false);

const examPageNum = ref(1);
const examPageSize = ref(10);
const examTotal = ref(0);

async function handleSearch(query: string) {
  loading.value = true;
  try {
    const res = await searchClass({
      classname: query,
      pageNum: 1,
      pageSize: 100
    });
     if (res.code === 200) {
        const rows = res.data?.rows || res.data?.records || [];
        courses.value = rows.map((item: any) => {
          if (typeof item === 'string') {
            return { id: parseInt(item, 10) || 0, courseName: item };
          }
          return item;
        });
     } else {
      ElMessage.error(res.msg || '搜索班级失败');
    }
  } catch (error) {
    ElMessage.error('搜索班级失败');
  } finally {
    loading.value = false;
  }
}

watch(selectedCourseId, (newVal, oldVal) => {
  if (newVal === oldVal) return;
  examPageNum.value = 1;
  if (newVal) {
    loadExams();
  } else {
    exams.value = [];
    examTotal.value = 0;
  }
});

async function loadExams() {
  if (!selectedCourseId.value) return;
  
  loading.value = true;
  try {
    const res = await getExamByCourse({
      courseId: selectedCourseId.value,
      pageNum: examPageNum.value,
      pageSize: examPageSize.value
    });
    if (res.code === 200) {
      exams.value = res.data?.rows || res.data?.records || [];
      examTotal.value = parseInt(String(res.data?.total || '0'));
    } else {
      ElMessage.error(res.msg || '获取考试列表失败');
    }
  } catch (error) {
    ElMessage.error('获取考试列表失败');
  } finally {
    loading.value = false;
  }
}

function handlePageChange(pageNum: number) {
  examPageNum.value = pageNum;
  loadExams();
}

function handlePageSizeChange(pageSize: number) {
  examPageSize.value = pageSize;
  examPageNum.value = 1;
  loadExams();
}

function getStatusText(status: number | undefined) {
  switch (status) {
    case 0: return '未开始';
    case 1: return '进行中';
    case 2: return '已结束';
    default: return '';
  }
}

function getStatusClass(status: number | undefined) {
  switch (status) {
    case 0: return 'bg-gray-100 text-gray-700';
    case 1: return 'bg-blue-100 text-blue-700';
    case 2: return 'bg-green-100 text-green-700';
    default: return 'bg-gray-100 text-gray-700';
  }
}

function joinExam(id: number, endTime?: string) {
  router.push({ path: `/exam/${id}`, query: { endTime } });
}

async function viewResult(id: number) {
  const res = await checkExamStatus(id);
  if (res.code === 200) {
    if (res.data == null) {
      ElMessage.info('未参加考试');
    } else if (res.data === 1 || res.data === 2) {
      ElMessage.info('等待老师批改');
    } else if (res.data === 3) {
      router.push(`/results/${id}`);
    } else {
      ElMessage.error('无法获取考试状态');
    }
  } else {
    ElMessage.error(res.msg || '获取考试状态失败');
  }
}
</script>

<template>
  <div class="min-h-screen bg-gray-100">
    <Sidebar />
    
    <main class="ml-64 p-8">
      <div class="mb-8">
        <h1 class="text-3xl font-bold text-gray-800">我的考试</h1>
        <p class="text-gray-600 mt-2">查看和参加您的所有考试</p>
      </div>

      <div class="mb-6">
        <label class="block text-sm font-medium text-gray-700 mb-2">选择班级</label>
        <ElSelect
          v-model="selectedCourseId"
          placeholder="请输入班级名称搜索"
          filterable
          remote
          :remote-method="handleSearch"
          :loading="loading"
          clearable
          style="width: 300px"
        >
          <ElOption
            v-for="course in courses"
            :key="course.id"
            :label="course.courseName"
            :value="course.id"
          />
        </ElSelect>
      </div>

      <div v-if="!selectedCourseId" class="text-center py-20">
        <svg class="w-20 h-20 text-gray-400 mx-auto mb-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 21V5a2 2 0 00-2-2H7a2 2 0 00-2 2v16m14 0h2m-2 0h-5m-9 0H3m2 0h5M9 7h1m-1 4h1m4-4h1m-1 4h1m-5 10v-5a1 1 0 011-1h2a1 1 0 011 1v5m-4 0h4" />
        </svg>
        <h3 class="text-xl font-semibold text-gray-500 mb-2">请选择班级</h3>
        <p class="text-gray-400">输入班级名称搜索并选择班级</p>
      </div>

      <div v-else class="space-y-6">
        <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          <div
            v-for="exam in exams"
            :key="exam.id"
            class="bg-white rounded-xl shadow-lg overflow-hidden hover:shadow-xl transition-shadow"
          >
            <div class="p-6">
              <div class="flex items-start justify-between mb-4">
                <div>
                  <span
                    :class="[
                      'px-3 py-1 rounded-full text-xs font-medium mb-2 inline-block',
                      getStatusClass(exam.status)
                    ]"
                  >
                    {{ getStatusText(exam.status) }}
                  </span>
                  <h3 class="text-lg font-semibold text-gray-800">{{ exam.title }}</h3>
                </div>
              </div>

              <div class="space-y-3 text-sm text-gray-600 mb-6">
                <div class="flex items-center">
                  <svg class="w-4 h-4 mr-2 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 21V5a2 2 0 00-2-2H7a2 2 0 00-2 2v16m14 0h2m-2 0h-5m-9 0H3m2 0h5M9 7h1m-1 4h1m4-4h1m-1 4h1m-5 10v-5a1 1 0 011-1h2a1 1 0 011 1v5m-4 0h4" />
                  </svg>
                  <span>{{ exam.courseName }}</span>
                </div>
                <div class="flex items-center">
                  <svg class="w-4 h-4 mr-2 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z" />
                  </svg>
                  <span>开始时间: {{ exam.startTime }}</span>
                </div>
                <div class="flex items-center">
                  <svg class="w-4 h-4 mr-2 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z" />
                  </svg>
                  <span>结束时间: {{ exam.endTime }}</span>
                </div>
              </div>

              <div class="flex space-x-3">
                <button
                  v-if="exam.status === 1"
                  @click="joinExam(exam.id, exam.endTime)"
                  class="flex-1 px-4 py-3 bg-blue-600 text-white rounded-lg font-medium hover:bg-blue-700 transition-colors"
                >
                  参加考试
                </button>
                <button
                  v-else-if="exam.status === 2"
                  @click="viewResult(exam.id)"
                  class="flex-1 px-4 py-3 bg-gray-600 text-white rounded-lg font-medium hover:bg-gray-700 transition-colors"
                >
                  查看成绩
                </button>
                <button
                  v-else
                  class="flex-1 px-4 py-3 bg-gray-100 text-gray-500 rounded-lg font-medium cursor-not-allowed"
                  disabled
                >
                  未开始
                </button>
              </div>
            </div>
          </div>
        </div>

        <div v-if="examTotal > 0" class="flex justify-center">
          <ElPagination
            v-model:current-page="examPageNum"
            v-model:page-size="examPageSize"
            :total="examTotal"
            :page-sizes="[10, 20, 50]"
            layout="total, sizes, prev, pager, next, jumper"
            @current-change="handlePageChange"
            @size-change="handlePageSizeChange"
          />
        </div>
      </div>

      <div v-if="selectedCourseId && exams.length === 0" class="text-center py-20">
        <svg class="w-20 h-20 text-gray-400 mx-auto mb-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 19v-6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2a2 2 0 002-2zm0 0V9a2 2 0 012-2h2a2 2 0 012 2v10m-6 0a2 2 0 002 2h2a2 2 0 002-2m0 0V5a2 2 0 012-2h2a2 2 0 012 2v14a2 2 0 01-2 2h-2a2 2 0 01-2-2z" />
        </svg>
        <h3 class="text-xl font-semibold text-gray-500 mb-2">暂无考试</h3>
        <p class="text-gray-400">该班级暂无考试安排</p>
      </div>
    </main>
  </div>
</template>
