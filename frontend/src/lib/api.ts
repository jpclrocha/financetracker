import axios, { AxiosError } from "axios";
import { env } from "./env";

declare module "axios" {
	export interface InternalAxiosRequestConfig {
		_retry?: boolean;
	}
}

const api = axios.create({
	baseURL: env.VITE_BACKEND_URL,
	withCredentials: true,
});

let isRefreshing = false;
let queue: (() => void)[] = [];

api.interceptors.response.use(
	(res) => res,
	async (error: AxiosError) => {
		const original = error.config;
		if (error.response?.status === 401 && !original._retry) {
			if (isRefreshing) {
				return new Promise((resolve, reject) => {
					queue.push(() => {
						api(original).then(resolve).catch(reject);
					});
				});
			}

			original._retry = true;
			isRefreshing = true;
			try {
				await axios.post(
					`${env.VITE_BACKEND_URL}/auth/refresh`,
					{},
					{ withCredentials: true }
				);
				isRefreshing = false;
				queue.forEach((cb) => cb());
				queue = [];
				return api(original);
			} catch (err) {
				isRefreshing = false;
				queue = [];
				// Refresh failed -> force logout
				window.location.href = "/login";
				return Promise.reject(err);
			}
		}
		return Promise.reject(error);
	}
);

export default api;

export interface ApiError {
	status: string;
	message: string;
	path: string;
	timestamp: Date;
}
