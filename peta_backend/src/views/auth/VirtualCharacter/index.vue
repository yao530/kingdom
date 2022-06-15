<script lang="ts">
import { defineComponent, reactive, toRefs, ref, computed, onMounted } from 'vue';
import { useFormModal } from '@/hooks/useFormModal';
import CommonTable from '@/components/CommonTable/index.vue';
import md5 from 'blueimp-md5';
import { message } from 'ant-design-vue';
import Api from '@/api/VirtualCharacter';
import RoleApi from '@/api/CharacterRole';
import MetaApi from '@/api/Meta';
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
	theme: '虚拟IP',
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
				label: '人物头像',
				field: 'characterAvatar',
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
				label: '虚拟人物名称',
				field: 'characterName',
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
					label: '出生宇宙',
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
						placeholder: '请出生宇宙'
					},
					rules: [
						{
							required: false,
							message: '必选'
						}
					]
			},
			{
				type: 'date',
				label: '出生时间',
				field: 'birthDate',
				value: '',
				props: {
					showTime: true
				},
				rules: []
			},
			{
				type: 'select',
				label: '性别',
				field: 'sex',
				value: undefined,
				props: {
					placeholder: '请选择类型'
				},
				options: constants.SexType.list.map((item) => {
					return {
						value: item.value,
						label: item.describe
					};
				}),
				rules: [
					{
						required: true,
						message: '性别不能为空'
					}
				]
			},
			{
				type: 'select',
				label: '角色类型',
				field: 'characterRoleId',
				value: undefined,
				asyncOptions: async () => {
					const data = await http.request({
						url: RoleApi.list
					});
					return data.map((item) => {
						return {
							label: item.characterRoleName,
							value: item.id
						};
					});
				},
				props: {
					placeholder: '请选择角色'
				},
				rules: [
					{
						required: false,
						message: '必选'
					}
				]
			},
			{
				type: 'input',
				label: '风格类型',
				field: 'characterStyles',
				value: '',
				props: {
					placeholder: '请输入风格类型'
				},
				rules: []
			},
			{
				type: 'textarea',
				label: '信息简介',
				field: 'characterInformation',
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
					checkedChildren: '活动中', // 默认值，可改文案
					unCheckedChildren: '飞天', // 默认值，可改文案
				}
			},
			{
				type: 'editor',
				label: '详情',
				field: 'context',
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
		title: '人物名称',
		dataIndex: 'characterName'
	},
	{
		title: '人物头像',
		dataIndex: 'characterAvatar',
		customRender: imageRender.set({
		width: '50px'
		})
	},
	{
		title: '出生宇宙',
		dataIndex: 'metaName'
	},
	{
		title: '性别',
		dataIndex: 'sex',
		customRender: constantRender(constants.SexType.dict)
	},
	{
		title: '人物类型',
		dataIndex: 'characterRoleName'
	},
	{
		title: '关注粉丝',
		dataIndex: 'totalFocusNumber'
	},
	{
		title: '分身数量',
		dataIndex: 'publishAvatarNumber'
	},

	{
		title: '状态',
		dataIndex: 'status',
		// customRender: booleanRender.set('开', '关'), // 改文案的话就这么写
		customRender: booleanRender
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

								id: record.id
							},
							modelRef
						);
					} else {
						Object.assign(params, modelRef, {

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
