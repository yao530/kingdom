import { Storage } from '@/utils/Storage';
import { ACCESS_TOKEN, CURRENT_USER } from '@/store/mutation-types';
import { IUserState } from './interface';

export const state: IUserState = {
	token: Storage.get(ACCESS_TOKEN, ''),
	name: '',
	welcome: '',
	avatar: '',
	roles: [],
	info: Storage.get(CURRENT_USER, {}),
	menuPermissionDict: {}
};
