export interface IDict {
	[value: number | string]: string;
}
export interface IConstantDict extends Map<string | number, string | number> {}
export interface IConstantsDict extends Map<string, IConstantDict> {}
export interface IConstantItem {
	value: number | string;
	describe: string;
}
export interface IConstantItemDict {
	[value: string]: string;
}
export interface IConstantItemValueDict {
	[value: string]: string | undefined;
}
