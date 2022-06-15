<template>
	<a-modal v-model:visible="visible" :confirmLoading="confirmLoading" title="菜单" @ok="handleOk">
		<a-form ref="formRef" :model="formState" :rules="rules" v-bind="cols">
			<a-form-item label="菜单名称" name="menuName">
				<a-input v-model:value="formState.menuName" />
			</a-form-item>
			<a-form-item label="菜单链接" name="menuUrl">
				<a-input v-model:value="formState.menuUrl" />
			</a-form-item>
			<a-form-item name="menuIcon">
				<template #label>
					菜单图标
					<a href="https://2x.antdv.com/components/icon-cn" target="view_window">
						<LinkOutlined />
					</a>
				</template>
				<a-input v-model:value="formState.menuIcon" />
			</a-form-item>
			<a-form-item label="菜单编码" name="menuCode">
				<a-input v-model:value="formState.menuCode" />
			</a-form-item>
			<a-form-item label="上级菜单" name="parentMenuId">
				<a-select v-model:value="formState.parentMenuId">
					<a-select-option v-for="menu in parentMenuOptions" :key="menu.value" :value="menu.value">{{
						menu.title
					}}</a-select-option>
				</a-select>
			</a-form-item>
			<a-form-item label="排序值" name="sort">
				<a-input-number v-model:value="formState.sort" :min="0" :max="999" />
			</a-form-item>
			<a-form-item label="备注" name="remark">
				<a-textarea v-model:value="formState.remark" />
			</a-form-item>
		</a-form>
	</a-modal>
</template>
<script lang="ts">
import { reactive, toRefs, ref, toRaw, UnwrapRef, PropType } from 'vue';
import { ValidateErrorEntity } from 'ant-design-vue/es/form/interface';
import http from '@/utils/http/axios';
import { generateCols } from '@/utils/common';
import { IMenusInfoState, IMenusForm, IParentMenuOption } from '../interface';
import { IMenus, IMenu } from '@/interface/BE/menu';
import MenuApi from '@/api/Menu';
import { LinkOutlined } from '@ant-design/icons-vue';
export default {
	components: {
		LinkOutlined
	},
	props: {
		menuTree: {
			type: Array as PropType<IMenus>,
			require: true
		}
	},
	emits: ['onRefresh'],
	setup(props, ctx) {
		const formRef = ref();
		const initForm: IMenusForm = {
			menuName: '',
			menuUrl: '',
			menuIcon: '',
			menuCode: '',
			parentMenuId: 0,
			status: 1,
			sort: 0,
			remark: ''
		};
		const formState: UnwrapRef<IMenusForm> = reactive<IMenusForm>({ ...initForm });
		const state = reactive<IMenusInfoState>({
			visible: false,
			confirmLoading: false,
			id: undefined,
			parentMenuOptions: []
		});
		const rules = {
			menuName: [
				{ required: true, message: '必填', trigger: 'blur' },
				{ max: 10, message: '最多10个字', trigger: 'blur' }
			],
			menuUrl: [{ required: true, message: '必填', trigger: 'blur' }],
			menuCode: [{ required: true, message: '必填', trigger: 'blur' }],
			parentMenuId: [{ required: true, message: '必选', trigger: 'change', type: 'number' }],
			status: [{ required: true, message: '必选', trigger: 'change' }]
		};
		async function handleOk() {
			const res = await formRef.value.validate();
			const params = {
				...toRaw(formState),
				id: state.id
			};
			state.confirmLoading = true;
			const data = await http
				.request({
					url: MenuApi.save,
					method: 'POST',
					params
				})
				.finally(() => {
					state.confirmLoading = false;
				});
			ctx.emit('onRefresh');
			state.visible = false;
		}
		function show(record?: IMenu) {
			let parentMenuOptions: IParentMenuOption[] = props.menuTree!.map((item) => {
				return {
					title: item.menuName,
					value: item.id
				};
			});
			if (record) {
				Object.assign(formState, record);
				parentMenuOptions = parentMenuOptions.filter((item) => item.value !== record.id);
				state.id = record.id;
			} else {
				state.id = undefined;
				Object.assign(formState, initForm);
			}
			parentMenuOptions.unshift({ title: '无', value: 0 });
			state.parentMenuOptions = parentMenuOptions;
			state.visible = true;
		}
		const resetForm = () => {
			formRef.value.resetFields();
		};

		return {
			formState,
			...toRefs(state),
			formRef,
			rules,
			handleOk,
			show,
			cols: generateCols(6, 16)
		};
	}
};
</script>
