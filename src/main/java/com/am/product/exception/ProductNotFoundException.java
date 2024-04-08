package com.am.product.exception;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class ProductNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4849848342777687720L;
	
	private String productCode;

	public ProductNotFoundException(String productCode) {
		this("Product Not Found In Database", productCode);
	}

	public ProductNotFoundException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		log.error("Exception: {}: {}", message, cause.getStackTrace());
	}

	public ProductNotFoundException(String message, Throwable cause) {
		super(message, cause);
		log.error("Exception: {}: {}", message, cause.getStackTrace());
	}

	public ProductNotFoundException(String message, String productCode) {
		super(message);
		this.productCode = productCode;
		log.error("Exception: {}", message);
	}

	public ProductNotFoundException(Throwable cause) {
		super(cause);
		log.error("Exception: {} : {}", cause.getMessage(), cause.getStackTrace());
	}
}
