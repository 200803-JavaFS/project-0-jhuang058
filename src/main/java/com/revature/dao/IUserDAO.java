package com.revature.dao;

import com.revature.models.User;

public interface IUserDAO {
	
	public User findByUsername(String username);

	public boolean registerUser(User u);

	public boolean updateUser(User u);

	public User findByName(String fname, String lname);

}
