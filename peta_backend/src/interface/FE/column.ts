export interface IColumnOption {
	label: string;
	value: string | number;
}
interface IColumn {
	dataIndex: string;
	title: string;
	customRender?: (any) => any;
	asyncOptions?: () => Promise<IColumnOption[]>; // 异步选项的数据
	options?: IColumnOption[];
}
export default IColumn;
