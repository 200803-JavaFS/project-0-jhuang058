package com.revature.models;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Customer {
	
	private String username;
	private String password;
	List<Account> accounts = new LinkedList<Account>();
	
	public Customer(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}

	public Customer() {
		super();
	}
	
	public static void register() {
		Scanner scan = new Scanner(System.in);
		
		System.out.println("Please enter your username.");
		String username = scan.nextLine();
		
		System.out.println("Please enter your password.");
		String password = scan.nextLine();
		
		scan.close();

		Customer customer1 = new Customer(username, password);
		System.out.println("Registration successful"+"\n Username: "+username + "\n Password: "+password); 
	}
	
	public void openNewAccount() {
		accounts.add(new Account());
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	

}
