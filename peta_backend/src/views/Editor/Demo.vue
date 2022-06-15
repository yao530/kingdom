<template>
	<view>
		<Editor v-model="rich" />
		<a @click="display">console</a>
	</view>
</template>

<script lang="ts" setup>
import { ref } from 'vue';
import Editor from './Editor.vue';

const rich = ref('');

const display = () => {
	console.log(rich.value);
};
</script>

<style scoped></style>
