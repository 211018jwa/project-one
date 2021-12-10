package com.revature.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.revature.dto.LoginDTO;
import com.revature.dto.MessageDTO;
import com.revature.model.Users;
import com.revature.service.AuthorizationService;
import com.revature.service.UsersService;

import io.javalin.Javalin;
import io.javalin.http.Handler;

public class AuthenticationController implements Controller {
	
	
	private UsersService userService;
	private AuthorizationService authorizationService;
	
	public AuthenticationController() {
		this.userService = new UsersService();
	}
	
	
	private Handler login = (ctx) -> {
		LoginDTO loginDto = ctx.bodyAsClass(LoginDTO.class);
		Users user = this.userService.getUsernameAndPassword(loginDto.getUsername(), loginDto.getPassword());
		
		HttpServletRequest req = ctx.req;
		
		HttpSession session = req.getSession();
		session.setAttribute("currentuser", user);
		
		ctx.json(user);
	};
	
	private Handler logout = (ctx) -> {
		HttpServletRequest req = ctx.req;
		
		req.getSession().invalidate();
		
		ctx.json(new MessageDTO("Logout Succesfully"));
		
	};
	
	private Handler checkIfLoggedIn = (ctx) -> {
		HttpSession session = ctx.req.getSession();
		
		
		if(!(session.getAttribute("currentuser")==null)) {
			ctx.json(session.getAttribute("currentuser"));
			ctx.status(200);
		}else {
			ctx.json(new MessageDTO("User is not logged in"));
			ctx.status(401);
		}
	};
	
	private Handler insertNewUser = (ctx) -> {
		Users user = (Users) ctx.req.getSession().getAttribute("currentuser");
		
		String username = ctx.formParam("Username");
		String password = ctx.formParam("Password");
		String firstName = ctx.formParam("First_Name");
		String lastName = ctx.formParam("Last_Name");
		String email = ctx.formParam("Email");
		String role = ctx.formParam("Role");
		
		Users u = this.userService.insertNewUser(user, username, password, firstName, lastName, email, role);
		
		ctx.json(u);
		ctx.status(200);
	};

	@Override
	public void registerEndPoints(Javalin app) {
		app.post("/login", login);
		app.post("/logout", logout);
		app.post("/addUser", insertNewUser);
		app.get("/checkloginstatus", checkIfLoggedIn);
		
	}

}
