<template>
	<a-date-picker v-model:value="modelValue" placeholder="选择日期" v-bind="$attrs" />
</template>
<script lang="ts">
import { defineComponent, PropType, computed } from 'vue';
import moment from 'moment';

export default defineComponent({
	name: 'FormDate',
	components: {},
	props: {
		value: [Number]
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
