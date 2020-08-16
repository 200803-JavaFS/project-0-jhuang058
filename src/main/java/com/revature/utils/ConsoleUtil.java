package com.revature.utils;

import java.sql.Date;
import java.util.List;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.revature.Driver;
import com.revature.models.Account;
import com.revature.models.User;
import com.revature.services.AccountService;
import com.revature.services.UserService;

public class ConsoleUtil {
	
	private static final Logger log = LogManager.getLogger(Driver.class);
	private static final Scanner scan = new Scanner(System.in); //where to close scanner?
	private AccountService as = new AccountService();
	private UserService us = new UserService();
	
	
	public void beginApp() {
		
		System.out.println("Welcome \n"
				+ "Please type the number of action from the following menu and press enter: \n"
				+ "1. Log in as an existing user \n"
				+ "2. Register as a new user \n"
				+ "E. Exit the application");
		
		String i = scan.nextLine().toUpperCase();
		optionSwitch(i);
		
	}
	
	
	private void optionSwitch(String i) {
		
		switch(i) {
		case "1":
			login();
			break;
		case "2":
			System.out.println("Please create a username");
			String newUsername = scan.nextLine();
			if (newUsername != "" && us.findByUsername(newUsername) == null) {
				System.out.println(
						"Please create a password \n" + "(the password must be have a minimum of 6 characters)");
				String newPassword = scan.nextLine();
				if (newPassword.length() >= 6) {
					System.out.println("Enter your first name");
					String fname = scan.nextLine();
					System.out.println("Enter your last name");
					String lname = scan.nextLine();
					System.out.println("Enter your date of birth in the format: yyyy-mm-dd");
					String dob = scan.nextLine();
					System.out.println("Enter 1 to create a new customer account \n"
							+ "2 to create a new employee account \n" + "3 to create a new admin account");
					int type = scan.nextInt();
					scan.nextLine();

					if (type >= 1 && type <= 3) {
						User user = new User(newUsername, newPassword, fname, lname, Date.valueOf(dob), type);
						if (us.registerUser(user)) {
							System.out.println("Registration successful!");
							login();
						} else {
							System.out.println("Something went wrong. Please try again.");
							beginApp();
						}
					} else {
						System.out.println("Unable to create new account");
						beginApp();
					}
				} else {
					System.out.println("Invalid Password. Passwords must have at least 6 characters.");
					optionSwitch("2");
				}	
			} else {
				System.out.println("Username is unavailable. Please try again.");
				optionSwitch("2");
			}
			break;
		case "E":
			exitMessage();
			break;
		default:
			System.out.println("Please select a valid option from the menu.");
			beginApp();
			break;
		}
	}
	

