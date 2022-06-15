import http from '@/utils/http/axios';
import { RequestEnum } from '@/enums/httpEnum';

enum CharacterRoleApi {
	page = 'characterRole/getPage',
	list = 'characterRole/getList',
	remove = 'characterRole/remove',
	save = 'characterRole/saveOrUpdate'
}
export default CharacterRoleApi;

/**
 * 获取角色列表
 */
export function getRoleList() {
	return http.request({
		url: CharacterRoleApi.list
	});
}

/**
 * 新建角色
 * @param params
 */
export function postAdminRole(params) {
	return http.request(
		{
			url: CharacterRoleApi.save,
			params
		},
		{
			isShowErrorMessage: true, // 是否显示错误提示信息
			successMessageText: '操作成功'
		}
	);
}
