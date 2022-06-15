<template>
	<div id="WordCloud"></div>
</template>
<script lang="ts">
import { defineComponent, ref, watchEffect } from 'vue';
import { onMounted } from 'vue';
import echarts from '@/utils/echarts';

export default defineComponent({
	name: 'WordCloud',
	components: {},
	props: {
		Device_type: {
			type: Object
		}
	},
	setup(props) {
		let chart;
		onMounted(() => {
			chart = echarts.init(document.getElementById('WordCloud') as HTMLElement);
			chart.showLoading();
			const width = (document.getElementById('WordCloud') as HTMLElement).clientWidth;
			console.log('widthwidthwidth', width);
			if (chart) {
				chart.hideLoading();
				const data: any[] = [];
				for (let i = 0; i < 6; i++) {
					data.push({
						name: '空调' + i,
						value: 2 ** Math.min(10, i + 1)
					});
				}
				chart.setOption({
					title: {
						text: '本月设备电量云图',
						left: 20
					},
					grid: {
						left: '20'
						// bottom: '20',
						// containLabel: true
					},
					series: [
						{
							type: 'wordCloud',
							gridSize: 2,
							sizeRange: [12, 50],
							rotationRange: [-90, 90],
							// shape: 'pentagon',
							shape: 'diamond',
							// shape: 'triangle',
							keepAspect: false,
							left: 'center',
							top: 'center',
							width: '100%',
							height: 150,
							drawOutOfBound: true,
							textStyle: {
								normal: {
									color: function () {
										return (
											'rgb(' +
											[
												Math.round(Math.random() * 255),
												Math.round(Math.random() * 255),
												Math.round(Math.random() * 255)
											].join(',') +
											')'
										);
									}
								},
								emphasis: {
									shadowBlur: 10,
									shadowColor: '#333'
								}
							},
							//数据自己加
							data
						}
					]
				});

				setTimeout(() => {
					chart.resize();
				}, 100);
			}
		});
	}
});
</script>
<style lang="less" scoped>
#WordCloud {
	height: 300px;
	padding-top: 20px;
	// margin-right: 20px;
	background: #fff;
	flex-shrink: 0;
	flex-grow: 1;
}
</style>
