import type { ApiResponse, ExamPaper, GeneratePaperRequest, Question, SubmitAnswerRequest, PaperCreateRequest, PageResponse, ExamPaperDetail, ExamProcessResponse } from '../types';
import request from '../utils/request';
import { mockPapers, mockQuestions, generateMockPaper } from '../mock';

export interface PaperListRequest {
  pageNum: number;
  pageSize: number;
}

export function getPaperList(data: PaperListRequest): Promise<ApiResponse<PageResponse<ExamPaper>>> {
  return request.post('/exam/page', data).then(res => res.data);
}

export function getPaperById(id: number): Promise<ApiResponse<ExamPaperDetail>> {
  return request.post('/exam/detail', null, { params: { paperId: id } }).then(res => res.data);
}

export function getPaperWithQuestions(id: number): Promise<ApiResponse<ExamPaper>> {
  return request.post('/exam/detail', null, { params: { paperId: id } }).then(res => res.data);
}

export function generatePaper(data: GeneratePaperRequest): Promise<ApiResponse<ExamPaper>> {
  return new Promise(resolve => {
    setTimeout(() => {
      const typeCounts = data.typeDistribution || {
        single_choice: data.questionCount || 20,
      };
      const newPaper = generateMockPaper(data.title, data.duration, typeCounts);
      newPaper.totalScore = data.totalScore || 100;
      mockPapers.push(newPaper);
      resolve({
        code: 200,
        message: '生成成功',
        data: newPaper,
        success: true,
      });
    }, 1000);
  });
}

export function deletePaper(id: number): Promise<ApiResponse<null>> {
  return request.post('/exam/delete', null, { params: { paperId: id } }).then(res => res.data);
}

export function getPaperQuestions(paperId: number): Promise<ApiResponse<Question[]>> {
  return new Promise(resolve => {
    setTimeout(() => {
      const paper = mockPapers.find(p => p.id === paperId);
      if (!paper) {
        resolve({
          code: 404,
          message: '试卷不存在',
          data: null,
        });
        return;
      }
      
      const questions = mockQuestions.slice(0, Math.min(paper.questionCount, mockQuestions.length));
      resolve({
        code: 200,
        message: 'success',
        data: questions,
      });
    }, 300);
  });
}

export function submitExam(paperId: number, data: SubmitAnswerRequest): Promise<ApiResponse<number>> {
  return new Promise(resolve => {
    setTimeout(() => {
      const paper = mockPapers.find(p => p.id === paperId);
      if (!paper) {
        resolve({
          code: 404,
          message: '试卷不存在',
          data: null,
        });
        return;
      }
      
      const questions = mockQuestions.slice(0, Math.min(paper.questionCount, mockQuestions.length));
      let score = 0;
      let totalScore = 0;
      
      questions.forEach(q => {
        totalScore += q.score;
        const userAnswer = data.answers[q.id];
        if (userAnswer === q.answer) {
          score += q.score;
        }
      });
      
      const recordId = Date.now();
      resolve({
        code: 200,
        message: '提交成功',
        data: recordId,
        success: true,
      });
    }, 500);
  });
}

export function createPaper(data: PaperCreateRequest): Promise<ApiResponse<ExamPaper>> {
  return request.post('/page/createpaper', data).then(res => res.data);
}

export interface StartExamRequest {
  paperId: number;
  duration: number;
  startTime: number | null;
  courseId?: number;
}

export function startExam(data: StartExamRequest): Promise<ApiResponse<any>> {
  return request.post('/exam/start', data).then(res => res.data);
}

export interface GetAllExamRequest {
  pageNum: number;
  pageSize: number;
  userid: number;
}

export function getAllTeacherExams(data: GetAllExamRequest): Promise<ApiResponse<PageResponse<ExamPaper>>> {
  return request.post('/exam/allteacher', data).then(res => res.data);
}

export interface SearchClassRequest {
  classname: string;
  pageNum: number;
  pageSize: number;
}

export interface ClassItem {
  id: number;
  courseName: string;
}

export function searchClass(data: SearchClassRequest): Promise<ApiResponse<PageResponse<ClassItem>>> {
  return request.post('/joinexam/class', data).then(res => res.data);
}
export function searchGradingClass(data: SearchClassRequest): Promise<ApiResponse<PageResponse<ClassItem>>> {
  return request.post('/queryScore/class', data).then(res => res.data);
}
export function getGradingExamList(data: { courseName: string; pageNum: number; pageSize: number }): Promise<ApiResponse<any>> {
  return request.post('/queryScore/examList', data).then(res => res.data);
}

export interface GetExamByCourseRequest {
  courseId: number;
  pageNum: number;
  pageSize: number;
}

export function getExamByCourse(data: GetExamByCourseRequest): Promise<ApiResponse<PageResponse<ExamPaper>>> {
  return request.post('/joinexam', data).then(res => res.data);
}
export function getExamProcess(examId: number): Promise<ApiResponse<ExamProcessResponse>> {
  return request.post('/examProcess', null, { params: { examId } }).then(res => res.data);
}


export function submitExamProcess(data: Record<string, string>): Promise<ApiResponse<any>> {
  return request.post('/examProcess/submitExam', data).then(res => res.data);
}
export function cacheQuestion(data: Record<string, string>): Promise<ApiResponse<any>> {
  return request.post('/examProcess/cacheQuestion', data).then(res => res.data);
}

export function checkExamStatus(examId: number): Promise<ApiResponse<any>> {
  return request.post('/queryScore/status', null, { params: { examId } }).then(res => res.data);
}

export function getGradingStudentList(examId: number, pageNum: number, pageSize: number): Promise<ApiResponse<any>> {
  return request.post('/queryScore/studentList', { examId, pageNum, pageSize }).then(res => res.data);
}
