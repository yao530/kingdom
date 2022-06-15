import { IConstantItem, IConstantItemDict, IConstantItemValueDict } from '@/interface/intefaces';

const constantMap = {
	// 菜单权限
	MenuAuthStatus: [
		{ value: 0, describe: '不可见' },
		{ value: 1, describe: '读' },
		{ value: 2, describe: '可读写' }
	],
	// 账号状态
	AccountStatus: [
		{ value: 0, describe: '禁用' },
		{ value: 1, describe: '可用' }
	],
	// banner类型
	BannerType: [
		{ value: 1, describe: '纯图片' },
		{ value: 2, describe: '图文详情' },
		{ value: 3, describe: '网址跳转' },
		{ value: 4, describe: '其他' }
	],
	LocationType: [
		{ value: 1, describe: '首页' }
	],
	// 性别类型
	SexType: [
		{ value: 1, describe: '男' },
		{ value: 0, describe: '女' }
	],
	// 性别类型
	CharacterRoleType: [
		{ value: 1, describe: '平台角色' },
		{ value: 2, describe: '职位角色' }
	],
	StoryType: [
		{ value: 1, describe: '王国故事' },
		{ value: 2, describe: '宠物故事' },
		{ value: 3, describe: '番外故事' }
	],
	NftType: [
		{ value: 1, describe: '故事脚本NFT' },
		{ value: 2, describe: '故事背景NFT' },
		{ value: 3, describe: 'AvatarNFT' },
		{ value: 4, describe: '叙事事件NFT' },
		{ value: 5, describe: '叙事道具NFT' },
		{ value: 6, describe: '叙事权益NFT' },
		{ value: 7, describe: '叙事音乐NFT' },
		{ value: 8, describe: '叙事彩蛋NFT' }
	],
	NftOpenType: [
		{ value: 1, describe: '普通开启' },
		{ value: 2, describe: '盲盒开启' }
	],
	MainDisplayType: [
		{ value: 0, describe: '否' },
		{ value: 1, describe: '是' }
	],
	SoldType: [
		{ value: 1, describe: '限量空投' },
		{ value: 2, describe: '预约抽签' },
		{ value: 3, describe: '限量抢购' },
		{ value: 4, describe: '定向赠送' }
	],
	AirDropUserAuthType: [
		{ value: 1, describe: '所有注册实名用户' },
		{ value: 2, describe: '活动时间段内新注册用户' },
		{ value: 3, describe: '活动时间段内登录用户' },
		{ value: 4, describe: '活动时间段邀请用户与被邀请新用户同时有' }

	],
	PayType: [
		{ value: 1, describe: '微信H5' },
		{ value: 2, describe: '微信APP' },
		{ value: 3, describe: '支付宝H5' },
		{ value: 4, describe: '支付宝APP' }
	],
	OrderStatus: [
		{ value: 0, describe: '待支付' },
		{ value: 1, describe: '已支付' },
		{ value: 2, describe: '已支付' },
		{ value: 3, describe: '其他' }
	],
	PlatFormSettingType: [
		{ value: 1, describe: '关于我们' },
		{ value: 2, describe: '用户注册协议' },
		{ value: 3, describe: '隐私协议' },
		{ value: 4, describe: '公告' }
	],
	// 销售状态
	SoldStatus: [
		{ value: 0, describe: '待开始' },
		{ value: 1, describe: '活动中' },
		{ value: 2, describe: '抽签中' },
		{ value: 3, describe: '热销中' },
		{ value: 4, describe: '已售馨' }
	],

	// 创作者审核状态
	CreatorStatus: [
		{ value: 0, describe: '无效' },
		{ value: 1, describe: '待审核' },
		{ value: 2, describe: '审核通过' },
		{ value: 3, describe: '审核不通过' }
	],
	// 审核状态
	CheckStatus: [
		{ value: 0, describe: '待审核' },
		{ value: 1, describe: '审核通过' },
		{ value: 2, describe: '审核不通过' }
	],
	MintStatus: [
		{ value: 0, describe: '待铸造' },
		{ value: 1, describe: '铸造中' },
		{ value: 2, describe: '已铸造' },
		{ value: 3, describe: '铸造失败' }
	],
	PublisStatus: [
		{ value: 0, describe: '待上架' },
		{ value: 1, describe: '已上架' },
		{ value: 2, describe: '已下架' }
	],
	DropAuthType:[
		{ value: 1, describe: '参加即可' },
		{ value: 2, describe: '按抽签' },
		{ value: 3, describe: '按邀请排名' },
		{ value: 4, describe: '按完成邀请先后' }
	],
	MetaType: [
		{ value: 1, describe: '主宇宙' },
		{ value: 2, describe: '故事宇宙' }
	],

};
type TConstantName = keyof typeof constantMap;
interface IConstantProperty {
	list: IConstantItem[];
	dict: IConstantItemDict;
	valueDict: IConstantItemValueDict;
}
type TModel = {
	[p in TConstantName]: IConstantProperty;
};

var _obj: TModel = <TModel>{};
for (let constantName in constantMap) {
	const statuses = constantMap[constantName];
	const value_dict = new Map();
	_obj[constantName] = {
		list: statuses,
		dict: {},
		valueDict: {}
	};
	statuses.forEach((status) => {
		_obj[constantName].dict[status.value] = status.describe;
		_obj[constantName].valueDict[status.value] = status.value;
	});
}

export default _obj;
