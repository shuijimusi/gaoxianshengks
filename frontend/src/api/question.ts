import type { ApiResponse, Question, QuestionRequest, PageResponse } from '../types';
import request from '../utils/request';

export interface QuestionListRequest {
  pageNum: number;
  pageSize: number;
  content?: string;
  chapter?: string;
  difficulty?: number;
  type?: number;
}

export function getQuestionList(data: QuestionListRequest): Promise<ApiResponse<PageResponse<Question>>> {
  return request.post('/question', data).then(res => res.data);
}

export function getQuestionForSelector(data: QuestionListRequest): Promise<ApiResponse<PageResponse<Question>>> {
  return request.post('/page/selectquestion', data).then(res => res.data);
}

export function getQuestionById(id: number): Promise<ApiResponse<Question>> {
  return request.get(`/question/${id}`).then(res => res.data);
}

export function getQuestionByImage(id: number): Promise<ApiResponse<Question>> {
  return request.post('/question/getoneimage', null, { params: { questionId: id } }).then(res => res.data);
}

export function createQuestion(data: QuestionRequest): Promise<ApiResponse<Question>> {
  return request.post('/question/add', data).then(res => res.data);
}

export const addQuestion = createQuestion;

export function updateQuestion(data: QuestionRequest): Promise<ApiResponse<Question>> {
  return request.post('/question/update', data).then(res => res.data);
}

export function deleteQuestion(id: number): Promise<ApiResponse<null>> {
  return request.post('/question/delete', null, { params: { questionId: id } }).then(res => res.data);
}

export function getQuestionCount(): Promise<ApiResponse<number>> {
  return request.get('/question/returnallquestion').then(res => res.data);
}