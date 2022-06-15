export interface MenuItem {
	authStatus: number;
	childrenList: MenuItem[];
	id: number;
	menuCode: string;
	menuIcon: string | null;
	menuId: number;
	menuLevel: number;
	menuName: string;
	menuUrl: string;
	sort: number;
}

/**
 * @description: 获取菜单接口
 */
export interface GetByUserIdParams {
	roleId: number | string;
}

/**
 * @description: 获取菜单返回值
 */
export type GetMenuListByUserIdResult = MenuItem[];

/**
 * 角色code返回值
 */
export type GetAuthCodeByUserIdResult = string[];

/**
 * 角色code返回值
 */
export interface EditRoleMenuParamsItem {
	authStatus: number;
	id: number;
	menuId: number;
}
export interface EditRoleMenuParams {
	roleId: number;
	roleMenuReqs: EditRoleMenuParamsItem[];
}
