package com.playtomic.tests.wallet.exception;

import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;

@Getter
@Setter
public class BaseException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	private String statusCode;
	private List<String> arguments;
	
	public BaseException(String message, String... arguments) {
		super(message);
		this.arguments = Arrays.asList(arguments);
	}
	
	public BaseException(String message, Throwable cause, String... arguments) {
		super(message, cause);
		this.arguments = Arrays.asList(arguments);
	}	

}