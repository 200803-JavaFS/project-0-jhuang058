package com.revature.services;

import java.sql.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.revature.dao.IUserDAO;
import com.revature.dao.UserDAO;
import com.revature.models.User;
import com.revature.utils.ConsoleUtil;

public class UserService {
	private static final Logger log = LogManager.getLogger(AccountService.class);
	public IUserDAO uDao = new UserDAO();
	
	public User login(String username, String password) {
		log.info("logging in");
		
		try {
			User user = findByUsername(username);
			
			String userPw = user.getPassword();

			if (password.equals(userPw)) {
				//logging
				int userType = user.getUserType();
				String fname = user.getFirstName();
				String lname = user.getLastName();
				
				return user;
			} else {
				System.out.println("Log in attempt failed. Username and password do not match.");
				return null;
			}
			
		} catch (NullPointerException e) {
			System.out.println("Error: Username does not exist.");
			return null;
		}
		
	}
	
	public void logout() {
		log.info("Logging out.");
		System.out.println("=================================================");
	}
	

	public User findByUsername(String username) {
		log.info("Finding User with the username "+username);
		return uDao.findByUsername(username);
	}

	public User registerCustomer(String username, String password, String fname, String lname, String dob) {
		log.info("Registering new customer user");
		if (!username.equals("") && findByUsername(username) == null) {

			if (password.length() >= 6) {
					System.out.println("New customer successfully registered!");
					User user = new User(username, password, fname, lname, Date.valueOf(dob), 1);
					uDao.registerUser(user);
					return user;
			} else {
				System.out.println("Registration failed. Passwords must have at least 6 characters.");
				return null;
			}	
		} else {
			System.out.println("Registration failed. Username is unavailable.");
			return null;
		}
		
	}

	public boolean updateUser(User user) {
		log.info("Updating User: "+user);
		return uDao.updateUser(user);	
	}

	public User findByName(String fname, String lname) {
		log.info("Finding User: "+lname+", "+fname);
		return uDao.findByName(fname, lname);
	}

	

}
