import { getUserMenus } from '@/api/Menu';
import { constantRouterComponents } from './modules';
import router, { routes } from '@/router/';
import dashboard from '@/router/staticModules/dashboard';
import NotFoundRoute from '@/router/staticModules/NotFound';
import { RouteRecordRaw } from 'vue-router';
import { Empty } from 'ant-design-vue';
import { RouterTransition } from '@/components/transition';
import store from '@/store';
import { getMenuPermission } from '@/utils/common';
/**
 * 动态生成菜单
 * @param token
 * @returns {Promise<Router>}
 */
export const generatorDynamicRouter = (): Promise<RouteRecordRaw[]> => {
	return new Promise((resolve, reject) => {
		getUserMenus()
			.then((resData) => {
				store.commit('user/setMenuPermissionDict', getMenuPermission({}, resData));
				const menuNav: any = [];
				const childrenNav = [];
				menuNav.push(childrenNav);
				const routeList = list1tree(resData);
				const layout = routes.find((item) => item.name == 'Layout')!;
				layout.children = [...dashboard, ...routeList, NotFoundRoute];
				router.addRoute(layout);
				resolve(layout.children);
			})
			.catch((err) => {
				if (err) {
					console.error(err);
				}
				reject(err);
			});
	});
};
/**
 * 异步生成菜单树， 方案一
 * @param dataList
 */
const list1tree = (srcMenus): RouteRecordRaw[] => {
	return srcMenus.map((srcMenu) => {
		// 路由对应的组件
		const childrenList = srcMenu.childrenList || [];
		const component = childrenList.length ? RouterTransition : constantRouterComponents[srcMenu.menuUrl] || Empty;
		const routeConf: RouteRecordRaw = {
			path: `/${srcMenu.menuUrl}`,
			// 路由名称，建议唯一
			name: `${srcMenu.menuUrl}`,
			children: list1tree(childrenList || []),
			// 该路由对应页面的 组件 (动态加载)
			component: component,
			// meta: 页面标题, 菜单图标, 页面权限(供指令权限用，可去掉)
			meta: {
				title: srcMenu.menuName,
				icon: srcMenu.menuIcon || '<RedditOutlined />',
				authStatus: srcMenu.authStatus
			}
		};
		if (routeConf.children?.length) {
			routeConf.redirect = { name: routeConf.children[0].name };
		}
		return routeConf;
	});
};
