//
// /********************************自动导包 start********************************/
// const file = require.context('./', true, /index.ts$/);
// const modules = {};
// file.keys().forEach((key) => {
//     const name = key.replace(/(.\/)*([^/]+).*/ig,"$2")
//     modules[name] = file(key).default
// });
// /********************************自动导包 end********************************/
//
// export default modules
import asyncRoute from './async-route';
import user from './user';
import lockscreen from './lockscreen';
import currentAccount from './currentAccount';

export default {
	asyncRoute,
	user,
	lockscreen,
	currentAccount
};
