package com.fable.outer.biz.exception;

@SuppressWarnings("serial")
public class CommonWebException extends RuntimeException {

	public CommonWebException() {
		super();
	}

	public CommonWebException(String message, Throwable cause) {
		super(message, cause);
	}

	public CommonWebException(String message) {
		super(message);
	}

	public CommonWebException(Throwable cause) {
		super(cause);
	}

}
