<template>
	<div>
		<div class="global_page_header">
			<a-input-search v-if="config.showSearch" placeholder="搜索关键字" style="width: 180px" @search="onSearch" />
			<DateRangeTemplate
				v-if="config.dateRange"
				v-bind="config.dateRange"
				@change="onDateRangeChange"
				@ok="onDateRangeOk"
			/>
			<!-- <a-button v-if="config.saveUrl" v-permission="2" class="margin_l_20" type="primary" @click="onEdit()">
				新建{{ config.theme }}
			</a-button> -->
		</div>
		<CommonTable
			ref="tableRef"
			:extraParams="extraParams"
			v-bind="config.commonTableProps"
			:removeUrl="config.removeUrl"
			:pageUrl="config.pageUrl"
			:listUrl="config.listUrl"
			@onEdit="onEdit"
		/>
	</div>
</template>
<script lang="ts">
import { defineComponent, reactive, toRefs, ref, computed, onMounted } from 'vue';
import { useFormModal } from '@/hooks/useFormModal';
import CommonTable from '@/components/CommonTable/index.vue';
import DateRangeTemplate from '@/components/DateRangeTemplate/index.vue';
import http from '@/utils/http/axios';
import md5 from 'blueimp-md5';
import { message } from 'ant-design-vue';
import { hasPermission } from '@/utils/permission/hasPermission';
import Api from '@/api/Order';
import constants from '@/enums/constants';
import IColumn from '@/interface/FE/column';
import booleanRender from '@/columnCustomRender/booleanRender';
import imageRender from '@/columnCustomRender/imageRender';
import ICurdConfig, { IDateRangeConfig } from '@/interface/FE/ICurdConfig';
import constantRender from '@/columnCustomRender/constantRender';
import formatTimeRender from '@/columnCustomRender/formatTimeRender';
/**
 * @description 表格列
 */
const columns: IColumn[] = [
	{
				title: '订单编号',
				dataIndex: 'orderNo'
			},
			{
				title: '支付用户',
				dataIndex: 'userName'
			},
			{
				title: '用户手机',
				dataIndex: 'mobilePhone'
			},
			{
				title: '藏品类型',
				dataIndex: 'collectionType',
				customRender: constantRender(constants.NftType.dict)
			},
			{
				title: '藏品名称',
				dataIndex: 'collectionName'
			},
			{
				title: '藏品图片',
				dataIndex: 'collectionCover',
				customRender: imageRender.set({
					width: '50px'
				})
			},

			{
				title: '合约地址',
				dataIndex: 'blockSmartContractAddress'
			},
			{
				title: '藏品ID',
				dataIndex: 'nftId'
			},
			{
				title: '创作者',
				dataIndex: 'artCreatorName'
			},
			{
				title: '支付金额',
				dataIndex: 'payableFee'
			},
			{
				title: '支付方式',
				dataIndex: 'payWay',
				customRender: constantRender(constants.PayType.dict)
			},
			{
				title: '订单状态',
				dataIndex: 'status',
				//customRender: booleanRender.set('已发布', '未发布') // 改文案的话就这么写
					customRender: constantRender(constants.OrderStatus.dict)
			},
			{
				title: '下单时间',
				dataIndex: 'createTime',
				customRender: formatTimeRender
			},
];

/**
 * @description 表单项。工厂模式保证每次打开都是新的
 */
function getFormSchema(): FormSchema {
	return {
		style: {
			width: 'auto'
		},
		formItemLayout: {
			labelCol: {
				span: 5
			},
			wrapperCol: {
				span: 20
			}
		},
		width: 1000,
		formItem: [
			{
				type: 'input',
				label: '可选项名称',
				field: 'optionName',
				value: undefined,
				props: {
					placeholder: '请填写可选项名称'
				},

				rules: [
					{
						required: true,
						message: '可选项名称不能为空'
					}
				]
			},
			{
				type: 'input',
				label: '可选项编号',
				field: 'optionCode',
				value: '',
				props: {
					placeholder: '可选项编号'
				}
			},

			{
				type: 'textarea',
				label: '备注',
				field: 'remark',
				value: '',
				props: {
					placeholder: '角色描述'
				}
			}
		]
	};
}
const config: ICurdConfig = {
	theme: '订单',
	showSearch: true,
	dateRange: true,
	pageUrl: Api.page,
	commonTableProps: {
		columns
	},
	getFormSchema
};

export default defineComponent({
	name: 'Banner',
	components: {
		CommonTable,
		DateRangeTemplate
	},
	setup() {
		const tableRef = ref<InstanceType<typeof CommonTable>>();
		const hasRoleRetrievePermission = hasPermission('Roles');
		const state = reactive({
			searchValue: undefined,
			dateRangeValue: undefined
		});
		const extraParams = computed(() => {
			const keywordName = typeof config.showSearch === 'string' ? config.showSearch : 'keyWord';
			return Object.assign(
				{
					[keywordName]: state.searchValue
				},
				state.dateRangeValue
			);
		});

		const onEdit = (record?) => {
			let formSchema = config.getFormSchema();
			useFormModal({
				title: `${record ? '编辑' : '新建'}${config.theme}`,
				fields: record,
				formSchema,
				handleOk: async (modelRef, state) => {
					const params: any = {};
					if (record) {
						Object.assign(
							params,
							{
								password: record.password,
								id: record.id
							},
							modelRef
						);
					} else {
						Object.assign(params, modelRef, {
							password: md5(modelRef.password)
						});
					}
					await http.request({
						url: config.saveUrl,
						params
					});
					message.success('操作成功');
					tableRef.value?.getData();
				}
			});
		};
		function onSearch(value) {
			state.searchValue = value;
		}
		function onDateRangeChange(event) {
			console.log('change', event);
			if (typeof config.dateRange == 'object') {
				const dateRange = config.dateRange as IDateRangeConfig;
			}
			(state.dateRangeValue as any) = {
				startTime: event[0] ? event[0].valueOf() : undefined,
				endTime: event[1] ? event[1].valueOf() : undefined
			};
		}
		function onDateRangeOk(event) {
			// console.log('ok', event);
		}

		return {
			...toRefs(state),
			extraParams,
			config,
			tableRef,
			onEdit,
			onSearch,
			onDateRangeChange,
			onDateRangeOk
		};
	}
});
</script>

<style lang="scss"></style>
