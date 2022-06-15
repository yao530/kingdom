<template>
	<div class="container">
		<div class="title">成本中心本月用电占比</div>
		<a-table
			size="small"
			class="table"
			:scroll="{ y: 140 }"
			:dataSource="list"
			:columns="columns"
			rowKey="name"
			:pagination="false"
		/>
	</div>
</template>
<script setup lang="ts">
import { computed } from 'vue';
import { IElectricStructure } from '@/api/Interior/types/res';
const props = defineProps<{
	data: IElectricStructure;
	total: number;
}>();

const columns = [
	{
		title: '类型',
		dataIndex: 'name'
	},
	{
		title: '合计',
		dataIndex: 'sum'
	}
	// {
	// 	title: '占比',
	// 	dataIndex: 'ratio'
	// }
];
const list = computed(() => {
	const data = props.data || {};
	return Object.keys(data)
		.map((name) => {
			const sum = data[name];
			const ratio = Math.floor((sum / props.total) * 10000) / 100 + '%';
			return {
				name,
				sum,
				ratio
			};
		})
		.sort((p, n) => n.sum - p.sum);
});
</script>
<style lang="scss" scoped>
@import './ElectricCommon.scss';
@import './ElectricResetTable.scss';

.container {
	margin-bottom: 20px;
}
</style>
