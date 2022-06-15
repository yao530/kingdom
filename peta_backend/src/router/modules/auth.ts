const modulesFiles = require.context('@/views/auth', true, /\.vue$/);
const componentDict = {};
modulesFiles.keys().forEach((path) => {
	if (!path.endsWith('/index.vue')) return;
	const match = path.match(/.\/(.*).index.vue/);
	if (match) {
		const routeHash = match[1];
		componentDict[routeHash] = () => import(`@/views/auth${path.slice(1)}`);
	}
});

export default componentDict;
