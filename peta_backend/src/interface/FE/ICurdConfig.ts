import IColumn from '@/interface/FE/column';
interface ICommonTableProps {
	columns: IColumn[];
	actionWidth?: number;
	childrenColumnName?: string;
}
interface IGetFormSchema {
	(params?: any): FormSchema;
}
export interface IDateRangeConfig {
	fields?: string[];
	showTime?: boolean;
}

interface ICurdConfig {
	theme: string;
	showSearch?: boolean | string;
	dateRange?: boolean | IDateRangeConfig;
	saveUrl?: string;
	removeUrl?: string;
	pageUrl?: string;
	listUrl?: string;
	detailUrl?:string;
	getFormSchema: IGetFormSchema;
	commonTableProps: ICommonTableProps;
}
export default ICurdConfig;
