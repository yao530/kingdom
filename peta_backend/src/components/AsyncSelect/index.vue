<template>
	<a-select v-model:value="modelValue" v-bind="propAttrs" :loading="loading" :options="options" />
</template>
<script lang="ts">
import { defineComponent, toRefs, reactive, watch, PropType, onMounted, computed } from 'vue';
import http from '@/utils/http/axios';

interface IParams {
	[k: string]: string | number;
}
export default defineComponent({
	name: 'AsyncSelect',
	components: {},
	props: {
		params: {
			type: Object
		},
		url: {
			type: String
		},
		format: {
			type: Function
		},
		value: {
			type: [String, Number],
			default: undefined
		}
	},
	emits: ['update:value'],
	setup(props, { attrs, emit }) {
		const state = reactive({
			options: [],
			loading: false
		});
		const modelValue = computed({
			get: () => props.value,
			set: (val) => {
				emit('update:value', val);
			}
		});
		const propAttrs = computed(() => {
			return {};
		});

		watch(
			() => props.params,
			() => {
				getData();
			}
		);
		watch(
			() => props.url,
			() => {
				getData();
			}
		);
		onMounted(getData);

		async function getData() {
			state.options = [];
			if (!props.url) {
				return;
			}
			state.loading = true;
			const data = await http
				.request({
					url: props.url,
					params: props.params
				})
				.finally(() => {
					state.loading = false;
				});
			state.options = data.map(props.format);
		}
		return {
			...toRefs(state),
			modelValue,
			propAttrs
		};
	}
});
</script>
