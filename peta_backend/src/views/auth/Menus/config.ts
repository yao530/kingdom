import Api from '@/api/Menu';
import constants from '@/enums/constants';
import IColumn from '@/interface/FE/column';
import booleanRender from '@/columnCustomRender/booleanRender';
import constantRender from '@/columnCustomRender/constantRender';
import imageRender from '@/columnCustomRender/imageRender';
import formatTimeRender from '@/columnCustomRender/formatTimeRender';
import ICurdConfig from '@/interface/FE/ICurdConfig';

/**
 * @description 表格列
 */
const columns: IColumn[] = [
	{
		title: '菜单名称',
		dataIndex: 'menuName'
	},
	{
		title: '菜单链接',
		dataIndex: 'menuUrl'
	},
	{
		title: '菜单编码',
		dataIndex: 'menuCode'
	},
	{
		title: '排序',
		dataIndex: 'sort'
	},
	{
		title: '备注',
		dataIndex: 'remark'
	}
];

const config: ICurdConfig = {
	theme: '菜单',
	listUrl: Api.list,
	removeUrl: Api.remove,
	saveUrl: Api.save,
	getFormSchema: () => {
		return {
			formItem: []
		};
	},
	commonTableProps: {
		columns,
		childrenColumnName: 'childrenList'
	}
};

export default config;
