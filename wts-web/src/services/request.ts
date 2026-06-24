import axios, { AxiosError, InternalAxiosRequestConfig } from 'axios';

const request = axios.create({
  baseURL: '/api/v1',
  timeout: 30000,
  headers: {
    'Content-Type': 'application/json',
  },
});

// 请求拦截器 — 自动附加 JWT Token
request.interceptors.request.use(
  (config: InternalAxiosRequestConfig) => {
    const token = localStorage.getItem('access_token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error),
);

// Token 刷新锁 — 防止多个 401 同时触发刷新
let isRefreshing = false;
let refreshSubscribers: Array<{
  resolve: (token: string) => void;
  reject: (error: unknown) => void;
}> = [];

const subscribeTokenRefresh = (
  resolve: (token: string) => void,
  reject: (error: unknown) => void,
) => {
  refreshSubscribers.push({ resolve, reject });
};

const onTokenRefreshed = (token: string) => {
  refreshSubscribers.forEach((subscriber) => subscriber.resolve(token));
  refreshSubscribers = [];
};

const onTokenRefreshFailed = (error: unknown) => {
  refreshSubscribers.forEach((subscriber) => subscriber.reject(error));
  refreshSubscribers = [];
};

// 响应拦截器 — 统一处理错误和 Token 刷新
request.interceptors.response.use(
  (response) => {
    const data = response.data;
    // 业务错误码处理
    if (data.code && data.code !== 200) {
      return Promise.reject(new Error(data.message || '请求失败'));
    }
    return data;
  },
  async (error: AxiosError) => {
    const originalRequest = error.config as InternalAxiosRequestConfig & {
      _retry?: boolean;
    };

    // 401 未认证 — 尝试刷新 Token（带并发锁）
    if (error.response?.status === 401 && !originalRequest._retry) {
      originalRequest._retry = true;

      if (isRefreshing) {
        // 已有刷新请求在进行中，排队等待
        return new Promise((resolve, reject) => {
          subscribeTokenRefresh((newToken) => {
            originalRequest.headers.Authorization = `Bearer ${newToken}`;
            resolve(request(originalRequest));
          }, reject);
        });
      }

      isRefreshing = true;
      const refreshToken = localStorage.getItem('refresh_token');

      if (refreshToken) {
        try {
          const res = await axios.post('/api/v1/auth/refresh', {
            refreshToken,
          });
          const { accessToken } = res.data.data;
          localStorage.setItem('access_token', accessToken);
          originalRequest.headers.Authorization = `Bearer ${accessToken}`;
          onTokenRefreshed(accessToken);
          return request(originalRequest);
        } catch (refreshError) {
          // 刷新失败，跳转登录
          localStorage.removeItem('access_token');
          localStorage.removeItem('refresh_token');
          onTokenRefreshFailed(refreshError);
          window.location.href = '/login';
          return Promise.reject(refreshError);
        } finally {
          isRefreshing = false;
        }
      } else {
        isRefreshing = false;
        onTokenRefreshFailed(error);
        window.location.href = '/login';
      }
    }

    // 403 权限不足
    if (error.response?.status === 403) {
      console.error('没有操作权限');
    }

    return Promise.reject(error);
  },
);

export default request;
