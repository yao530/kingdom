import Api from '@/api/Banner';
import constants from '@/enums/constants';
import IColumn from '@/interface/FE/column';
import booleanRender from '@/columnCustomRender/booleanRender';
import constantRender from '@/columnCustomRender/constantRender';
import imageRender from '@/columnCustomRender/imageRender';
import ICurdConfig from '@/interface/FE/ICurdConfig';

/**
 * @description 表格列
 */
const columns: IColumn[] = [
	{
		title: '图片',
		dataIndex: 'bannerImage',
		customRender: imageRender
	},
	{
		title: '类型',
		dataIndex: 'bannerType',
		customRender: constantRender(constants.BannerType.dict)
	},
	{
		title: '应用模块',
		dataIndex: 'locationType',
		customRender: constantRender(constants.LocationType.dict)
	},
	{
		title: '状态',
		dataIndex: 'status',
		customRender: booleanRender
	},
	{
		title: '备注',
		dataIndex: 'remark'
	}
];

/**
 * @description 表单项。工厂模式保证每次打开都是新的
 */
function getFormSchema(): FormSchema {
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
		width: 1000,
		formItem: [
			{
				type: 'image',
				label: '图片',
				field: 'bannerImage',
				value: '',
				props: {
					placeholder: '图片必传'
				},
				rules: [
					{
						required: true,
						message: '图片必传'
					}
				]
			},
			{
				type: 'select',
				label: '应用模块',
				field: 'locationType',
				value: undefined,
				props: {
					placeholder: '请选择类型'
				},
				options: constants.LocationType.list.map((item) => {
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
				label: '类型',
				field: 'bannerType',
				value: undefined,
				props: {
					placeholder: '请选择类型'
				},
				options: constants.BannerType.list.map((item) => {
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
				type: 'textarea',
				label: '图文内容',
				field: 'context',
				value: '',
				props: {
					placeholder: '图文内容'
				}
			},
			{
				type: 'input',
				label: '跳转网址',
				field: 'linkUrl',
				value: '',
				props: {
					placeholder: '跳转网址'
				}
			},
			{
				type: 'input',
				label: '排序',
				field: 'sort',
				value: '0',
				props: {
					placeholder: '排序'
				}
			},
			{
				type: 'textarea',
				label: '描述',
				field: 'remark',
				value: '',
				props: {
					placeholder: '角色描述'
				}
			}
		]
	};
}
const config: ICurdConfig = {
	theme: '轮播图',
	pageUrl: Api.page,
	listUrl: Api.list,
	removeUrl: Api.remove,
	saveUrl: Api.save,
	getFormSchema,
	commonTableProps: {
		columns
	}
};

export default config;
