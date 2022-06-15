<template>
	<div class="container">
		<div>
			<h1>{{ title }}</h1>
			<h1>{{ subTitle }}</h1>
		</div>
		<img src="~@/assets/404.gif" alt="" />
	</div>
</template>

<script setup lang="ts">
import { computed, watch, onMounted, onUnmounted, ref, shallowRef } from 'vue';
const props = defineProps<{
	title: String;
	subTitle: String;
}>();
</script>

<style lang="scss" scoped>
.container {
	display: flex;
	width: 100%;
	height: 100%;
	background-color: white;
	justify-content: center;
	align-items: center;
}
</style>
