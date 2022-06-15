<template>
	<div>
		<div class="global_page_header">
			<a-input-search v-if="config.showSearch" placeholder="搜索关键字" style="width: 180px" @search="onSearch" />
			<a-button v-if="config.saveUrl" v-permission="2" class="margin_l_20" type="primary" @click="onEdit()">
				新建{{ config.theme }}
			</a-button>
		</div>
		<CommonTable
			ref="tableRef"
			v-bind="config.commonTableProps"
			:extraParams="extraParams"
			:listUrl="config.listUrl"
			:removeUrl="config.removeUrl"
			:pageUrl="config.pageUrl"
			@onEdit="onEdit"
		>
			<template #extraActon="{ record }">
				<a-button size="small" type="link" @click="editPassword(record)">修改密码</a-button>
				<a-divider type="vertical" />
			</template>
		</CommonTable>
	</div>
</template>
<script lang="ts">
import { defineComponent, reactive, toRefs, ref, computed, onMounted } from 'vue';
import { useFormModal } from '@/hooks/useFormModal';
import CommonTable from '@/components/CommonTable/index.vue';
import md5 from 'blueimp-md5';
import { message } from 'ant-design-vue';
import { hasPermission } from '@/utils/permission/hasPermission';
import Api from '@/api/Account';
import RoleApi from '@/api/ChildRole';
import ICurdConfig from '@/interface/FE/ICurdConfig';
import http from '@/utils/http/axios/index';
import IColumn from '@/interface/FE/column';
import booleanRender from '@/columnCustomRender/booleanRender';
/**
 * @description 表格列
 */
const columns: IColumn[] = [
	{
		title: '账号名称',
		dataIndex: 'accountName'
	},
	{
		title: '登录账号',
		dataIndex: 'accountLoginName'
	},
	{
		title: '联系人',
		dataIndex: 'contactPerson'
	},
	{
		title: '联系人手机',
		dataIndex: 'mobilePhone'
	},
	{
		title: '账号类型',
		dataIndex: 'roleName',
		asyncOptions: async () => {
			const permission = hasPermission('Roles', 1);
			if (!permission) return [];

			const data = await http.request({
				url: RoleApi.list
			});
			return data.map((item) => {
				return {
					label: item.roleName,
					value: item.id
				};
			});
		}
	},
	{
		title: '状态',
		dataIndex: 'status',
		// customRender: booleanRender.set('开', '关'), // 改文案的话就这么写
		customRender: booleanRender
	}
];

/**
 * @description 表单项。工厂模式保证每次打开都是新的
 */
function getPasswordItem(hiddenPasswordItem?): FormItem {
	return {
		type: 'input',
		label: '密码',
		field: 'password',
		value: undefined,
		hidden: hiddenPasswordItem,
		props: {
			placeholder: '请输入密码',
			type: 'password'
		},
		rules: [
			{
				required: true,
				message: '密码不能为空'
			},
			{
				min: 2,
				max: 18,
				message: '长度为3-18个字符',
				trigger: 'blur'
			}
		]
	};
}
function getFormSchema(hiddenPasswordItem = false): FormSchema {
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
				label: '账号名称',
				field: 'accountName',
				value: '',
				props: {
					placeholder: '请输入账号名'
				},
				rules: [
					{
						required: true,
						message: '账号名不能为空'
					}
				]
			},
			{
				type: 'input',
				label: '联系人',
				field: 'contactPerson',
				value: '',
				props: {
					placeholder: '请输入联系人'
				},
				rules: []
			},
			{
				type: 'input',
				label: '联系人手机号',
				field: 'mobilePhone',
				value: '',
				props: {
					placeholder: '请输入手机号'
				},
				rules: []
			},
			{
				type: 'input',
				label: '登录后台账号',
				field: 'accountLoginName',
				value: '',
				props: {
					placeholder: '请输入登录账号'
				},
				rules: [
					{
						required: true,
						message: '登录账号不能为空'
					}
				]
			},
			getPasswordItem(hiddenPasswordItem),
			{
				type: 'select',
				label: '账号类型',
				field: 'roleId',
				value: undefined,
				asyncOptions: async () => {
					const data = await http.request({
						url: RoleApi.list
					});
					return data.map((item) => {
						return {
							label: item.roleName,
							value: item.id
						};
					});
				},
				props: {
					placeholder: '请选择类型'
				},
				rules: [
					{
						required: true,
						message: '账号类型必选'
					}
				]
			},

			{
				type: 'switch',
				label: '状态',
				field: 'status',
				value: 1, // 如果value是布尔值, 那么结果也会是 true 或 false
				props: {
					// checkedChildren: '启用', // 默认值，可改文案
					// unCheckedChildren: '禁用', // 默认值，可改文案
				}
			},
			{
				type: 'textarea',
				label: '描述',
				field: 'remark',
				value: '',
				props: {
					placeholder: '描述'
				}
			}
		]
	};
}

const config: ICurdConfig = {
	theme: '账号',
	showSearch: true, // 不传 | 布尔值 | 搜索参数的字段名
	pageUrl: Api.list, // 不分页接口
	removeUrl: Api.remove, // 不传的话不显示删除
	saveUrl: Api.save, // 不传的话不显示新建和编辑
	getFormSchema,
	commonTableProps: {
		columns,
		actionWidth: 240
	}
};

export default defineComponent({
	name: 'Accounts',
	components: {
		CommonTable
	},
	setup() {
		const tableRef = ref<InstanceType<typeof CommonTable>>();
		const hasRoleRetrievePermission = hasPermission('Roles');
		const state = reactive({
			searchValue: undefined
		});
		const extraParams = computed(() => {
			const keywordName = typeof config.showSearch === 'string' ? config.showSearch : 'keyWord';
			return {
				[keywordName]: state.searchValue
			};
		});

		const onEdit = (record?) => {
			let formSchema = config.getFormSchema(!!record);
			const fields = record
				? {
						...record,
						districts: [record.province, record.city, record.area]
				  }
				: undefined;

			useFormModal({
				title: `${record ? '编辑' : '新建'}${config.theme}`,
				fields,
				formSchema,
				handleOk: async (modelRef) => {
					let params: any = {};
					const [province, city, area] = modelRef.districts;
					if (record) {
						params = {
							...modelRef,
							password: record.password,
							province,
							city,
							area
						};
					} else {
						params = {
							...modelRef,
							password: md5(modelRef.password),
							province,
							city,
							area
						};
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

		function editPassword(record) {
			let formSchema = config.getFormSchema();
			formSchema.formItem = [getPasswordItem()];
			useFormModal({
				title: `修改’${record.accountLoginName}‘的密码`,
				formSchema,
				handleOk: async (modelRef, state) => {
					// province city area
					const params: any = {};
					Object.assign(params, record, {
						password: md5(modelRef.password)
					});
					await http.request({
						url: config.saveUrl,
						params
					});
					message.success('操作成功');
					tableRef.value?.getData();
				}
			});
		}
		function onSearch(value) {
			state.searchValue = value;
		}

		return {
			...toRefs(state),
			extraParams,
			config,
			tableRef,
			onEdit,
			onSearch,
			editPassword
		};
	}
});
</script>

<style lang="scss"></style>
