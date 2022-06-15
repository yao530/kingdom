<script lang="ts">
import { defineComponent, reactive, toRefs, ref, computed, onMounted } from 'vue';
import { useFormModal } from '@/hooks/useFormModal';
import CommonTable from '@/components/CommonTable/index.vue';
import md5 from 'blueimp-md5';
import { message } from 'ant-design-vue';
import Api from '@/api/CharacterRole';
import RoleApi from '@/api/SysRole';
import ICurdConfig from '@/interface/FE/ICurdConfig';
import http from '@/utils/http/axios/index';
import booleanRender from '@/columnCustomRender/booleanRender';
import formatTimeRender from '@/columnCustomRender/formatTimeRender';
import constants from '@/enums/constants';
import constantRender from '@/columnCustomRender/constantRender';

const config: ICurdConfig = {
	theme: '人物角色',
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
					type: 'input',
					label: '人物角色名称',
					field: 'characterRoleName',
					value: '',
					props: {
						placeholder: '角色名称'
					},
					rules: [
						{
							required: true,
							message: '角色名称不能为空'
						}
					]
				},
				{
				type: 'select',
				label: '角色类型',
				field: 'characterRoleType',
				value: undefined,
				props: {
					placeholder: '请选择类型'
				},
				options: constants.CharacterRoleType.list.map((item) => {
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
					label: '关联后台角色ID',
					field: 'roleId',
					value: undefined,
					asyncOptions: async () => {
						const data = await http.request({
							url: RoleApi.list,
							params: {
								id: 0
							}
						});
						return data.map((item) => {
							return {
								label: item.roleName,
								value: item.id
							};
						});
					},
					props: {
						placeholder: '请选择后台角色'
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
					label: '备注',
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
				title: '人物角色名称',
				dataIndex: 'characterRoleName'
			},
			{
		title: '角色类型',
		dataIndex: 'characterRoleType',
		customRender: constantRender(constants.CharacterRoleType.dict)
	},
			{
				title: '人物数量',
				dataIndex: 'characterNumber'
			},
			{
				title: '备注',
				dataIndex: 'remark'
			},
			{
				title: '创建时间',
				dataIndex: 'createTime',
				customRender: formatTimeRender
			},
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
