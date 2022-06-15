import { ActionContext } from 'vuex';
import { ICurrentAccountState } from './type';
import AccountApi from '@/api/Account';
import { ACCESS_TOKEN, CURRENT_USER, IS_LOCKSCREEN } from '@/store/mutation-types';
import { Storage } from '@/utils/Storage';
import store from '@/store';
import { IStore } from '@/store/types';
import http from '@/utils/http/axios';

export const actions = {
	async getAccountInfo({ commit }: ActionContext<ICurrentAccountState, IStore>, userInfo) {
		const data = await http.request({
			url: AccountApi.getCurrentAccount
		});
		commit('setAccountInfo', data);
	}
};
