// create-api.ts
import { App, createVNode, render, VNode } from 'vue';
import FormModal from './form-modal.vue';
import zhCN from 'ant-design-vue/es/locale/zh_CN';
import { ConfigProvider } from 'ant-design-vue';

interface ModalInstance {
	remove(): void;
	$updateProps(props: any): void;
}

interface Options {
	handleOk: (modelRef: any, state) => Promise<any>; // 点击提交表单
	formSchema: FormSchema; // 表单描述属性
	fields?: object; // 字段默认填充值，一般编辑表单是传入
	hiddenFields?: string[]; // 需要隐藏的表单项
	[key: string]: any;
}

let _app;

/**
 * 创建表单模态框
 * @param modalOptions
 * @param formOptions
 */
export const useFormModal = (options: Options): VNode<ModalInstance> => {
	// 组件实例
	let separateComponent;
	const container = document.createElement('div');
	// 移除组件
	const remove = () => {
		separateComponent = null;
		render(null, container);
		container.remove();
	};
	separateComponent = createVNode(
		ConfigProvider,
		{
			locale: zhCN
		},
		[createVNode(FormModal, { ...options, remove })]
	);
	// 使组件继承App实例上下文
	const mainContext = _app ? _app._instance?.appContext || _app._context : separateComponent.appContext;
	separateComponent.appContext = mainContext;

	render(separateComponent, container);
	return separateComponent;
};

// 暴露一个插件 API
const install = (app: App) => {
	_app = app;
};
export default install;