	private void login() {
		System.out.println("Log In:");
		System.out.println("Please enter your username.");
		String username = scan.nextLine();
		
		System.out.println("Please Enter your password.");
		String password = scan.nextLine();
		
		try {
			User user = us.findByUsername(username);
			String userPw = user.getPassword();
			String fname = user.getFirstName();
			String lname = user.getLastName();
			int userType = user.getUserType();

			if (password.equals(userPw)) {
				switch (userType) {
				case 1:
					log.info(fname+" "+lname+" logged in as Customer.");
					System.out.println("Hello "+fname+" "+lname+"! Welcome to Your Favorite Bank.");
					customerMenu(username);
					break;
				case 2:
					log.info(fname+" "+lname+" logged in as Employee.");
					System.out.println("Hello "+fname+" "+lname+"!");
					employeeMenu();
					break;
				case 3:
					log.info(fname+" "+lname+" logged in as Admin.");
					System.out.println("Hello "+fname+" "+lname+"!");
					adminMenu();
					break;
				}

			} else {
				log.info("Log in attempt failed.");
				System.out.println("Password is incorrect.");
				login();
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
			System.out.println("Username not found. *Reminder: Usernames are case-sensitive");
			login();
		}
		
	}
	
	private void customerMenu(String username) {
		System.out.println("What would you like help with today?");
		System.out.println("================================================");
		System.out.println("1. Access existing account(s) \n"
				+ "2. Apply for a new bank account \n"
				+ "3. Edit your personal information \n"
				+ "E. Exit the application");
		
		String opt = scan.nextLine().toUpperCase();
		
		switch(opt) {
		case "1":
			showAccounts(username);
			break;
		case "2":
			createAccount(username);
			break;
		case "3": 
			editInfo(username);
			break;
		case "E":
			exitMessage();
			break;
		default:
			System.out.println("Option invalid.");
			customerMenu(username);
		}
	}
	
	private void employeeMenu() {
		System.out.println("Please select an option from the list: \n"
				+ "1. View pending account applications \n"
				+ "2. View account information \n"
				+ "3. View account balances \n"
				+ "4. View customer information \n"
				+ "E. Exit");
		String option = scan.nextLine().toUpperCase();
		switch(option) {
		case "1":
			viewPendingApps();
			break;
		case "2":
			showAccountsE();
			break;
		case "3":
			viewBalanceE();
			break;
		case "4":
			viewCustomerInfo();
			break;
		case "E":
			System.out.println("'Til next time!");
			break;
		default:
			System.out.println("Invalid option. Please select an option from the menu.");
			employeeMenu();
			break;
		}
	}


	private void adminMenu() {
		System.out.println("Please select an option from the list: \n"
				+ "1. View account applications \n"
				+ "2. Manage account(s) \n"
				+ "3. Cancel account(s) \n"
				+ "E. Exit");
		String option2 = scan.nextLine().toUpperCase();
		switch(option2) {
		case "1":
			viewPendingAppsA();
			break;
		case "2":
			System.out.println("Enter the username of the account owner");
			String un = scan.nextLine();
			manageAccountA(un);
			break;
		case "3":
			System.out.println("Please enter the account number for the account you would like to access.");
			int i = scan.nextInt();
			scan.nextLine();
			Account a = as.findByAccNum(i);
			
			if (a != null) {
				System.out.println("Confirm cancel account? Enter yes/no.");
				String ans = scan.nextLine().toLowerCase();
				if (ans.equals("yes")) {
					a.setAccountStatus("cancelled");
					if (as.updateAccount(a)) {
						System.out.println("Account cancelled.");
						adminMenu();
					} else {
						adminMenu();
					}
				} else {
					adminMenu();
				}
				break;
			} else {
				System.out.println("Account number not found. Please try again.");
				adminMenu();
			}
			
		case "E":
			System.out.println("Adios. We shall meet again!");
			break;
		default:
			System.out.println("Invalid option. Please select an option from the menu.");
			adminMenu();
			break;
		}
	}

	private void viewCustomerInfo() {
		System.out.println("Enter customer's first name");
		String fname = scan.nextLine();
		System.out.println("Enter customer's last name");
		String lname = scan.nextLine();
		
		User u = us.findByName(fname, lname);
		System.out.println("Name: "+u.getFirstName()+" "+u.getLastName()+"\n"
				+ "Username: "+u.getUsername()+"\n"
				+ "DOB: "+u.getdOB());
		
		System.out.println("================================================");
		System.out.println("Enter 'P' to return to the previous menu, \n"
				+ "'E' to exit.");
		String o = scan.nextLine().toUpperCase();
		switch(o) {
		case "P":
			employeeMenu();
			break;
		case "E":
			System.out.println("Ciao!");
			break;
		}
	}

	private void viewBalanceE() {
		
		double totBalance = 0;
		
		System.out.println("Enter the username of account owner");
		String un = scan.nextLine();
		
		List<Account> list = as.findByAccOwner(un);
		if (list != null) {
			for (Account a : list) {
				int accNum = a.getAccountNumber();
				String accType = a.getAccountType();
				double balance = a.getAccountBalance();
				totBalance += balance;

				System.out.println("The balance in " + accType + " account "
						+ Integer.toString(accNum).substring(5) + " is $" + balance);
			}

			System.out.println("The total balance in user's account(s) is $" + totBalance);

			System.out.println("================================================");
			System.out.println("Enter 'P' to return to the previous menu, \n" + "'E' to exit.");
			String o = scan.nextLine().toUpperCase();
			switch (o) {
			case "P":
				employeeMenu();
				break;
			case "E":
				System.out.println("Ciao. 'Til next time!");
				break;
			}
		} else {
			System.out.println("Username not found. Please try again.");
			viewBalanceE();
		}
	}
	
	
	private void showAccountsE() {
		System.out.println("Enter the username of account owner");
		String un = scan.nextLine();
		
		List<Account> list = as.findByAccOwner(un);
		
		if (list != null) {
			for (Account a : list) {
				System.out.println(a);
			}

			System.out.println("================================================");
			System.out.println("Enter 'P' to return to the previous menu, \n" + "'E' to exit.");
			String o = scan.nextLine().toUpperCase();
			switch (o) {
			case "P":
				employeeMenu();
				break;
			case "E":
				System.out.println("Ciao!");
				break;
			}
		} else {
			System.out.println("Username not found. Please try again.");
			showAccountsE();
		}
		

	}


	private void editInfo(String username) {
		User user = us.findByUsername(username);
		
		System.out.println("What information would you like to update? \n"
				+ "1. password \n"
				+ "2. first name \n"
				+ "3. last name \n"
				+ "P. return to previous menu \n"
				+ "E. Exit.");
		
		String opt = scan.nextLine().toUpperCase();
		
		switch (opt) {
		case "1": 
			System.out.println("Enter your current password");
			String curPw = scan.nextLine();
			if (user.getPassword().equals(curPw)) {
				System.out.println("Enter new password (must have at least 6 characters)");
				String newPw1 = scan.nextLine();
				System.out.println("Confirm new password");
				String newPw2 = scan.nextLine();
				if (newPw1.equals(newPw2) && newPw1.length() >= 6) {
					user.setPassword(newPw1);
					if (us.updateUser(user)) {
						System.out.println("You password has been changed. Please re-login.");
						login();
					} else {
						System.out.println("Something went wrong. Please try again.");
						editInfo(username);
					} 
				} else {
					System.out.println("Error: your passwords do not match, or it does not meet the password requirement. Please try again.");
					editInfo(username);
				}
			} else {
				System.out.println("The password you entered is incorrect.");
				login();
			}
			break;
		case "2":
			System.out.println("Enter your new first name.");
			String newfname = scan.nextLine();
			user.setFirstName(newfname);
			if (us.updateUser(user)) {
				System.out.println("Your first name has been updated.");
				editInfo(username);
			} else {
				System.out.println("Something went wrong. Please try again.");
				editInfo(username);
			}
			break;
		case "3":
			System.out.println("Enter your new last name.");
			String newlname = scan.nextLine();
			user.setLastName(newlname);
			if (us.updateUser(user)) {
				System.out.println("Your last name has been updated.");
				editInfo(username);
			} else {
				System.out.println("Something went wrong. Please try again.");
				editInfo(username);
			}
			break;
		case "P": 
			customerMenu(username);
			break;
		case "E":
			exitMessage();
			break;
		default:
			System.out.println("Option invalid. Please try again.");
			editInfo(username);
		}
	}
		

	private void createAccount(String username) {
		
		User user = us.findByUsername(username);
		
		System.out.println("Which type of account would you like to open? \n"
				+ "Enter [1] for a Checking account, [2] for a Savings account \n"
				+ "Enter [P] to return to the previous menu");
		String answer = scan.nextLine().toUpperCase();
		String type = null;
		
		switch (answer) {
		case "1":
			type = "Checking";
			break;
		case "2":
			type = "Savings";
			break;
		case "P":
			customerMenu(username);
			break;
		default:
			System.out.println("The account type is not available. Please try again.");
			createAccount(username);
			break;
		}

		Account a = new Account(type, user, 0.00, "pending");
		
		if (as.insertAccount(a)) {
			System.out.println("Thanking you for opening a new "+type+" Account. \n"
					+ "Please allow up to 24 hours for your account to be approved.");
			System.out.println("================================================");
			System.out.println("Enter 'P' to return to the previous menu, \n"
					+ "'E' to exit.");
			String o = scan.nextLine().toUpperCase();
			switch(o) {
			case "P":
				customerMenu(username);
				break;
			case "E":
				exitMessage();
				break;
			}
		} else {
			System.out.println("Something went wrong, please try again.");
			createAccount(username);
		}
		
	}


	private void showAccounts(String username) {
		List<Account> list = as.findByAccOwner(username);
		
		for(Account a : list) {
			System.out.println(a);
		}
		
		System.out.println("========================================");
		manageAccount(username);
		
	}


	private void manageAccount(String username) {
		System.out.println("What would you like to do?");
		System.out.println("1. View Balance");
		System.out.println("2. Deposit");
		System.out.println("3. Withdraw");
		System.out.println("4. Transfer Funds");
		System.out.println("P. Return to previous menu");
		System.out.println("E. Exit");
		String option = scan.nextLine().toUpperCase();
		
		switch(option) {
		case "1": 
			viewBalance(username);
			break;
		case "2":
			deposit(username);
			break;
		case "3":
			withdraw(username);
			break;
		case "4":
			transferMoney(username);
			break;
		case "P":
			customerMenu(username);
			break;
		case "E":
			exitMessage();
			break;
		default:
			System.out.println("Invalid option. Please select an option from the menu.");
			manageAccount(username);
			break;
		}
	}
	
	private void manageAccountA(String username) {
		System.out.println("What would you like to do?");
		System.out.println("1. View Balance");
		System.out.println("2. Deposit");
		System.out.println("3. Withdraw");
		System.out.println("4. Transfer Funds");
		System.out.println("P. Return to previous menu");
		System.out.println("E. Exit");
		String option = scan.nextLine().toUpperCase();
		
		switch(option) {
		case "1": 
			viewBalance(username);
			break;
		case "2":
			deposit(username);
			break;
		case "3":
			withdraw(username);
			break;
		case "4":
			transferMoney(username);
			break;
		case "P":
			adminMenu();
			break;
		case "E":
			System.out.println("Adios!");
			break;
		default:
			System.out.println("Invalid option. Please select an option from the menu.");
			manageAccount(username);
			break;
		}
	}


	
	private void viewPendingApps() {
		List<Account> list = as.viewPending();
		
		if (list.size() == 0) {
			System.out.println("There are no pending applications at this time.");
			System.out.println("=========================================================");
			employeeMenu();
		} else {
			for (int i = 0; i < list.size(); i++ ) {
				System.out.println(list.get(i));
			}
			
			System.out.println("=========================================================");
			System.out.println("1. Approve all \n"
					+ "2. Deny all \n"
					+ "3. Approve/Deny by account number \n"
					+ "4. Approve/Deny by account owner \n"
					+ "P. Return to previous menu \n"
					+ "E. Exit");
			String action = scan.nextLine().toUpperCase();
			
			switch(action) {
			case "1":
				if (as.changeAllPending("approved")) {
					employeeMenu();
				}
				break;
			case "2": 
				if (as.changeAllPending("denied")) {
					employeeMenu();
				}
				break;
			case "3":
				System.out.println("Enter account number");
				int i = scan.nextInt();
				scan.nextLine();
				System.out.println("Enter 1 to approve, 2 to deny account application");
				String i2 = scan.nextLine();
				switch(i2) {
				case "1":
					as.changeStatusByNum(i, "approved");
					viewPendingApps();
					break;
				case "2":
					as.changeStatusByNum(i, "denied");
					viewPendingApps();
					break;
				default:
					viewPendingApps();
					break;
				}
			case "4":
				System.out.println("Enter username of account owner");
				String username = scan.nextLine();
				System.out.println("Enter 1 to approve, 2 to deny user's account application(s)");
				String i3 = scan.nextLine();
				switch (i3) {
				case "1":
					as.changeStatusByOwner(username, "approved");
					viewPendingApps();
					break;
				case "2":
					as.changeStatusByOwner(username, "denied");
					viewPendingApps();
					break;
				default:
					viewPendingApps();
					break;
				}
				break;
			case "P":
				employeeMenu();
				break;
			case "E":
				System.out.println("Adios!");
				break;
			default:
				System.out.println("Invalid option. Please select an option from the menu.");
				viewPendingApps();
				break;
			}
		}
		
	}
	
	private void viewPendingAppsA() {
		List<Account> list = as.viewPending();
		
		if (list.size() == 0) {
			System.out.println("There are no pending applications at this time.");
			System.out.println("=========================================================");
			adminMenu();
		} else {
			for (int i = 0; i < list.size(); i++ ) {
				System.out.println(list.get(i));
			}
			
			System.out.println("=========================================================");
			System.out.println("1. Approve all \n"
					+ "2. Deny all \n"
					+ "3. Approve/Deny by account number \n"
					+ "4. Approve/Deny by account owner \n"
					+ "P. Return to previous menu \n"
					+ "E. Exit");
			String action = scan.nextLine().toUpperCase();
			
			switch(action) {
			case "1":
				if (as.changeAllPending("approved")) {
					adminMenu();
				}
				break;
			case "2": 
				if (as.changeAllPending("denied")) {
					adminMenu();
				}
				break;
			case "3":
				System.out.println("Enter account number");
				int i = scan.nextInt();
				scan.nextLine();
				System.out.println("Enter 1 to approve, 2 to deny account application");
				String i2 = scan.nextLine();
				switch(i2) {
				case "1":
					as.changeStatusByNum(i, "approved");
					viewPendingApps();
					break;
				case "2":
					as.changeStatusByNum(i, "denied");
					viewPendingApps();
					break;
				default:
					viewPendingApps();
					break;
				}
			case "4":
				System.out.println("Enter username of account owner");
				String username = scan.nextLine();
				System.out.println("Enter 1 to approve, 2 to deny user's account application(s)");
				String i3 = scan.nextLine();
				switch (i3) {
				case "1":
					as.changeStatusByOwner(username, "approved");
					viewPendingApps();
					break;
				case "2":
					as.changeStatusByOwner(username, "denied");
					viewPendingApps();
					break;
				default:
					viewPendingApps();
					break;
				}
				break;
			case "P":
				adminMenu();
				break;
			case "E":
				System.out.println("Adios!");
				break;
			default:
				System.out.println("Invalid option. Please select an option from the menu.");
				viewPendingApps();
				break;
			}
		}
		
	}

	private void transferMoney(String username) {
		System.out.println("Enter the account number for the account FROM which you would like to transfer");
		int accFrom = scan.nextInt();
		scan.nextLine();		
		Account a1 = as.findByAccNum(accFrom);
		if (a1 != null && a1.getAccountStatus().toLowerCase().equals("approved")) {
			double b = a1.getAccountBalance();
			System.out.println("Your account balance is $"+b);
			
			System.out.println("How much would you like to transfer?");
			double amt = scan.nextDouble();
			scan.nextLine();
			
			if (amt <= b && amt > 0) {
				System.out.println("Which account would you like to transfer to? Enter the account number.");
				int accTo = scan.nextInt();
				scan.nextLine();
				
				Account a2 = as.findByAccNum(accTo);
				
				if (a2 != null && a2.getAccountStatus().toLowerCase().equals("approved")) {
					a1.setAccountBalance(a1.getAccountBalance()-amt);
					a2.setAccountBalance(a2.getAccountBalance()+amt);
					
					if (as.updateAccount(a1) && as.updateAccount(a2)) {
						System.out.println("You have successfully transfered $"+amt+" from Account *****"+Integer.toString(accFrom).substring(5)+"\n"
								+ "to Account *****"+Integer.toString(accTo).substring(5));
						System.out.println("================================================");
						System.out.println("Enter 'P' to return to the previous menu, \n"
								+ "'E' to exit.");
						String o = scan.nextLine().toUpperCase();
						switch(o) {
						case "P":
							manageAccount(username);
							break;
						case "E":
							exitMessage();
							break;
						}
					} else {
						System.out.println("Something went wrong. Please try again.");
						transferMoney(username);
					}
					
				} else {
					System.out.println("Error. Please try again.");
					transferMoney(username);
				}

			} else {
				System.out.println("Error. Please try again.");
				manageAccount(username);
			}
			
		} else {
			System.out.println("Error. Please try again.");
			transferMoney(username);
		}
	}

	private void withdraw(String username) {
		System.out.println("Enter the account number for the account from which to withdraw");
		int accNum = scan.nextInt();
		scan.nextLine();
		System.out.println("How much would you like to withdraw?");
		double amt = scan.nextDouble();
		scan.nextLine();
		
		Account a = as.findByAccNum(accNum);
		
		if (a != null && a.getAccountStatus().toLowerCase().equals("approved")) {
			double b = a.getAccountBalance();
			
			if (amt <= b && amt > 0) {
				a.setAccountBalance(a.getAccountBalance() - amt);

				if (as.updateAccount(a)) {
					System.out.println("You have withdrawn $" + amt + " from the account ending in "+Integer.toString(accNum).substring(5));
					System.out.println("Your new balance is $"+a.getAccountBalance());
					System.out.println("================================================");
					System.out.println("Enter 'P' to return to the previous menu, \n" + "'E' to exit.");
					String o = scan.nextLine().toUpperCase();
					switch (o) {
					case "P":
						manageAccount(username);
						break;
					case "E":
						exitMessage();
						break;
					}
				} else {
					System.out.println("Something went wrong. Please try again.");
					withdraw(username);
				}
			} else {
				System.out.println("Error. Please try again.");
				withdraw(username);
			}

		} else {
			System.out.println("Error. Please try again.");
			withdraw(username);
		}
	}

	private void deposit(String username) {
		System.out.println("Enter the account number for the account to make the deposit");
		int accNum = scan.nextInt();
		scan.nextLine();
		System.out.println("How much would you like to deposit?");
		double amt = scan.nextDouble();
		scan.nextLine();
		
		Account a = as.findByAccNum(accNum);
		
		if (a!=null && a.getAccountStatus().toLowerCase().equals("approved") && amt > 0) {
			
			a.setAccountBalance(a.getAccountBalance()+amt);
			
			if (as.updateAccount(a)) {
				System.out.println("You have deposited $"+amt+" into the account ending in "+Integer.toString(accNum).substring(5));
				System.out.println("Your new balance is $"+a.getAccountBalance());
				System.out.println("================================================");
				System.out.println("Enter 'P' to return to the previous menu, \n"
						+ "'E' to exit.");
				String o = scan.nextLine().toUpperCase();
				switch(o) {
				case "P":
					manageAccount(username);
					break;
				case "E":
					exitMessage();
					break;
				}
			} else {
				System.out.println("Something went wrong. Please try again.");
				deposit(username);
			}
		} else {
			System.out.println("Error. Please try again.");
			deposit(username);
		}
	}

	private void viewBalance(String username) {
		
		double totBalance = 0;
		
		List<Account> list = as.findByAccOwner(username);
		for (Account a : list) {
			int accNum = a.getAccountNumber();
			String accType = a.getAccountType();
			double balance = a.getAccountBalance();
			totBalance += balance;
			
			System.out.println("The balance in "+accType+" account *****"+Integer.toString(accNum).substring(5)+" is $"+balance);
		}
		System.out.println("------------------------------------------------");
		System.out.println("The total balance in all account(s) is $"+totBalance);
		System.out.println("================================================");
		System.out.println("Enter 'P' to return to the previous menu, \n"
				+ "'E' to exit.");
		String o = scan.nextLine().toUpperCase();
		switch(o) {
		case "P":
			manageAccount(username);
			break;
		case "E":
			exitMessage();
			break;
		}
		
	}
	
	private void exitMessage() {
		
		String a = "Wealth consists not in having great possessions, but in having few wants. \n"
				+ "-- Epictetus";
		String b = "Everyday is a bank account, and time is our currency. No one is rich, no one is poor, we've got 24 hours each. \n"
				+ "-- Christopher Rice";
		String c = "Believe you can and you're halfway there. \n"
				+ "--Theodore Roosevelt";
		String d = "Frugality includes all the other virtues. \n"
				+ "--Cicero";
		String e = "Annual income twenty pounds, annual expenditure nineteen six, result happiness. \n"
				+ "Annual income twenty pounds, annual expenditure twenty pound ought and six, result misery. \n"
				+ "-- Charles Dickens";
		String f = "A nickel ain't worth a dime anymore. \n"
				+ "--Yogi Berra";
		String g = "It is not the man who has too little, but the man who craves more, that is poor. \n"
				+ "-- Seneca";
		
		String [] quotes = {a, b, c, d, e, f, g};
		
		int rand = (int)(Math.random() * 7) + 0;
		
		String msg = quotes[rand];
		
		System.out.println("We appreciate your business. Have a wonderful day! \n \n"
				+ msg);
		
	}


}


