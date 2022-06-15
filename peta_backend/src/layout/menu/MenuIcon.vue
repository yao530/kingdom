<template>
	<component :is="iconName" />
</template>
<script lang="ts">
import { defineComponent, VueElement, Component, ref } from 'vue';
import Vue from 'vue';
import * as antdIconVue from '@ant-design/icons-vue';
const icons = {};
Object.values(antdIconVue).forEach((item: any) => {
	if ((item as any).displayName) {
		icons[item.displayName] = item;
	}
});

export default defineComponent({
	components: icons,
	props: {
		iconComponentText: {
			type: String
		}
	},
	setup(props) {
		const iconName = ref('SmileOutlined');
		if (typeof props.iconComponentText === 'string') {
			// <TranslationOutlined />
			const match = props.iconComponentText.match(/<(.*) \/>/);
			if (match) {
				const content = match[1];
				if (content) {
					iconName.value = content;
				}
			}
		}
		return {
			iconName
		};
	}
});
</script>
