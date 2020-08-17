package com.revature.services;

import java.util.ArrayList;
import java.util.Collections;
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
		log.info("Finding Account from database");
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
		log.info("Changing account status");
		List<Account> pList = dao.findByStatus("pending");
		
		int i = 0;
		
		for (Account a: pList) {
			a.setAccountStatus(status);
			dao.updateAccount(a);
			++i;
		}
		if (i == pList.size()) {
			System.out.println("Account(s) have been successfully "+status);
			return true;
		} else {
			System.out.println("Changing account status - Action failed");
			return false;
		}
	}

	public boolean changeStatusByNum(int num, String status) {
		log.info("Changing account status");
		Account a = dao.findByAccNum(num);
		a.setAccountStatus(status);
		boolean b = dao.updateAccount(a);
		
		if (b) {
			System.out.println("Account(s) have been successfully "+status);
		} else {
			System.out.println("Changing account status - Action failed.");
		}
		
		return b;
	}


	public boolean changeStatusByOwner(String username, String status) {
		log.info("Changing account status");
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
			System.out.println("Account(s) have been successfully "+status);
		} else {
			System.out.println("Changing account status - Action failed.");
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
		log.info("Initiating a transfer of funds");
		
		if (amt <= a1.getAccountBalance() && amt > 0 && a2 != null
				&& a2.getAccountStatus().toLowerCase().equals("approved")) {

			a1.setAccountBalance(a1.getAccountBalance() - amt);
			a2.setAccountBalance(a2.getAccountBalance() + amt);

			if (updateAccount(a1) && updateAccount(a2)) {
				System.out.println("Transfered $" + amt + " from Account *****"
						+ Integer.toString(a1.getAccountNumber()).substring(5) + "\n" + "to Account *****"
						+ Integer.toString(a2.getAccountNumber()).substring(5));
				double[] balances = { a1.getAccountBalance(), a2.getAccountBalance() };
				System.out.println("Remaining account balance in account: $" + a1.getAccountBalance());
				return balances;
			} else {
				System.out.println("Transfer has failed.");
				return null;
			}
		} else {
			System.out.println("Error.");
			return null;
		}
	}

	public double withdraw(Account a1, double amt) {
		log.info("Initiating a withdrawal");
		
		if (amt <= a1.getAccountBalance() && amt > 0) {
			a1.setAccountBalance(a1.getAccountBalance() - amt);
			if (updateAccount(a1)) {
				System.out.println("Withdrew $" + amt + " from  "+a1.getAccountType()+" Account *****"
						+ Integer.toString(a1.getAccountNumber()).substring(5));
				System.out.println("Remaining account balance: $"+ a1.getAccountBalance());
				return a1.getAccountBalance();
			} else {
				System.out.println("Withdraw has failed.");
				return -1;
			}
		} else {
			System.out.println("Error.");
			return -1;
		}
		

	}

	public double deposit(Account a1, double amt) {
		log.info("Initiating a deposit");
		
		if (amt > 0) {
			a1.setAccountBalance(a1.getAccountBalance() + amt);
			if (updateAccount(a1)) {
				System.out.println("Deposited $" + amt + " into "+a1.getAccountType()+" Account *****"
						+ Integer.toString(a1.getAccountNumber()).substring(5));
				System.out.println("Updated account balance: $"+ a1.getAccountBalance());
				return a1.getAccountBalance();
			} else {
				System.out.println("Deposit has failed.");
				return -1;
			}
		} else {
			System.out.println("Error.");
			return -1;
		}
		
	}

	public void insertOwnership(List<User> owners) {
		log.info("Updating account ownership");
		for (User u : owners) {
			dao.addOwnership(u);
		}
		
	}
	
	public int pullNewAccNum(User user, List<User> owners) {
		List<Account> list = dao.findByAccOwner(user.getUsername());
		List<Integer> accNums = new ArrayList<>();
		for (Account acc : list) {
			accNums.add(acc.getAccountNumber());
		}

		return Collections.max(accNums);
	}

}
