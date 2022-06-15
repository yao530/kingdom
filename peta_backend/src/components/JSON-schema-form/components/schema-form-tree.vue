<template>
	<a-tree-select
		v-model:value="modelValue"
		style="width: 100%"
		:dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
		:tree-data="treeData"
		tree-default-expand-all
		v-bind="formItem.props"
		v-on="formItem.eventObject"
	/>
</template>
<script lang="ts">
import { defineComponent, PropType, computed } from 'vue';

export default defineComponent({
	name: 'SchemaFormSelect',
	components: {},
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
		const modelValue = computed({
			get: () => props.value,
			set: (val) => emit('update:value', val)
		});
		function getTree(srcList) {
			if (!srcList) {
				return srcList;
			}
			return srcList.map((item) => {
				return {
					title: item.label,
					value: item.value,
					children: getTree(item.children)
				};
			});
		}
		const treeData = computed(() => {
			return getTree(props.formItem.options);
		});
		console.log(treeData, props.formItem.options, props.formItem);

		return {
			modelValue,
			treeData
		};
	}
});
</script>
