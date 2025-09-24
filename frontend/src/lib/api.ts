import axios from 'axios';
import { env } from './env';

export const api = axios.create({
	baseURL: env.VITE_BACKEND_URL,
});

export interface ApiError {
	status: string;
	message: string;
	path: string;
	timestamp: Date;
}