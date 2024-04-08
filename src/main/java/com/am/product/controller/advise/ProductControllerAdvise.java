package com.am.product.controller.advise;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.MissingRequestValueException;
import org.springframework.web.server.ServerWebInputException;

import com.am.product.exception.ProductNotFoundException;
import com.am.product.responses.ErrorResponse;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class ProductControllerAdvise {

	private static final String FAILURE = "Failure";

	@ExceptionHandler(ProductNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ErrorResponse handleProductNotFoundException(ProductNotFoundException exception) {
		logException(exception);
		var rresponse = generateErrorResponse(FAILURE, Arrays.asList(exception.getMessage()));
		rresponse.setProductCode(exception.getProductCode());

		return rresponse;
	}

//	@ExceptionHandler(DuplicateKeyException.class)
//	@ResponseStatus(HttpStatus.BAD_REQUEST)
//	public ErrorResponse handleDuplicateKeyException(DuplicateKeyException exception) {
//		logException(exception);
//		return generateErrorResponse(FAILURE, Arrays.asList("Product Already Exists"));
//
//	}

	@ExceptionHandler(MissingRequestValueException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorResponse handleMissingRequestValueException(MissingRequestValueException exception) {
		logException(exception);
		return generateErrorResponse(FAILURE, Arrays.asList(exception.getReason()));
	}

	@ExceptionHandler(ServerWebInputException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorResponse handleInvalidInputException(ServerWebInputException exception) {
		logException(exception);
		return generateErrorResponse(FAILURE, Arrays.asList(exception.getReason(), exception.getCause().toString()));
	}

	@ExceptionHandler(WebExchangeBindException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorResponse handleInvalidInputExceptionValidation(WebExchangeBindException exception) {
		logException(exception);
		List<String> errorList = null;
		if (!CollectionUtils.isEmpty(exception.getAllErrors())) {
			errorList = exception.getAllErrors().stream().map(ObjectError::getDefaultMessage).toList();
		}

		return generateErrorResponse(FAILURE, errorList);
	}

	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public Object handleException(Exception exception) {
		logException(exception);
		return generateErrorResponse(FAILURE, Arrays.asList(exception.getMessage()));

	}

	/**
	 * @param exception
	 * @return
	 */
	private <T extends Throwable> void logException(T exception) {
		log.error("Exception in Processing", exception);
	}

	/**
	 * @return
	 */
	private ErrorResponse generateErrorResponse(String status, List<String> errors) {
		return ErrorResponse.builder().status(status).error(errors).build();
	}

}
