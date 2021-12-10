package com.revature.dao;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import com.revature.model.Reimbursement;
import com.revature.util.JDBCUtility;

public class ReimbursementDao {
	
	
	public List<Reimbursement> getAllReimbursements() throws SQLException {
		
		try(Connection con = JDBCUtility.getConnection()){
			List<Reimbursement> reimbList = new ArrayList<>();			
			String sql = "SELECT reim.reimb_id, reim.reimb_amount, reim.reimb_submitted, reim.reimb_resolved, reim.reimb_description, reim.reimb_receipt, reim.reimb_author, "
					+ "reim.reimb_resolver, reim.reimb_status, reim.reimb_type, a.user_first_name as a_first_name, a.user_last_name as a_last_name, "
					+ "r.user_first_name  as r_user_first_name, r.user_last_name as r_user_last_name "
					+ "FROM ers_reimbursement reim "
					+ "INNER JOIN ers_users a "
					+ "ON reim.reimb_author = a.ers_users_id "
					+ "LEFT JOIN ers_users r "
					+ "ON reim.reimb_resolver = r.ers_users_id; ";
			
			PreparedStatement pstmt = con.prepareStatement(sql);
			
			ResultSet rs = pstmt.executeQuery();
			
			while (rs.next()) {
				int id = rs.getInt("reimb_id");
				double amount = rs.getDouble("reimb_amount");
				String submitted = rs.getString("reimb_submitted");
				String resolved = rs.getString("reimb_resolved");
				String desc = rs.getString("reimb_description");
				int authorId = rs.getInt("reimb_author");
				int resolverId = rs.getInt("reimb_resolver");
				String status = rs.getString("reimb_status");
				String type = rs.getString("reimb_type");
				String firstName = rs.getString("a_first_name");
				String lastName = rs.getString("a_last_name");
				
				Reimbursement reimbursement = new Reimbursement(id,amount,submitted,resolved,desc,authorId,resolverId,status,type,firstName,lastName);
				
				reimbList.add(reimbursement);
			}
			return reimbList;
		}
		
	}

	public List<Reimbursement> getAllReimbursementByEmployee(int employeeId) throws SQLException {
		
		try(Connection con = JDBCUtility.getConnection()){
			List<Reimbursement> reimbList = new ArrayList<>();			
			String sql = "SELECT * FROM ers_reimbursement WHERE reimb_author = ?; ";
			
			PreparedStatement pstmt = con.prepareStatement(sql);
			
			pstmt.setInt(1, employeeId);
			
			ResultSet rs = pstmt.executeQuery();
			
			while (rs.next()) {
				int id = rs.getInt("reimb_id");
				double amount = rs.getDouble("reimb_amount");
				String submitted = rs.getString("reimb_submitted");
				String resolved = rs.getString("reimb_resolved");
				String desc = rs.getString("reimb_description");
				int authorId = rs.getInt("reimb_author");
				int resolverId = rs.getInt("reimb_resolver");
				String status = rs.getString("reimb_status");
				String type = rs.getString("reimb_type");
				
				Reimbursement reimbursement = new Reimbursement(id,amount,submitted,resolved,desc,authorId,resolverId,status,type);
				
				reimbList.add(reimbursement);
			}
			return reimbList;
		}
	}

	public Reimbursement addReimbursement(double amount, String desc, int authorId, 
			String reimbType, InputStream image) throws SQLException {
		try(Connection con = JDBCUtility.getConnection()){	
			
			con.setAutoCommit(false);
			String sql = "INSERT INTO ers_reimbursement (reimb_amount, reimb_submitted, reimb_description, "
					+ "reimb_receipt, reimb_author,reimb_type) VALUES (?,now(),?,?,?,?) ";
			
			PreparedStatement pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			
			pstmt.setDouble(1, amount);
			pstmt.setString(2, desc);
			pstmt.setBinaryStream(3, image);
			pstmt.setInt(4, authorId);
			pstmt.setString(5, reimbType);
			
			int numberOfInsertedRecords = pstmt.executeUpdate();
			
			if(numberOfInsertedRecords !=1) {
				throw new SQLException("Issue occurred when adding assignment");
			}
			ResultSet rs = pstmt.getGeneratedKeys();
			
			rs.next();
			int generatedId = rs.getInt(1);
			
			con.commit();
			return new Reimbursement(generatedId,amount,rs.getString("reimb_submitted"),rs.getString("reimb_resolved"),desc,authorId,rs.getInt("reimb_resolver"),rs.getString("reimb_status"),reimbType);
		}
	}

