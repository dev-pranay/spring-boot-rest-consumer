package com.pk.main.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Exception class for catching exception raised while performing DB operations.
 * 
 * @author PranaySK
 */

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
@Data
@EqualsAndHashCode(callSuper = false)
public class DbException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private String errorCode;

	public DbException(String errorCode, String exMsg) {
		super(exMsg);
		this.errorCode = errorCode;
	}

}
