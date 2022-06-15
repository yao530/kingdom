export interface IRouteMeta {
	title: string;
	icon: string;
	authStatus: number;
	hidden: boolean;
}

export interface IRouteInfo {
	meta?: IRouteMeta;
	name: string;
	children?: IRouteInfo[];
}
