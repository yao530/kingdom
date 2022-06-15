<template>
	<div>

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
				<a-button class="mintBtn" size="small" type="link" @click="mint(record)">铸造</a-button>
				<a-divider type="vertical" />
				<!-- <a-button class="setting" size="small" type="link" @click="setSoldSetting(record)">销售设置</a-button>
				<a-divider type="vertical" /> -->
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
import Api from '@/api/Stories';
import StoryTaleApi from '@/api/StoryTale';
import StoryApi from '@/api/Stories';
import {getDetail} from '@/api/Stories';

import ICurdMintConfig from '@/interface/FE/ICurdMintConfig';
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
		title: '故事标题',
		dataIndex: 'storyTitle'
	},
	{
		title: '故事封面',
		dataIndex: 'storyCover',
		customRender: imageRender.set({
		width: '50px'
		})
	},
	{
		title: '故事Ip',
		dataIndex: 'storyTaleTitle'
	},
	{
		title: '铸造数量',
		dataIndex: 'mintAmount'
	},
	{
		title: '关注数量',
		dataIndex: 'focusNumber'
	},
	{
		title: '上架状态',
		dataIndex: 'status',
		customRender: constantRender(constants.SoldStatus.dict)
	},
	{
		title: '铸造状态',
		dataIndex: 'mintStatus',
		customRender: booleanRender.set('已铸造', '待铸造'), // 改文案的话就这么写
	},
	{
				title: '最后更新',
				dataIndex: 'updateTime',
				customRender: formatTimeRender
	}
];

/**
 * @description 表单项。工厂模式保证每次打开都是新的
 */
