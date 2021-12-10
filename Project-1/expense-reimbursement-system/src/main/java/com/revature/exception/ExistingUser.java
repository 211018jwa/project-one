package com.revature.exception;

public class ExistingUser extends Exception {
	
	public ExistingUser() {
		super();
	}

	public ExistingUser(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		
	}

	public ExistingUser(String message, Throwable cause) {
		super(message, cause);
		
	}

	public ExistingUser(String message) {
		super(message);
	}

	public ExistingUser(Throwable cause) {
		super(cause);
	}
	
}
