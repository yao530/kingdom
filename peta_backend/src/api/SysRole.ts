import http from '@/utils/http/axios';
import { RequestEnum } from '@/enums/httpEnum';

enum RoleApi {
	list = 'roleEntity/getSystemRoles',
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
