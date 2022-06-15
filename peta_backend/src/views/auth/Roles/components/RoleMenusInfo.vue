<template>
	<Modal
		v-model:visible="visible"
		:width="400"
		:confirmLoading="loading"
		:title="title"
		:afterClose="afterClose"
		@ok="handleOk"
	>
		<Spin :spinning="loading">
			<div class="content">
				<Tree
					v-model:expandedKeys="expandedKeys"
					:show-line="true"
					:show-icon="false"
					:selectable="false"
					defaultExpandAll
				>
					<TreeNode v-for="menuLevel1 in menusForm" :key="menuLevel1.id">
						<template #title>
							<div class="menu_raw">
								<div>{{ menuLevel1.menuName }}</div>
								<div class="place_hr"></div>
								<RadioGroup v-model:value="menuLevel1.authStatus" size="small">
									<RadioButton v-for="item in MenuAuthStatus" :key="item.value" :value="item.value">
										{{ item.describe }}
									</RadioButton>
								</RadioGroup>
							</div>
						</template>
						<TreeNode v-for="menuLevel2 in menuLevel1.childrenList" :key="menuLevel2.id">
							<template #title>
								<div class="menu_raw">
									<div>{{ menuLevel2.menuName }}</div>
									<div class="place_hr"></div>
									<RadioGroup v-model:value="menuLevel2.authStatus" size="small">
										<RadioButton
											v-for="item in MenuAuthStatus"
											:key="item.value"
											:value="item.value"
											>{{ item.describe }}</RadioButton
										>
									</RadioGroup>
								</div>
							</template>
						</TreeNode>
					</TreeNode>
				</Tree>
			</div>
		</Spin>
	</Modal>
</template>
<script lang="ts">
import { defineComponent, reactive, toRefs, ref, watch, PropType, Prop, onMounted, computed } from 'vue';
import { message, Modal, Spin, Tree, Radio } from 'ant-design-vue';
import { CarryOutOutlined, FormOutlined } from '@ant-design/icons-vue';
import { getMenus, getRoleMenus, editRoleMenu } from '@/api/Menu';
import Constance from '@/enums/constants';
import { IRole } from '@/interface/BE/role';
import { getMenuPermission } from '@/utils/common';
import { EditRoleMenuParamsItem } from '@/interface/RES/menuModel';

interface IFormItem {
	menuName: string;
	id: number;
	authStatus: number;
	menuId: number;
	childrenList: IFormItem[];
}

export default defineComponent({
	components: {
		Modal,
		Spin,
		Tree,
		TreeNode: Tree.TreeNode,
		RadioButton: Radio.Button,
		RadioGroup: Radio.Group,
		CarryOutOutlined,
		FormOutlined
	},
	props: {
		theme: {
			type: String
		},
		title: {
			type: String
		},
		record: {
			type: Object as PropType<IRole>,
			require: true
		}
	},

	emits: ['destroy', 'callback'],
	setup(props, ctx) {
		const state = reactive<{
			visible: boolean;
			loading: boolean;
			menusForm: IFormItem[];
			expandedKeys: string[];
		}>({
			visible: true,
			loading: false,
			menusForm: [],
			expandedKeys: []
		});

		onMounted(() => {
			getData();
		});

		function getTree(srcMenus, menuPermissionDict) {
			if (!srcMenus) {
				return srcMenus;
			}
			return srcMenus.map((item) => {
				return {
					menuName: item.menuName,
					id: item.id,
					menuId: item.menuId,
					authStatus: menuPermissionDict[item.menuUrl] || 0,
					childrenList: getTree(item.childrenList, menuPermissionDict)
				};
			});
		}
		function getIds(container, srcMenus) {
			srcMenus.forEach((item) => {
				container.push(item.id);
				if (item.childrenList) {
					getIds(container, item.childrenList);
				}
			});
			return container;
		}
		function getReqs(container: EditRoleMenuParamsItem[], items) {
			items.forEach((item) => {
				container.push({
					authStatus: item.authStatus,
					id: item.id,
					menuId: item.menuId
				});
				if (item.childrenList) {
					getReqs(container, item.childrenList);
				}
			});
			return container;
		}
		async function getData() {
			state.loading = true;
			const roleMenus = await getRoleMenus({ roleId: props.record!.id }).finally(() => {
				state.loading = false;
			});
			const menuPermissionDict = getMenuPermission({}, roleMenus);
			state.menusForm = getTree(roleMenus, menuPermissionDict);
			state.expandedKeys = getIds([], roleMenus);
		}

		function afterClose() {
			ctx.emit('destroy');
		}
		async function handleOk() {
			await editRoleMenu({
				roleId: props.record!.id,
				roleMenuReqs: getReqs([], state.menusForm)
			});
			message.success('操作成功');
			state.visible = false;
		}
		return {
			...toRefs(state),
			afterClose,
			handleOk,
			MenuAuthStatus: Constance.MenuAuthStatus.list
		};
	}
});
</script>

<style lang="scss" scoped>
.menu_raw {
	display: flex;
	// min-width: 350px;
	align-items: center;
}

:deep .ant-tree.ant-tree-show-line li span.ant-tree-switcher {
	float: left;
}

:deep .ant-tree li .ant-tree-node-content-wrapper {
	display: block;
}

:deep .ant-tree li {
	padding: 6px 0;
}

.place_hr {
	margin: 0 8px;
	border-top: 1px dashed #ddd;
	flex: 1;
}
</style>
