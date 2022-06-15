<script lang="ts">
import { defineComponent, reactive, toRefs, ref, computed, onMounted } from 'vue';
import { useFormModal } from '@/hooks/useFormModal';
import CommonTable from '@/components/CommonTable/index.vue';
import md5 from 'blueimp-md5';
import { message } from 'ant-design-vue';
import Api from '@/api/Collection';
import StoryApi from '@/api/Stories';
import StoryTaleApi from '@/api/StoryTale';
import VirtualCharacterApi from '@/api/VirtualCharacter';
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
	theme: '作品',
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
					label: '作品封面',
					field: 'collectionCover',
					value: '',
					props: {
						placeholder: '文件必传'
					},
					rules: [
						{
							required: true,
							message: '文件必传'
						}
					]
				},
				{
				type: 'image',
				label: '作品原文件',
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
					label: '作品名称',
					field: 'collectionName',
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
							url: StoryTaleApi.list

						});
						return data.map((item) => {
							return {
								label: item.taleTitle,
								value: item.id
							};
						});
					},
					props: {
						placeholder: '请选择人物故事IP必选'
					},
					rules: [
						{
							required: true,
							message: '必选'
						}
					]
				},
				{
					type: 'select',
					label: '故事脚本',
					field: 'storyId',
					value: undefined,
					asyncOptions: async () => {
						const data = await http.request({
							url: StoryApi.list,
							params: {
								id: 0
							}
						});
						return data.map((item) => {
							return {
								label: item.storyTitle,
								value: item.id
							};
						});
					},
					props: {
						placeholder: '主题展示非必选，其他必选'
					},
					rules: [
						{
							required: false,
							message: '必选'
						}
					]
				},

				{
					type: 'select',
					label: 'NFT类型',
					field: 'collectionType',
					value: undefined,
					props: {
						placeholder: '请选择类型'
					},
					options: constants.NftType.list.map((item) => {
						return {
							value: item.value,
							label: item.describe
						};
					}),
					rules: [
						{
							required: true,
							message: '不能为空'
						}
					]
				},
				{
					type: 'select',
					label: '主题作品',
					field: 'mainDisplay',
					value: undefined,
					props: {
						placeholder: '请选择类型'
					},
					options: constants.MainDisplayType.list.map((item) => {
						return {
							value: item.value,
							label: item.describe
						};
					}),
					rules: [
						{
							required: true,
							message: '不能为空'
						}
					]
				},
				{
					type: 'editor',
					label: '作品详情',
					field: 'context',
					value: '',
					props: {
						placeholder: '请输入风格描述'
					},
					rules: []
				}
			]
		};
	},
	commonTableProps: {
		columns: [
			{
				title: '作品名称',
				dataIndex: 'collectionName'
			},
			{
				title: '作品封面',
				dataIndex: 'collectionCover',
				customRender: imageRender.set({
					width: '50px'
				})
			},
			{
				title: '故事名称',
				dataIndex: 'storyTitle'
			},
			{
				title: '人物名称',
				dataIndex: 'characterName'
			},

			{
				title: '作品类型',
				dataIndex: 'collectionType',
				customRender: constantRender(constants.NftType.dict)
			},
			{
				title: '售卖方式',
				dataIndex: 'soldType',
				customRender: constantRender(constants.SoldType.dict)
			},
			{
				title: '开始售卖',
				dataIndex: 'startTime',
				customRender: formatTimeRender
			},
			{
				title: '结束售卖',
				dataIndex: 'endTime',
				customRender: formatTimeRender
			},
			{
				title: '数量',
				dataIndex: 'mintAmount'
			},
			{
				title: '价格',
				dataIndex: 'price'
			},
			{
				title: '审核状态',
				dataIndex: 'status',
				customRender: constantRender(constants.CheckStatus.dict)
			},{
				title: '上架状态',
				dataIndex: 'soldStatus',
				customRender: constantRender(constants.SoldStatus.dict)
			},
			{
				title: '排序',
				dataIndex: 'sort'
			},
			{
				title: '提交时间',
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
