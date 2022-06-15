import { createRouter, createWebHashHistory, RouteRecordRaw } from 'vue-router';

import { createRouterGuards } from './router-guards';
import 'nprogress/css/nprogress.css'; // 进度条样式

import shared from './staticModules/shared';
import dashboard from '@/router/staticModules/dashboard';
import redirect from '@/router/staticModules/redirect';
import { App } from 'vue';

export const routes: Array<RouteRecordRaw> = [
	{
		path: '/',
		name: 'Layout',
		redirect: '/welcome',
		component: () => import('@/layout/index.vue'),
		meta: {
			title: '首页'
		},
		children: [...dashboard, ...redirect]
	},
	...shared
];

const router = createRouter({
	history: createWebHashHistory(''),
	routes
});

export function setupRouter(app: App) {
	app.use(router);
	// 创建路由守卫
	createRouterGuards(router);
}
export default router;
