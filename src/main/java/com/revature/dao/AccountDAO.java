package com.revature.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.revature.models.Account;
import com.revature.models.User;
import com.revature.utils.ConnectionUtility;

public class AccountDAO implements IAccountDAO {

	private IUserDAO uDao = new UserDAO();

	@Override
	public Account findByAccNum(int i) {
		try (Connection conn = ConnectionUtility.getConnection()) {
			String sql = "SELECT * FROM accounts INNER JOIN ownership ON account_number = account_number_fk WHERE account_number = "
					+ i + ";";

			Statement statement = conn.createStatement();

			ResultSet result1 = statement.executeQuery(sql);

			if (result1.next()) {
				Account a = new Account();
				a.setAccountBalance(result1.getDouble("account_balance"));
				a.setAccountNumber(result1.getInt("account_number"));
				a.setAccountType(result1.getString("account_type"));
				a.setAccountStatus(result1.getString("account_status"));

				a.setAccountOwner(getAccountOwners(i));

				return a;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Account number not found.");
		}

		return null;
	}
	
	public List<User> getAccountOwners(int i) {
		try (Connection conn = ConnectionUtility.getConnection()) {
			String sql = "SELECT * FROM ownership WHERE account_number_fk = "+i+";";

			Statement statement = conn.createStatement();

			ResultSet result = statement.executeQuery(sql);

			List<User> owners = new ArrayList<User>();

			while (result.next()) {
				
				User u = uDao.findByUsername(result.getString("username_fk"));
				
				owners.add(u);
			}

			return owners;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public List<Account> findByStatus(String status) {
		try (Connection conn = ConnectionUtility.getConnection()) {
			String sql = "SELECT * FROM accounts WHERE account_status = ?;";

			PreparedStatement statement = conn.prepareStatement(sql);

			statement.setString(1, status);

			ResultSet result = statement.executeQuery();

			List<Account> list = new ArrayList();
			List<User> owners = new ArrayList<User>();

			while (result.next()) {
				int i = result.getInt("account_number");

				String sql2 = "SELECT * FROM ownership WHERE account_number_fk = " + i + ";";
				ResultSet result2 = conn.createStatement().executeQuery(sql2);

				while (result2.next()) {
					owners.add(uDao.findByUsername(result2.getString("username_fk")));
				}

				Account a = new Account(i, result.getString("account_type"), owners,
						result.getDouble("account_balance"), result.getString("account_status"));

				list.add(a);
			}

			return list;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public List<Account> findByAccOwner(String username) {
		try (Connection conn = ConnectionUtility.getConnection()) {
			String sql = "SELECT * FROM ownership WHERE username_fk = ?;"; // returns a list of account numbers

			PreparedStatement statement = conn.prepareStatement(sql);

			statement.setString(1, username);

			ResultSet result = statement.executeQuery();

			List<Account> list = new ArrayList<Account>();

			while (result.next()) {
				int i = result.getInt("account_number_fk");
				Account a = findByAccNum(i);
				list.add(a);

			}

			return list;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;

	}

	@Override
	public boolean addAccount(Account a, List<User> owners) {
		try (Connection conn = ConnectionUtility.getConnection()) {

			String sql = "INSERT INTO accounts (account_type, account_balance, account_status)" + "VALUES (?, ?, ?);";

			PreparedStatement statement = conn.prepareStatement(sql);

			int index = 0;
			statement.setString(++index, a.getAccountType());
			statement.setDouble(++index, a.getAccountBalance());
			statement.setString(++index, a.getAccountStatus());

			statement.execute();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}

	}

	@Override
	public boolean updateAccount(Account a) {
		try (Connection conn = ConnectionUtility.getConnection()) {
			String sql = "UPDATE accounts SET account_type = ?, account_balance = ?, "
					+ "account_status = ? WHERE account_number = ?;";

			PreparedStatement statement = conn.prepareStatement(sql);

			int index = 0;
			statement.setString(++index, a.getAccountType());
			statement.setDouble(++index, a.getAccountBalance());
			statement.setString(++index, a.getAccountStatus());
			statement.setInt(++index, a.getAccountNumber());

			statement.execute();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	@Override
	public boolean addOwnership(User owner) {
		List<Account> list = findByStatus("pending");
		List<Integer> accNums = new ArrayList<>();
		for (Account acc : list) {
			accNums.add(acc.getAccountNumber());
		}

		int accNum = Collections.max(accNums);

		try (Connection conn = ConnectionUtility.getConnection()) {

			String sql = "INSERT INTO ownership (username_fk, account_number_fk)" + "VALUES (?, ?)";

			PreparedStatement statement = conn.prepareStatement(sql);

			statement.setString(1, owner.getUsername());
			statement.setInt(2, accNum);

			return statement.execute();

		} catch (SQLException e) {
			e.printStackTrace();

		}
		return false;

	}
}
