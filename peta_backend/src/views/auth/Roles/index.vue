<template>
	<div>
		<div v-permission="2" class="global_page_header">
			<a-button v-if="config.saveUrl" class="margin_l_20" type="primary" @click="onEdit()">新建角色</a-button>
		</div>
		<CommonTable
			ref="tableRef"
			v-bind="config.commonTableProps"
			:pageUrl="config.pageUrl"
			:listUrl="config.listUrl"
			:removeUrl="config.removeUrl"
			@onEdit="onEdit"
		>
			<template #extraActon="{ record }">
				<a-button size="small" type="link" @click="editMenus(record)">编辑权限</a-button>
				<a-divider type="vertical" />
			</template>
		</CommonTable>
	</div>
</template>
<script lang="ts">
import { defineComponent, reactive, toRefs, createVNode, computed, ref } from 'vue';
import { postAdminRole } from '@/api/Role';
import CommonTable from '@/components/CommonTable/index.vue';
import RoleMenusInfo from './components/RoleMenusInfo.vue';
import * as separate from '@/utils/separate';
import { useFormModal } from '@/hooks/useFormModal';
import Api from '@/api/Role';
import RoleTypeApi from '@/api/RoleType';
import { IRole } from '@/interface/BE/role';
import constants from '@/enums/constants';
import IColumn from '@/interface/FE/column';
import ICurdConfig from '@/interface/FE/ICurdConfig';
import http from '@/utils/http/axios/index';
/**
 * @description 表格列
 */
const columns: IColumn[] = [
	// 角色列表
	{
		title: '角色名称',
		dataIndex: 'roleName'
	},
	{
		title: '角色编码',
		dataIndex: 'roleCode'
	},
	{
		title: '备注',
		dataIndex: 'remark'
	}
];
/**
 * @description 表单项。工厂模式保证每次打开都是新的
 */
const getFormSchema = (): FormSchema => ({
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
			label: '角色名称',
			field: 'roleName',
			value: '',
			props: {
				placeholder: '请输入角色名称'
			},
			rules: [
				{
					required: true,
					message: '角色名称不能为空'
				}
			]
		},
		{
			type: 'input',
			label: '角色编码',
			field: 'roleCode',
			value: '',
			props: {
				placeholder: '请输入角色编码'
			},
			rules: [
				{
					required: true,
					message: '角色编码不能为空'
				}
			]
		},
		{
			type: 'textarea',
			label: '描述',
			field: 'remark',
			value: '',
			props: {
				placeholder: '系统角色不可删除'
			}
		}
	]
});

const config: ICurdConfig = {
	theme: '角色',
	listUrl: Api.list,
	removeUrl: Api.remove,
	saveUrl: Api.save,
	getFormSchema,
	commonTableProps: {
		columns,
		actionWidth: 240
	}
};

const wrap = separate.generate(RoleMenusInfo);

export default defineComponent({
	name: 'Roles',
	components: {
		CommonTable
	},
	setup() {
		const tableRef = ref<InstanceType<typeof CommonTable>>();

		const state = reactive({
			tableLoading: false,
			rowSelection: {
				onChange: (selectedRowKeys, selectedRows) => {
					state.rowSelection.selectedRowKeys = selectedRowKeys;
				},
				selectedRowKeys: []
			}
		});

		// 添加角色
		const onEdit = (record?) => {
			useFormModal({
				title: `${record ? '编辑' : '新建'}角色`,
				fields: record,
				formSchema: config.getFormSchema(),
				handleOk: async (modelRef, state) => {
					const params = modelRef;
					await postAdminRole(params);
					tableRef.value?.getData();
				}
			});
		};

		function editMenus(record) {
			wrap.show({
				title: `‘${record.roleName}’的菜单权限`,
				record
			});
		}

		return {
			...toRefs(state),
			config,
			tableRef,
			onEdit,
			editMenus
		};
	}
});
</script>

<style lang="scss"></style>
