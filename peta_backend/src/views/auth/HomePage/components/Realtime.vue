<template>
	<a-card>
		<div class="header">
			<div class="title">实时监测数据</div>
			<div class="place"></div>
			<!-- <a-input-search v-model:value="keyword" :placeholder="data.length + '条记录'" style="width: 200px" /> -->
		</div>
		<a-table
			size="small"
			:scroll="{ y: 300 }"
			:dataSource="data"
			:columns="columns"
			rowKey="datetime"
			:pagination="false"
		/>
	</a-card>
</template>
<script lang="ts">
import type { IRealTimeListType } from '@/api/Interior/types/res';

import { defineComponent, reactive, toRefs, ref, computed, onMounted } from 'vue';
import { useStore } from '@/store';
import { getRealTimes } from '@/api/Interior/services';

function generateSorter(field) {
	return (rowA, rowB) => rowA[field] - rowB[field];
}
const wid = 92;

export default defineComponent({
	props: {
		data: Array
	},
	setup(props) {
		// const data = computed(()=>{
		// 	return props.data;
		// });
		// let store = useStore();
		// const accountId = computed(() => store.state.currentAccount?.accountInfo?.id);

		const realTimes = ref<IRealTimeListType>([]);
		const keyword = ref('');
		// const dataSource = computed(() => {
		// 	if (keyword.value) {
		// 		return realTimes.value.filter((item) => {
		// 			for (let fieldKey in item) {
		// 				if (item[fieldKey].toString().includes(keyword.value)) {
		// 					return true;
		// 				}
		// 			}
		// 			return false;
		// 		});
		// 	} else {
		// 		return realTimes.value;
		// 	}
		// });
		// onMounted(() => {
		// 	getData();
		// });
		// async function getData() {
		// 	let data = await getRealTimes(accountId.value as number);
		// 	// data = data.concat(data.map((item, index) => {
		// 	// 	return {...item, datetime: index.toString()};
		// 	// }));
		// 	// data = data.concat(data.map((item, index) => {
		// 	// 	return {...item, datetime: (index + 10).toString()};
		// 	// }));
		// 	realTimes.value = data;
		// }
		return {
			// keyword,
			// data,
			// dataSource,
			columns: [
				{
					title: '设备名称',
					dataIndex: 'equip_name'
				},
				{
					title: '时间',
					dataIndex: 'datetime',
					width: 120
				},
				{
					title: 'A相电压',
					dataIndex: 'Ua',
					// width: wid,
					sorter: generateSorter('Ua')
				},
				{
					title: 'B相电压',
					dataIndex: 'Ub',
					// width: wid,
					sorter: generateSorter('Ub')
				},
				{
					title: 'C相电压',
					dataIndex: 'Uc',
					// width: wid,
					sorter: generateSorter('Uc')
				},
				{
					title: 'A相电流',
					dataIndex: 'Ia',
					// width: wid,
					sorter: generateSorter('Ia')
				},
				{
					title: 'B相电流',
					dataIndex: 'Ib',
					// width: wid,
					sorter: generateSorter('Ib')
				},
				{
					title: 'C相电流',
					dataIndex: 'Ic',
					// width: wid,
					sorter: generateSorter('Ic')
				},
				{
					title: '功率因子',
					dataIndex: 'T_P_F',
					// width: wid,
					sorter: generateSorter('T_P_F')
				},
				{
					title: 'A相谐波',
					dataIndex: 'Ia_THD',
					// width: wid,
					sorter: generateSorter('Ia_THD')
				},
				{
					title: 'B相谐波',
					dataIndex: 'Ib_THD',
					// width: wid,
					sorter: generateSorter('Ib_THD')
				},
				{
					title: 'C相谐波',
					dataIndex: 'Ic_THD',
					// width: wid,
					sorter: generateSorter('Ic_THD')
				}
			]
		};
	}
});
</script>
<style lang="scss">
.header {
	display: flex;
	align-items: flex-end;
	padding-bottom: 20px;

	.title {
		font-size: 18px;
		font-weight: bold;
		color: #464646;
	}

	.place {
		flex: 1;
	}
}
</style>
