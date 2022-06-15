export interface IMenusForm {
	menuName: string;
	menuUrl: string;
	menuIcon: string;
	menuCode: string;
	parentMenuId: number;
	status: 1;
	remark: string;
	sort: number;
}
export interface IParentMenuOption {
	value: number;
	title: string;
}
export interface IMenusInfoState {
	visible: boolean;
	confirmLoading: boolean;
	id?: number;
	parentMenuOptions: IParentMenuOption[];
}
