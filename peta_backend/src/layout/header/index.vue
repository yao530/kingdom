<template>
	<ALayoutHeader class="layout-header">
		<div class="left-options">
			<span class="menu-fold" @click="() => $emit('update:collapsed', !collapsed)">
				<component :is="collapsed ? 'menu-unfold-outlined' : 'menu-fold-outlined'" />
			</span>
			<a-breadcrumb>
				<template v-for="(routeItem, rotueIndex) in $route.matched" :key="routeItem.name">
					<a-breadcrumb-item>
						<span>{{ routeItem.meta.title }}</span>
						<template v-if="routeItem.children.length" #overlay>
							<a-menu :selectedKeys="[$route.matched[rotueIndex + 1]?.name]">
								<template v-for="childItem in routeItem.children">
									<a-menu-item v-if="!childItem.meta?.hidden" :key="childItem.name">
										<router-link :to="{ name: childItem.name }" custom #="{ navigate }">
											<div @click="navigate">{{ childItem.meta?.title }}</div>
										</router-link>
									</a-menu-item>
								</template>
							</a-menu>
						</template>
					</a-breadcrumb-item>
				</template>
			</a-breadcrumb>
		</div>
		<div class="right-options">
			<template v-for="item in iconList" :key="item.icon.name">
				<a-tooltip placement="bottom">
					<template #title>
						<span>{{ item.tips }}</span>
					</template>
					<component :is="item.icon" v-on="item.eventObject || {}" />
				</a-tooltip>
			</template>
			<!--      切换全屏-->
			<component :is="fullscreenIcon" @click="toggleFullScreen" />
			<Dropdown>
				<a-avatar>{{ username }}</a-avatar>
				<template #overlay>
					<a-menu>
						<a-menu-item>
							<div>个人中心</div>
						</a-menu-item>
						<a-menu-divider />
						<a-menu-item>
							<div @click.prevent="doLogout"><poweroff-outlined /> 退出登录</div>
						</a-menu-item>
					</a-menu>
				</template>
			</Dropdown>
		</div>
	</ALayoutHeader>
</template>

<script lang="ts">
import { defineComponent, reactive, toRefs, computed, createVNode } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import components from '@/layout/header/components';
import { message, Modal } from 'ant-design-vue';
import { QuestionCircleOutlined } from '@ant-design/icons-vue';
import { useStore } from '@/store';

export default defineComponent({
	name: 'PageHeader',
	components: { ...components },
	props: {
		collapsed: {
			type: Boolean
		}
	},
	emits: ['update:collapsed'],
	setup() {
		const store = useStore();

		const state = reactive({
			fullscreenIcon: 'FullscreenOutlined'
		});

		const router = useRouter();
		const route = useRoute();

		const username = computed(() => store.getters['user/userInfo']?.username);

		// 退出登录
		const doLogout = () => {
			Modal.confirm({
				title: '您确定要退出登录吗？',
				icon: createVNode(QuestionCircleOutlined),
				onOk: () => {
					console.log(router, '退出登录');
					// logout({})
					store.dispatch('user/logout').then((res) => {
						message.success('成功退出登录');
						// 移除标签页
						router
							.replace({
								name: 'login',
								query: {
									redirect: route.fullPath
								}
							})
							.finally(() => location.reload());
					});
				}
			});
		};

		// 切换全屏图标
		const toggleFullscreenIcon = () =>
			(state.fullscreenIcon =
				document.fullscreenElement !== null ? 'FullscreenExitOutlined' : 'FullscreenOutlined');

		// 监听全屏切换事件
		document.addEventListener('fullscreenchange', toggleFullscreenIcon);

		// 全屏切换
		const toggleFullScreen = () => {
			if (!document.fullscreenElement) {
				document.documentElement.requestFullscreen();
			} else {
				if (document.exitFullscreen) {
					document.exitFullscreen();
				}
			}
		};

		// 图标列表
		const iconList = [
			{
				icon: 'LockOutlined',
				tips: '锁屏',
				eventObject: {
					click: () => store.commit('lockscreen/setLock', true)
				}
			}
		];

		return {
			...toRefs(state),
			iconList,
			username,
			toggleFullScreen,
			doLogout
		};
	}
});
</script>

<style lang="scss" scoped>
.layout-header {
	position: sticky;
	top: 0;
	z-index: 10;
	display: flex;
	height: $header-height;
	padding: 0;
	background-color: #fff;
	justify-content: space-between;
	align-items: center;

	.left-options {
		display: flex;
		align-items: center;

		.menu-fold {
			padding: 0 24px;
			cursor: pointer;
		}
	}

	.right-options {
		display: flex;
		align-items: center;

		> * {
			margin-right: 20px;
			cursor: pointer;
		}
	}
}
</style>
