package com.pk.main.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Exception class for catching exception raised while data insertion in DB.
 * 
 * @author PranaySK
 */

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
@Data
@EqualsAndHashCode(callSuper = false)
public class DataInsertionException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private String errorCode;

	public DataInsertionException(String errorCode, String exMsg) {
		super(exMsg);
		this.errorCode = errorCode;
	}

}
