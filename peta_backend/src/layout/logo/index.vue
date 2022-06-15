<template>
	<div class="logo">
		<!-- <img src="~@/assets/images/login_text.png" alt="" /> -->
		<span class="project-title" >宠物王国</span>
	</div>
</template>

<script>
import { defineComponent, defineProps } from 'vue';
const component = defineComponent({
	props: {
		collapsed: Boolean
	}
});
export default component;
</script>

<style lang="scss" scoped>
.logo {
	display: flex;
	height: 64px;
	padding-left: 24px;
	overflow: hidden;
	line-height: 64px;
	white-space: nowrap;
	align-items: center;

	img {
		height: 28px;
		margin-right: 8px;
	}
	// .title {
	// 	margin-bottom: 0;
	// 	color: white;
	// }
}

.project-title{
	color: #fff;
	font-size: 28px;
}
</style>
