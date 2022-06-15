<script lang="ts">
import { defineComponent, reactive, toRefs, ref, computed, onMounted } from 'vue';
import { useFormModal } from '@/hooks/useFormModal';
import CommonTable from '@/components/CommonTable/index.vue';
import md5 from 'blueimp-md5';
import { message } from 'ant-design-vue';
import Api from '@/api/PlatForm';


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
	theme: '配置',
	pageUrl: Api.page,
	listUrl: Api.list,
	removeUrl: Api.remove,
	saveUrl: Api.save,
	getFormSchema: function getFormSchema() {
		return {
			style: {
				width: '1800px'
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
					label: '设置类型',
					field: 'type',
					value: undefined,
					props: {
						placeholder: '请选择类型'
					},
					options: constants.PlatFormSettingType.list.map((item) => {
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
					label: '内容',
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
				title: '设置类型',
				dataIndex: 'type',
				customRender: constantRender(constants.PlatFormSettingType.dict)
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
			},
			{
				title: '最近修改时间',
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
