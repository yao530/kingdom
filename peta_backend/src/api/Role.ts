import http from '@/utils/http/axios';
import { RequestEnum } from '@/enums/httpEnum';

enum RoleApi {
	list = 'roleEntity/getList',
	remove = 'roleEntity/remove',
	save = 'roleEntity/saveOrUpdata'
}
export default RoleApi;

/**
 * 获取角色列表
 */
export function getRoleList() {
	return http.request({
		url: RoleApi.list
	});
}

/**
 * 新建角色
 * @param params
 */
export function postAdminRole(params) {
	return http.request(
		{
			url: RoleApi.save,
			params
		},
		{
			isShowErrorMessage: true, // 是否显示错误提示信息
			successMessageText: '操作成功'
		}
	);
}
