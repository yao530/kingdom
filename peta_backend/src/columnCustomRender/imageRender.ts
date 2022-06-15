import { h } from 'vue';
import { Modal } from 'ant-design-vue';

function imageRender({ text, index }) {
	return imageRender.set()({ text, index });
}
imageRender.set = function (style?) {
	style = Object.assign(
		{
			width: '100px',
			cursor: 'pointer'
		},
		style
	);
	return ({ text, index }) => {
		return h('img', {
			src: text,
			style,
			onClick: () => {
				Modal.info({
					content: h('img', {
						style: {
							width: '100%'
						},
						src: text
					})
				});
			}
		});
	};
};

export default imageRender;
