import { RouteRecordRaw } from 'vue-router';
import { RouterTransition } from '@/components/transition';

const routeName = 'dashboard';

const routes: Array<RouteRecordRaw> = [
	{
		path: '/welcome',
		name: `${routeName}-welcome`,
		meta: {
			title: '系统看板',
			icon: 'icon-shouye'
		},
		component: () =>
			import(/* webpackChunkName: "dashboard-welcome" */ '@/views/shared/dashboard/welcome/index.vue')
	}
];

export default routes;
