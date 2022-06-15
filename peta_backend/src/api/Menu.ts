import http from '@/utils/http/axios';
import { GetByUserIdParams, GetMenuListByUserIdResult, EditRoleMenuParams } from '@/interface/RES/menuModel';

enum MenuApi {
	save = 'menuEntity/edit',
	remove = 'menuEntity/remove',
	list = 'menuEntity/getList',
	userMenuList = 'roleMenuEntity/getList',
	roleMenus = 'roleMenuEntity/getRoleList',
	editRoleMenu = 'roleMenuEntity/editRoleMenu'
}
export default MenuApi;

/**
 * @description: 根据用户token获取用户菜单
 */
export async function getUserMenus() {
	const res = await http.request<GetMenuListByUserIdResult>({
		url: MenuApi.userMenuList,
		method: 'POST'
	});
	return res;
}
/**
 * @description: 获取所有菜单
 */
export async function getMenus() {
	return await http.request({
		url: MenuApi.list
	});
}

/**
 * 根据用户Id获取权限编码
 * @param params
 */
export function getRoleMenus(params: GetByUserIdParams) {
	return http.request({
		url: MenuApi.roleMenus,
		params
	});
}

export function editRoleMenu(params: EditRoleMenuParams) {
	return http.request({
		url: MenuApi.editRoleMenu,
		params: {
			...params
		}
	});
}
