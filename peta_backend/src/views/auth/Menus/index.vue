<template>
	<div>
		<div v-permission="2" class="global_page_header">
			<a-button v-if="config.saveUrl" class="margin_l_20" type="primary" @click="showInfo()"
				>新建{{ config.theme }}</a-button
			>
		</div>
		<CommonTable
			ref="tableRef"
			v-bind="config.commonTableProps"
			:pageUrl="config.pageUrl"
			:listUrl="config.listUrl"
			:removeUrl="config.removeUrl"
			@onEdit="showInfo"
			@dataSourceUpdate="onDataSourceUpdate"
		/>
		<MenusInfo ref="MenusInfoRef" :menuTree="dataSource" @onRefresh="onRefresh" />
	</div>
</template>
<script lang="ts">
import { defineComponent, ref, onMounted, reactive, toRefs } from 'vue';
import { useRoute } from 'vue-router';
import { IMeta } from '@/router/types';
import CommonTable from '@/components/CommonTable/index.vue';
import MenusInfo from './components/MenusInfo.vue';
import config from './config';

const Component = defineComponent({
	name: 'Menus',
	components: {
		CommonTable,
		MenusInfo
	},
	setup() {
		const MenusInfoRef = ref();
		const tableRef = ref<InstanceType<typeof CommonTable>>();

		const route = useRoute();
		const meta = route.meta as unknown as IMeta;
		const state = reactive({
			dataSource: [],
			loading: false,
			authStatus: meta.authStatus
		});
		function onDataSourceUpdate(menus) {
			state.dataSource = menus;
		}
		function showInfo(event?) {
			MenusInfoRef.value?.show(event);
		}
		async function onRefresh() {
			console.log(tableRef.value);
			tableRef.value?.getData();
		}

		return {
			...toRefs(state),
			config,
			MenusInfoRef,
			tableRef,
			columns: [
				{
					title: '菜单名称',
					dataIndex: 'menuName'
				},
				{
					title: '菜单链接',
					dataIndex: 'menuUrl'
				},
				{
					title: '菜单编码',
					dataIndex: 'menuCode'
				},
				{
					title: '排序',
					dataIndex: 'sort'
				},
				{
					title: '备注',
					dataIndex: 'remark'
				}
			],
			showInfo,
			onRefresh,
			onDataSourceUpdate
		};
	}
});
export default Component;
</script>
