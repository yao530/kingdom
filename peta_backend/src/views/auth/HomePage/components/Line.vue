<template>
	<div ref="node" class="node"></div>
</template>
<script lang="ts">
import moment from 'moment';
import { defineComponent, ref, shallowRef, watchEffect, computed, watch, nextTick } from 'vue';
import { onMounted, onUnmounted } from 'vue';
import { useStore } from '@/store';
import type { InteriorHoursType } from '@/api/Interior/types/res';
import { IHourPoint } from '@/api/Interior/types/res';
import echarts from '@/utils/echarts';

interface IProject {
	name: string;
	type: string;
	stack: string;
	dataM?: Object;
	data: number[];
}

const timeTemplate = 'D号 HH点';

export default defineComponent({
	name: 'Line',
	components: {},
	props: {
		data: {
			type: Array
		},
		title: String
	},
	setup(props, ctx) {
		const node = ref();
		const chart = shallowRef();
		let store = useStore();
		// const accountId = computed(() => store.state.currentAccount?.accountInfo?.id);
		const data = props.data as IHourPoint[];
		function chartResize() {
			chart.value.resize();
		}

		watch(data, setChart);
		onMounted(init);
		onUnmounted(() => {
			window.removeEventListener('resize', chartResize, false);
		});

		function getXAxisData() {
			let start = moment();
			for (let i = 0; i < data.length; i++) {
				const point = data[i];
				const mom = moment(point.date).hour(point.hour);
				if (mom.isBefore(start)) {
					start = mom;
				}
			}
			const arr: moment.Moment[] = [];
			for (let i = 0; i < 24; i++) {
				arr.push(start.clone().add(i, 'hour'));
			}
			return arr;
		}

		function getSeries() {
			const xMoment = getXAxisData();
			const projM = {};
			data.forEach((point) => {
				const name = (point.feeder_cabinet_name || point.transformer_name) as string;
				if (!projM[name]) {
					projM[name] = {
						name,
						type: 'line',
						// stack: 'Total',
						dataM: {}
					};
				}
				const mom = moment(point.date).hour(point.hour);
				const x = mom.format(timeTemplate);
				projM[name].dataM[x] = point.power;
			});
			return Object.values(projM).map((line) => {
				const { name, type, stack, dataM } = line as IProject; // @ts-ignore
				const data = xMoment.map((mom) => {
					const x = mom.format(timeTemplate);
					return (dataM as Object)[x];
				});
				return {
					name,
					type,
					stack,
					data
				};
			});
		}

		async function init() {
			if (!node.value) return console.error('node');
			chart.value = echarts.init(node.value as HTMLElement);
			window.addEventListener('resize', chartResize, false);
			chart.value.showLoading();
			setChart();
		}

		function setChart() {
			if (!chart.value) {
				return console.warn('not chart');
			}
			chart.value.hideLoading();
			const series = getSeries();
			console.log(series);

			chart.value.setOption({
				title: {
					text: props.title,
					left: 'left'
				},
				tooltip: {
					order: 'valueDesc',
					trigger: 'axis'
				},
				legend: {
					data: series.map((t) => t.name)
				},
				grid: {
					left: '20',
					right: '30',
					bottom: '20',
					containLabel: true
				},
				toolbox: {
					// feature: {
					// 	saveAsImage: {}
					// }
				},
				xAxis: {
					type: 'category',
					boundaryGap: false,
					data: getXAxisData().map((t) => t.format(timeTemplate))
				},
				yAxis: {
					type: 'value'
				},
				series
			});
		}

		return {
			node
		};
	}
});
</script>
<style lang="less" scoped>
.node {
	height: 355px;
	padding-top: 20px;
	background: #fff;
}
</style>
