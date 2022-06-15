<script lang="ts">
import { defineComponent, reactive, toRefs, ref, computed, onMounted } from 'vue';
import { useFormModal } from '@/hooks/useFormModal';
import CommonTable from '@/components/CommonTable/index.vue';
import md5 from 'blueimp-md5';
import { message } from 'ant-design-vue';
import Api from '@/api/Creator';
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
	theme: '作者',
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
				type: 'select',
				label: '角色',
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
					placeholder: '请选择所属角色'
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
					type: 'select',
					label: '审核状态',
					field: 'status',
					value: undefined,
					props: {
						placeholder: '请选择类型'
					},
					options: constants.CreatorStatus.list.map((item) => {
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
				type: 'textarea',
				label: '审核备注',
				field: 'remark',
				value: '',
				props: {
					placeholder: '备注'
				}
			}
		]
		};
	},
	commonTableProps: {
		columns: [
			{
		title: '用户名称',
		dataIndex: 'userNick'
	},
	{
		title: '用户头像',
		dataIndex: 'userAvatar',
		customRender: imageRender.set({
			width: '50px'
		})
	},
	{
		title: '手机号',
		dataIndex: 'mobilePhone'
	},

	{
		title: '申请创作者型',
		dataIndex: 'characterRoleName',
	},
	{
		title: '申请状态',
		dataIndex: 'status',
		customRender: constantRender(constants.CreatorStatus.dict)
	},{
		title: '申请时间',
		dataIndex: 'approveTime',
		customRender: formatTimeRender
	},
	{
		title: '审核时间',
		dataIndex: 'lastCheckTime',
		customRender: formatTimeRender
	},
	{
		title: '审核备注',
		dataIndex: 'checkRemark'
	}

		]
	}
};

export default defineComponent({
	name: 'CreatorManager',
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
