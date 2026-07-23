import type { ApiResponse, ExamRecord, SubmitAnswerRequest, Question, ExamAnswer } from '../types';
import { mockRecords, mockAnswers, mockQuestions, generateMockExamRecord } from '../mock';

export function startExam(paperId: number, userId: number): Promise<ApiResponse<ExamRecord>> {
  return new Promise(resolve => {
    setTimeout(() => {
      const record = generateMockExamRecord(paperId, userId);
      mockRecords.push(record);
      resolve({
        code: 200,
        message: '考试开始',
        data: record,
      });
    }, 300);
  });
}

export function getExamQuestions(recordId: number): Promise<ApiResponse<Question[]>> {
  return new Promise(resolve => {
    setTimeout(() => {
      const record = mockRecords.find(r => r.id === recordId);
      if (!record) {
        resolve({
          code: 404,
          message: '考试记录不存在',
          data: null,
        });
        return;
      }
      
      const questions = mockQuestions.slice(0, 10);
      resolve({
        code: 200,
        message: 'success',
        data: questions,
      });
    }, 300);
  });
}

export function submitExam(recordId: number, data: SubmitAnswerRequest): Promise<ApiResponse<ExamRecord>> {
  return new Promise(resolve => {
    setTimeout(() => {
      const record = mockRecords.find(r => r.id === recordId);
      if (!record) {
        resolve({
          code: 404,
          message: '考试记录不存在',
          data: null,
        });
        return;
      }
      
      let score = 0;
      let totalScore = 0;
      
      Object.entries(data.answers).forEach(([questionId, userAnswer]) => {
        const question = mockQuestions.find(q => q.id === parseInt(questionId));
        if (question) {
          totalScore += question.score;
          if (question.type === 1 || question.type === 3) {
            if (userAnswer === question.answer) {
              score += question.score;
            }
          } else if (question.type === 4) {
            if (userAnswer.trim().toLowerCase() === question.answer.trim().toLowerCase()) {
              score += question.score;
            } else {
              score += question.score * 0.5;
            }
          } else {
            score += question.score * 0.8;
          }
        }
      });
      
      record.status = 3;
      record.submitTime = new Date().toLocaleString();
      record.endTime = new Date().toLocaleString();
      record.totalScore = totalScore;
      record.score = Math.round(score * 100) / 100;
      
      Object.entries(data.answers).forEach(([questionId, userAnswer]) => {
        const question = mockQuestions.find(q => q.id === parseInt(questionId));
        if (question) {
          mockAnswers.push({
            id: Date.now() + Math.random(),
            recordId,
            questionId: parseInt(questionId),
            questionType: question.type,
            userAnswer,
            correctAnswer: question.answer,
            score: userAnswer === question.answer ? question.score : question.score * 0.5,
            fullScore: question.score,
            isCorrect: userAnswer === question.answer ? 1 : 0,
          });
        }
      });
      
      resolve({
        code: 200,
        message: '提交成功',
        data: record,
      });
    }, 500);
  });
}

export function getExamRecords(userId?: number): Promise<ApiResponse<ExamRecord[]>> {
  return new Promise(resolve => {
    setTimeout(() => {
      let result = [...mockRecords];
      if (userId) {
        result = result.filter(r => r.userId === userId);
      }
      resolve({
        code: 200,
        message: 'success',
        data: result,
      });
    }, 300);
  });
}

export function getExamRecordById(id: number): Promise<ApiResponse<ExamRecord>> {
  return new Promise(resolve => {
    setTimeout(() => {
      const record = mockRecords.find(r => r.id === id);
      if (record) {
        resolve({
          code: 200,
          message: 'success',
          data: record,
        });
      } else {
        resolve({
          code: 404,
          message: '考试记录不存在',
          data: null,
        });
      }
    }, 300);
  });
}

export function getExamAnswers(recordId: number): Promise<ApiResponse<ExamAnswer[]>> {
  return new Promise(resolve => {
    setTimeout(() => {
      const answers = mockAnswers.filter(a => a.recordId === recordId);
      resolve({
        code: 200,
        message: 'success',
        data: answers,
      });
    }, 300);
  });
}