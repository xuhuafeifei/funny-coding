// src/utils/api.ts
import axios from 'axios';
import type { AxiosRequestConfig, AxiosResponse } from 'axios';

const api = axios.create({
  baseURL: '',
  timeout: 5000,
  withCredentials: true
});

// 请求拦截器
api.interceptors.request.use(config => {
  return config;
}, error => {
  return Promise.reject(error);
});

// 响应拦截器
api.interceptors.response.use(response => {
  return response;
}, error => {
  if (error.response?.status === 401) {
    console.error('未授权，请重新登录');
  }
  return Promise.reject(error);
});

const wrapper = (url: string): string => {
  // return `/api/articles/${url}`;
  return url;
};

export const get = <T = any>(url: string, params?: AxiosRequestConfig['params']): Promise<AxiosResponse<T>> => {
  console.log(url)
  return api.get(wrapper(url), { params });
};

export const post = <T = any>(url: string, data?: any): Promise<AxiosResponse<T>> => {
  console.log(url)
  return api.post(wrapper(url), data).then((response) => response);
};

export const put = <T = any>(url: string, data?: any): Promise<AxiosResponse<T>> => {
  return api.put(wrapper(url), data);
};

export const del = <T = any>(url: string): Promise<AxiosResponse<T>> => {
  return api.delete(url);
};