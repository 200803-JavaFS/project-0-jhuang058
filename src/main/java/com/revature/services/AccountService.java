package com.revature.services;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.revature.dao.AccountDAO;
import com.revature.dao.IAccountDAO;
import com.revature.models.Account;
import com.revature.models.User;

public class AccountService {
	
	private static final Logger log = LogManager.getLogger(AccountService.class);
	public IAccountDAO dao = new AccountDAO();

	public Account findByAccNum(int i) {
		log.info("Finding Account with account number "+i);
		return dao.findByAccNum(i);
	}
	
	public List<Account> findByAccOwner(String username) {
		log.info("Finding Account(s) by owner.");
		return dao.findByAccOwner(username);
		
	}

	public List<Account> viewPending() {
		log.info("Displaying all pending Account(s).");
		return dao.findByStatus("pending");
	}

	public boolean changeAllPending(String status) {
		List<Account> pList = dao.findByStatus("pending");
		
		int i = 0;
		
		for (Account a: pList) {
			a.setAccountStatus(status);
			dao.updateAccount(a);
			++i;
		}
		if (i == pList.size()) {
			log.info("Account(s) have been successfully "+status);
			return true;
		} else {
			log.info("Changing account status - Action failed");
			return false;
		}
	}

	public boolean changeStatusByNum(int num, String status) {
		
		Account a = dao.findByAccNum(num);
		a.setAccountStatus(status);
		boolean b = dao.updateAccount(a);
		
		if (b) {
			log.info("Account(s) have been successfully "+status);
		} else {
			log.info("Changing account status - Action failed.");
		}
		
		return b;
	}


	public boolean changeStatusByOwner(String username, String status) {
		List<Account> list = dao.findByAccOwner(username);
		List<Account> pList = dao.findByStatus("pending");
		
		list.retainAll(pList);
		
		int i = 0;
		
		for (Account a : list) {
			a.setAccountStatus(status);
			dao.updateAccount(a);
			++i;
		}
		
		boolean b = list.size() == i;
		if (b) {
			log.info("Account(s) have been successfully "+status);
		} else {
			log.info("Changing account status - Action failed.");
		}
		return b;
	}

	public boolean insertAccount(Account a, List<User> owners) {
		log.info("Inserting new account");
		
		 return dao.addAccount(a, owners);
		
	}

	public boolean updateAccount(Account a) {
		log.info("Account updated");
		return dao.updateAccount(a);
		
	}
	
	
	public double[] transferMoney(Account a1, Account a2, double amt) {

		a1.setAccountBalance(a1.getAccountBalance() - amt);
		a2.setAccountBalance(a2.getAccountBalance() + amt);

		if (updateAccount(a1) && updateAccount(a2)) {
			log.info("Transfered $" + amt + " from Account *****"
					+ Integer.toString(a1.getAccountNumber()).substring(5) + "\n" + "to Account *****"
					+ Integer.toString(a2.getAccountNumber()).substring(5));
			double[] balances = { a1.getAccountBalance(), a2.getAccountBalance() };
			System.out.println("Remaining account balance in account: $"+ a1.getAccountBalance());
			return balances;
		} else {
			log.info("Transfer has failed.");
			return null;
		}
	}

	public double withdraw(Account a1, double amt) {
		a1.setAccountBalance(a1.getAccountBalance() - amt);
		if (updateAccount(a1)) {
			log.info("Withdrew $" + amt + " from  "+a1.getAccountType()+" Account *****"
					+ Integer.toString(a1.getAccountNumber()).substring(5));
			System.out.println("Remaining account balance: $"+ a1.getAccountBalance());
			return a1.getAccountBalance();
		} else {
			log.info("Withdraw has failed.");
			return 0;
		}
	}

	public double deposit(Account a1, double amt) {
		a1.setAccountBalance(a1.getAccountBalance() + amt);
		if (updateAccount(a1)) {
			log.info("Deposited $" + amt + " into "+a1.getAccountType()+" Account *****"
					+ Integer.toString(a1.getAccountNumber()).substring(5));
			System.out.println("Updated account balance: $"+ a1.getAccountBalance());
			return a1.getAccountBalance();
		} else {
			log.info("Deposit has failed.");
			return 0;
		}
		
	}

	public void insertOwnership(List<User> owners) {
		log.info("Updating account ownership");
		for (User u : owners) {
			dao.addOwnership(u);
		}
		
	}

}
