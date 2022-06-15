enum StoryApi {
	list = 'story/getList',
	page = 'story/getMePage',
	creatorPage = 'story/getPage',
	mint = 'story/mint',
	save = 'story/saveOrUpdate',
	remove = 'story/remove',
	detail = 'story/getDetail'
}
export default StoryApi;
import http from '@/utils/http/axios';

export function getDetail(params) {
	return http.request(
		{
			url: StoryApi.detail,
			params,
			method: 'POST',

		},
		{
			isTransformRequestResult: false
		}
	);
}
