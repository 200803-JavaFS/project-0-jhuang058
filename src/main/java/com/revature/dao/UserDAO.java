package com.revature.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.revature.models.User;
import com.revature.utils.ConnectionUtility;

public class UserDAO implements IUserDAO {

	@Override
	public User findByUsername(String username) {
		try(Connection conn = ConnectionUtility.getConnection()) {
			String sql = "SELECT * FROM users WHERE username = ?;";
			
			PreparedStatement statement = conn.prepareStatement(sql);
			
			statement.setString(1, username);
			
			ResultSet result = statement.executeQuery();
			
			if (result.next()) {
				User u = new User(result.getString("username"), 
						result.getString("pw"),
						result.getString("first_name"),
						result.getString("last_name"),
						result.getDate("dob"),
						result.getInt("user_type"));
				
				return u;
			}
		} catch(SQLException e) {
			e.printStackTrace();
			System.out.println("Username does not exist.");
		}
		
		return null;
	}

	@Override
	public boolean registerUser(User u) {
		try(Connection conn = ConnectionUtility.getConnection()) {
			String sql = "INSERT INTO users (username, pw, first_name, last_name, dob, user_type)"
					+ "VALUES (?, ?, ?, ?, ?, ?);";
			
			PreparedStatement statement = conn.prepareStatement(sql);
			
			int index = 0;			
			statement.setString(++index, u.getUsername());
			statement.setString(++index, u.getPassword());
			statement.setString(++index, u.getFirstName());
			statement.setString(++index, u.getLastName());
			statement.setDate(++index, u.getdOB());
			statement.setInt(++index, u.getUserType());
			
			statement.execute();
			return true;
			
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return false;
		
	}

	@Override
	public boolean updateUser(User u) {
		try (Connection conn = ConnectionUtility.getConnection()) {
			String sql = "UPDATE users SET pw = ?, first_name = ?, last_name = ?, dob = ?, user_type = ? "
					+ "WHERE username = ?";
			
			PreparedStatement statement = conn.prepareStatement(sql);

			int index = 0;
			statement.setString(++index, u.getPassword());
			statement.setString(++index, u.getFirstName());
			statement.setString(++index, u.getLastName());
			statement.setDate(++index, u.getdOB());
			statement.setInt(++index, u.getUserType());
			statement.setString(++index, u.getUsername());
			
			statement.execute();
			return true;
		}catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}

	@Override
	public User findByName(String fname, String lname) {
		try(Connection conn = ConnectionUtility.getConnection()) {
			String sql = "SELECT * FROM users WHERE first_name = ? AND last_name = ?;";
			
			PreparedStatement statement = conn.prepareStatement(sql);
			
			statement.setString(1, fname);
			statement.setString(2, lname);
			
			ResultSet result = statement.executeQuery();
			
			if (result.next()) {
				User u = new User(result.getString("username"), 
						result.getString("pw"),
						result.getString("first_name"),
						result.getString("last_name"),
						result.getDate("dob"),
						result.getInt("user_type"));
				
				return u;
			}
		} catch(SQLException e) {
			e.printStackTrace();
			System.out.println("Username does not exist.");
		}
		
		return null;
	}


}


