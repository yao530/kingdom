import { h } from 'vue';
import { IConstantItemDict } from '@/interface/intefaces';
export default function (dict: IConstantItemDict) {
	return ({ text, index }) => {
		return h('span', {}, dict[text]);
	};
}
