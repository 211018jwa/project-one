package com.revature.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.revature.model.Users;
import com.revature.util.JDBCUtility;

public class UserDAO {
	
	public Users user;
	
	public Users getUsernameAndPassword(String username, String password) throws SQLException 
	{
		try (
			Connection con = JDBCUtility.getConnection()){
			
			String sql = "SELECT * FROM ers_users WHERE ers_username = ? AND ers_password = ? ";
			
			PreparedStatement pstmt = con.prepareStatement(sql);
			pstmt.setString(1, username);
			pstmt.setString(2, password);
			
			ResultSet rs = pstmt.executeQuery();
			
			if(rs.next()) {
				int id = rs.getInt("ers_users_id");
				String userName = rs.getString("ers_username");
				String pass = rs.getString("ers_password");
				String firstName = rs.getString("user_first_name");
				String lastName = rs.getString("user_last_name");
				String eMail = rs.getString("user_email");
				String userRole = rs.getString("user_role");
				
				return new Users(id, userName, pass, firstName, lastName, eMail, userRole);
				
			}else {
				return null;
			}
			
			
		}
	}
	
	public Users insertUser(String username, String password, String firstName, String lastName, String email, String role) throws SQLException {
		try(
				Connection con = JDBCUtility.getConnection()){
			
				String sql = "INSERT INTO public.ers_users(ers_username,ers_password,user_first_name,user_last_name,user_email,user_role) "
						+ " VALUES(?,crypt(?,gen_salt('bf')),?,?,?,?) ";
				
				PreparedStatement pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				
				pstmt.setString(1, username);
				pstmt.setString(2, password);
				pstmt.setString(3, firstName);
				pstmt.setString(4, lastName);
				pstmt.setString(5, email);
				pstmt.setString(6, role);
				
				int numberOfUserInserted = pstmt.executeUpdate();
				
				if(numberOfUserInserted!=1) {
					throw new SQLException("Adding new user was unsuccessfull");
				}
				
				ResultSet rs = pstmt.getGeneratedKeys();
				
				rs.next();
				
				int automaticallyGenerated = rs.getInt(1);
				
				return new Users(automaticallyGenerated,username,password,
						firstName, lastName,
						email,role);
				
				}
		}
	

	public List<Users> getAllUsers() throws SQLException {
		
		List<Users> listOfUsers = new ArrayList<>();
		try(Connection con = JDBCUtility.getConnection()){
			String sql = "SELECT * FROM expense.ers_users; ";
			
			PreparedStatement pstmt = con.prepareStatement(sql);
			
			ResultSet rs = pstmt.executeQuery();
			
			while (rs.next()) {
				int id = rs.getInt("ers_users_id");
				String userName = rs.getString("ers_username");
				String pass = rs.getString("ers_password");
				String firstName = rs.getString("user_first_name");
				String lastName = rs.getString("user_last_name");
				String eMail = rs.getString("user_email");
				String userRole = rs.getString("user_role");
				
				Users u = new Users(id, userName, pass, firstName, lastName, eMail, userRole);

				listOfUsers.add(u);
			}
		}
		return listOfUsers;
	}

	public Users getUserById(int userId) throws SQLException {
		try(
				Connection con = JDBCUtility.getConnection()){
				String sql = "SELECT * FROM expense.ers_users WHERE ers_users_id = ? ";
			
				PreparedStatement pstmt = con.prepareStatement(sql);
				pstmt.setInt(1, userId);
				
				ResultSet rs = pstmt.executeQuery();
				
				if(rs.next()) {
					return new Users(rs.getInt("ers_users_id"),rs.getString("ers_username"),rs.getString("ers_password"),rs.getString("user_first_name"),
							rs.getString("user_last_name"), rs.getString("user_email"),rs.getString("user_role"));
					
				}else {
					return null;
				}
		}
	}

	public void deleteUserById(int id) throws SQLException {
		try(Connection con = JDBCUtility.getConnection()){
			String sql = "DELETE FROM expense.ers_users"
					+ " WHERE ers_users_id = ?; ";
			
			PreparedStatement pstmt = con.prepareStatement(sql);
			
			pstmt.setInt(1, id);
			
			int numberOfRecordsDeleted = pstmt.executeUpdate();
			
			if(numberOfRecordsDeleted!=1) {
				throw new SQLException("Unable to delete user record with id " + id);
			}
		}
		
	}
}
