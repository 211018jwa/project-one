package com.revature.service;

import java.io.InputStream;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.revature.dao.ReimbursementDao;
import com.revature.exception.InvalidParameterException;
import com.revature.exception.ReimbursementAlreadyApproved;
import com.revature.exception.ReimbursementImageNotFoundException;
import com.revature.exception.ReimbursemntIdNotFoundException;
import com.revature.exception.UnauthorizedException;
import com.revature.model.Reimbursement;
import com.revature.model.Users;

public class ReimbursementService {
	
	private ReimbursementDao reimbDao;
	
	public ReimbursementService() {
		this.reimbDao = new ReimbursementDao();
	}
	
	public ReimbursementService(ReimbursementDao reimbDao) {
		this.reimbDao = reimbDao;
	}
	
	
	

	public List<Reimbursement> getAllReimbursements(Users currentlyLoggedInUser) throws SQLException {
		List<Reimbursement> reimList = null;
		
		if(currentlyLoggedInUser.getRole().equals("manager")) {
			reimList = this.reimbDao.getAllReimbursements();
		}else if(currentlyLoggedInUser.getRole().equals("employee")) {
			reimList = this.reimbDao.getAllReimbursementByEmployee(currentlyLoggedInUser.getId());
		}

		return reimList;
	}

	public Reimbursement addReimbursement(Users currentlyLoggedInUser, String mimeType, String amount, String desc,String reimbType, InputStream content) throws InvalidParameterException, SQLException {
		
		double amount1 = Double.parseDouble(amount);
		
		Set <String> allowedFilesTypes = new HashSet<>();
		allowedFilesTypes.add("image/jpeg");
		allowedFilesTypes.add("image/png");
		allowedFilesTypes.add("image/gif");
		
		if(!allowedFilesTypes.contains(mimeType)) {
			throw new InvalidParameterException("When adding an image, only PNG, JPEG or GIF are allowed");
		}
		
		int authorId = currentlyLoggedInUser.getId();
		Reimbursement addedReimb = this.reimbDao.addReimbursement(amount1,desc,authorId,reimbType,content);
		
		return addedReimb;
	}

	public Reimbursement approveReimbursement(Users currentlyLoggedInUser, String id, String status) throws InvalidParameterException, SQLException, ReimbursementAlreadyApproved {
		try {
		int reimbId = Integer.parseInt(id);
		
		Reimbursement reimb = this.reimbDao.getReimbursementById(reimbId);
		
		if(reimb==null) {
			throw new InvalidParameterException("Null");
		}

		if(reimb.getResolverId() == 0) {
			this.reimbDao.approveReimbursement(reimbId, status, currentlyLoggedInUser.getId());
		}else {
			throw new ReimbursementAlreadyApproved("Reimbursement already been approved");
		}
		return this.reimbDao.getReimbursementById(reimbId);
	}catch(NumberFormatException e) {
		throw new InvalidParameterException("Reimbursement id supplied must be an int");
	}
	}

	public InputStream getImageFromReimbursementById(Users currentlyLoggedInUser, String reimbId) throws SQLException, UnauthorizedException, ReimbursementImageNotFoundException, InvalidParameterException {
		
		try {
			int id = Integer.parseInt(reimbId);
			
			if(currentlyLoggedInUser.getRole().equals("employee")) {
				int employeeId = currentlyLoggedInUser.getId();
				List<Reimbursement> reimbursementThatBelongToEmployee = this.reimbDao.getAllReimbursementByEmployee(employeeId);
				
				Set<Integer> employeeIdEncountered = new HashSet<>();
				
				for(Reimbursement a:reimbursementThatBelongToEmployee) {
					employeeIdEncountered.add(a.getId());
				}
				
				if (!employeeIdEncountered.contains(id)) {
					throw new UnauthorizedException("You cannot access the receipt of reimbursement that do not belong to yourself");
				}
			}
			
			InputStream image = this.reimbDao.getImageFromReimbursementById(id);
			
			if(image==null) {
				throw new ReimbursementImageNotFoundException("Receipt was not found for reimbursement id " + id);
			}
			return image;
		}catch(NumberFormatException e) {
			throw new InvalidParameterException("Reimbursement id must be int");
		}
	}

	public Reimbursement getReimbursementById(String reimbId) throws ReimbursemntIdNotFoundException, SQLException, InvalidParameterException {
		try {
			int id = Integer.parseInt(reimbId);
			
			Reimbursement reimId = this.reimbDao.getReimbursementById(id);
			
			if (reimId==null) {
				throw new ReimbursemntIdNotFoundException("Reimbursement with id " +reimId +" was not found");
			}
			return reimId;
		}catch(NumberFormatException e) {
			throw new InvalidParameterException("Reimbursement id must be int");
		}
		
	}

	public List<Reimbursement> getAllPendingReimbursement(Users currentlyLoggedInUser, String status) throws SQLException {
		
		List<Reimbursement> reimList = null;
		
		
		
		if(currentlyLoggedInUser.getRole().equals("manager")) {
			reimList = this.reimbDao.getAllPendingReimbursements(status);
//		}else if(currentlyLoggedInUser.getRole().equals("employee")) {
//			reimList = this.reimbDao.getAllPendingReimbursementByEmployee(currentlyLoggedInUser.getId());
		}
		return reimList;
	}

}
