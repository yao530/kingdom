<template>
	<div id="DeviceTypePie"></div>
</template>
<script lang="ts">
import { defineComponent, ref, watchEffect } from 'vue';
import { onMounted } from 'vue';
import echarts from '@/utils/echarts';

export default defineComponent({
	name: 'Echarts',
	components: {},
	props: {
		Device_type: {
			type: Object
		}
	},
	setup(props) {
		let chart;
		onMounted(() => {
			// 需要获取到element,所以是onMounted的Hook
			chart = echarts.init(document.getElementById('DeviceTypePie') as HTMLElement);
			chart.showLoading();
		});
		watchEffect(() => {
			if (props.Device_type && chart) {
				chart.hideLoading();
				const data: any[] = [];
				for (let name in props.Device_type) {
					const value = props.Device_type[name];
					data.push({ name, value });
				}
				chart.setOption({
					legend: false,
					title: {
						text: '设备类型占比',
						left: 20
					},
					tooltip: {
						trigger: 'item'
					},
					grid: {
						left: '0%',
						top: '0%',
						bottom: '0%',
						right: '0%',
						containLabel: true
					},
					series: [
						{
							type: 'pie',
							radius: '50%',
							data,
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
		});
	}
});
</script>
<style lang="less" scoped>
#DeviceTypePie {
	width: 260px;
	height: 300px;
	padding-top: 20px;
	margin-right: 20px;
	background: #fff;
	flex-shrink: 0;
}
</style>
