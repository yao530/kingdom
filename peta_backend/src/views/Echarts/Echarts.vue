<template>
	<div class="page_container">
		<div id="myChart" :style="{ width: '300px', height: '300px' }"></div>
		<div class="content"></div>
	</div>
</template>
<script lang="ts">
import { defineComponent, ref, watchEffect } from 'vue';
import { onMounted } from 'vue';
import echarts from '@/utils/echarts';

export default defineComponent({
	name: 'Echarts',
	components: {},
	setup() {
		onMounted(() => {
			// 需要获取到element,所以是onMounted的Hook
			let myChart = echarts.init(document.getElementById('myChart') as HTMLElement);
			// 绘制图表
			myChart.setOption({
				xAxis: {
					data: ['4-3', '4-4', '4-5', '4-6', '4-7', '4-8', '4-9']
				},
				yAxis: {},
				series: [
					{
						name: '用户量',
						type: 'line',
						data: [8, 15, 31, 13, 15, 22, 11]
					}
				]
			});
			window.onresize = function () {
				// 自适应大小
				myChart.resize();
			};
		});
	}
});
</script>
<style lang="less" scoped>
.page_container {
	height: 100vh;
	background: #f5f5f5;
}

.content {
	display: flex;
	justify-content: center;
}
</style>
