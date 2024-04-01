package com.amway.product.controller.advise;

import java.util.Arrays;
import java.util.List;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.MissingRequestValueException;
import org.springframework.web.server.ServerWebInputException;

import com.amway.product.exception.ProductNotFoundException;
import com.amway.product.responses.ErrorResponse;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@RestControllerAdvice
@Slf4j
public class ProductControllerAdvise {

	private static final String FAILURE = "Failure";

	@ExceptionHandler(ProductNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public Mono<ErrorResponse> handleProductNotFoundException(ProductNotFoundException exception) {
		logException(exception);
		var response = generateErrorResponse(FAILURE, Arrays.asList(exception.getMessage()));
		response.setProductCode(exception.getProductCode());

		return Mono.just(response);
	}

	@ExceptionHandler(DuplicateKeyException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public Mono<ErrorResponse> handleDuplicateKeyException(DuplicateKeyException exception) {
		logException(exception);
		var response = generateErrorResponse(FAILURE, Arrays.asList("Product Already Exists"));

		return Mono.just(response);
	}


	@ExceptionHandler(MissingRequestValueException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public Mono<ErrorResponse> handleMissingRequestValueException(MissingRequestValueException exception) {
		logException(exception);
		var response = generateErrorResponse(FAILURE, Arrays.asList(exception.getReason()));
		return Mono.just(response);
	}
	
	@ExceptionHandler(ServerWebInputException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public Mono<ErrorResponse> handleInvalidInputException(ServerWebInputException exception) {
		logException(exception);
		var response = generateErrorResponse(FAILURE, Arrays.asList(exception.getReason(), exception.getCause().toString()));
		return Mono.just(response);
	}

	@ExceptionHandler(WebExchangeBindException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public Mono<ErrorResponse> handleInvalidInputExceptionValidation(WebExchangeBindException exception) {
		logException(exception);
		List<String> errorList = null;
		if (!CollectionUtils.isEmpty(exception.getAllErrors())) {
			 errorList = exception.getAllErrors().stream().map(ObjectError::getDefaultMessage).toList();
		}

		return Mono.just(generateErrorResponse(FAILURE, errorList));
	}
	
	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public Mono<Object> handleException(Exception exception) {
		logException(exception);
		var response = generateErrorResponse(FAILURE, Arrays.asList(exception.getMessage()));

		return Mono.just(response);
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
