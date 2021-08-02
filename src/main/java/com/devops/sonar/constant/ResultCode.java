package com.devops.sonar.constant;

/**
 * 多状态码自定义返回
 * 大家可以随意自定义枚举扩展
 */
public enum ResultCode {
	SUCCESS(1, "报警成功"),
	FAILED(0, "报警失败"),
	VALIDATE_FAILED(400, "参数检验失败"),
	UNAUTHORIZED(401, "暂未登录或token已经过期"),
	FORBIDDEN(403, "没有相关权限");
	private long code;
	private String message;

	private ResultCode(long code, String message) {
		this.code = code;
		this.message = message;
	}

	public long getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}
}
