import { Component } from 'vue';
import { App, createVNode, render, VNode } from 'vue';
interface ISeparate {
	comInstance: null | VNode;
	domContainer: HTMLElement | null;
	show: (props: any) => ISeparate;
	destroy: () => void;
}
let _app;

export function generate(vueComponent: Component): ISeparate {
	const obj: ISeparate = {
		// 组件实例
		comInstance: null,
		domContainer: null,
		show(props: any) {
			this.domContainer = document.createElement('div');
			this.comInstance = createVNode(vueComponent, {
				...props,
				onDestroy: this.destroy
			});
			// 使当前模态框继承App实例上下文
			const mainContext = _app ? _app._instance?.appContext || _app._context : this.comInstance.appContext;
			this.comInstance.appContext = mainContext;
			render(this.comInstance, this.domContainer);
			document.body.appendChild(this.domContainer);
			return this;
		},
		// 移除组件
		destroy() {
			obj.comInstance = null;
			if (obj.domContainer !== null) {
				render(null, obj.domContainer);
				obj.domContainer.remove();
			}
		}
	};
	return obj;
}

// 暴露一个插件 API
export function install(app: App) {
	_app = app;
}
