/**
 * @description: 登录接口返回值
 */
export interface LoginResultModel {
	accountCode: string;
	accountLogo: string;
	accountName: string;
	token: string;
	roleId: string;
	roleName: string;
}
