import type { App } from 'vue';
// import { AButton } from '@/components/button/index';
import {
	Modal,
	Upload,
	Spin,
	Tree,
	TreeSelect,
	Table,
	Menu,
	Input,
	InputNumber,
	Form,
	Card,
	Checkbox,
	Radio,
	Col,
	Row,
	Select,
	DatePicker,
	Tag,
	Divider,
	Cascader,
	Popconfirm,
	Button
} from 'ant-design-vue';
import 'ant-design-vue/dist/antd.less';

export function setupAntd(app: App<Element>) {
	// app.component('AButton', AButton);
	app.use(Form)
		.use(Upload)
		.use(Spin)
		.use(Tree)
		.use(TreeSelect)
		.use(Input)
		.use(InputNumber)
		.use(Modal)
		.use(Table)
		.use(Menu)
		.use(Card)
		.use(Checkbox)
		.use(Radio)
		.use(Col)
		.use(Row)
		.use(Select)
		.use(DatePicker)
		.use(Tag)
		.use(Divider)
		.use(Cascader)
		.use(Popconfirm)
		.use(Button);
}
