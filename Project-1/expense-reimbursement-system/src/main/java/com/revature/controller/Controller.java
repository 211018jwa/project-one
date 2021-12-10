package com.revature.controller;

import io.javalin.Javalin;

public interface Controller {
	
	public void registerEndPoints(Javalin app);

}
