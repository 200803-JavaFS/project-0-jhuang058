package com.revature;

import java.util.List;

import com.revature.dao.AccountDAO;
import com.revature.dao.IAccountDAO;
import com.revature.models.User;

public class tempDriver {
	
	public static void main(String[] args) {
		
		IAccountDAO dao = new AccountDAO();
		List<User> owners = dao.findByAccNum(100000000).getAccountOwner();
		for (User o : owners) {
			System.out.println(o);
		}
		System.out.println("joint acc");
		List<User> owners2 = dao.findByAccNum(100000016).getAccountOwner();
		for (User o : owners2) {
			System.out.println(o);
		}
		
	}

}
