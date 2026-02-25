import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Add token to requests
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Auth APIs
export const authAPI = {
  signup: (data) => api.post('/auth/signup', data),
  login: (data) => api.post('/auth/login', data),
  test: () => api.get('/auth/test'),
};

// Chatbot APIs
export const chatbotAPI = {
  sendMessage: (data) => api.post('/chatbot/message', data),
  getHistory: () => api.get('/chatbot/history'),
  clearHistory: () => api.delete('/chatbot/history'),
};

// Interview APIs
export const interviewAPI = {
  startInterview: (data) => api.post('/interview/start', data),
  submitAnswer: (data) => api.post('/interview/answer', data),
  getHistory: () => api.get('/interview/history'),
  getSessionDetails: (sessionId) => api.get(`/interview/${sessionId}`),
};

// Resume APIs
export const resumeAPI = {
  analyzeResume: (formData) => {
    return api.post('/resume/analyze', formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    });
  },
  getHistory: () => api.get('/resume/history'),
  getReportDetails: (reportId) => api.get(`/resume/${reportId}`),
};

// Roadmap APIs
export const roadmapAPI = {
  getTodayPlan: () => api.get('/roadmap/today'),
  generatePlan: (date) => api.get('/roadmap/generate', { params: { date } }),
  markTaskComplete: (planId, taskIndex) => 
    api.put(`/roadmap/${planId}/task/${taskIndex}/complete`),
  getHistory: () => api.get('/roadmap/history'),
};

// Coding APIs
export const codingAPI = {
  getAllProblems: () => api.get('/coding/problems'),
  getProblemById: (id) => api.get(`/coding/problems/${id}`),
  getProblemsByDifficulty: (difficulty) => 
    api.get(`/coding/problems/difficulty/${difficulty}`),
  submitCode: (data) => api.post('/coding/submit', data),
  getUserSubmissions: () => api.get('/coding/submissions'),
  getProblemSubmissions: (problemId) => 
    api.get(`/coding/submissions/problem/${problemId}`),
};

// Admin APIs
export const adminAPI = {
  addProblem: (data) => api.post('/admin/problems', data),
  updateProblem: (problemId, data) => api.put(`/admin/problems/${problemId}`, data),
  deleteProblem: (problemId) => api.delete(`/admin/problems/${problemId}`),
  getAllProblems: () => api.get('/admin/problems'),
  getAllStudentsProgress: () => api.get('/admin/students/progress'),
  getStudentProgress: (studentId) => api.get(`/admin/students/${studentId}/progress`),
};

export default api;
