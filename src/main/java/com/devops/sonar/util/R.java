

package com.devops.sonar.util;

import com.devops.sonar.constant.ResultCode;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 响应信息主体
 *
 * @param <T>
 * @author
 */
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class R<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	private long code;

	@Getter
	@Setter
	private String msg;

	@Getter
	@Setter
	private T data;

	public static <T> R<T> ok() {
		return restResult(null, ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage());
	}

	public static <T>R<T> ok(T data) {
		return restResult(data, ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage());
	}

	public static <T> R<T> ok(T data, String msg) {
		return restResult(data, ResultCode.SUCCESS.getCode(), msg);
	}

	public static <T> R<T> failed() {
		return restResult(null, ResultCode.FAILED.getCode(), ResultCode.FAILED.getMessage());
	}

	public static <T> R<T> failed(String msg) {
		return restResult(null, ResultCode.FAILED.getCode(), msg);
	}

	public static <T> R<T> failed(T data) {
		return restResult(data, ResultCode.FAILED.getCode(), ResultCode.FAILED.getMessage());
	}

	public static <T> R<T> failed(T data, String msg) {
		return restResult(data, ResultCode.FAILED.getCode(), msg);
	}


	/**
	 * 参数验证失败返回结果
	 * @param message 提示信息
	 */
	public static <T> R<T> validateFailed(String message) {
		return new R<T>(ResultCode.VALIDATE_FAILED.getCode(), message, null);
	}

	/**
	 * 未登录返回结果
	 */
	public static <T> R<T> unauthorized(T data) {
		return new R<T>(ResultCode.UNAUTHORIZED.getCode(), ResultCode.UNAUTHORIZED.getMessage(), data);
	}

	/**
	 * 未授权返回结果
	 */
	public static <T> R<T> forbidden(T data) {
		return new R<T>(ResultCode.FORBIDDEN.getCode(), ResultCode.FORBIDDEN.getMessage(), data);
	}

	private static <T> R<T> restResult(T data, long code, String msg) {
		R<T> apiResult = new R<>();
		apiResult.setCode(code);
		apiResult.setData(data);
		apiResult.setMsg(msg);
		return apiResult;
	}

}
