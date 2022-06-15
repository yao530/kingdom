import router from '@/router';
import { App } from 'vue';
import store from '@/store';
import constants from '@/enums/constants';
/**
 * 第一种权限验证形式
 */

/**
 * 判断是否拥有某种权限
 * @param {string} menuName
 * @param {number} authLevel
 */
export const hasPermission = (menuName: string, authLevel: number = 1): boolean => {
	const authStatus: number =
		store.state.user.menuPermissionDict[menuName] || (constants.MenuAuthStatus.valueDict[0] as unknown as number);
	return authStatus >= authLevel;
};
export const hasPermissionCurrentRoute = (authLevel: number = 1): boolean => {
	const currentRoute = router.currentRoute.value;
	if (typeof currentRoute.name === 'string') {
		return hasPermission(currentRoute.name as string, authLevel);
	}
	return false;
};

// 暴露一个插件 API
const hasPermissionInstall = (app: App) => {
	// 在 this 上挂载一个贯穿方法，用 provider 也行
	app.config.globalProperties.$hasPermission = hasPermission;
};
export default hasPermissionInstall;
