export interface IHourPoint {
	feeder_cabinet_name?: string;
	transformer_name?: string;
	date: string;
	hour: number;
	power: number;
}

export interface IRealTime {
	equip_name: string;
	datetime: string;

	Ua: number; // 相电压
	Ub: number;
	Uc: number;

	Ia: number; // 相电流
	Ib: number;
	Ic: number;

	T_P_F: number; // 功率因子

	Ia_THD: number; // 相谐波
	Ib_THD: number;
	Ic_THD: number;
}
export interface IElectricStructure {
	[name: string]: number;
}

export interface IInteriorAccount {
	CM_Electric: number; // 本月
	LM_Electric: number; // 上月
	CY_Electric: number; // 年度累计用电量
	Online_Qty: number; // 在线监测网点数
	On_Line_Qty: number; // 在线设备总数
	On_Data_Qty: number; // 监测数据总量

	'24h_FC_Load': IHourPoint[];
	'24h_TF_Load': IHourPoint[];

	ECC_Electric: IElectricStructure; // 成本中心本月用电占比
	Total_ECC_Electric: number; //ECC用电总计（用于计算占比）

	Dept_Electric: IElectricStructure; // 部门本月用电占比
	Total_Dept_Electric: number; // 部门总计

	Type_Electric: IElectricStructure; // 设备类别用电占比
	Total_Type_Electric: number; // 设备类别用电占比

	Realtime_Data: IRealTime[];

	On_Power: number; // 监测总电量
	Eh: number; // 峰时段电量
	Em: number; // 平时段电量
	El: number; // 谷时段电量
	Equip_type: {
		[type: string]: number;
	};
	Device_type: {
		[type: string]: number;
	};
	Dept_Electricity: {
		[dept: string]: number;
	};
}

export interface IInteriorHourData {
	Date: string;
	Hour: number;
	Power: number;
}

export type InteriorHoursType = IInteriorHourData[];

export type IRealTimeListType = IRealTime[];
