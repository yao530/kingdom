<template>
	<div class="info_card_box">
		<div class="left_area" :style="{ background: background }">
			<DashboardOutlined />
		</div>
		<div class="right_area">
			<div class="count_box">
				<div class="count_number">{{ text }}</div>
			</div>
			<div class="describe">{{ describe }}</div>
		</div>
	</div>
</template>
<script lang="ts">
import { UserOutlined, DashboardOutlined } from '@ant-design/icons-vue';
import { ref, defineComponent, watchEffect, computed, onUnmounted } from 'vue';

const DEFAULT_TEXT = '-';
const STEP_TOTAL = 30;

export default defineComponent({
	name: 'InforCard',
	components: {
		UserOutlined,
		DashboardOutlined
	},
	props: {
		background: {
			type: String,
			default: '#2d8cf0'
		},
		count: {
			type: Number
		},
		describe: {
			type: String
		},
		precision: {
			type: Number,
			default: 2
		}
	},
	setup(props) {
		const num = ref<number | string>();
		const text = computed(() => {
			if (num.value === undefined) {
				return DEFAULT_TEXT;
			}
			if (parseFloat(num.value as string).toString() !== 'NaN') {
				return num.value.toLocaleString();
			}
			return num.value;
		});

		let TIME_ID: number;

		watchEffect(updateViewText);
		onUnmounted(() => {
			clearTimeout(TIME_ID);
		});

		function updateViewText() {
			clearTimeout(TIME_ID);
			if (props.count === num.value) {
				return;
			}
			if (props.count === undefined) {
				return num.value === props.count;
			}
			if (typeof props.count === 'number') {
				if (typeof num.value === 'number') {
					let step = Math.sign(props.count - num.value) * (props.count / STEP_TOTAL);
					let next = num.value + step;
					if (next > props.count) {
						num.value = props.count;
					} else {
						const dec = 10 ** props.precision;
						next = Math.ceil(next * dec) / dec;
						num.value = next;
					}
				} else {
					num.value = 0;
				}
			}
			TIME_ID = setTimeout(updateViewText, 40);
		}

		return {
			text
		};
	}
});
</script>
<style lang="scss" scoped>
$leftWid: 33%;

.info_card_box {
	display: flex;
	height: 94px;
	overflow: hidden;
	border-radius: 4px;
	box-shadow: 2px 2px 4px 1px rgb(0 0 0 / 10%);
}

.left_area {
	display: flex;
	width: $leftWid;
	height: 100%;
	font-size: 36px;
	color: #fff;
	align-items: center;
	justify-content: center;
}

.right_area {
	width: 100% - $leftWid;
	height: 100%;
	color: #515a6e;
	background: #fff;

	.count_box {
		display: flex;
		height: 62%;
		padding-top: 10px;
		align-items: center;
		justify-content: center;

		.count_number {
			font-size: 28px;
		}
	}

	.describe {
		font-size: 14px;
		text-align: center;
	}
}
</style>
