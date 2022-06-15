import { createApp } from 'vue';
import App from './App.vue';
import router, { setupRouter } from './router';
import { setupStore } from '@/store';
import useFormModal from '@/hooks/useFormModal';
import useCreateModal from '@/hooks/useCreateModal';
import * as separate from '@/utils/separate';
import moment from 'moment';
import { setupAntd, setupDirectives, setupGlobalMethods, setupCustomComponents } from '@/plugins';
window.moment = moment;
const app = createApp(App);

app.use(useFormModal);
app.use(useCreateModal);
app.use(separate.install);
// 注册全局常用的ant-design-vue组件
setupAntd(app);
// 注册全局自定义组件,如：<svg-icon />
setupCustomComponents(app);
// 注册全局自定义指令，如：v-permission权限指令
setupDirectives(app);
// 注册全局方法，如：app.config.globalProperties.$message = message
setupGlobalMethods(app);
// 挂载vuex状态管理
setupStore(app);
// 挂载路由
setupRouter(app);
// 路由准备就绪后挂载APP实例
router.isReady().then(() => app.mount('#app'));
declare global {
	interface Window {
		_app: any;
	}
}
window._app = app;
