<template>
	<QuillEditor ref="editorRef" :content="value" :options="editorOption" contentType="html" @textChange="textChange" />
</template>
<script lang="ts">
import { defineComponent, ref, reactive } from 'vue';
import { Quill, QuillEditor } from '@vueup/vue-quill';
import '@vueup/vue-quill/dist/vue-quill.snow.css';
import { container, ImageExtend, QuillWatch } from 'quill-image-extend-module';

Quill.register('modules/ImageExtend', ImageExtend);

export default defineComponent({
	components: { QuillEditor },
	props: {
		value: {
			type: String,
			default: ''
		}
	},
	emits: ['update:value'],
	setup(props, { emit }) {
		const editorRef = ref();
		const editorOption = reactive({
			modules: {
				ImageExtend: {
					loading: true,
					name: 'file',
					action: 'api/imageUpload/imgUploadFile',
					response: (res) => {
						return res.data.url;
					}
				},
				toolbar: {
					container: container,
					handlers: {
						image: function () {
							// @ts-ignore
							QuillWatch.emit(this.quill.id);
						}
					}
				}
			}
		});

		const textChange = (delta, oldContents, source) => {
			emit('update:value', editorRef.value.getHTML());
		};

		return {
			editorRef,
			editorOption,
			textChange
		};
	}
});
</script>
