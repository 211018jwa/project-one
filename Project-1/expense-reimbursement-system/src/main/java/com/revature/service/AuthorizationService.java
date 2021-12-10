package com.revature.service;

import com.revature.exception.UnauthorizedException;
import com.revature.model.Users;

public class AuthorizationService {
	
	public void authorizeEmployeeAndManager(Users user) throws UnauthorizedException {
		if(user == null || !(user.getRole().equals("employee") || user.getRole().equals("manager"))) {
			throw new UnauthorizedException("You must have a employee or manager role to access");
		}
	}
	
	public void authorizeManager(Users user) throws UnauthorizedException {
		if(user == null || !user.getRole().equals("manager")) {
			throw new UnauthorizedException("You must have a manager role to access");
		}
	}

	public void authorizeEmployee(Users user) throws UnauthorizedException {
		if(user==null || !(user.getRole().equals("employee"))) {
			throw new UnauthorizedException("You must a employee role to access");
		}
		
	}

}
