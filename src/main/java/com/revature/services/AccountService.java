package com.revature.services;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.revature.dao.AccountDAO;
import com.revature.dao.IAccountDAO;
import com.revature.models.Account;

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

	public boolean insertAccount(Account a) {
		log.info("New account inserted");
		return dao.addAccount(a);
		
	}

	public boolean updateAccount(Account a) {
		log.info("Account updated");
		return dao.updateAccount(a);
		
	}

}
