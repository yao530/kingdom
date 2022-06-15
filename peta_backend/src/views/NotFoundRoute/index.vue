<template>
	<div class="page-container">
		<div>
			<h1>404</h1>
			<h1>此页面不存在或无权限访问</h1>
			<router-link to="/" class="ant-btn ant-btn-primary">回到首页</router-link>
		</div>
		<img src="~@/assets/404.gif" alt="" />
	</div>
</template>

<script lang="ts">
import { defineComponent } from 'vue';
export default defineComponent({
	name: '404'
});
</script>

<style lang="scss" scoped>
.page-container {
	display: flex;
	width: 100%;
	height: 100%;
	background-color: white;
	justify-content: center;
	align-items: center;
}
</style>
