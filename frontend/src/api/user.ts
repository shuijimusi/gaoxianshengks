import type { ApiResponse, LoginRequest, LoginResponse, RegisterRequest, User } from '../types';
import { mockUsers } from '../mock';
import request from '../utils/request';

export async function login(data: LoginRequest): Promise<ApiResponse<LoginResponse>> {
  const response = await request.post('/login', data);
  return response.data as ApiResponse<LoginResponse>;
}

export async function register(data: RegisterRequest): Promise<ApiResponse<User>> {
  const response = await request.post('/register', data);
  return response.data as ApiResponse<User>;
}

export function getUserList(): Promise<ApiResponse<User[]>> {
  return new Promise(resolve => {
    setTimeout(() => {
      resolve({
        code: 200,
        message: 'success',
        data: mockUsers,
      });
    }, 300);
  });
}

export interface PermissionResponse {
  id: number;
  account: string;
  realName: string;
  role: number;
}

export async function getPermission(): Promise<ApiResponse<PermissionResponse>> {
  const response = await request.get('/user/getpermission');
  return response.data as ApiResponse<PermissionResponse>;
}