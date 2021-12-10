package com.revature.exception;

public class ReimbursemntIdNotFoundException extends Exception {

	public ReimbursemntIdNotFoundException() {
		super();
	}

	public ReimbursemntIdNotFoundException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ReimbursemntIdNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public ReimbursemntIdNotFoundException(String message) {
		super(message);
	}

	public ReimbursemntIdNotFoundException(Throwable cause) {
		super(cause);
	}

}
