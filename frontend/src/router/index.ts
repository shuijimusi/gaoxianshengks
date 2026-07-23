import { createRouter, createWebHistory } from 'vue-router';
import { useUserStore } from '../stores/user';

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/',
      redirect: '/login',
    },
    {
      path: '/login',
      name: 'Login',
      component: () => import('../views/Login.vue'),
      meta: { requiresGuest: true },
    },
    {
      path: '/register',
      name: 'Register',
      component: () => import('../views/Register.vue'),
      meta: { requiresGuest: true },
    },
    {
      path: '/home',
      name: 'Home',
      component: () => import('../views/Home.vue'),
      meta: { requiresAuth: true },
    },
    {
      path: '/courses',
      name: 'CourseManagement',
      component: () => import('../views/CourseManagement.vue'),
      meta: { requiresAuth: true, requiresTeacher: true },
    },
    {
      path: '/courses/join',
      name: 'JoinCourse',
      component: () => import('../views/JoinCourse.vue'),
      meta: { requiresAuth: true, requiresStudent: true },
    },
    {
      path: '/courses/my',
      name: 'MyCourses',
      component: () => import('../views/MyCourses.vue'),
      meta: { requiresAuth: true, requiresStudent: true },
    },
    {
      path: '/questions',
      name: 'QuestionManagement',
      component: () => import('../views/QuestionManagement.vue'),
      meta: { requiresAuth: true, requiresTeacher: true },
    },
    {
      path: '/papers',
      name: 'PaperManagement',
      component: () => import('../views/PaperManagement.vue'),
      meta: { requiresAuth: true, requiresTeacher: true },
    },
    {
      path: '/exams',
      name: 'ExamManagement',
      component: () => import('../views/ExamManagement.vue'),
      meta: { requiresAuth: true, requiresTeacher: true },
    },
    {
      path: '/papers/:id',
      name: 'PaperDetail',
      component: () => import('../views/PaperDetail.vue'),
      meta: { requiresAuth: true },
    },
    {
      path: '/papers/generate',
      name: 'PaperGenerate',
      component: () => import('../views/PaperGenerate.vue'),
      meta: { requiresAuth: true, requiresTeacher: true },
    },
    {
      path: '/papers/create',
      name: 'PaperCreate',
      component: () => import('../views/PaperCreate.vue'),
      meta: { requiresAuth: true, requiresTeacher: true },
    },
    {
      path: '/exam/:id',
      name: 'Exam',
      component: () => import('../views/Exam.vue'),
      meta: { requiresAuth: true },
    },
    {
      path: '/myexam',
      name: 'MyExam',
      component: () => import('../views/MyExam.vue'),
      meta: { requiresAuth: true, requiresStudent: true },
    },
    {
      path: '/grading/:examId?',
      name: 'ExamGrading',
      component: () => import('../views/ExamGrading.vue'),
      meta: { requiresAuth: true, requiresTeacher: true },
    },
    {
      path: '/results',
      name: 'Results',
      component: () => import('../views/Results.vue'),
      meta: { requiresAuth: true },
    },
    {
      path: '/results/:id',
      name: 'ResultDetail',
      component: () => import('../views/ResultDetail.vue'),
      meta: { requiresAuth: true },
    },
  ],
});

router.beforeEach((to, from, next) => {
  const userStore = useUserStore();

  if (to.meta.requiresAuth && (!userStore.isLoggedIn || !userStore.user)) {
    next('/login');
  } else if (to.meta.requiresGuest && userStore.isLoggedIn && userStore.user) {
    next('/home');
  } else if (to.meta.requiresTeacher && !userStore.isAdmin && !userStore.isTeacher) {
    next('/home');
  } else if (to.meta.requiresStudent && !userStore.isStudent) {
    next('/home');
  } else {
    next();
  }
});

export default router;
