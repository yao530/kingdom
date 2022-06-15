export interface IMenu {
	authStatus: number | null;
	childrenList: IMenu[];
	id: number;
	menuCode: string;
	menuIcon: string;
	menuId: number;
	menuLevel: number;
	menuName: string;
	menuUrl: string;
}

export interface IMenus extends Array<IMenu> {
	[index: number]: IMenu;
}
