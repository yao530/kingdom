<template>
	<DatePicker v-model:value="modelValue" v-bind="formItem.props" />
</template>
<script lang="ts">
import { defineComponent, PropType, computed } from 'vue';
import moment from 'moment';
import { DatePicker } from 'ant-design-vue';

export default defineComponent({
	name: 'SchemaFormInput',
	components: {
		DatePicker
	},
	props: {
		formItem: {
			// 表单项
			type: Object as PropType<FormItem>,
			default: () => ({})
		},
		value: Number
	},
	emits: ['update:value'],
	setup(props, { attrs, emit }) {
		const modelValue = computed({
			get: () => (props.value ? moment(props.value) : undefined),
			set: (val) => {
				const new_value = val ? val.valueOf() : undefined;
				emit('update:value', new_value);
			}
		});
		return {
			modelValue
		};
	}
});
</script>
