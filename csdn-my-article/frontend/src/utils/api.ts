// src/utils/api.ts
import axios from 'axios';
import type { AxiosRequestConfig, AxiosResponse } from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:8080',
  timeout: 5000
});

export const get = <T = any>(url: string, params?: AxiosRequestConfig['params']): Promise<AxiosResponse<T>> => {
  return api.get(url, { params });
};

export const post = <T = any>(url: string, data?: any): Promise<AxiosResponse<T>> => {
  return api.post(url, data);
};

export const put = <T = any>(url: string, data?: any): Promise<AxiosResponse<T>> => {
  return api.put(url, data);
};

export const del = <T = any>(url: string): Promise<AxiosResponse<T>> => {
  return api.delete(url);
};