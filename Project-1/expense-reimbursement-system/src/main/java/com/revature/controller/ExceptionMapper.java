package com.revature.controller;

import javax.security.auth.login.FailedLoginException;

import com.revature.dto.MessageDTO;
import com.revature.exception.ReimbursementAlreadyApproved;
import com.revature.exception.ReimbursementImageNotFoundException;
import com.revature.exception.ReimbursemntIdNotFoundException;
import com.revature.exception.UnauthorizedException;
import com.revature.exception.UserNotFoundException;

import io.javalin.Javalin;

public class ExceptionMapper {
	
	public void mapException(Javalin app) {
		app.exception(FailedLoginException.class, (e, ctx) -> {
			ctx.status(400);
			ctx.json(new MessageDTO(e.getMessage()));
		});
		
		app.exception(UnauthorizedException.class, (e, ctx) -> {
			ctx.status(400);
			ctx.json(new MessageDTO(e.getMessage()));
		});
		
		app.exception(ReimbursementAlreadyApproved.class, (e, ctx) -> {
			ctx.status(400);
			ctx.json(new MessageDTO(e.getMessage()));
		});
		
		app.exception(ReimbursementImageNotFoundException.class, (e, ctx) -> {
			ctx.status(400);
			ctx.json(new MessageDTO(e.getMessage()));
		});
		
		app.exception(UserNotFoundException.class, (e, ctx) -> {
			ctx.status(400);
			ctx.json(new MessageDTO(e.getMessage()));
		});
		
		app.exception(ReimbursemntIdNotFoundException.class, (e, ctx) -> {
			ctx.status(400);
			ctx.json(new MessageDTO(e.getMessage()));
		});
		
		
	}
	

}
