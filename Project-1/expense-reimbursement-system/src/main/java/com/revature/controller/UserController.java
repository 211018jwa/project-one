package com.revature.controller;

import java.util.List;
import com.revature.model.Users;
import com.revature.service.AuthorizationService;
import com.revature.service.UsersService;

import io.javalin.Javalin;
import io.javalin.http.Handler;

public class UserController implements Controller {
	
	private AuthorizationService authorizationService;
	private UsersService userService;
	
	public UserController() {
		this.authorizationService = new AuthorizationService();
		this.userService = new UsersService();
	}
	
	private Handler getAllUsers = (ctx) -> {
		Users user = (Users) ctx.req.getSession().getAttribute("currentuser");
		this.authorizationService.authorizeManager(user);
		
		List<Users> users = this.userService.getAllUsers();
		ctx.json(users);
	};
	
	private Handler getUserById = (ctx) -> {
		Users user = (Users) ctx.req.getSession().getAttribute("currentuser");
		this.authorizationService.authorizeManager(user);
		
		String id = ctx.pathParam("id");
		
		Users users = this.userService.getUserById(id);
		ctx.json(users);
	};
	
	private Handler deleteUserById = (ctx) -> {
		Users user = (Users) ctx.req.getSession().getAttribute("currentuser");
		this.authorizationService.authorizeManager(user);
		
		String id = ctx.pathParam("id");
		
		this.userService.deleteUserById(id);
		
		ctx.json("Deleted Succesfully");
	};
	
	@Override
	public void registerEndPoints(Javalin app) {
//		app.post("/users", insertNewUser);
		app.get("/users", getAllUsers);
		app.get("/users/{id}", getUserById);
		app.delete("/users/delete/{id}", deleteUserById);
		
	}

}
