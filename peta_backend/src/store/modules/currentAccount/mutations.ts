import { ICurrentAccountState, IAccountInfo, IMenuPermission } from './type';

export const mutations = {
	// setToken: (state: ICurrentAccountState, token: string) => {
	// 	state.token = token;
	// },
	setAccountInfo: (state: ICurrentAccountState, accountInfo: IAccountInfo) => {
		console.log('accountInfo', accountInfo);
		state.accountInfo = accountInfo;
	}
	// setMenuPermissionDict: (state: ICurrentAccountState, menuPermissionDict: IMenuPermission) => {
	// 	state.menuPermissionDict = menuPermissionDict;
	// }
};
