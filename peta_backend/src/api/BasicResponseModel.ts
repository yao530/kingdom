export interface BasicResponseModel<T = any> {
	code: number;
	message: string;
	data: T;
}
export interface BasicPageParams {
	pageNumber: number;
	pageSize: number;
	total: number;
}
