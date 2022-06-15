<template>
	<a-layout class="layout">
		<a-layout-sider
			v-model:collapsed="collapsed"
			:width="asiderWidth"
			:trigger="null"
			collapsible
			class="layout-sider"
		>
			<Logo :collapsed="collapsed" />
			<AsideMenu :collapsed="collapsed" />
		</a-layout-sider>

		<a-layout>
			<!--      页头 start-->
			<page-header v-model:collapsed="collapsed" />
			<!--      页头end-->
			<!--      内容区域start-->
			<a-layout-content class="layout-content">
				<div class="view_content">
					<router-view v-slot="{ Component }">
						<!-- <keep-alive>
							</keep-alive> -->
						<component :is="Component" />
					</router-view>
					<!-- <a-card>

					</a-card> -->
				</div>
			</a-layout-content>

			<!--      内容区域end-->
			<!--      页脚start-->
			<!--      页脚end-->
		</a-layout>
	</a-layout>
</template>

<script lang="ts">
import { defineComponent, ref, computed } from 'vue';
import { Layout } from 'ant-design-vue';
import Logo from './logo/index.vue';
import AsideMenu from './menu/menu.vue';
import PageHeader from './header/index.vue';

export default defineComponent({
	name: 'Layout',
	components: {
		PageHeader,
		AsideMenu,
		Logo,
		[Layout.name]: Layout,
		[Layout.Content.name]: Layout.Content,
		[Layout.Sider.name]: Layout.Sider
	},
	setup() {
		const collapsed = ref<boolean>(false);
		// 自定义侧边栏菜单收缩和展开时的宽度
		const asiderWidth = computed(() => (collapsed.value ? 80 : 208));

		return {
			collapsed,
			asiderWidth
		};
	}
});
</script>

<style lang="scss" scoped>
.layout {
	display: flex;
	height: 100vh;
	overflow: hidden;

	.ant-layout {
		overflow: hidden;
	}

	.layout-content {
		flex: none;
	}
}

.view_content {
	/* height: calc(100vh - #{$header-height}); */
	height: calc(100vh - 64px);
	padding: 20px 14px 20px;
	overflow: auto;
}
</style>
