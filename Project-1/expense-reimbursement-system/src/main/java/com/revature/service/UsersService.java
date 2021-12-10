package com.revature.service;

import java.sql.SQLException;
import java.util.List;

import javax.security.auth.login.FailedLoginException;

import com.revature.dao.UserDAO;
import com.revature.exception.InvalidParameterException;
import com.revature.exception.UserNotFoundException;
import com.revature.model.Users;

public class UsersService {
	
	private UserDAO userDao;
	
	
	public UsersService() {
		this.userDao = new UserDAO();
	}

	public UsersService(UserDAO mockUserDao) {
		this.userDao = mockUserDao;
	}
	

	public Users getUsernameAndPassword(String username, String password) throws FailedLoginException, SQLException {
		
		Users user = this.userDao.getUsernameAndPassword(username, password);
		
		if(user==null) {
			throw new FailedLoginException("Incorrect username and/or password");
		}
		return user;
	}
	
	public Users insertNewUser(Users currentlyLoggedInUser, String username, String password, String firstName, String lastName, String email, String role) throws SQLException, InvalidParameterException {
		
		if(username==null || username.trim().equals("")) {
			throw new InvalidParameterException("Username cannot be empty");
		}
		if(password==null || password.trim().equals("")) {
			throw new InvalidParameterException("Password cannot be empty");
		}

		if(firstName==null || firstName.trim().equals("")){
			throw new InvalidParameterException("First name cannot be empty");
		}
		
		if(lastName==null || lastName.trim().equals("")){
			throw new InvalidParameterException("Last name cannot be empty");
		}
		
		if(email==null || email.trim().equals("")) {
			throw new InvalidParameterException("Email cannot be empty");
		}
		if(role == null || role.trim().equals("")) {
			throw new InvalidParameterException("Role cannot be empty");
		}
		
		Users userInserted = this.userDao.insertUser(username,password,firstName,lastName,email,role);
		return userInserted;
	}
	
	
	

	public List<Users> getAllUsers() throws SQLException {
		
		List<Users> users = this.userDao.getAllUsers();
		
		return users;
	}
	
	public Users getUserById(String id) throws InvalidParameterException, SQLException, UserNotFoundException {
		
		try {
			
			int userId= Integer.parseInt(id);
			
			Users usersId = this.userDao.getUserById(userId);
			
			if(usersId==null) {
				throw new UserNotFoundException("User with id " + userId + " was not found");
			}
			return usersId;	
		}catch(NumberFormatException e){
			throw new InvalidParameterException("Id must be valid");
		}
		
		
	}

	public void deleteUserById(String id) throws SQLException, UserNotFoundException, InvalidParameterException {
		try {
			int userId = Integer.parseInt(id);
			
			Users user = this.userDao.getUserById(userId);
			
			if(user==null) {
				throw new UserNotFoundException("User with id " + userId + " was not found");
			}
			
			this.userDao.deleteUserById(userId);
			
		}catch(NumberFormatException e){
			throw new InvalidParameterException("Id must be valid");
			}
		}

//	public Users updateUserById(Users dto, String id) {
//		try {
//			int userId = Integer.parseInt(id);
//			
//			Users user = this.userDao.getUserById(userId);
//			
//			if(user==null) {
//				throw new UserNotFoundException("User with id " + userId + " was not found");
//			}
//			
//			Users updateUser = this.userDao.updateUserById()
//		}
//		return null;
//	}
}
	
