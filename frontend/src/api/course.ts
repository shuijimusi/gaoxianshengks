import type { ApiResponse } from '../types';
import request from '../utils/request';

export interface Course {
  id: number;
  courseName: string;
  courseCode: string;
  teacherId: number;
  createTime: string;
  studentNum: number;
}

export interface CreateCourseRequest {
  courseName: string;
}

export interface CreateCourseResponse {
  courseCode: string;
}

export function createCourse(data: CreateCourseRequest): Promise<ApiResponse<CreateCourseResponse>> {
  return request.post('/course/create', data).then(res => res.data);
}

export function getTeacherCourses(): Promise<ApiResponse<Course[]>> {
  return request.get('/course/teacher').then(res => res.data);
}

export function joinCourse(courseCode: string): Promise<ApiResponse<null>> {
  return request.post('/course/join', null, { params: { courseCode } }).then(res => res.data);
}

export function getStudentCourses(): Promise<ApiResponse<Course[]>> {
  return request.post('/course/getcourse').then(res => res.data);
}

export function deleteCourse(id: number): Promise<ApiResponse<null>> {
  return request.post('/course/delete', null, { params: { courseId: id } }).then(res => res.data);
}