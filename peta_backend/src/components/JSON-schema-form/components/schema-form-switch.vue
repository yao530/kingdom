<template>
	<a-switch v-model:checked="modelValue" v-bind="switchProps" v-on="formItem.eventObject" />
</template>
<script lang="ts">
import { defineComponent, PropType, computed } from 'vue';
import { Switch } from 'ant-design-vue';

export default defineComponent({
	name: 'SchemaFormSwitch',
	components: {
		[Switch.name]: Switch
	},
	props: {
		formItem: {
			// 表单项
			type: Object as PropType<FormItem>,
			default: () => ({})
		},
		value: undefined as any // 表单项值
	},
	emits: ['update:value'],
	setup(props, { attrs, emit }) {
		const switchProps = computed(() => {
			return Object.assign(
				{
					checkedChildren: '启用',
					unCheckedChildren: '禁用'
				},
				props.formItem.props
			);
		});
		const options = typeof props.value === 'number' ? [0, 1] : [false, true];
		const modelValue = computed({
			get: () => !!props.value,
			set: (val) => emit('update:value', options[Number(val)])
		});

		return {
			modelValue,
			switchProps
		};
	}
});
</script>
