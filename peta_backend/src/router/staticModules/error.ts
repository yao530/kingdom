// import { RouterTransition } from '@/components/transition';

// const routeName = 'error';

// export const notFound = {
// 	path: '/:pathMatch(.*)*',
// 	name: 'NotFound',
// 	component: () => import('@/views/shared/error/404.vue')
// };

// export const errorRoutes = {
// 	path: '/error',
// 	name: routeName,
// 	redirect: '/error/404',
// 	component: RouterTransition,
// 	meta: {
// 		title: '错误页',
// 		icon: 'EditOutlined',
// 		hidden: true
// 	},
// 	children: [
// 		{
// 			path: '404',
// 			name: `${routeName}-404`,
// 			meta: {
// 				title: '404',
// 				icon: 'UserOutlined'
// 			},
// 			component: () => import('@/views/shared/error/404.vue')
// 		}
// 	]
// };

// export default [notFound, errorRoutes];
