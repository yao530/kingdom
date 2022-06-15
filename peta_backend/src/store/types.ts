import { IAsyncRouteState } from '@/store/modules/async-route';
import { IUserState } from '@/store/modules/user/interface';
import { ILockscreenState } from '@/store/modules/lockscreen';
import { ICurrentAccountState } from '@/store/modules/currentAccount';

export interface IStore {
	asyncRoute: IAsyncRouteState;
	user: IUserState;
	lockscreen: ILockscreenState;
	currentAccount: ICurrentAccountState;
}
