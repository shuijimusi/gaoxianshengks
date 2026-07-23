<script setup lang="ts">
import { ref } from 'vue';
import { createCourse, getTeacherCourses, deleteCourse } from '../api/course';
import type { Course } from '../api/course';
import { ElMessage, ElDialog, ElTable, ElTableColumn, ElInput, ElButton } from 'element-plus';
import Sidebar from '../components/Sidebar.vue';

const courses = ref<Course[]>([]);
const loading = ref(false);

const showCreateDialog = ref(false);
const showSuccessDialog = ref(false);
const newCourseName = ref('');
const generatedUuid = ref('');

async function loadCourses() {
  loading.value = true;
  const res = await getTeacherCourses();
  if (res.code === 200 && res.data) {
    courses.value = res.data;
  }
  loading.value = false;
}

async function handleCreate() {
  if (!newCourseName.value.trim()) {
    ElMessage.warning('请输入课程名称');
    return;
  }
  
  const res = await createCourse({ courseName: newCourseName.value.trim() });
  if (res.code === 200 && res.msg) {
    generatedUuid.value = res.msg;
    showCreateDialog.value = false;
    showSuccessDialog.value = true;
    newCourseName.value = '';
    await loadCourses();
  } else {
    ElMessage.error('创建失败');
  }
}

async function handleDelete(id: number) {
  const res = await deleteCourse(id);
  if (res.code === 200) {
    ElMessage.success('删除成功');
    await loadCourses();
  } else {
    ElMessage.error('删除失败');
  }
}

function copyUuid() {
  navigator.clipboard.writeText(generatedUuid.value);
  ElMessage.success('已复制到剪贴板');
}

function copyCourseUuid(uuid: string) {
  navigator.clipboard.writeText(uuid);
  ElMessage.success('已复制');
}

loadCourses();
</script>

<template>
  <div class="min-h-screen bg-gray-100">
    <Sidebar />
    
    <main class="ml-64 p-8">
      <div class="flex items-center justify-between mb-8">
        <div>
          <h1 class="text-3xl font-bold text-gray-800">课程管理</h1>
          <p class="text-gray-600 mt-2">管理您创建的课程</p>
        </div>
        <button
          @click="showCreateDialog = true"
          class="px-6 py-3 bg-blue-600 text-white font-medium rounded-lg hover:bg-blue-700 transition-colors shadow-lg"
        >
          创建课程
        </button>
      </div>

      <div class="bg-white rounded-xl shadow-lg overflow-hidden">
        <div v-if="courses.length === 0" class="p-12 text-center">
          <div class="text-gray-400 mb-4">
            <svg class="w-16 h-16 mx-auto" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5" d="M19 21V5a2 2 0 00-2-2H7a2 2 0 00-2 2v16m14 0h2m-2 0h-5m-9 0H3m2 0h5M9 7h1m-1 4h1m4-4h1m-1 4h1m-5 10v-5a1 1 0 011-1h2a1 1 0 011 1v5m-4 0h4" />
            </svg>
          </div>
          <p class="text-gray-500">暂无课程，点击上方按钮创建</p>
        </div>

        <div v-else>
          <div class="overflow-x-auto">
            <ElTable :data="courses" border v-loading="loading" stripe>
              <ElTableColumn prop="id" label="ID" width="60" />
              <ElTableColumn prop="courseName" label="课程名称" min-width="200">
                <template #default="{ row }">
                  <span class="font-medium text-gray-800">{{ row.courseName }}</span>
                </template>
              </ElTableColumn>
              <ElTableColumn prop="courseCode" label="课程码" min-width="300">
                <template #default="{ row }">
                  <div class="flex items-center space-x-2">
                    <code class="text-sm text-gray-600 bg-gray-100 px-3 py-1 rounded font-mono">{{ row.courseCode }}</code>
                    <button
                      @click="copyCourseUuid(row.courseCode)"
                      class="text-blue-600 hover:text-blue-700"
                    >
                      <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 16H6a2 2 0 01-2-2V6a2 2 0 012-2h8a2 2 0 012 2v2m-6 12h8a2 2 0 002-2v-8a2 2 0 00-2-2h-8a2 2 0 00-2 2v8a2 2 0 002 2z" />
                      </svg>
                    </button>
                  </div>
                </template>
              </ElTableColumn>
              <ElTableColumn prop="studentNum" label="学生人数" width="100">
                <template #default="{ row }">
                  <span class="text-sm font-medium text-gray-800">{{ row.studentNum }}人</span>
                </template>
              </ElTableColumn>
              <ElTableColumn prop="createTime" label="创建时间" width="180" />
              <ElTableColumn label="操作" width="100">
                <template #default="{ row }">
                  <button
                    @click="handleDelete(row.id)"
                    class="text-red-600 hover:text-red-700"
                  >
                    <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" />
                    </svg>
                  </button>
                </template>
              </ElTableColumn>
            </ElTable>
          </div>
        </div>
      </div>
    </main>

    <ElDialog title="创建课程" v-model="showCreateDialog" width="400px">
      <div class="space-y-4">
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-2">课程名称 *</label>
          <ElInput v-model="newCourseName" placeholder="请输入课程名称" />
        </div>
      </div>
      <template #footer>
        <div class="flex justify-end space-x-2">
          <ElButton @click="showCreateDialog = false">取消</ElButton>
          <ElButton type="primary" @click="handleCreate">创建</ElButton>
        </div>
      </template>
    </ElDialog>

    <ElDialog title="课程创建成功" v-model="showSuccessDialog" width="450px" :closable="false">
      <div class="space-y-4">
        <div class="bg-green-50 p-4 rounded-lg">
          <p class="text-green-700 text-sm">课程已成功创建！</p>
          <p class="text-green-600 mt-1">请将下方的课程码告知学生，学生可通过此课程码加入课程。</p>
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-2">课程码</label>
          <div class="flex items-center space-x-2">
            <code class="flex-1 text-sm text-gray-600 bg-gray-100 px-4 py-3 rounded font-mono text-center">{{ generatedUuid }}</code>
            <button
              @click="copyUuid"
              class="px-4 py-2 bg-blue-500 text-white text-sm rounded hover:bg-blue-600"
            >
              复制
            </button>
          </div>
        </div>
      </div>
      <template #footer>
        <div class="flex justify-end">
          <ElButton type="primary" @click="showSuccessDialog = false">确定</ElButton>
        </div>
      </template>
    </ElDialog>
  </div>
</template>