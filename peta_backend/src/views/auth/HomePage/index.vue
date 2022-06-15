<template>
	<div v-if="accountData" class="home_container">
		<div class="block_sums">
			<div>
				<InforCard background="#f44336" describe="本月累计用电量" :count="accountData.CM_Electric" />
			</div>
			<div>
				<InforCard background="#4caf50" describe="上月累计用电量" :count="accountData.LM_Electric" />
			</div>
			<div>
				<InforCard background="#2196f3" describe="年度累计用电量" :count="accountData.CY_Electric" />
			</div>
			<div>
				<InforCard
					background="#ff9800"
					describe="在线监测网点"
					:count="accountData.Online_Qty"
					:precision="0"
				/>
			</div>
		</div>

		<div class="layout_central">
			<div class="lines">
				<div class="wrap">
					<Line title="24小时馈线柜负荷趋势图" :data="accountData['24h_FC_Load']" />
				</div>
				<Line title="24小时变压器负荷趋势图" :data="accountData['24h_TF_Load']" />
			</div>
			<div class="ratios">
				<ElectricECC :data="accountData.ECC_Electric" :total="accountData.Total_ECC_Electric" />
				<ElectricDept :data="accountData.Dept_Electric" :total="accountData.Total_Dept_Electric" />
				<ElectricType :data="accountData.Type_Electric" :total="accountData.Total_Type_Electric" />
			</div>
		</div>
		<Realtime :data="accountData.Realtime_Data" />
	</div>
	<ErrorMsg v-else-if="false" title="出错了" sub-title="出错了" />
	<div v-else-if="loading" class="spin_wrap">
		<a-spin size="large" />
	</div>
</template>
<script lang="ts">
import { defineComponent, reactive, toRefs, ref, computed, onMounted } from 'vue';
import InforCard from '@/components/InforCard/index.vue';
import ErrorMsg from '@/components/ErrorMsg/index.vue';
import { getAccountData } from '@/api/Interior/services';
import { useStore } from '@/store';
import { IInteriorAccount } from '@/api/Interior/types/res';
import DeviceTypePie from './components/DeviceTypePie.vue';
import WordCloud from './components/WordCloud.vue';
import ElectricDept from './components/ElectricDept.vue';
import ElectricECC from './components/ElectricECC.vue';
import ElectricType from './components/ElectricType.vue';

import Line from './components/Line.vue';
import Realtime from './components/Realtime.vue';

export default defineComponent({
	name: 'HomePage',
	components: {
		InforCard,
		ErrorMsg,
		DeviceTypePie,
		WordCloud,
		ElectricECC,
		ElectricDept,
		ElectricType,
		Line,
		Realtime
	},
	setup() {
		let store = useStore();
		const accountInfo = computed(() => store.state.currentAccount.accountInfo);
		const accountData = ref<IInteriorAccount | null>(null);
		const loading = ref(false);

		onMounted(() => {
			getData();
		});

		async function getData() {
			loading.value = true;
			const data = await getAccountData(accountInfo.value!.id);
			loading.value = false;
			accountData.value = data;
		}

		return {
			accountData,
			loading
		};
	}
});
</script>

<style lang="scss" scoped>
.block_sums {
	display: flex;
	margin-bottom: 20px;

	> div {
		flex: 1;

		&:not(:first-child) {
			margin-left: 20px;
		}
	}
}

.layout_central {
	display: flex;
	// height: 730px;
	margin-bottom: 20px;

	.lines {
		width: 74.4%;
		margin-right: 20px;

		.wrap {
			margin-bottom: 20px;
		}
	}

	.ratios {
		flex: 1;
		width: 0;
	}
}

.spin_wrap {
	display: flex;
	height: 64vh;
	align-items: center;
	justify-content: center;
}
</style>
