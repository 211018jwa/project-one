package com.revature.controller;


import java.io.InputStream;
import java.util.List;

import org.apache.tika.Tika;

import com.revature.dto.MessageDTO;

import com.revature.model.Reimbursement;
import com.revature.model.Users;
import com.revature.service.AuthorizationService;
import com.revature.service.ReimbursementService;

import io.javalin.Javalin;
import io.javalin.http.Handler;
import io.javalin.http.UploadedFile;

public class ReimbursementController implements Controller{
	
	private AuthorizationService authorizationService;
	
	private ReimbursementService reimbursementService;
	
	public ReimbursementController() {
		this.authorizationService = new AuthorizationService();
		this.reimbursementService = new ReimbursementService();
	}
	
	private Handler getAllReimbursements = (ctx) ->{
		
		Users currentlyLoggedInUser = (Users) ctx.req.getSession().getAttribute("currentuser");
		this.authorizationService.authorizeEmployeeAndManager(currentlyLoggedInUser);
		
		List<Reimbursement> reimbursement = this.reimbursementService.getAllReimbursements(currentlyLoggedInUser);
		
		ctx.json(reimbursement);
	};
	
	private Handler addReimbursement = (ctx) ->{
		
		Users currentlyLoggedInUser = (Users) ctx.req.getSession().getAttribute("currentuser");
		this.authorizationService.authorizeEmployee(currentlyLoggedInUser);
		
		String amount = ctx.formParam("amount");
		String reimbType = ctx.formParam("reimbType");
		String desc = ctx.formParam("description");
		
		
		
		UploadedFile file = ctx.uploadedFile("reimb_receipt");
		
		if(file==null) {
			ctx.status(400);
			ctx.json(new MessageDTO("Must have an image to upload"));
			return;
		}
		
		InputStream content = file.getContent();
		
		Tika tika = new Tika();
		
		String mimeType = tika.detect(content);
		
		
		Reimbursement addedReimb = this.reimbursementService.addReimbursement(currentlyLoggedInUser, mimeType, amount, desc, reimbType, content);
		ctx.json(addedReimb);
		ctx.status(201);
	};
	
	private Handler approveReimbursement = (ctx) -> {
		Users currentlyLoggedInUser = (Users) ctx.req.getSession().getAttribute("currentuser");
		this.authorizationService.authorizeManager(currentlyLoggedInUser);
		
		String id = ctx.formParam("re_id");
		String status = ctx.formParam("status");
		
		Reimbursement approvedReimb = this.reimbursementService.approveReimbursement(currentlyLoggedInUser,id,status);
		ctx.json(approvedReimb);
	};
	
	private Handler getImageFromReimbursementById = (ctx) -> {
		Users currentlyLoggedInUser = (Users) ctx.req.getSession().getAttribute("currentuser");
		this.authorizationService.authorizeEmployeeAndManager(currentlyLoggedInUser);
		
		String reimbId = ctx.pathParam("id");
		
		InputStream image = this.reimbursementService.getImageFromReimbursementById(currentlyLoggedInUser, reimbId);
		
		Tika tika = new Tika();
		String mimeType = tika.detect(image);
		
		ctx.contentType(mimeType);
		ctx.result(image);
	};
	
	private Handler getReimbursementById = (ctx) -> {
		Users currentlyLoggedInUser = (Users) ctx.req.getSession().getAttribute("currentuser");
		this.authorizationService.authorizeEmployeeAndManager(currentlyLoggedInUser);
		
		String reimbId = ctx.pathParam("reimbId");
		
		Reimbursement reimb = this.reimbursementService.getReimbursementById(reimbId);
		
		ctx.json(reimb);
		
	};
	
	private Handler getAllPendingReimbursement = (ctx) -> {
		Users currentlyLoggedInUser = (Users) ctx.req.getSession().getAttribute("currentuser");
		this.authorizationService.authorizeEmployeeAndManager(currentlyLoggedInUser);
		
		String status = ctx.pathParam("statusType");
		
		List<Reimbursement> reimbursement = this.reimbursementService.getAllPendingReimbursement(currentlyLoggedInUser, status);
		
		ctx.json(reimbursement);
	};
	
	
	
	
	

	@Override
	public void registerEndPoints(Javalin app) {
		
		app.get("/reimbursement", getAllReimbursements);
		app.post("/reimbursement", addReimbursement);
		app.patch("/reimbursement/status", approveReimbursement);
		app.get("/reimbursement/{id}/image", getImageFromReimbursementById);
		app.get("/reimbursement/{reimbId}", getReimbursementById);
		app.get("/reimbursement/status/{statusType}", getAllPendingReimbursement);
	}

}

