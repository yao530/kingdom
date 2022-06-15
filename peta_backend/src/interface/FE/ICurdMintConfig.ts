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

interface ICurdMintConfig {
	theme: string;
	showSearch?: boolean | string;
	dateRange?: boolean | IDateRangeConfig;
	saveUrl?: string;
	removeUrl?: string;
	pageUrl?: string;
	listUrl?: string;
	detailUrl?:string;
	mintUrl?:string;
	getFormSchema: IGetFormSchema;
	getMintSchema:IGetFormSchema;
	commonTableProps: ICommonTableProps;
}
export default ICurdMintConfig;
