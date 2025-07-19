package com.ezee.insurence.controller.IO;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.ezee.insurence.exception.ErrorCode;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@SuppressWarnings("serial")
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseIO<T> implements Serializable {
	private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private int status;
	private String errorCode;
	private String errorDesc;
	private String datetime;
	private T data;

	public ResponseIO() {
		super();
	}

	public ResponseIO(T data) {
		super();
		this.data = data;
	}

	public static <T> ResponseIO<T> failure(ErrorCode error) {
		ResponseIO<T> response = new ResponseIO<T>();
		response.setStatus(0);
		response.setErrorDesc(error.getMessage());
		response.setErrorCode(error.getCode());
		response.setDatetime(dateFormat.format(new Date()));
		return response;
	}

	public static <T> ResponseIO<T> failure(ErrorCode error, T t) {
		ResponseIO<T> response = new ResponseIO<T>(t);
		response.setStatus(0);
		response.setErrorDesc(error.getMessage());
		response.setErrorCode(error.getCode());
		response.setDatetime(dateFormat.format(new Date()));
		return response;
	}

	public static <T> ResponseIO<T> failure(String errorCode, String errorMessage) {
		ResponseIO<T> response = new ResponseIO<T>();
		response.setStatus(0);
		response.setErrorCode(errorCode);
		response.setErrorDesc(errorMessage);
		response.setDatetime(dateFormat.format(new Date()));
		return response;
	}

	public static <T> ResponseIO<T> success(T t) {
		ResponseIO<T> response = new ResponseIO<T>(t);
		response.setStatus(1);
		response.setDatetime(dateFormat.format(new Date()));
		return response;
	}

	public static ResponseIO<BaseIO> success() {
		ResponseIO<BaseIO> response = new ResponseIO<BaseIO>();
		response.setStatus(1);
		response.setDatetime(dateFormat.format(new Date()));
		return response;
	}
}
