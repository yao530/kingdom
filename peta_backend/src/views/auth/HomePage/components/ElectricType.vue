<template>
	<div class="container">
		<div class="title">设备类别本月用电Top10</div>
		<div ref="node" class="node"></div>
	</div>
</template>
<script setup lang="ts">
import { computed, watch, onMounted, onUnmounted, ref, shallowRef } from 'vue';
import echarts from '@/utils/echarts';
import { IElectricStructure } from '@/api/Interior/types/res';
const props = defineProps<{
	data: IElectricStructure;
	total: number;
}>();

const node = ref();
const chart = shallowRef();
const list = computed(() => {
	const data = props.data;
	return Object.keys(data)
		.map((name) => {
			const value = data[name];
			return {
				name,
				value
			};
		})
		.sort((p, n) => n.value - p.value);
});
const xAxisData = list.value.map((t) => t.name);
const seriesData = list.value.map((t) => t.value);

watch(props.data, setChart);
onMounted(init);
onUnmounted(() => {
	window.removeEventListener('resize', chartResize, false);
});
function init() {
	if (!node.value) return console.error('node');
	chart.value = echarts.init(node.value as HTMLElement);
	window.addEventListener('resize', chartResize, false);
	chart.value.showLoading();
	setChart();
}
function chartResize() {
	chart.value.resize();
}
function setChart() {
	if (!chart.value) {
		return console.warn('not chart');
	}
	chart.value.hideLoading();

	chart.value.setOption({
		tooltip: {
			trigger: 'axis',
			axisPointer: {
				type: 'shadow'
			}
		},
		grid: {
			top: '8%',
			left: '0%',
			right: '0%',
			bottom: '4%',
			containLabel: true
		},
		xAxis: [
			{
				type: 'category',
				data: xAxisData,
				axisLabel: {
					interval: 0,
					rotate: 40
				}
			}
		],
		yAxis: [
			{
				type: 'value'
			}
		],
		series: [
			{
				// name: 'Direct',
				type: 'bar',
				barWidth: '60%',
				data: seriesData
			}
		]
	});
}
</script>
<style lang="scss" scoped>
@import './ElectricCommon.scss';

.container {
	// margin-bottom: 20px;
}

.node {
	height: 190px;
}
</style>
