import { Storage } from '@/utils/Storage';
import { ACCESS_TOKEN } from '@/store/mutation-types';
import { ICurrentAccountState } from './type';

export const state: ICurrentAccountState = {
	token: Storage.get(ACCESS_TOKEN, ''),
	menuPermissionDict: {},
	accountInfo: undefined
};
