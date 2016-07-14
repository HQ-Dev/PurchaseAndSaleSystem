package com.archy.test.meta;

public class Json {

	private int code;
	private String message;
	private Boolean result;
	
	public Json() {};
	
	public Json(int code, String message,Boolean result) {
		this.code  =code;
		this.message = message;
		this.result = result;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Boolean getResult() {
		return result;
	}

	public void setResult(Boolean result) {
		this.result = result;
	}

	
}
