package com.revature;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.revature.utils.ConsoleUtil;

public class Driver {
	
	private static final Logger log = LogManager.getLogger(Driver.class);
	
	public static void main(String[] args) {
		
		log.info("The application has started");
		
		//Customer.register();
		
		ConsoleUtil cons = new ConsoleUtil();
		cons.beginApp();
		
//		AccountService as = new AccountService();
//		List<Account> l1 = as.findByAccOwner("Kanye.West");
//		List<Account> l2 = as.viewPending();
//		System.out.println("In l1:");
//		for (Account a: l1) {System.out.println(a);}
//		System.out.println("In l2");
//		for (Account a: l2) {System.out.println(a);}
//		l1.retainAll(l2);
//		System.out.println("after retainAll() in l1:");
////		System.out.println(l1.get(0));
//		for (Account a: l1) {System.out.println(a);}
		
		
//		UserService us = new UserService();
//		User u = us.findByUsername("Kanye.West");
//		System.out.println(u.getPassword());
		
//		IUserDAO dao = new UserDAO();
//		User u = dao.findByUsername("Kanye.West");
//		System.out.println(u);
		
//		IAccountDAO dao = new AccountDAO();
//		Account a = dao.findByAccNum(100000000);
//		System.out.println(a);
		
	}

}
