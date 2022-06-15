<template>
	<a-cascader
		v-model:value="modelValue"
		:options="district"
		tree-default-expand-all
		v-bind="formItem.props"
		showSearch
		v-on="formItem.eventObject"
	/>
</template>
<script lang="ts">
import { defineComponent, PropType, computed } from 'vue';
import district from '@/utils/district';

export default defineComponent({
	name: 'SchemaFormDistrict',
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
	setup(props, { emit }) {
		const modelValue = computed({
			get: () => props.value,
			set: (val) => emit('update:value', val)
		});

		return {
			modelValue,
			district
		};
	}
});
</script>
