import axios from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:8080',
  timeout: 5000
});

export const get = (url, params) => {
  return api.get(url, { params });
};

export const post = (url, data) => {
  return api.post(url, data);
};

export const put = (url, data) => {
  return api.put(url, data);
};

export const del = (url) => {
  return api.delete(url);
};