	public void approveReimbursement(int id, String status, int resloverId) throws SQLException {
			try(Connection con = JDBCUtility.getConnection()){	
				String sql = "UPDATE ers_reimbursement SET reimb_status = ?, reimb_resolved = now(), reimb_resolver = ? "
						+ "WHERE reimb_id = ?; ";
			
				PreparedStatement pstmt = con.prepareStatement(sql);
				
				pstmt.setString(1, status);
				pstmt.setInt(2, resloverId);
				pstmt.setInt(3, id);
				
				int updateReimb= pstmt.executeUpdate();
				
				if(updateReimb !=1) {
					throw new SQLException("Something went wrong");
					}
				}
			}
	
	public Reimbursement getReimbursementById(int reimbId) throws SQLException {
		
		try(Connection con = JDBCUtility.getConnection()){			
			String sql = "SELECT * FROM ers_reimbursement WHERE reimb_id = ?; ";
			
			PreparedStatement pstmt = con.prepareStatement(sql);
			
			pstmt.setInt(1, reimbId);
			
			ResultSet rs = pstmt.executeQuery();
			
			while (rs.next()) {
				int id = rs.getInt("reimb_id");
				double amount = rs.getDouble("reimb_amount");
				String submitted = rs.getString("reimb_submitted");
				String resolved = rs.getString("reimb_resolved");
				String desc = rs.getString("reimb_description");
				int authorId = rs.getInt("reimb_author");
				int resolverId = rs.getInt("reimb_resolver");
				String status = rs.getString("reimb_status");
				String type = rs.getString("reimb_type");
				
				Reimbursement reimbursement = new Reimbursement(id,amount,submitted,resolved,desc,authorId,resolverId,status,type);
				return reimbursement;
			}
			return null;
			
		}
	}

	public InputStream getImageFromReimbursementById(int id) throws SQLException {
		try(Connection con = JDBCUtility.getConnection()){
			String sql = "SELECT reimb_receipt FROM ers_reimbursement WHERE reimb_id = ?; ";
			
			PreparedStatement pstmt = con.prepareStatement(sql);
			
			pstmt.setInt(1, id);
			
			ResultSet rs = pstmt.executeQuery();
			
			if(rs.next()) {
				InputStream image = rs.getBinaryStream("reimb_receipt");
				
				return image;
			}
			return null;

		}
			}

	public List<Reimbursement> getAllPendingReimbursements(String status) throws SQLException {
		List<Reimbursement> reimList = new ArrayList<>();
		try(Connection con = JDBCUtility.getConnection()){
			String sql = "SELECT * FROM ers_reimbursement WHERE reimb_status = ?; ";
			
			PreparedStatement pstmt = con.prepareStatement(sql);
			
			pstmt.setString(1, status);
			
			ResultSet rs = pstmt.executeQuery();
			
			while (rs.next()) {
				int id = rs.getInt("reimb_id");
				double amount = rs.getDouble("reimb_amount");
				String submitted = rs.getString("reimb_submitted");
				String resolved = rs.getString("reimb_resolved");
				String desc = rs.getString("reimb_description");
				int authorId = rs.getInt("reimb_author");
				int resolverId = rs.getInt("reimb_resolver");
				String status1 = rs.getString("reimb_status");
				String type = rs.getString("reimb_type");
				
				Reimbursement reimbursement = new Reimbursement(id,amount,submitted,resolved,desc,authorId,resolverId,status1,type);
				
				reimList.add(reimbursement);
			}
			return reimList;
			
		}
		
	}

	
}