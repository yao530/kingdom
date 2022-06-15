/**
 * 基础路由
 * @type { *[] }
 */
export const constantRouterMap = [
  {
    path: '/',
    component: () => import('@/views/layouts/index'),
    redirect: '/home',
    meta: {
      title: '首页',
      keepAlive: false,
      tabbarShow: false
    },
    children: [
      {
        path: '/home',
        name: 'Home',
        component: () => import('@/views/home/index'),
        meta: { title: '首页', keepAlive: false, tabbarShow: true }
      },
      {
        path: '/story',
        name: 'Story',
        component: () => import('@/views/home/story'),
        meta: { title: '情报', keepAlive: false, tabbarShow: true }
      },
      {
        path: '/about',
        name: 'About',
        component: () => import('@/views/home/about'),
        meta: { title: '分身', keepAlive: false, tabbarShow: false }
      },
      {
        path: '/artDetail',
        name: 'ArtDetail',
        component: () => import('@/views/home/artDetail'),
        meta: { title: '藏品详情', keepAlive: false, tabbarShow: false }
      },
      {
        path: '/pay',
        name: 'Pay',
        component: () => import('@/views/home/pay'),
        meta: { title: '支付', keepAlive: false, tabbarShow: false }
      },
      {
        path: '/paySusscess',
        name: 'PaySusscess',
        component: () => import('@/views/home/paySusscess'),
        meta: { title: '支付', keepAlive: false, tabbarShow: false }
      },
      {
        path: '/order',
        name: 'Order',
        component: () => import('@/views/home/order'),
        meta: { title: '订单列表', keepAlive: false, tabbarShow: false }
      },
      {
        path: '/characterDetail',
        name: 'CharacterDetail',
        component: () => import('@/views/home/characterDetail'),
        meta: { title: '人物信息', keepAlive: false, tabbarShow: false }
      },
      {
        path: '/storyInfo',
        name: 'StoryInfo',
        component: () => import('@/views/home/storyInfo'),
        meta: { title: '故事', keepAlive: false, tabbarShow: false }
      },
      {
        path: '/storyDetail',
        name: 'StoryDetail',
        component: () => import('@/views/home/storyDetail'),
        meta: { title: '故事详情', keepAlive: false, tabbarShow: false }
      },
      {
        path: '/userCenter',
        name: 'UserCenter',
        component: () => import('@/views/home/userCenter'),
        meta: { title: '我的', keepAlive: false, tabbarShow: true }
      },
      // {
      //   path: '/avatarList',
      //   name: 'AvatarList',
      //   component: () => import('@/views/home/avatarList'),
      //   meta: { title: '分身藏品', keepAlive: false, tabbarShow: false }
      // },
      {
        path: '/myArtDetail',
        name: 'MyArtDetail',
        component: () => import('@/views/home/myArtDetail'),
        meta: { title: '我的藏品', keepAlive: false, tabbarShow: false }
      },
      {
        path: '/staticTextView',
        name: 'StaticTextView',
        component: () => import('@/views/home/staticTextView'),
        meta: { title: '详情', keepAlive: false, tabbarShow: false }
      },
      {
        path: '/verifyRegister',
        name: 'VerifyRegister',
        component: () => import('@/views/home/verifyRegister'),
        meta: { title: '安全认证', keepAlive: false, tabbarShow: false }
      },
      {
        path: '/login',
        name: 'Login',
        component: () => import('@/views/home/login'),
        meta: { title: '登录', keepAlive: false, tabbarShow: false }
      },
      {
        path: '/applyCharacter',
        name: 'ApplyCharacter',
        component: () => import('@/views/home/applyCharacter'),
        meta: { title: '创作申请', keepAlive: false, tabbarShow: false }
      },
      {
        path: '/collectionAlbum',
        name: 'CollectionAlbum',
        component: () => import('@/views/home/collectionAlbum'),
        meta: { title: '收藏合辑', keepAlive: false, tabbarShow: false }
      },
      {
        path: '/collectionAlbumDetail',
        name: 'CollectionAlbumDetail',
        component: () => import('@/views/home/collectionAlbumDetail'),
        meta: { title: '藏品合辑', keepAlive: false, tabbarShow: false }
      },
      {
        path: '/blindBox',
        name: 'BlindBox',
        component: () => import('@/views/home/blindBox'),
        meta: { title: '盲盒', keepAlive: false, tabbarShow: false }
      }
    ]
  }
]
