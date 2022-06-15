import { AxiosRequestConfig } from 'axios';
import { AxiosTransform } from './axiosTransform';

export interface CreateAxiosOptions extends AxiosRequestConfig {
	transform?: AxiosTransform;
	requestOptions?: RequestOptions;
}

export interface RequestOptions {
	prefix?: string;
	// 请求参数拼接到url
	joinParamsToUrl?: boolean;
	// 格式化请求参数时间
	formatDate?: boolean;
	//  是否处理请求结果
	isTransformRequestResult?: boolean;
	// 是否显示提示信息
	isShowMessage?: boolean;
	// 成功的文本信息
	successMessageText?: string;
	// 是否显示成功信息
	isShowSuccessMessage?: boolean;
	// 是否显示失败信息
	isShowErrorMessage?: boolean;
	// 错误的文本信息
	errorMessageText?: string;
	// 是否加入url
	joinPrefix?: boolean;
	// 接口地址， 不填则使用默认apiUrl
	apiUrl?: string;
	// 错误消息提示类型
	errorMessageMode?: 'none' | 'modal';
}

export interface Result<T = any> {
	code: number;
	message: string;
	data?: T;
	timestamp: number;
}