export interface IMenuPermission {
	[menuName: string]: number;
}
interface IAccountInfo {}
export type IUserState = {
	token: string;
	name: string;
	welcome: string;
	avatar: string;
	roles: any[];
	info: any;
	menuPermissionDict: IMenuPermission;
	accountInfo?: IAccountInfo;
};
