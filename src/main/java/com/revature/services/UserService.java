package com.revature.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.revature.dao.IUserDAO;
import com.revature.dao.UserDAO;
import com.revature.models.User;

public class UserService {
	private static final Logger log = LogManager.getLogger(AccountService.class);
	public IUserDAO uDao = new UserDAO();

	public User findByUsername(String username) {
		log.info("Finding User with the username "+username);
		return uDao.findByUsername(username);
	}

	public boolean registerUser(User user) {
		log.info("Registering User: "+user);
		return uDao.registerUser(user);
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
