import http from '@/utils/http/axios';
import { IInteriorAccount, InteriorHoursType, IRealTimeListType } from './types/res';
const api = 'forward/toDDH';
/**
 * @description:
 */
export async function getAccountData(accountId: number) {
	const d = await http.request<IInteriorAccount>({
		url: api,
		params: {
			path: `account/${accountId}`
		}
	});
	return d;
}

/**
 * @description: 24小时配电负荷趋势图
 */
export function getHours(accountId: number) {
	return http.request<InteriorHoursType>({
		url: api,
		params: {
			path: `e_hourlydata/${accountId}`
		}
	});
}

/**
 * @description: 实时监测数据
 */
export function getRealTimes(accountId: number) {
	return http.request<IRealTimeListType>({
		url: api,
		params: {
			path: `realtime/${accountId}`
		}
	});
}
