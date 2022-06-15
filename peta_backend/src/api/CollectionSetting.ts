import http from '@/utils/http/axios';


enum CollectionSettingApi {
	getOne = 'collectionSoldSetting/getDetail',
	save = 'collectionSoldSetting/saveOrUpdate',

}
export default CollectionSettingApi;


export function getDetail(params) {
	return http.request(
		{
			url: CollectionSettingApi.getOne,
			params,
			method: 'POST',

		},
		{
			isTransformRequestResult: false
		}
	);
}
export function save(params) {
	return http.request(
		{
			url: CollectionSettingApi.save,
			params,
			method: 'POST',

		},
		{
			isTransformRequestResult: false
		}
	);
}
