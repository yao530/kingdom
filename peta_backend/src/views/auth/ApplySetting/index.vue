<script lang="ts">
import { defineComponent, reactive, toRefs, ref, computed, onMounted } from 'vue';
import { useFormModal } from '@/hooks/useFormModal';
import CommonTable from '@/components/CommonTable/index.vue';
import md5 from 'blueimp-md5';
import { message } from 'ant-design-vue';
import Api from '@/api/ApplySeting';
import RoleApi from '@/api/CharacterRole';
import ICurdConfig from '@/interface/FE/ICurdConfig';
import http from '@/utils/http/axios/index';
import IColumn from '@/interface/FE/column';
import booleanRender from '@/columnCustomRender/booleanRender';
import imageRender from '@/columnCustomRender/imageRender';
import constants from '@/enums/constants';
import constantRender from '@/columnCustomRender/constantRender';
import formatTimeRender from '@/columnCustomRender/formatTimeRender';

const config: ICurdConfig = {
	theme: '招募活动',
	pageUrl: Api.page,
	removeUrl: Api.remove,
	saveUrl: Api.save,
	getFormSchema: function getFormSchema() {
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
		formItem: [
			{
				type: 'image',
				label: '活动封面',
				field: 'settingCover',
				value: '',
				props: {
					placeholder: '图片必传'
				},
				rules: [
					{
						required: false,
						message: '图片必传'
					}
				]
			},
			{
				type: 'input',
				label: '活动名称',
				field: 'settingName',
				value: '',
				props: {
					placeholder: '请输入名称'
				},
				rules: [
					{
						required: true,
						message: '名称不能为空'
					}
				]
			},
			{
				type: 'input',
				label: '活动期数',
				field: 'number',
				value: '',
				props: {
					placeholder: '请输入活动期数'
				},
				rules: []
			},
			{
				type: 'date',
				label: '开始时间',
				field: 'startTime',
				value: '',
				props: {
					showTime: true
				},
				rules: []
			},
			{
				type: 'date',
				label: '结束时间',
				field: 'endTime',
				value: '',
				props: {
					showTime: true
				},
				rules: []
			},

			{
				type: 'textarea',
				label: '活动描述',
				field: 'simpleDescription',
				value: '',
				props: {
					placeholder: '活动描述'
				},
			},

			{
				type: 'switch',
				label: '状态',
				field: 'status',
				value: 1, // 如果value是布尔值, 那么结果也会是 true 或 false
				props: {
					checkedChildren: '启用', // 默认值，可改文案
					unCheckedChildren: '禁用', // 默认值，可改文案
				}
			},

		]
		};
	},
	commonTableProps: {
		columns: [
	{
		title: '活动名称',
		dataIndex: 'settingName'
	},
	{
		title: '活动封面',
		dataIndex: 'settingCover',
		customRender: imageRender.set({
		width: '50px'
		})
	},
	{
		title: '活动期数',
		dataIndex: 'number'
	},
	{
				title: '开始时间',
				dataIndex: 'startTime',
				customRender: formatTimeRender
			},
			{
				title: '结束时间',
				dataIndex: 'startTime',
				customRender: formatTimeRender
			},
	{
		title: '申请人数',
		dataIndex: 'applyNumber'
	},
	{
		title: '通过数',
		dataIndex: 'passNumber'
	},

	{
		title: '活动描述',
		dataIndex: 'simpleDescription'
	},

	{
		title: '状态',
		dataIndex: 'status',
		customRender: booleanRender.set('启用', '禁用'), // 改文案的话就这么写
		// customRender: booleanRender
	},
	{
				title: '创建时间',
				dataIndex: 'createTime',
				customRender: formatTimeRender
	}
]
	}
};

export default defineComponent({
	name: 'EccRate',
	components: {
		CommonTable
	},
	setup() {
		const tableRef = ref<InstanceType<typeof CommonTable>>();
		const state = reactive({});

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

		return {
			...toRefs(state),
			config,
			tableRef,
			onEdit
		};
	}
});
</script>

<template>
	<div>
		<div v-permission="2" class="global_page_header">
			<a-button v-if="config.saveUrl" class="margin_l_20" type="primary" @click="onEdit()"
				>新建{{ config.theme }}</a-button
			>
		</div>
		<CommonTable
			ref="tableRef"
			v-bind="config.commonTableProps"
			:removeUrl="config.removeUrl"
			:pageUrl="config.pageUrl"
			:listUrl="config.listUrl"
			@onEdit="onEdit"
		/>
	</div>
</template>

<style lang="scss"></style>
