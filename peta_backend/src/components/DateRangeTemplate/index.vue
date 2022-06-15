<template>
	<a-range-picker :show-time="showTime" class="margin_l_20" :style="style" @change="onChange" @ok="onOk" />
</template>

<script lang="ts">
import moment from 'moment';
import { defineComponent, PropType, SetupContext } from 'vue';
import { IDateRangeConfig } from '@/interface/FE/ICurdConfig';
export default {
	props: {
		showTime: {
			type: [Boolean, Object] as PropType<boolean | IDateRangeConfig>,
			default() {
				return {
					defaultValue: [moment('00:00:00', 'HH:mm:ss'), moment('23:59:59', 'HH:mm:ss')]
				};
			}
		},
		style: {
			type: Object,
			default() {
				return {
					width: '370px'
				};
			}
		}
	},
	emits: ['change', 'ok'],
	setup(props, ctx) {
		function onChange() {
			ctx.emit('change', ...arguments);
		}
		function onOk() {
			ctx.emit('ok', ...arguments);
		}
		return {
			onChange,
			onOk
		};
	}
};
</script>
