/**
 * 用户信息模块
 */
import { state } from './state';
import { mutations } from './mutations';
import { actions } from './actions';

export type { ICurrentAccountState } from './type';

export default {
	namespaced: true,
	state,
	mutations,
	actions
};
