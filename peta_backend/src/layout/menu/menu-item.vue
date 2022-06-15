<template>
	<template v-if="!menuInfo.meta?.hidden">
		<a-sub-menu v-if="menuInfo.children?.length" :key="menuInfo.name" v-bind="$attrs">
			<template v-if="menuInfo.meta" #title>
				<MenuIcon :iconComponentText="menuInfo.meta?.icon" />
				<span>{{ menuInfo.meta.title }}</span>
			</template>
			<template v-for="item in menuInfo.children" :key="item.name">
				<MenuItem v-if="item.children" :key="item.name" :menu-info="item" />
				<template v-else>
					<a-menu-item :key="item.name">
						<MenuIcon :iconComponentText="item.meta?.icon" />
						<span>{{ item.meta?.title }}</span>
					</a-menu-item>
				</template>
			</template>
		</a-sub-menu>
		<template v-else>
			<a-menu-item :key="menuInfo.name">
				<MenuIcon :iconComponentText="menuInfo.meta?.icon" />
				<span>{{ menuInfo.meta?.title }}</span>
			</a-menu-item>
		</template>
	</template>
</template>

<script lang="ts">
import { defineComponent, Prop, PropType } from 'vue';
import { IconFont } from '@/components/iconfont';
import { IRouteInfo } from '@/interface/FE/routeInfo';
import MenuIcon from './MenuIcon.vue';

export default defineComponent({
	name: 'MenuItem',
	components: {
		IconFont,
		MenuIcon
	},
	props: {
		menuInfo: {
			type: Object as PropType<IRouteInfo>,
			default: () => ({})
		}
	}
});
</script>

<style scoped></style>
