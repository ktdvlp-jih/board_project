import axios from 'axios';

const API = axios.create({
  baseURL: 'http://localhost:8080', // Spring Boot 서버 주소
});

export const getBoards = () => API.get('/api/boards');
