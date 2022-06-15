import { App } from 'vue';

import hasPermissionInstall from '@/utils/permission/hasPermission';

/**
 * 注册全局方法
 * @param app
 */
export function setupGlobalMethods(app: App) {
	app.use(hasPermissionInstall);
}
