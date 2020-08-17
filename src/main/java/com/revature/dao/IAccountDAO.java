package com.revature.dao;

import java.util.List;

import com.revature.models.Account;
import com.revature.models.User;

public interface IAccountDAO {
	
	public Account findByAccNum(int i);
	public List<Account> findByStatus(String status);
	public List<Account> findByAccOwner(String username);
	public boolean updateAccount(Account a);
	boolean addAccount(Account a, List<User> owners);
	boolean addOwnership(User owner);

}
