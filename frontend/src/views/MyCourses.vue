<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { ElMessage } from 'element-plus';
import { getStudentCourses } from '../api/course';
import type { Course } from '../api/course';
import Sidebar from '../components/Sidebar.vue';

const courses = ref<Course[]>([]);
const loading = ref(false);

async function loadCourses() {
  loading.value = true;
  try {
    const res = await getStudentCourses();
    if (res.code === 200) {
      courses.value = res.data || [];
    }
  } catch (error) {
    console.error('获取班级列表失败', error);
  } finally {
    loading.value = false;
  }
}

onMounted(() => {
  loadCourses();
});
</script>

<template>
  <div class="min-h-screen bg-gray-100">
    <Sidebar />
    
    <main class="ml-64 p-8">
      <div class="mb-8">
        <h1 class="text-3xl font-bold text-gray-800">我加入的班级</h1>
        <p class="text-gray-600 mt-2">查看你已加入的所有班级</p>
      </div>

      <div class="bg-white rounded-xl shadow-lg p-6" v-loading="loading">
        <div v-if="courses.length === 0 && !loading" class="text-center py-12">
          <svg class="w-16 h-16 text-gray-400 mx-auto mb-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5" d="M19 21V5a2 2 0 00-2-2H7a2 2 0 00-2 2v16m14 0h2m-2 0h-5m-9 0H3m2 0h5M9 7h1m-1 4h1m4-4h1m-1 4h1m-5 10v-5a1 1 0 011-1h2a1 1 0 011 1v5m-4 0h4" />
          </svg>
          <p class="text-gray-500 text-lg">暂未加入任何班级</p>
          <p class="text-gray-400 text-sm mt-2">请前往"加入班级"页面加入班级</p>
        </div>

        <div v-else class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          <div
            v-for="course in courses"
            :key="course.id"
            class="bg-gradient-to-br from-blue-50 to-white border border-blue-100 rounded-xl p-6 hover:shadow-lg transition-shadow"
          >
            <div class="flex items-start justify-between mb-4">
              <div class="w-12 h-12 bg-blue-500 rounded-lg flex items-center justify-center">
                <svg class="w-6 h-6 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 6.253v13m0-13C10.832 5.477 9.246 5 7.5 5S4.168 5.477 3 6.253v13C4.168 18.477 5.754 18 7.5 18s3.332.477 4.5 1.253m0-13C13.168 5.477 14.754 5 16.5 5c1.747 0 3.332.477 4.5 1.253v13C19.832 18.477 18.247 18 16.5 18c-1.746 0-3.332.477-4.5 1.253" />
                </svg>
              </div>
              <span class="px-3 py-1 bg-green-100 text-green-700 text-sm rounded-full">已加入</span>
            </div>
            <h3 class="text-lg font-semibold text-gray-800 mb-2">{{ course.courseName }}</h3>
            <div class="space-y-1 text-sm text-gray-600">
              <p>课程码：{{ course.courseCode }}</p>
              <p>加入班级时间：{{ course.createTime }}</p>
            </div>
          </div>
        </div>
      </div>
    </main>
  </div>
</template>
