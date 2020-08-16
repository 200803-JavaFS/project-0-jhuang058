package com.revature.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.revature.models.Account;
import com.revature.utils.ConnectionUtility;

public class AccountDAO implements IAccountDAO {
	
	private IUserDAO uDao = new UserDAO();

	@Override
	public Account findByAccNum(int i) {
		try(Connection conn = ConnectionUtility.getConnection()) {
			String sql = "SELECT * FROM accounts WHERE account_number = "+i+";";
			
			Statement statement = conn.createStatement();
					
			ResultSet result = statement.executeQuery(sql);
			
			if (result.next()) {
				Account a = new Account(result.getInt("account_number"), 
						result.getString("account_type"),
						uDao.findByUsername(result.getString("account_owner")),
						result.getDouble("account_balance"),
						result.getString("account_status"));
				
				return a;
			}
		} catch(SQLException e) {
			e.printStackTrace();
			System.out.println("Account number not found.");
		}
		
		return null;
	}


	@Override
	public List<Account> findByStatus(String status) {
		try(Connection conn = ConnectionUtility.getConnection()) {
			String sql = "SELECT * FROM accounts WHERE account_status = ?;";
			
			PreparedStatement statement = conn.prepareStatement(sql);
			
			statement.setString(1, status);
			
			ResultSet result = statement.executeQuery();
			
			List<Account> list = new ArrayList();
			
			while(result.next()) {
				Account a = new Account(result.getInt("account_number"), 
						result.getString("account_type"),
						uDao.findByUsername(result.getString("account_owner")),
						result.getDouble("account_balance"),
						result.getString("account_status"));
				

				list.add(a);
			}
			
			return list;
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}


	@Override
	public List<Account> findByAccOwner(String username) {
		try(Connection conn = ConnectionUtility.getConnection()) {
			String sql = "SELECT * FROM accounts WHERE account_owner = ?;";
			
			PreparedStatement statement = conn.prepareStatement(sql);
			
			statement.setString(1, username);
			
			ResultSet result = statement.executeQuery();
			
			List<Account> list = new ArrayList();
			
			while(result.next()) {
				Account a = new Account(result.getInt("account_number"), 
						result.getString("account_type"),
						uDao.findByUsername(result.getString("account_owner")),
						result.getDouble("account_balance"),
						result.getString("account_status"));
				
				list.add(a);
			}
			
			return list;
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		return null;
		
	}


	@Override
	public boolean addAccount(Account a) {
		try (Connection conn = ConnectionUtility.getConnection()) {

			String sql = "INSERT INTO accounts (account_type, account_owner, account_balance, account_status)"
					+ "VALUES (?, ?, ?, ?);";

			PreparedStatement statement = conn.prepareStatement(sql);

			int index = 0;
			statement.setString(++index, a.getAccountType());
			statement.setString(++index, a.getAccountOwner().getUsername());
			statement.setDouble(++index, a.getAccountBalance());
			statement.setString(++index, a.getAccountStatus());

			statement.execute();
			return true;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
		
	}


	@Override
	public boolean updateAccount(Account a) {
		try (Connection conn = ConnectionUtility.getConnection()) {
			String sql = "UPDATE accounts SET account_type = ?, account_owner = ?, account_balance = ?, "
					+ "account_status = ? WHERE account_number = ?;";
			
			PreparedStatement statement = conn.prepareStatement(sql);

			int index = 0;
			statement.setString(++index, a.getAccountType());
			statement.setString(++index, a.getAccountOwner().getUsername());
			statement.setDouble(++index, a.getAccountBalance());
			statement.setString(++index, a.getAccountStatus());
			statement.setInt(++index, a.getAccountNumber());
			
			statement.execute();
			return true;
		}catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}

}
