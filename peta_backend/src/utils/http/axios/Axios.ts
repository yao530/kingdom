import type { AxiosRequestConfig, AxiosInstance, AxiosResponse } from 'axios';
import { message } from 'ant-design-vue';
import axios from 'axios';
import { AxiosCanceler } from './axiosCancel';
import { isFunction } from '@/utils/is';
import { cloneDeep } from 'lodash-es';

import type { RequestOptions, CreateAxiosOptions, Result } from './types';
import { ResultEnum } from '@/enums/httpEnum';

export * from './axiosTransform';

/**
 * @description:  axios模块
 */
export class VAxios {
	private axiosInstance: AxiosInstance;
	private options: CreateAxiosOptions;

	constructor(options: CreateAxiosOptions) {
		this.options = options;
		this.axiosInstance = axios.create(options);
		this.setupInterceptors();
	}

	/**
	 * @description:  创建axios实例
	 */
	private createAxios(config: CreateAxiosOptions): void {
		this.axiosInstance = axios.create(config);
	}

	private getTransform() {
		const { transform } = this.options;
		return transform;
	}

	getAxios(): AxiosInstance {
		return this.axiosInstance;
	}

	/**
	 * @description: 重新配置axios
	 */
	configAxios(config: CreateAxiosOptions) {
		if (!this.axiosInstance) {
			return;
		}
		this.createAxios(config);
	}

	/**
	 * @description: 设置通用header
	 */
	setHeader(headers: any): void {
		if (!this.axiosInstance) {
			return;
		}
		Object.assign(this.axiosInstance.defaults.headers, headers);
	}

	/**
	 * @description: 拦截器配置
	 */
	private setupInterceptors() {
		const transform = this.getTransform();
		if (!transform) {
			return;
		}
		const { requestInterceptors, requestInterceptorsCatch, responseInterceptors, responseInterceptorsCatch } =
			transform;

		const axiosCanceler = new AxiosCanceler();

		// 请求拦截器配置处理
		this.axiosInstance.interceptors.request.use((config: AxiosRequestConfig) => {
			const { headers: { ignoreCancelToken } = { ignoreCancelToken: false } } = config;
			!ignoreCancelToken && axiosCanceler.addPending(config);
			if (requestInterceptors && isFunction(requestInterceptors)) {
				config = requestInterceptors(config);
			}
			return config;
		}, undefined);

		// 请求拦截器错误捕获
		requestInterceptorsCatch &&
			isFunction(requestInterceptorsCatch) &&
			this.axiosInstance.interceptors.request.use(undefined, requestInterceptorsCatch);

		// 响应结果拦截器处理
		this.axiosInstance.interceptors.response.use((xhrRes: AxiosResponse<any>) => {
			xhrRes && axiosCanceler.removePending(xhrRes.config);
			if (responseInterceptors && isFunction(responseInterceptors)) {
				xhrRes = responseInterceptors(xhrRes);
			}
			return xhrRes;
		}, undefined);

		// 响应结果拦截器错误捕获
		responseInterceptorsCatch &&
			isFunction(responseInterceptorsCatch) &&
			this.axiosInstance.interceptors.response.use(undefined, responseInterceptorsCatch);
	}

	/**
	 * @description:   请求方法
	 */
	request<T = any>(config: AxiosRequestConfig, options?: RequestOptions): Promise<T> {
		let conf: AxiosRequestConfig = cloneDeep(config);
		const transform = this.getTransform();
		const { requestOptions } = this.options;

		const opt: RequestOptions = Object.assign({}, requestOptions, options);

		const { beforeRequestHook, requestCatch, transformRequestData } = transform || {};
		if (beforeRequestHook && isFunction(beforeRequestHook)) {
			conf = beforeRequestHook(conf, opt);
		}
		return new Promise((resolve, reject) => {
			this.axiosInstance
				.request<any, AxiosResponse<Result>>(conf)
				.then((xhrRes: AxiosResponse<Result>) => {
					if (xhrRes.status !== 200) {
						reject(xhrRes as unknown as Promise<T>);
					}
					// 请求是否被取消
					const isCancel = axios.isCancel(xhrRes);
					if (transformRequestData && isFunction(transformRequestData) && !isCancel) {
						const ret = transformRequestData(xhrRes, opt);
						return resolve(ret);
					}
					reject(xhrRes as unknown as Promise<T>);
				})
				.catch((e: Error) => {
					if (requestCatch && isFunction(requestCatch)) {
						reject(requestCatch(e));
						return;
					}
					reject(e);
				});
		});
	}
}
