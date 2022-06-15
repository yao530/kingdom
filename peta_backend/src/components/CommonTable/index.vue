<template>
	<a-table
		:dataSource="dataSource"
		:columns="viewColumns"
		:rowKey="rowKey"
		:childrenColumnName="childrenColumnName"
		:loading="loading"
		:defaultExpandAllRows="defaultExpandAllRows"
		:pagination="pageProperty"
		@change="onChange"
	>
		<template #action="{ record }">
			<slot :record="record" name="extraActon"></slot>
			<template v-if="removeUrl">
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
import { defineComponent, PropType, ref, toRefs, SetupContext, onMounted, nextTick, Prop, reactive, watch } from 'vue';
import http from '@/utils/http/axios';
import { message } from 'ant-design-vue';
import { PaginationProps } from 'ant-design-vue';
import IColumn, { IColumnOption } from '@/interface/FE/column';
import { hasPermissionCurrentRoute } from '@/utils/permission/hasPermission';

interface IState {
	pageProperty: boolean | PaginationProps;
	loading: boolean;
	dataSource: any[];
	viewColumns: any[];
	filter: {
		[keyName: string]: string | number;
	};
}

export default defineComponent({
	name: 'CommonTable',
	props: {
		columns: {
			type: Array,
			default() {
				return [];
			}
		} as Prop<TableColumn[]>,
		removeUrl: {
			type: String
		},
		listUrl: {
			type: String
		},
		pageUrl: {
			type: String
		},
		actionWidth: {
			type: Number,
			default: 140
		},
		rowKey: {
			default: 'id',
			type: String
		},
		extraParams: {
			type: Object,
			default() {
				return {};
			}
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
	emits: ['onEdit', 'dataSourceUpdate'],
	setup(props: any, ctx) {
		const srcColumns = props.columns as [];
		const isPage = !!props.pageUrl;

		const state = reactive<IState>({
			loading: false,
			dataSource: [],
			viewColumns: [],
			pageProperty: false,
			filter: {}
		});

		function initPageProperty() {
			state.pageProperty = {
				current: 1,
				pageSize: 10,
				total: 0,
				showTotal(total) {
					return `共${total}项 `;
				}
			};
		}
		async function getData() {
			state.loading = true;
			const url = isPage ? props.pageUrl : props.listUrl;
			let params: any = {};
			if (isPage) {
				params = {
					pageSize: (state.pageProperty as PaginationProps).pageSize,
					pageNumber: (state.pageProperty as PaginationProps).current
				};
			}
			if (props.extraParams) {
				Object.assign(params, state.filter, props.extraParams);
			}
			const data = await http
				.request({
					url,
					method: 'POST',
					params
				})
				.finally(() => {
					state.loading = false;
				});
			if (isPage) {
				state.dataSource = data.records || [];
				(state.pageProperty as PaginationProps).pageSize = data.size;
				(state.pageProperty as PaginationProps).total = data.total;
				(state.pageProperty as PaginationProps).current = data.current;
			} else {
				state.dataSource = data;
			}
			ctx.emit('dataSourceUpdate', state.dataSource);
		}
		async function onDelete(record) {
			await http
				.request({
					url: props.removeUrl,
					params: {
						id: record.id
					}
				})
				.finally(() => {
					state.loading = false;
				});
			message.success('操作成功');
			getData();
		}
		function handleEdit(record) {
			ctx.emit('onEdit', record);
		}
		function onChange(pagination, filters, sorter, { currentDataSource }) {
			if (isPage && pagination && pagination.current !== undefined) {
				(state.pageProperty as PaginationProps).pageSize = pagination.pageSize;
				(state.pageProperty as PaginationProps).current = pagination.current;
			}
			state.filter = {};
			for (let keyName in filters) {
				state.filter[keyName] = filters[keyName][0];
			}
			getData();
		}
		async function getViewColumns() {
			const viewColumns: TableColumn[] = [];
			for (let index = 0; index < srcColumns.length; index++) {
				const scrCol: IColumn = srcColumns[index];
				const viewCol: any = Object.assign({}, scrCol);
				if (!!viewCol.asyncOptions) {
					try {
						viewCol.options = await viewCol.asyncOptions();
					} catch (err) {}
				}
				if (viewCol.options) {
					viewCol.filters = viewCol.options.map((item) => {
						return {
							text: item.label,
							value: item.value
						};
					});
				}
				viewCol.filterMultiple = false;
				viewColumns.push(viewCol);
			}
			if (hasPermissionCurrentRoute(2)) {
				viewColumns.push({
					title: '操作',
					key: 'id',
					dataIndex: 'id',
					width: props.actionWidth,
					slots: { customRender: 'action' }
				});
			}

			state.viewColumns = viewColumns;
		}
		watch(
			() => props.extraParams,
			() => {
				if (state.pageProperty) {
					(state.pageProperty as PaginationProps).current = 1;
				}
				getData();
			}
		);
		onMounted(() => {
			if (isPage) {
				initPageProperty();
			}
			getData();
			setTimeout(getViewColumns, 100);
		});
		return {
			...toRefs(state),
			getData,
			handleEdit,
			onDelete,
			onChange
		};
	}
});
</script>
