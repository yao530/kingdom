import { IUserState, IMenuPermission } from './interface';

export const mutations = {
	setToken: (state: IUserState, token: string) => {
		state.token = token;
	},
	setAvatar: (state: IUserState, avatar: string) => {
		state.avatar = avatar;
	},
	setRoles: (state: IUserState, roles) => {
		state.roles = roles;
	},
	setUserInfo: (state: IUserState, info) => {
		state.info = info;
	},
	setMenuPermissionDict: (state: IUserState, menuPermissionDict: IMenuPermission) => {
		state.menuPermissionDict = menuPermissionDict;
	}
};
