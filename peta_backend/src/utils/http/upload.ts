import http from '@/utils/http/axios';
import UploadApi from '@/api/Upload';
import { ContentTypeEnum } from '@/enums/httpEnum';
export default function upload(file) {
	return http.request({
		url: UploadApi.uploadFile,
		params: {
			file
		},
		headers: { 'Content-Type': ContentTypeEnum.FORM_DATA }
	});
}
