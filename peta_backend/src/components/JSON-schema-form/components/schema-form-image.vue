<template>
	<Upload
		v-model:file-list="fileList"
		name="file"
		list-type="picture-card"
		class="avatar-uploader"
		:show-upload-list="false"
		:action="action"
		:before-upload="beforeUpload"
		@change="handleChange"
	>
		<img v-if="modelValue" :src="modelValue" alt="avatar" />
		<div v-else>
			<LoadingOutlined v-if="loading" />
			<PlusOutlined v-else />
			<div class="ant-upload-text">{{ placeholder }}</div>
		</div>
	</Upload>
</template>
<script lang="ts">
import { defineComponent, PropType, computed, reactive, toRefs } from 'vue';
import UploadApi from '@/api/Upload';
import prefix from '@/api/prefixUrl';
import { PlusOutlined, LoadingOutlined } from '@ant-design/icons-vue';
import { message } from 'ant-design-vue';
import { Upload } from 'ant-design-vue';
function getBase64(img: Blob, callback: (base64Url: string) => void) {
	const reader = new FileReader();
	reader.addEventListener('load', () => callback(reader.result as string));
	reader.readAsDataURL(img);
}
interface FileItem {
	uid: string;
	name?: string;
	status?: string;
	response?: string;
	url?: string;
	type?: string;
	size: number;
	originFileObj: any;
}

interface FileInfo {
	file: FileItem;
	fileList: FileItem[];
}
interface IState {
	loading: boolean;
	fileList: FileItem[];
}

export default defineComponent({
	name: 'SchemaFormImage',
	components: {
		Upload,
		PlusOutlined,
		LoadingOutlined
	},
	props: {
		placeholder: {
			type: String,
			default: '上传图片'
		},
		formItem: {
			// 表单项
			type: Object as PropType<FormItem>,
			default: () => ({})
		},
		value: undefined as any // 表单项值
	},
	emits: ['update:value'],
	setup(props, { attrs, emit }) {
		const modelValue = computed({
			get: () => props.value,
			set: (val) => emit('update:value', val)
		});
		const state = reactive<IState>({
			fileList: [],
			loading: false
		});

		function beforeUpload(file) {
			const isJpgOrPng = file.type === 'image/jpeg' || file.type === 'image/png';
			if (!isJpgOrPng) {
				message.error('只能上传图片!');
			}
			const maxSize = 10;
			const vol = file.size / 1024 / 1024 < maxSize;
			if (!vol) {
				message.error(`不能超过 ${maxSize}MB!`);
			}
			return isJpgOrPng && vol;
		}
		function handleChange(info) {
			if (info.file.status === 'uploading') {
				state.loading = true;
				return;
			}
			if (info.file.status === 'done') {
				state.loading = false;
				modelValue.value = info.file?.response?.data?.url;
			}
			if (info.file.status === 'error') {
				state.loading = false;
				message.error('upload error');
			}
		}
		return {
			...toRefs(state),
			modelValue,
			action: `${prefix}${UploadApi.uploadFile}`,
			beforeUpload,
			handleChange
		};
	}
});
</script>

<style>
.avatar-uploader > .ant-upload {
	width: 128px;
	height: 128px;
}

.avatar-uploader img {
	width: 100%;
}

.ant-upload-select-picture-card i {
	font-size: 32px;
	color: #999;
}

.ant-upload-select-picture-card .ant-upload-text {
	margin-top: 8px;
	color: #666;
}
</style>
