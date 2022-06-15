import type { ObjectDirective } from 'vue';
import { hasPermissionCurrentRoute } from '@/utils/permission/hasPermission';

export const permission: ObjectDirective = {
	mounted(el: HTMLButtonElement, binding, vnode) {
		if (binding.value == undefined) return;
		const visible = hasPermissionCurrentRoute(binding.value);
		if (!visible) {
			el.remove();
		}
	}
};
