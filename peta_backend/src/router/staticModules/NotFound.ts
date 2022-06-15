import { RouteRecordRaw } from 'vue-router';

const route: RouteRecordRaw = {
	path: '/:pathMatch(.*)*',
	name: `404`,
	meta: {
		title: '404',
		icon: 'icon-shouye',
		hidden: true
	},
	component: () => import('@/views/NotFoundRoute/index.vue')
};
export default route;
