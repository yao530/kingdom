<template>
	<div class="login_container">
		<div class="login_content">
			<div class="logo_container">
				<!-- <img src="~@/assets/images/login_text.png" /> -->
				<span class="project-title" >宠物王国</span>
			</div>
			<a-form layout="horizontal" :model="formInline" @submit.prevent="handleSubmit">
				<a-form-item>
					<a-input v-model:value="formInline.username" size="large" placeholder="请输入账号">
						<template #prefix><UserOutlined /></template>
					</a-input>
				</a-form-item>
				<a-form-item>
					<a-input
						v-model:value="formInline.password"
						size="large"
						type="password"
						placeholder="请输入密码"
						autocomplete="new-password"
					>
						<template #prefix><LockOutlined /></template>
					</a-input>
				</a-form-item>
				<a-form-item>
					<a-checkbox v-model:checked="formInline.remember">记住我</a-checkbox>
				</a-form-item>
				<a-form-item>
					<a-button type="primary" html-type="submit" size="large" :loading="loading" block> 登录 </a-button>
				</a-form-item>
			</a-form>
		</div>
	</div>
</template>

<script lang="ts">
import { defineComponent, reactive, toRefs } from 'vue';
import { message } from 'ant-design-vue';
import { UserOutlined, LockOutlined } from '@ant-design/icons-vue';
import md5 from 'blueimp-md5';
import { useRoute, useRouter } from 'vue-router';
import { useStore } from '@/store';
import { Storage } from '@/utils/Storage';
import { SvgIcon } from '@/components/svg-icon';
import { REMEMBER_ACCOUNT } from '@/store/mutation-types';
export default defineComponent({
	name: 'Login',
	components: { UserOutlined, LockOutlined, SvgIcon },
	setup() {
		const rememberInfo = Storage.get(REMEMBER_ACCOUNT, {
			username: '',
			password: ''
		});
		const state = reactive({
			loading: false,
			formInline: {
				...rememberInfo,
				remember: !!rememberInfo.username
			}
		});

		const store = useStore();
		const router = useRouter();
		const route = useRoute();

		const handleSubmit = async () => {
			const { username, password, remember } = state.formInline;
			if (username.trim() == '' || password.trim() == '') {
				return message.warning('用户名或密码不能为空！');
			}
			state.loading = true;
			const params = {
				username,
				password: md5(password)
			};
			const res = await store.dispatch('user/login', params).finally(() => {
				state.loading = false;
			});

			if (res.code === 200) {
				if (remember) {
					Storage.set(REMEMBER_ACCOUNT, {
						username,
						password
					});
				} else {
					Storage.remove(REMEMBER_ACCOUNT);
				}
				message.success('登录成功！');
				let toPath = decodeURIComponent((route.query?.redirect || '/') as string);
				toPath = toPath === '404' ? '/' : toPath;
				router.replace(toPath);
			} else {
				message.error(res.message);
			}
		};
		return {
			...toRefs(state),
			handleSubmit
		};
	}
});
</script>

<style lang="scss" scoped>
.login_container {
	position: fixed;
	top: 0;
	left: 0;
	width: 100vw;
	height: 100vh;
	padding-top: 240px;
	background: url('~@/assets/images/login_bg.jpg');
	background-position: 50%;
	background-size: cover;
}
.project-title{
	color:#3c8882;
	font-size: 28px;
}
.login_content {
	position: absolute;
	top: 50%;
	right: 160px;
	width: 300px;
	padding: 0 20px 10px;
	background: #fff;
	border-radius: 4px;
	transform: translateY(-60%);

	.logo_container {
		display: flex;
		padding: 24px 0;

		img {
			height: 30px;
		}
	}

	:deep(.ant-form) {
		.ant-col {
			width: 100%;
		}

		.ant-form-item {
			margin-bottom: 20px;
		}

		.ant-form-item-label {
			padding-right: 6px;
		}
	}
}
</style>