function getMintSchema(hiddenMintItem?): FormSchema {
	return {
		formItem: [
				{
				type: 'image',
				label: '元文件',
				field: 'prefixFileUrl',
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
			// {
			// 		type: 'input',
			// 		label: '合约地址',
			// 		field: 'blockSmartContractAddress',
			// 		value: '',
			// 		props: {
			// 			placeholder: '请输入铸造合约地址'
			// 		},
			// 		rules: []
			// },
			{
					type: 'input',
					label: '铸造数量',
					field: 'mintAmount',
					value: '',
					props: {
						placeholder: '请输入数量'
					},
					rules: []
			},
			{
					type: 'select',
					label: '铸造状态',
					field: 'mintStatus',
					value: undefined,
					props: {
						placeholder: '请选择类型'
					},
					options: constants.MintStatus.list.map((item) => {
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
		]
	};
}
/**
 * @description 表单项。工厂模式保证每次打开都是新的
 */
// function getSettingItem(): FormSchema {
// 	return {
// 		formItem: [
// 			{
// 					type: 'input',
// 					label: '售卖价格',
// 					field: 'price',
// 					value: '',
// 					props: {
// 						placeholder: '必传'
// 					},
// 					rules: [
// 						{
// 							required: false,
// 							message: '必传'
// 						}
// 					]
// 			},
// 			{
// 					type: 'select',
// 					label: '售卖方式',
// 					field: 'soldType',
// 					value: undefined,
// 					props: {
// 						placeholder: '请选择类型'
// 					},
// 					options: constants.SoldType.list.map((item) => {
// 						return {
// 							value: item.value,
// 							label: item.describe
// 						};
// 					}),
// 					rules: [
// 						{
// 							required: true,
// 							message: '不能为空'
// 						}
// 					]
// 				},
// 				{
// 				type: 'switch',
// 				label: '是否要邀请',
// 				field: 'needInvited',
// 				value: 1, // 如果value是布尔值, 那么结果也会是 true 或 false
// 				props: {
// 					checkedChildren: '是', // 默认值，可改文案
// 					unCheckedChildren: '否', // 默认值，可改文案
// 				}
// 			},
// 				{
// 					type: 'input',
// 					label: '需邀请人数',
// 					field: 'needInvitedNumber',
// 					value: '',
// 					props: {
// 						placeholder: '需邀请人数'
// 					},
// 					rules: [
// 						{
// 							required: false,
// 							message: '名称不能为空'
// 						}
// 					]
// 				},

// 				{
// 					type: 'select',
// 					label: '获得权限类型',
// 					field: 'dropType',
// 					value: undefined,
// 					props: {
// 						placeholder: '请选择获得类型'
// 					},
// 					options: constants.DropAuthType.list.map((item) => {
// 						return {
// 							value: item.value,
// 							label: item.describe
// 						};
// 					}),
// 					rules: [
// 						{
// 							required: true,
// 							message: '不能为空'
// 						}
// 					]
// 				},
// 				// {
// 				// 	type: 'input',
// 				// 	label: '需持有的分身',
// 				// 	field: 'dropAuthAvatarIds',
// 				// 	value: '',
// 				// 	props: {
// 				// 		placeholder: '请输入名称'
// 				// 	},
// 				// 	rules: [
// 				// 		{
// 				// 			required: false,
// 				// 			message: '名称不能为空'
// 				// 		}
// 				// 	]
// 				// },
// 				{
// 					type: 'select',
// 					label: '上架发布',
// 					field: 'publishStatus',
// 					value: undefined,
// 					props: {
// 						placeholder: '请选择发布状态'
// 					},
// 					options: constants.PublisStatus.list.map((item) => {
// 						return {
// 							value: item.value,
// 							label: item.describe
// 						};
// 					}),
// 					rules: [
// 						{
// 							required: true,
// 							message: '不能为空'
// 						}
// 					]
// 				},
// 				{
// 					type: 'date',
// 					label: '开始售卖',
// 					field: 'startTime',
// 					value: undefined,
// 					props: {
// 						showTime: true
// 					},
// 					rules: []
// 				},
// 				{
// 					type: 'date',
// 					label: '结束售卖',
// 					field: 'endTime',
// 					value: undefined,
// 					props: {
// 						showTime: true
// 					},
// 					rules: []
// 				},
// 		]
// 	};
// }

function getFormSchema(hiddenMintItem = false): FormSchema {
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
				field: 'storyCover',
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
				label: '故事名称',
				field: 'storyTitle',
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
				label: '选择故事',
				field: 'storyTaleId',
				value: undefined,
				asyncOptions: async () => {
					const data = await http.request({
						url: StoryTaleApi.list,
						params:{
							id:0
						}
					});
					return data.map((item) => {
						return {
							label: item.taleTitle,
							value: item.id
						};
					});
				},
				props: {
					placeholder: '请选择IP'
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
					options: constants.CheckStatus.list.map((item) => {
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
					label: '发布状态',
					field: 'publishStatus',
					value: undefined,
					props: {
						placeholder: '请选择类型'
					},
					options: constants.PublisStatus.list.map((item) => {
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
				label: '故事简介',
				field: 'storySketch',
				value: '',
				props: {
					placeholder: '请输入简介'
				},
				rules: []
			},
			{
					type: 'editor',
					label: '故事详情',
					field: 'context',
					value: '',

					props: {
						placeholder: '请输入内容'
					},
					rules: []
			}
		]
	};
}

const config: ICurdMintConfig = {
	theme: '故事',
	showSearch: true, // 不传 | 布尔值 | 搜索参数的字段名
	pageUrl: Api.page,
	listUrl: Api.list,
	removeUrl: Api.remove,
	saveUrl: Api.save,
	mintUrl: Api.mint,
	getFormSchema,
	getMintSchema,
	commonTableProps: {
		columns,
		actionWidth: 240
	}
};

export default defineComponent({
	name: 'StoryCheck',
	components: {
		CommonTable
	},
	setup() {
		const tableRef = ref<InstanceType<typeof CommonTable>>();

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
				title: `${record ? '审核' : '新建'}${config.theme}`,
				fields,
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

		function mint(record) {
			let formSchema = getMintSchema();
			const fields = record
				? {
						...record,
				  }
			: undefined;
			useFormModal({
				title: `藏品铸造`,
				fields,
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
					}
					console.log(fields)
					await http.request({
						url: config.mintUrl,
						params
					});
					message.success('操作成功');
					tableRef.value?.getData();
				}
			});
		}
		// function setSoldSetting(record) {
		// 	let formSchema = getSettingItem();
		// 	console.log("获取数据")
		// 	const fields = record
		// 		? {
		// 				...record,
		// 		  }
		// 	: undefined;
		// 	useFormModal({
		// 		title: `售卖设置`,
		// 		fields,
		// 		formSchema,
		// 		handleOk: async (modelRef, state) => {
		// 			const params: any = {};
		// 			if (record) {
		// 				Object.assign(
		// 					params,
		// 					{
		// 						id: record.id
		// 					},
		// 					modelRef
		// 				);
		// 			}
		// 			console.log(fields)
		// 			await http.request({
		// 				url: config.mintUrl,
		// 				params
		// 			});
		// 			message.success('操作成功');
		// 			tableRef.value?.getData();
		// 		}
		// 	});
		// }
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
			mint,
			// setSoldSetting
		};
	}
});
</script>

<style lang="scss">
.mintBtn{
	color: #E9E4C3
}
.setting{
	color: lightcoral
}
</style>
