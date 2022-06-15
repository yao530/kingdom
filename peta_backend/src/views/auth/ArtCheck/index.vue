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
				<a-button class="setting" size="small" type="link" @click="setSoldSetting(record)">销售设置</a-button>
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
import Api from '@/api/Collection';
import StoryApi from '@/api/Stories';
import StoryTaleApi from '@/api/StoryTale';
import CollectionSettingApi from '@/api/CollectionSetting';
import { getDetail } from '@/api/CollectionSetting';
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
				title: '作品名称',
				dataIndex: 'collectionName'
			},
			{
				title: '作品封面',
				dataIndex: 'collectionCover',
				customRender: imageRender.set({
					width: '50px'
				})
			},
			{
				title: '故事名称',
				dataIndex: 'storyTitle'
			},
			{
				title: '作品类型',
				dataIndex: 'collectionType',
				customRender: constantRender(constants.NftType.dict)
			},
			{
				title: '售卖方式',
				dataIndex: 'soldType',
				customRender: constantRender(constants.SoldType.dict)
			},
			{
				title: '数量',
				dataIndex: 'mintAmount'
			},
			{
				title: '价格',
				dataIndex: 'price'
			},

			{
				title: '审核状态',
				dataIndex: 'status',
				customRender: constantRender(constants.CheckStatus.dict)
			},{
				title: '铸造状态',
				dataIndex: 'mintStatus',
				customRender: constantRender(constants.MintStatus.dict)
			},{
				title: '上架状态',
				dataIndex: 'publishStatus',
				customRender: constantRender(constants.PublisStatus.dict)
			},
			{
				title: '售卖状态',
				dataIndex: 'soldStatus',
				customRender: constantRender(constants.SoldStatus.dict)
			},
			{
				title: '排序',
				dataIndex: 'sort'
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
			{
					type: 'input',
					label: '合约地址',
					field: 'smartContractAddress',
					value: '',
					props: {
						placeholder: '请输入铸造合约地址'
					},
					rules: []
			},
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
					type: 'input',
					label: '流通数量',
					field: 'publishAmount',
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
function getSettingItem(): FormSchema {
	return {
		formItem: [
			{
					type: 'input',
					label: '售卖价格',
					field: 'price',
					value: '',
					props: {
						placeholder: '必传'
					},
					rules: [
						{
							required: false,
							message: '必传'
						}
					]
			},
			{
					type: 'select',
					label: '售卖方式',
					field: 'soldType',
					value: '3',
					props: {
						placeholder: '请选择类型'
					},
					options: constants.SoldType.list.map((item) => {
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
					label: '空投用户',
					field: 'joinUserAuthType',
					value: '1',
					props: {
						placeholder: '请选择可参加空投用户类型'
					},
					options: constants.AirDropUserAuthType.list.map((item) => {
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
					label: '开启类型',
					field: 'collectionOpenType',
					value: '1',
					props: {
						placeholder: '请选择藏品开启类型'
					},
					options: constants.NftOpenType.list.map((item) => {
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
					type: 'input',
					label: '限购数量',
					field: 'limitSoldNumber',
					value: '1',
					props: {
						placeholder: '必传'
					},
					rules: [
						{
							required: false,
							message: '必传'
						}
					]
				},
				{
					type: 'date',
					label: '活动开始',
					field: 'bookStartTime',
					value: undefined,
					props: {
						showTime: true
					},
					rules: []
				},
				{
					type: 'date',
					label: '活动结束',
					field: 'bookEndTime',
					value: undefined,
					props: {
						showTime: true
					},
					rules: []
				},
				{
					type: 'select',
					label: '是否要邀请',
					field: 'needInvited',
					value: '0',
					props: {
						placeholder: '请选择获得类型'
					},
					options: constants.MainDisplayType.list.map((item) => {
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
					type: 'input',
					label: '需邀请人数',
					field: 'needInvitedNumber',
					value: '3',
					props: {
						placeholder: '需邀请人数'
					},
					rules: [
						{
							required: false,
							message: '名称不能为空'
						}
					]
				},

				{
					type: 'select',
					label: '获得权限类型',
					field: 'dropType',
					value: '1',
					props: {
						placeholder: '请选择获得类型'
					},
					options: constants.DropAuthType.list.map((item) => {
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
				// {
				// 	type: 'input',
				// 	label: '需持有的分身',
				// 	field: 'dropAuthAvatarIds',
				// 	value: '',
				// 	props: {
				// 		placeholder: '请输入名称'
				// 	},
				// 	rules: [
				// 		{
				// 			required: false,
				// 			message: '名称不能为空'
				// 		}
				// 	]
				// },
				{
					type: 'select',
					label: '上架发布',
					field: 'publishStatus',
					value: undefined,
					props: {
						placeholder: '请选择发布状态'
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
					type: 'date',
					label: '开始售卖',
					field: 'startTime',
					value: undefined,
					props: {
						showTime: true
					},
					rules: []
				},
				{
					type: 'date',
					label: '结束售卖',
					field: 'endTime',
					value: undefined,
					props: {
						showTime: true
					},
					rules: []
				},
		]
	};
}

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
					label: '作品封面',
					field: 'collectionCover',
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
					type: 'input',
					label: '作品名称',
					field: 'collectionName',
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
					label: '故事IP',
					field: 'storyTaleId',
					value: undefined,
					asyncOptions: async () => {
						const data = await http.request({
							url: StoryTaleApi.list

						});
						return data.map((item) => {
							return {
								label: item.taleTitle,
								value: item.id
							};
						});
					},
					props: {
						placeholder: '请选择人物故事IP必选'
					},
					rules: [
						{
							required: true,
							message: '必选'
						}
					]
				},
				{
					type: 'select',
					label: '故事脚本',
					field: 'storyId',
					value: undefined,
					asyncOptions: async () => {
						const data = await http.request({
							url: StoryApi.list,
							params: {
								id: 0
							}
						});
						return data.map((item) => {
							return {
								label: item.storyTitle,
								value: item.id
							};
						});
					},
					props: {
						placeholder: '主题展示非必选，其他必选'
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
					label: 'NFT类型',
					field: 'collectionType',
					value: undefined,
					props: {
						placeholder: '请选择类型'
					},
					options: constants.NftType.list.map((item) => {
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
					label: '主题显示',
					field: 'mainDisplay',
					value: undefined,
					props: {
						placeholder: '请选择类型'
					},
					options: constants.MainDisplayType.list.map((item) => {
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
					type: 'input',
					label: '排序',
					field: 'sort',
					value: '',
					props: {
						placeholder: '必传'
					},
					rules: [
						{
							required: false,
							message: '必传'
						}
					]
				},

				{
					type: 'select',
					label: '审核状态',
					field: 'status',
					value: undefined,
					props: {
						placeholder: '请选择审核状态'
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

const config: ICurdMintConfig = {
	theme: '作品',
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
	name: 'ArtCheck',
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

		async function setSoldSetting(record) {
			let formSchema = getSettingItem();

			const res = await getDetail({ id: record.id }).finally(() => {

			});

			const detail = res.data;
			const fields = detail
				? {
						...detail,
				  }
			: undefined;
			useFormModal({
				title: `售卖设置`,
				fields,
				formSchema,
				handleOk: async (modelRef, state) => {
					const params: any = {};
					if (detail) {
						Object.assign(
							params,
							{
								id: detail.id
							},
							modelRef
						);
					}

					await http.request({
						url: CollectionSettingApi.save,
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
			mint,
			setSoldSetting
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
