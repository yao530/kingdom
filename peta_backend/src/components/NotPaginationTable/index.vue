<template>
	<a-table
		:dataSource="dataSource"
		:columns="viewColumns"
		:rowKey="rowKey"
		:childrenColumnName="childrenColumnName"
		:loading="loading"
		:defaultExpandAllRows="defaultExpandAllRows"
		:pagination="false"
	>
		<template #action="{ record }">
			<slot :record="record" name="extraActon"></slot>
			<template v-if="dUrl">
				<a-popconfirm title="确定删除?" @confirm="onDelete(record)">
					<a-button size="small" type="link">删除</a-button>
				</a-popconfirm>
				<a-divider type="vertical" />
			</template>
			<a-button size="small" type="link" @click="handleEdit(record)">编辑</a-button>
		</template>
	</a-table>
</template>
<script lang="ts">
import { defineComponent, PropType, ref, SetupContext, onMounted, nextTick, Prop } from 'vue';
import http from '@/utils/http/axios';
import { message, TableColumn } from 'ant-design-vue';
import { Modal } from 'ant-design-vue';
import { number } from 'echarts';

export default defineComponent({
	props: {
		columns: {
			type: Array,
			default() {
				return [];
			}
		} as Prop<TableColumn[]>,
		rUrl: {
			type: String
		},
		actionWidth: {
			type: Number,
			default: 140
		},
		dUrl: {
			type: String
		},
		rowKey: {
			type: String,
			default: 'id'
		},
		childrenColumnName: {
			type: String,
			default: 'children'
		},
		defaultExpandAllRows: {
			type: Boolean,
			default: true
		}
	},
	emits: ['onEdit'],
	setup(props, ctx) {
		console.log('ctx-----', ctx.slots);
		const loading = ref<boolean>(false);
		const dataSource = ref([]);
		const viewColumns: TableColumn[] = [].concat(props.columns as []);
		viewColumns.push({
			title: '操作',
			key: 'id',
			dataIndex: 'id',
			width: props.actionWidth,
			slots: { customRender: 'action' }
		});
		async function getData() {
			loading.value = true;
			const data = await http
				.request({
					url: props.rUrl,
					method: 'POST'
				})
				.finally(() => {
					loading.value = false;
				});
			dataSource.value = data;
		}
		async function onDelete(record) {
			await http
				.request({
					url: props.dUrl,
					params: {
						id: record.id
					}
				})
				.finally(() => {
					loading.value = false;
				});
			message.success('操作成功');
			getData();
		}
		function handleEdit(record) {
			ctx.emit('onEdit', record);
		}
		onMounted(getData);
		return {
			dataSource,
			loading,
			viewColumns,
			getData,
			handleEdit,
			onDelete
		};
	}
});
</script>
