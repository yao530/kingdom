import http from '@/utils/http/axios';
import { RequestEnum } from '@/enums/httpEnum';

enum OrderApi {
	list = 'order/getList',
	page = 'order/getMePage',
	save = 'order/edit',
	remove = 'order/remove'
}
export default OrderApi;
