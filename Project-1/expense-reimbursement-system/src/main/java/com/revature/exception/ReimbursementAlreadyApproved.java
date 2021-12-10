package com.revature.exception;

public class ReimbursementAlreadyApproved extends Exception {

	public ReimbursementAlreadyApproved() {
		super();
	}

	public ReimbursementAlreadyApproved(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ReimbursementAlreadyApproved(String message, Throwable cause) {
		super(message, cause);
	}

	public ReimbursementAlreadyApproved(String message) {
		super(message);
	}

	public ReimbursementAlreadyApproved(Throwable cause) {
		super(cause);
	}

}
