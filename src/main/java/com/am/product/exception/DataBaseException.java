package com.am.product.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DataBaseException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4284533800018792889L;

	public DataBaseException() {
		super();
	}

	public DataBaseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		log.error("Exception: {}: {}", message, cause.getStackTrace());
	}

	public DataBaseException(String message, Throwable cause) {
		super(message, cause);
		log.error("Exception: {}: {}", message, cause.getStackTrace());
	}

	public DataBaseException(String message) {
		super(message);
		log.error("Exception: {}", message);
	}

	public DataBaseException(Throwable cause) {
		super(cause);
		log.error("Exception: {} : {}", cause.getMessage(), cause.getStackTrace());
	}

}
