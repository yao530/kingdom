export interface IMenuPermission {
	[menuName: string]: number;
}
export interface IAccountInfo {
	id: number;
	accountName: string;
}
export type ICurrentAccountState = {
	token: string;
	menuPermissionDict: IMenuPermission;
	accountInfo?: IAccountInfo;
};
