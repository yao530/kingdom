import { h } from 'vue';
import { IConstantItemDict } from '@/interface/intefaces';
import dayjs from 'dayjs';
function formatTimeRender({ text, index }) {
	return formatTimeRender.set()({ text, index });
}
formatTimeRender.set = function (tem = 'YYYY-MM-DD HH:mm:ss') {
	return ({ text, index }) => {
		return h('span', {}, text ? dayjs(text).format(tem) : '-');
	};
};

export default formatTimeRender;
