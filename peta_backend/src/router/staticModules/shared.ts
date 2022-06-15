import { RouteRecordRaw } from 'vue-router';
import { RouterTransition } from '@/components/transition';

/**
 * 不需要授权就可以访问的页面
 */
const routes: Array<RouteRecordRaw> = [
	{
		path: '/login',
		name: 'login',
		component: () => import(/* webpackChunkName: "login" */ '@/views/shared/login/index.vue')
	},
	{
		path: '/icons',
		name: 'icons',
		component: () => import(/* webpackChunkName: "icons" */ '@/views/shared/icons/index.vue')
	},
	{
		path: '/Echarts',
		name: 'Echarts',
		component: () => import('@/views/Echarts/Echarts.vue')
	},
	{
		path: '/Editor',
		name: 'Editor',
		component: () => import('@/views/Editor/Demo.vue')
	}
];

export default routes;
