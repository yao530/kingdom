<script lang="ts">
import { defineComponent, reactive, toRefs, ref, computed, onMounted } from 'vue';
import { useFormModal } from '@/hooks/useFormModal';
import CommonTable from '@/components/CommonTable/index.vue';
import md5 from 'blueimp-md5';
import { message } from 'ant-design-vue';
import Api from '@/api/Stories';
import StoryTaleApi from '@/api/StoryTale';
import StoryApi from '@/api/Stories';

import ICurdConfig from '@/interface/FE/ICurdConfig';
import http from '@/utils/http/axios/index';
import IColumn from '@/interface/FE/column';
import booleanRender from '@/columnCustomRender/booleanRender';
import imageRender from '@/columnCustomRender/imageRender';
import constants from '@/enums/constants';
import constantRender from '@/columnCustomRender/constantRender';
import formatTimeRender from '@/columnCustomRender/formatTimeRender';
import Editor from './Editor.vue';
const config: ICurdConfig = {
	theme: '故事',
	pageUrl: Api.page,
	listUrl: Api.list,
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
				label: '故事封面',
				field: 'storyCover',
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
				type: 'image',
				label: '故事文件',
				field: 'originalFileUrl',
				value: '',
				props: {
					placeholder: '文件必传'
				},
				rules: [
					{
						required: false,
						message: '文件必传'
					}
				]
			},
			{
				type: 'input',
				label: '故事名称',
				field: 'storyTitle',
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
				type: 'select',
				label: '故事IP',
				field: 'storyTaleId',
				value: undefined,
				asyncOptions: async () => {
					const data = await http.request({
						url: StoryTaleApi.list,
						params:{
							id:0
						}
					});
					return data.map((item) => {
						return {
							label: item.taleTitle,
							value: item.id
						};
					});
				},
				props: {
					placeholder: '请选择IP'
				},
				rules: [
					{
						required: false,
						message: '必选'
					}
				]
			},
			{
				type: 'textarea',
				label: '故事简介',
				field: 'storySketch',
				value: '',
				props: {
					placeholder: '请输入简介'
				},
				rules: []
			},
			{
					type: 'editor',
					label: '故事详情',
					field: 'context',
					value: '',
					props: {
						placeholder: '请输入内容'
					},
					rules: []
			}
		]
		};
	},
	commonTableProps: {
		columns: [
	{
		title: '故事标题',
		dataIndex: 'storyTitle'
	},
	{
		title: '故事封面',
		dataIndex: 'storyCover',
		customRender: imageRender.set({
		width: '50px'
		})
	},
	{
		title: '故事Ip',
		dataIndex: 'storyTaleTitle'
	},
	{
		title: '铸造数量',
		dataIndex: 'mintAmount'
	},
	{
		title: '关注数量',
		dataIndex: 'focusNumber'
	},
	{
		title: '上架状态',
		dataIndex: 'status',
		customRender: constantRender(constants.SoldStatus.dict)
	},
	{
		title: '铸造状态',
		dataIndex: 'mintStatus',
		customRender: booleanRender.set('已铸造', '待铸造'), // 改文案的话就这么写
	},
	{
				title: '最后更新',
				dataIndex: 'updateTime',
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
