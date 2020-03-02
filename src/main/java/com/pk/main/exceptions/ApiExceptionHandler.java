package com.pk.main.exceptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.pk.main.constants.AppConstants;
import com.pk.main.constants.ErrorConstants;

/**
 * Global Exception Handler class for catching exceptions raised in application.
 * 
 * @author PranaySK
 */

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

	private Logger log = LoggerFactory.getLogger(ApiExceptionHandler.class);

	@Override
	protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		Map<String, Object> responseBody = new HashMap<>();
		responseBody.put(AppConstants.SUCCESS, false);
		responseBody.put(AppConstants.ERROR_CODE, ErrorConstants.INVALID_DATA);
		responseBody.put(AppConstants.ERROR, ex.getParameterName() + " parameter is missing");
		responseBody.put(AppConstants.MESSAGE, ex.getMessage());
		log.error(ex.getMessage(), ex);
		return new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(Exception.class)
	public final ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) {
		List<String> details = new ArrayList<>();
		details.add(ex.getLocalizedMessage());
		Map<String, Object> responseBody = new HashMap<>();
		responseBody.put(AppConstants.SUCCESS, false);
		responseBody.put(AppConstants.ERROR_CODE, ErrorConstants.INTERNAL_SERVER_ERROR);
		responseBody.put(AppConstants.ERROR, ex.getLocalizedMessage());
		responseBody.put(AppConstants.MESSAGE, ex.getMessage());
		log.error(ex.getMessage(), ex);
		return new ResponseEntity<>(responseBody, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public final ResponseEntity<Object> handleMethodArgumentTypeMismatchException(
			MethodArgumentTypeMismatchException ex, WebRequest request) {
		Map<String, Object> responseBody = new HashMap<>();
		responseBody.put(AppConstants.SUCCESS, false);
		responseBody.put(AppConstants.ERROR_CODE, ErrorConstants.TYPE_MISMATCH);
		responseBody.put(AppConstants.MESSAGE, ex.getMessage());
		log.error(ex.getMessage(), ex);
		return new ResponseEntity<>(responseBody, HttpStatus.NOT_ACCEPTABLE);
	}

	@ExceptionHandler(DataInsertionException.class)
	public final ResponseEntity<Object> handleDataInsertionException(DataInsertionException ex, WebRequest request) {
		Map<String, Object> responseBody = new HashMap<>();
		responseBody.put(AppConstants.SUCCESS, false);
		responseBody.put(AppConstants.ERROR_CODE, ex.getErrorCode());
		responseBody.put(AppConstants.MESSAGE, ex.getMessage());
		log.error(ex.getMessage(), ex);
		return new ResponseEntity<>(responseBody, HttpStatus.EXPECTATION_FAILED);
	}

	@ExceptionHandler(DbException.class)
	public final ResponseEntity<Object> handleDbException(DbException ex, WebRequest request) {
		Map<String, Object> responseBody = new HashMap<>();
		responseBody.put(AppConstants.SUCCESS, false);
		responseBody.put(AppConstants.ERROR_CODE, ex.getErrorCode());
		responseBody.put(AppConstants.MESSAGE, ex.getMessage());
		log.error(ex.getMessage(), ex);
		return new ResponseEntity<>(responseBody, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	public static String getPath(WebRequest request) {
		return ((ServletWebRequest) request).getRequest().getRequestURL().toString();
	}

}