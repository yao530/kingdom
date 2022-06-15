<template>
	<div>
		<div class="global_page_header">
			<a-input-search v-if="config.showSearch" placeholder="搜索关键字" style="width: 180px" @search="onSearch" />
			<!-- <DateRangeTemplate
				v-if="config.dateRange"
				v-bind="config.dateRange"
				@change="onDateRangeChange"
			/> -->
			<a-button v-if="config.saveUrl" v-permission="2" class="margin_l_20" type="primary" @click="onEdit()">
				新建{{ config.theme }}
			</a-button>
		</div>
		<CommonTable
			ref="tableRef"
			v-bind="config.commonTableProps"
			:extraParams="extraParams"
			:pageUrl="config.pageUrl"
			:listUrl="config.listUrl"
			:removeUrl="config.removeUrl"
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
import DateRangeTemplate from '@/components/DateRangeTemplate/index.vue';
import http from '@/utils/http/axios';
import { message } from 'ant-design-vue';
import { hasPermission } from '@/utils/permission/hasPermission';
import { getRoleList } from '@/api/Role';
import Api from '@/api/Employee';
import RoleApi from '@/api/SysRole';
import { IRole } from '@/interface/BE/role';
import ICurdConfig, { IDateRangeConfig } from '@/interface/FE/ICurdConfig';
import booleanRender from '@/columnCustomRender/booleanRender';
import identitybooleanRender from '@/columnCustomRender/identitybooleanRender';
import formatTimeRender from '@/columnCustomRender/formatTimeRender';
import IColumn from '@/interface/FE/column';
import imageRender from '@/columnCustomRender/imageRender';
import md5 from 'blueimp-md5';
/**
 * @description 表格列
 */
const columns: IColumn[] = [
	{
		title: '用户名称',
		dataIndex: 'userNick'
	},
	{
		title: '用户编码',
		dataIndex: 'userCode'
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
		title: '用户类型',
		dataIndex: 'roleName',
	},
	{
		title: '账号状态',
		dataIndex: 'status',
		customRender: booleanRender
	},
	{
		title: '是否实名认知',
		dataIndex: 'identityStatus',
		customRender: identitybooleanRender
	},
	{
		title: '备注',
		dataIndex: 'remark'
	},
	{
		title: '创建时间',
		dataIndex: 'createTime',
		customRender: formatTimeRender
	}
];
/*

*/
const passwordItem: FormItem = {
	type: 'input',
	label: '新密码',
	field: 'password',
	value: '',
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

/**
 * @description 表单项。工厂模式保证每次打开都是新的
 */
function getFormSchema(hiddenPasswordItem): FormSchema {
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
				label: '用户头像',
				field: 'userAvatar',
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
				label: '用户名称',
				field: 'userNick',
				value: '',
				props: {
					placeholder: '请输入用户名称'
				},
				rules: [
					{
						required: true,
						message: '用户名称不能为空'
					}
				]
			},

			{
				type: 'input',
				label: '手机号',
				field: 'mobilePhone',
				value: '',
				props: {
					placeholder: '请输入手机号'
				},
				rules: [
					{
						required: true,
						message: '手机号不能为空'
					},
					{
						min: 11,
						message: '手机号不完整',
						trigger: 'blur'
					},
					{
						trigger: 'blur',
						validator: async (_, value) => {
							if (value && value.toString().length >= 11) {
								const isPhoneNumber = /^1[3456789]\d{9}$/.test(value);
								if (!isPhoneNumber) {
									return Promise.reject(`手机号有误`);
								}
							}
							return Promise.resolve();
						}
					}
				]
			},
			{
				type: 'input',
				label: '密码',
				field: 'password',
				value: '',
				hidden: hiddenPasswordItem,
				props: {
					placeholder: '请输入登录密码',
					type: 'password'
				},
				rules: [
					{
						required: true,
						message: '登录密码不能为空'
					}
				]
			},
			{
				type: 'select',
				label: '角色',
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
}
const config: ICurdConfig = {
	theme: '用户',
	showSearch: true,
	dateRange: true,
	pageUrl: Api.page,
	removeUrl: Api.remove,
	saveUrl: Api.save,
	getFormSchema,
	commonTableProps: {
		columns,
		actionWidth: 240
	}
};

export default defineComponent({
	name: 'Feeder',
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
			let formSchema = config.getFormSchema(!!record);
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
							password: md5(params.password)
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
			if (typeof config.dateRange == 'object') {
				const dateRange = config.dateRange as IDateRangeConfig;
			}
			(state.dateRangeValue as any) = {
				startTime: event[0] ? event[0].valueOf() : undefined,
				endTime: event[1] ? event[1].valueOf() : undefined
			};
		}
		function editPassword(record) {
			let formSchema = config.getFormSchema();
			formSchema.formItem = [passwordItem]; // 重置表单成只有密码项
			useFormModal({
				title: `修改’${record.userNick}‘的密码`,
				formSchema,
				handleOk: async (modelRef, state) => {
					const params: any = {};
					// 将之前的字段都赋值
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

		return {
			...toRefs(state),
			extraParams,
			config,
			tableRef,
			onDateRangeChange,
			onEdit,
			editPassword
		};
	}
});
</script>

<style lang="scss"></style>
