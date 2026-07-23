﻿export interface ApiResponse<T> {
  code: number;
  message?: string;
  msg?: string;
  data: T;
  success?: boolean;
}

export interface PageResponse<T> {
  records: T[];
  rows?: T[];
  total: number;
  size: number;
  pageSize?: number;
  current: number;
  pageNum?: number;
  pages: number;
}

export interface User {
  id: number;
  username: string;
  password: string;
  realName: string;
  email?: string;
  phone?: string;
  role: number;
  status: number;
  createTime: string;
}

export interface LoginRequest {
  account: string;
  password: string;
}

export interface LoginResponse {
  token: string;
  tokenType: string;
  expiresIn: number;
  user: {
    id: number;
    username: string;
    realName: string;
    role: number;
  };
}

export interface RegisterRequest {
  account: string;
  password: string;
  email: string;
  permission: number;
  realName?: string;
  phone?: string;
  role?: number;
}

export interface Question {
  id: number;
  type: number;
  chapter: string;
  difficulty: number;
  content: string;
  options: string | string[];
  answer: string;
  analysis: string;
  score: number;
  tags: string;
  creatorId: number;
  status: number;
  createTime: string;
}

export interface QuestionRequest {
  id?: number;
  type: number;
  chapter: string;
  difficulty: number;
  content: string;
  options?: string | string[];
  answer: string;
  analysis?: string;
  score: number;
  tags?: string;
}

export interface ExamPaper {
  id: number;
  title: string;
  description: string;
  totalScore: number;
  duration: number;
  questionCount: number;
  passScore?: number;
  status?: number;
  questions?: Question[];
  createTime?: string;
  username?: string;
  usernameName?: string;
  courseName?: string;
  startTime?: string;
  endTime?: string;
}

export interface ExamPaperDetail {
  id: number;
  title: string;
  description: string;
  totalScore: number;
  duration: number;
  passScore: number;
  questionCount: number;
  createTime: string;
  updateTime: string;
  creatorId: number;
}

export interface GeneratePaperRequest {
  title: string;
  description?: string;
  duration: number;
  totalScore: number;
  questionCount: number;
  questionCounts?: Record<string, number>;
  chapters?: string[];
  difficultyDistribution: Record<string, number>;
  typeDistribution?: Record<string, number>;
}

export interface ExamRecord {
  id: number;
  paperId: number;
  userId: number;
  status: number;
  startTime: string;
  endTime?: string;
  submitTime?: string;
  totalScore: number;
  score: number;
  duration?: number;
  answers?: Record<number, string>;
}

export interface ExamAnswer {
  id: number;
  recordId: number;
  questionId: number;
  questionType: number;
  userAnswer: string;
  correctAnswer: string;
  score: number;
  fullScore: number;
  isCorrect: number;
}

export interface SubmitAnswerRequest {
  answers: Record<number, string>;
}

export interface QuestionScoreDTO {
  questionId: number;
  questionType: number;
  questionScore: number;
}

export interface PaperCreateRequest {
  title: string;
  description: string;
  totalScore: number;
  duration: number;
  passScore: number;
  questionCount: number;
  questionIdScore: QuestionScoreDTO[];
  paperId?: number;
}

export const QUESTION_TYPES: Record<number, string> = {
  1: '单选题',
  2: '多选题',
  3: '判断题',
  4: '填空题',
  5: '简答题',
  6: '编程题',
};

export const DIFFICULTY_LEVELS: Record<number, string> = {
  1: '简单',
  2: '中等',
  3: '困难',
};

export const ROLES: Record<number, string> = {
  0: '管理员',
  1: '教师',
  2: '学生',
};

export const CHAPTERS = [
  '第一章 绪论',
  '第二章 线性表',
  '第三章 栈和队列',
  '第四章 串',
  '第五章 树和二叉树',
  '第六章 图',
  '第七章 查找',
  '第八章 排序',
];
export interface ExamProcessResponse {
  examStudentAnswers: any[];
  questions: Question[];
}
