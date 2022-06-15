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
import Api from '@/api/Meta';
import RoleApi from '@/api/ChildRole';
import ICurdConfig from '@/interface/FE/ICurdConfig';
import http from '@/utils/http/axios/index';
import IColumn from '@/interface/FE/column';
import booleanRender from '@/columnCustomRender/booleanRender';
import imageRender from '@/columnCustomRender/imageRender';
import constants from '@/enums/constants';
import constantRender from '@/columnCustomRender/constantRender';
import formatTimeRender from '@/columnCustomRender/formatTimeRender';
import Editor from './Editor.vue';


/**
 * @description 表格列
 */
const columns: IColumn[] = [
	{
		title: '宇宙名称',
		dataIndex: 'metaName'
	},
	{
		title: '宇宙logo',
		dataIndex: 'metaLogo',
		customRender: imageRender.set({
			width: '50px'
		})
	},
	{
		title: '宇宙类型',
		dataIndex: 'metaType',
		customRender: constantRender(constants.MetaType.dict)
	},
	{
		title: '宇宙介绍',
		dataIndex: 'metaDesc'
	},
	{
		title: '状态',
		dataIndex: 'status',
		customRender: booleanRender
	},
	{
		title: '创建时间',
		dataIndex: 'createTime',
		customRender: formatTimeRender
	}
];


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
				label: '宇宙名称',
				field: 'metaName',
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
					type: 'image',
					label: '宇宙logo',
					field: 'metaLogo',
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
					type: 'select',
					label: '宇宙类型',
					field: 'metaType',
					value: undefined,
					props: {
						placeholder: '请选择类型'
					},
					options: constants.MetaType.list.map((item) => {
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
					type: 'image',
					label: '宇宙banner',
					field: 'metaBanner',
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
					type: 'image',
					label: '地图模型',
					field: 'metaMapModel',
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
				type: 'textarea',
				label: '宇宙简介',
				field: 'metaDesc',
				value: '',
				props: {
					placeholder: '描述'
				}
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
}

const config: ICurdConfig = {
	theme: '元宇宙',
	showSearch: true, // 不传 | 布尔值 | 搜索参数的字段名
	pageUrl: Api.page, // 不分页接口
	removeUrl: Api.remove, // 不传的话不显示删除
	saveUrl: Api.save, // 不传的话不显示新建和编辑
	getFormSchema,
	commonTableProps: {
		columns,
		actionWidth: 240
	}
};

export default defineComponent({
	name: 'Meta',
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

				  }
				: undefined;

			useFormModal({
				title: `${record ? '编辑' : '新建'}${config.theme}`,
				fields,
				formSchema,
				handleOk: async (modelRef) => {
					let params: any = {};

					if (record) {
						params = {
							...modelRef,

						};
					} else {
						params = {
							...modelRef,
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
		};
	}
});
</script>

<style lang="scss"></style>
