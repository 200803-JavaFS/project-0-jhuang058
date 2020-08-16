package com.revature.dao;

import java.util.List;

import com.revature.models.Account;

public interface IAccountDAO {
	
	public Account findByAccNum(int i);
	public List<Account> findByStatus(String status);
	public List<Account> findByAccOwner(String username);
	public boolean addAccount(Account a);
	public boolean updateAccount(Account a);

}
