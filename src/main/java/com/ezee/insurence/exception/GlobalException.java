package com.ezee.insurence.exception;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.ezee.insurence.controller.IO.ResponseIO;

@ControllerAdvice
public class GlobalException {
	private static final Logger logger = LogManager.getLogger(GlobalException.class);

	@ExceptionHandler(ServiceException.class)
	public ResponseEntity<Object> handleServiceException(ServiceException e) {
		ResponseIO<Object> response;
		if (e.getErrorCode() != null) {
			response = ResponseIO.failure(e.getErrorCode(), e.getData());
		} else {
			response = ResponseIO.failure("500", e.getMessage());
		}
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> handleNullPointerException(NullPointerException e, WebRequest request) {
		logger.error("Defensive check issue - Null Pointer Access - ", e);
		e.printStackTrace();
		ResponseIO<Object> response = ResponseIO.failure(ErrorCode.UNDEFINE_EXCEPTION);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
}
