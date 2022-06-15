import { Tag } from 'ant-design-vue';
import { h } from 'vue';
import BooleanTag from '@/components/BooleanTag/index.vue';
function booleanRender({ text, index }) {
	return booleanRender.set()({ text, index });
}

booleanRender.set = function (activeText = '已认证', disableText = '未认证') {
	return ({ text, index }) => {
		const has = !!text;
		return h(BooleanTag, {
			color: has ? 'green' : undefined,
			text: has ? activeText : disableText
		});
	};
};
export default booleanRender;
