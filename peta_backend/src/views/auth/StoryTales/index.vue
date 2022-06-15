<script lang="ts">
import { defineComponent, reactive, toRefs, ref, computed, onMounted } from 'vue';
import { useFormModal } from '@/hooks/useFormModal';
import CommonTable from '@/components/CommonTable/index.vue';
import md5 from 'blueimp-md5';
import { message } from 'ant-design-vue';
import Api from '@/api/StoryTale';
import characterApi from '@/api/VirtualCharacter';
import MetaApi from '@/api/Meta';
import ICurdConfig from '@/interface/FE/ICurdConfig';
import http from '@/utils/http/axios/index';
import IColumn from '@/interface/FE/column';
import booleanRender from '@/columnCustomRender/booleanRender';
import imageRender from '@/columnCustomRender/imageRender';
import constants from '@/enums/constants';
import constantRender from '@/columnCustomRender/constantRender';
import formatTimeRender from '@/columnCustomRender/formatTimeRender';



const config: ICurdConfig = {
	theme: '故事IP',
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
				field: 'taleCover',
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
				label: '故事IP名称',
				field: 'taleTitle',
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
				label: '合约地址',
				field: 'smartContractAddress',
				value: '',
				props: {
					placeholder: '请输入合约地址'
				},
				rules: [
					{
						required: false,
						message: '名称不能为空'
					}
				]
			},
			{
				type: 'input',
				label: '故事标签',
				field: 'taleStyles',
				value: '',
				props: {
					placeholder: '请输入标签'
				},
				rules: []
			},
			{
				type: 'select',
				label: '故事类型',
				field: 'storyType',
				value: undefined,
				props: {
					placeholder: '请选择类型'
				},
				options: constants.StoryType.list.map((item) => {
					return {
						value: item.value,
						label: item.describe
					};
				}),
				rules: [
					{
						required: true,
						message: '类型不能为空'
					}
				]
			},
			{
				type: 'select',
				label: '选择作者',
				field: 'writerId',
				value: undefined,
				asyncOptions: async () => {
					const data = await http.request({
						url: characterApi.list,
						params:{
							characterRoleId:4
						}

					});
					return data.map((item) => {
						return {
							label: item.characterName,
							value: item.id
						};
					});
				},
				props: {
					placeholder: '请选择作者'
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
					label: '故事宇宙',
					field: 'metaId',
					value: undefined,
					asyncOptions: async () => {
						const data = await http.request({
							url: MetaApi.list,
							params: {
								id: 0
							}
						});
						return data.map((item) => {
							return {
								label: item.metaName,
								value: item.id
							};
						});
					},
					props: {
						placeholder: '请故事背景宇宙'
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
				label: '故事人物',
				field: 'characterId',
				value: undefined,
				asyncOptions: async () => {
					const data = await http.request({
						url: characterApi.list,
						params:{
							characterRoleId:0
			 			}
					});
					return data.map((item) => {
						return {
							label: item.characterName,
							value: item.id
						};
					});
				},
				props: {
					placeholder: '请选择人物'
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
				field: 'taleSketch',
				value: '',
				props: {
					placeholder: '故事人物信息简介'
				},
			},

			{
				type: 'switch',
				label: '状态',
				field: 'status',
				value: 1, // 如果value是布尔值, 那么结果也会是 true 或 false
				props: {
					checkedChildren: '连载中', // 默认值，可改文案
					unCheckedChildren: '完结', // 默认值，可改文案
				}
			},
			{
				type: 'textarea',
				label: '备注',
				field: 'remark',
				value: '',
				props: {
					placeholder: '描述'
				}
			}
		]
		};
	},
	commonTableProps: {
		columns: [
	{
		title: 'IP名称',
		dataIndex: 'taleTitle'
	},
	{
		title: '故事封面',
		dataIndex: 'taleCover',
		customRender: imageRender.set({
		width: '50px'
		})
	},
	{
		title: '故事人物',
		dataIndex: 'writerName'
	},
	{
		title: '故事宇宙',
		dataIndex: 'metaName'
	},
	{
		title: '故事类型',
		dataIndex: 'taleStyles'
	},
	{
		title: '故事作者',
		dataIndex: 'writerName',

	},
	{
		title: '故事话数',
		dataIndex: 'chapterNumber'
	},

	{
		title: '状态',
		dataIndex: 'status',
		// customRender: booleanRender.set('开', '关'), // 改文案的话就这么写
		customRender: booleanRender
	},
	{
				title: '铸造状态',
				dataIndex: 'mintStatus',
				customRender: booleanRender.set('已铸造', '待铸造'), // 改文案的话就这么写
			},
	{
		title: '最新更新',
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
