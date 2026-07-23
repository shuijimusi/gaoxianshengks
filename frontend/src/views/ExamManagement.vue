<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import type { ExamPaper } from '../types';
import { ElMessage, ElPagination } from 'element-plus';
import Sidebar from '../components/Sidebar.vue';
import { useUserStore } from '../stores/user';
import { getAllTeacherExams } from '../api/paper';

const router = useRouter();
const userStore = useUserStore();
const exams = ref<ExamPaper[]>([]);
const loading = ref(false);
const pageNum = ref(1);
const pageSize = ref(10);
const total = ref(0);

onMounted(async () => {
  await loadRecords();
});

async function loadRecords() {
  loading.value = true;
  try {
    const res = await getAllTeacherExams({
      pageNum: pageNum.value,
      pageSize: pageSize.value,
      userid: userStore.user?.id || 0
    });
    if (res.code === 200) {
      exams.value = res.data?.rows || [];
      total.value = parseInt(String(res.data?.total || '0'));
    } else {
      ElMessage.error(res.msg || '获取考试列表失败');
    }
  } catch (error) {
    ElMessage.error('获取考试列表失败');
  } finally {
    loading.value = false;
  }
}

function goToGrading(id: number) {
  router.push(`/grading/${id}`);
}

function handlePageChange(page: number) {
  pageNum.value = page;
  loadRecords();
}

function handlePageSizeChange(size: number) {
  pageSize.value = size;
  pageNum.value = 1;
  loadRecords();
}
</script>

<template>
  <div class="min-h-screen bg-gray-100">
    <Sidebar />
    
    <main class="ml-64 p-8">
      <div class="mb-8">
        <h1 class="text-3xl font-bold text-gray-800">考试管理</h1>
        <p class="text-gray-600 mt-2">管理所有考试记录和结果</p>
      </div>

      <div class="bg-white rounded-xl shadow-lg overflow-hidden">
        <div class="p-6 border-b border-gray-200">
          <h2 class="text-lg font-semibold text-gray-800">考试记录列表</h2>
        </div>

        <div class="divide-y divide-gray-200" v-loading="loading">
          <div
            v-for="exam in exams"
            :key="exam.id"
            class="p-6 hover:bg-gray-50"
          >
            <div class="flex items-start justify-between">
              <div class="flex-1">
                <div class="flex items-center space-x-3 mb-3">
                  <span class="text-lg font-bold text-gray-800">{{ exam.title }}</span>
                  <span
                    v-if="exam.status !== undefined"
                    :class="[
                      'px-3 py-1 rounded-full text-sm font-medium',
                      exam.status === 0 ? 'bg-gray-100 text-gray-700' :
                      exam.status === 1 ? 'bg-blue-100 text-blue-700' :
                      exam.status === 2 ? 'bg-green-100 text-green-700' : 'bg-gray-100 text-gray-700'
                    ]"
                  >
                    {{ exam.status === 0 ? '未开始' : exam.status === 1 ? '进行中' : exam.status === 2 ? '已结束' : '' }}
                  </span>
                <button
                  @click="goToGrading(exam.id)"
                  class="ml-auto px-3 py-1 bg-blue-600 text-white text-sm rounded-lg hover:bg-blue-700 transition-colors"
                >
                  试卷批改
                </button>
                </div>
                <div class="flex flex-wrap gap-x-6 gap-y-2 text-sm text-gray-600">
                  <span>创建老师: {{ exam.username }}</span>
                  <span>课程: {{ exam.courseName }}</span>
                  <span>开始时间: {{ exam.startTime }}</span>
                  <span>结束时间: {{ exam.endTime }}</span>
             </div>
            </div>
          </div>
          </div>
        </div>

        <div v-if="exams.length === 0" class="p-12 text-center">
          <svg class="w-16 h-16 text-gray-400 mx-auto mb-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 19v-6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2a2 2 0 002-2zm0 0V9a2 2 0 012-2h2a2 2 0 012 2v10m-6 0a2 2 0 002 2h2a2 2 0 002-2m0 0V5a2 2 0 012-2h2a2 2 0 012 2v14a2 2 0 01-2 2h-2a2 2 0 01-2-2z" />
          </svg>
          <p class="text-gray-500">暂无考试记录</p>
        </div>

        <div class="p-6 border-t border-gray-200 flex justify-center" v-if="total > 0">
          <ElPagination
            v-model:current-page="pageNum"
            v-model:page-size="pageSize"
            :total="total"
            :page-sizes="[10, 20, 50]"
            layout="total, sizes, prev, pager, next, jumper"
            @current-change="handlePageChange"
            @size-change="handlePageSizeChange"
          />
        </div>
      </div>
    </main>
  </div>
</template>
