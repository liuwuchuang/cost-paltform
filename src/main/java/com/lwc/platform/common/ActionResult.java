package com.lwc.platform.common;

public class ActionResult {
	
	/**
	 * 200 : 请求成功
	 * 400 : 参数错误
	 * 405 : 转到登录
	 */
	private int code;
	
	private String msg;
	
	private Object result;

	public ActionResult() {
		super();
	}

	public ActionResult(int code, String msg, Object result) {
		super();
		this.code = code;
		this.msg = msg;
		this.result = result;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}
}
