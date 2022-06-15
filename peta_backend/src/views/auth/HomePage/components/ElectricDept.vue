<template>
	<div class="container">
		<div class="title">部门本月用电占比</div>
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
			// const ratio = (Math.floor(value/props.total * 10000) / 100) + '%';
			return {
				name,
				value
				// ratio
			};
		})
		.sort((p, n) => n.value - p.value);
});

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
	console.log(111111111111);
	chart.value.resize();
}
function setChart() {
	if (!chart.value) {
		return console.warn('not chart');
	}
	chart.value.hideLoading();

	chart.value.setOption({
		tooltip: {
			trigger: 'item'
		},
		series: [
			{
				type: 'pie',
				radius: '50%',
				data: list.value,
				emphasis: {
					itemStyle: {
						shadowBlur: 10,
						shadowOffsetX: 0,
						shadowColor: 'rgba(0, 0, 0, 0.5)'
					}
				}
			}
		]
	});
}
</script>
<style lang="scss" scoped>
@import './ElectricCommon.scss';

.container {
	margin-bottom: 20px;
}

.node {
	height: 190px;
}
</style>
