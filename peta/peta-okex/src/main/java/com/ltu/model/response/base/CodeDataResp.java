package com.ltu.model.response.base;


import com.ltu.base.State;
import com.ltu.model.response.CodeResp;
import io.swagger.annotations.ApiModelProperty;

public class CodeDataResp<T> extends CodeResp {
	protected T data;

	@ApiModelProperty(value = "sessionId", notes = "用于无法使用浏览器session的情况，例如小程序")
	protected String sessionId;

	public CodeDataResp() {
		super();
	}

	public CodeDataResp(State state) {
		super(state);
	}

	public CodeDataResp(Integer code, String message) {
		super(code, message);
	}
	
	public CodeDataResp(Integer code, String message, T data) {
		super(code, message);
		this.data = data;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	/***
	 * 请求成功的常用方法
	 * @param strData 传入字符串数据
	 * @return 响应体
	 */
	public static CodeDataResp valueOfSuccess(String strData) {
		CodeDataResp response = new CodeDataResp(State.SUCCESS);
		response.setData(strData);
		return response;
	}

	/***
	 * 请求成功的常用方法
	 * @param data 传入数据，可为空
	 * @return 响应体
	 */
	public static <T> CodeDataResp<T> valueOfSuccess(T data) {
		CodeDataResp<T> response = new CodeDataResp<T>(State.SUCCESS);
		response.setData(data);
		return response;
	}

	/***
	 * 请求成功的常用方法
	 * @return 响应体
	 */
	public static <T> CodeDataResp valueOfSuccessEmptyData() {
		CodeDataResp<T> response = new CodeDataResp<T>(State.SUCCESS);
		return response;
	}

	/**
	 * 请求失败的常用方法
	 * @param message 请求失败原因
	 * @return
	 */
	public static <T> CodeDataResp valueOfFailed(String message) {
		CodeDataResp<T> response = new CodeDataResp<T>(State.CODE_FAILED,message);
		return response;
	}

	/**
	 * 请求失败：记录不存在
	 * @return
	 */
	public static <T> CodeDataResp valueOfErrorNoRecord() {
		CodeDataResp<T> response = new CodeDataResp<T>(State.CODE_VALIDATE_FAILED,"Record not exists.");
		return response;
	}

	/**
	 * 请求失败：无权限
	 * @return
	 */
	public static <T> CodeDataResp valueOfErrorUnAuthorized() {
		CodeDataResp<T> response = new CodeDataResp<T>(State.CODE_FORBIDDEN,"无权限操作");
		return response;
	}

	/**
	 * 请求失败：登录过期
	 * @return
	 */
	public static <T> CodeDataResp valueOfErrorNotLogin() {
		CodeDataResp<T> response = new CodeDataResp<T>(State.CODE_UNAUTHORIZED,"未登录或登录过期");
		return response;
	}

	/**
	 * 请求失败：登录过期
	 * @return
	 */
	public static <T> CodeDataResp valueOfErrorInvalidArgs() {
		CodeDataResp<T> response = new CodeDataResp<T>(State.CODE_VALIDATE_FAILED,"参数错误");
		return response;
	}

	/**
	 * 请求失败：登录过期
	 * @return
	 */
	public static <T> CodeDataResp valueOfErrorUserBan() {
		CodeDataResp<T> response = new CodeDataResp<T>(State.CODE_USER_BAN,"用户被禁用");
		return response;
	}
}
