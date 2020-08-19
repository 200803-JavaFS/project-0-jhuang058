package com.revature.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.revature.models.Account;
import com.revature.models.User;
import com.revature.services.AccountService;
import com.revature.services.UserService;

public class ConsoleUtil {

	private static final Scanner scan = new Scanner(System.in); // where to close scanner?
	private AccountService as = new AccountService();
	private UserService us = new UserService();

	public void beginApp() {

		System.out.println("Welcome! \n"
				+ "Please type the number of action from the following menu and press enter: \n"
				+ "1. Log in as an existing user \n" + "2. Register as a new user \n" + "E. Exit the application");

		String i = scan.nextLine().toUpperCase();
		optionSwitch(i);

	}

	private void optionSwitch(String i) {

		switch (i) {
		case "1":
			login();
			break;
		case "2":
			System.out.println("Please create a username");
			String newUsername = scan.nextLine();
			System.out.println("Please create a password \n" + "(the password must be have a minimum of 6 characters)");
			String newPassword = scan.nextLine();

			System.out.println("Enter your first name");
			String fname = scan.nextLine();
			System.out.println("Enter your last name");
			String lname = scan.nextLine();
			System.out.println("Enter your date of birth in the format: yyyy-mm-dd");
			String dob = scan.nextLine();

			if (us.registerCustomer(newUsername, newPassword, fname, lname, dob) != null) {
				login();
			} else {
				beginApp();
			}

			break;
		case "E":
			exitMessage();
			beginApp();
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

		User user = us.login(username, password);

		if (user != null) {
			String fname = user.getFirstName();
			String lname = user.getLastName();
			int userType = user.getUserType();

			switch (userType) {
			case 1:
				System.out.println("Hello " + fname + " " + lname + "! Welcome to Your Favorite Bank.");
				customerMenu(user);
				break;
			case 2:
				System.out.println("Hello " + fname + " " + lname + "!");
				employeeMenu(user);
				break;
			case 3:
				System.out.println("Hello " + fname + " " + lname + "!");
				adminMenu(user);
				break;
			}
		} else {
			beginApp();
		}
	}

	private void customerMenu(User user) {
		System.out.println("What would you like help with today?");
		System.out.println("================================================");
		System.out.println("1. Access existing account(s) \n" + "2. Apply for a new bank account \n"
				+ "3. Edit your personal or user information \n" + "E. Exit the application");

		String opt = scan.nextLine().toUpperCase();

		switch (opt) {
		case "1":
			manageAccount(user);
			break;
		case "2":
			createAccountOwnership(user);
			break;
		case "3":
			editInfo(user);
			break;
		case "E":
			exitMessage();
			us.logout();
			beginApp();
			break;
		default:
			System.out.println("Option invalid.");
			customerMenu(user);
		}
	}

	private void employeeMenu(User user) {
		System.out.println("Please select an option from the list: \n" + "1. View pending account applications \n"
				+ "2. View customer accounts \n" + "3. View account balances \n" + "4. View customer information \n"
				+ "E. Exit");
		String option = scan.nextLine().toUpperCase();
		switch (option) {
		case "1":
			viewPendingApps(user);
			break;
		case "2":
			showAccountsE(user);
			break;
		case "3":
			viewBalanceE(user);
			break;
		case "4":
			viewCustomerInfo(user);
			break;
		case "E":
			System.out.println("'Til next time!");
			us.logout();
			beginApp();
			break;
		default:
			System.out.println("Invalid option. Please select an option from the menu.");
			employeeMenu(user);
			break;
		}
	}

	private void adminMenu(User user) {
		System.out.println("Please select an option from the list: \n" + "1. View account applications \n"
				+ "2. View customer accounts \n"
				+ "3. View account balances \n"
				+ "4. View customer information \n"
				+ "5. Manage account(s) \n" + "6. Cancel account(s) \n" + "E. Exit");
		String option2 = scan.nextLine().toUpperCase();
		switch (option2) {
		case "1":
			viewPendingAppsA(user);
			break;
		case "2": //view customer accounts
			showAccountsE(user);
			break;
		case "3":
			viewBalanceE(user);
			break;
		case "4":
			viewCustomerInfo(user);
			break;
		case "5": // manage accounts
			manageAccountA(user);
			break;
		case "6": // manage accounts
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
						adminMenu(user);
					} else {
						adminMenu(user);
					}
				} else {
					adminMenu(user);
				}
			} else {
				System.out.println("Account number not found. Please try again.");
				adminMenu(user);
			}
			break;
		case "E":
			System.out.println("Adios. We shall meet again!");
			us.logout();
			beginApp();
			break;
		default:
			System.out.println("Invalid option. Please select an option from the menu.");
			adminMenu(user);
			break;
		}
	}

	private void viewCustomerInfo(User user) {
		System.out.println("Enter customer's first name");
		String fname = scan.nextLine();
		System.out.println("Enter customer's last name");
		String lname = scan.nextLine();

		User u = us.findByName(fname, lname);
		System.out.println("Name: " + u.getFirstName() + " " + u.getLastName() + "\n" + "Username: " + u.getUsername()
				+ "\n" + "DOB: " + u.getdOB());

		System.out.println("================================================");
		System.out.println("Enter 'P' to return to the previous menu, \n" + "'E' to exit.");
		String o = scan.nextLine().toUpperCase();
		switch (o) {
		case "P":
			if (user.getUserType() == 2) {
				employeeMenu(user);
			}else {
				adminMenu(user);
			}
			break;
		case "E":
			System.out.println("Ciao!");
			us.logout();
			beginApp();
			break;
		default:
			System.out.println("Please enter a valid option.");
			viewCustomerInfo(user);
			break;
		}
	}

	private void viewBalanceE(User user) {

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

				System.out.println("The balance in " + accType + " account " + accNum + " is $" + balance);
			}

			System.out.println("The total balance in user's account(s) is $" + totBalance);

			System.out.println("================================================");
			System.out.println("Enter 'P' to return to the previous menu, \n" + "'E' to exit.");
			String o = scan.nextLine().toUpperCase();
			switch (o) {
			case "P":
				if (user.getUserType() == 2) {
					employeeMenu(user);
				}else {
					adminMenu(user);
				}
				break;
			case "E":
				System.out.println("Ciao. 'Til next time!");
				us.logout();
				beginApp();
				break;
			default:
				System.out.println("Please enter a valid option.");
				viewBalanceE(user);
				break;
			}
		} else {
			System.out.println("Username not found. Please try again.");
			viewBalanceE(user);
		}
	}

	private void showAccountsE(User user) {
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
				if (user.getUserType() == 2) {
					employeeMenu(user);
				} else {
					adminMenu(user);
				}
				break;
			case "E":
				System.out.println("Ciao!");
				us.logout();
				beginApp();
				break;
			default:
				System.out.println("Please enter a valid option.");
				showAccountsE(user);
				break;
			}
		} else {
			System.out.println("Username not found. Please try again.");
			showAccountsE(user);
		}

	}

	private void editInfo(User user) {
		String username = user.getUsername();

		System.out.println("What information would you like to update? \n" + "1. password \n" + "2. first name \n"
				+ "3. last name \n" + "P. return to previous menu \n" + "E. Exit.");

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
						editInfo(user);
					}
				} else {
					System.out.println(
							"Error: your passwords do not match, or it does not meet the password requirement. Please try again.");
					editInfo(user);
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
				editInfo(user);
			} else {
				System.out.println("Something went wrong. Please try again.");
				editInfo(user);
			}
			break;
		case "3":
			System.out.println("Enter your new last name.");
			String newlname = scan.nextLine();
			user.setLastName(newlname);
			if (us.updateUser(user)) {
				System.out.println("Your last name has been updated.");
				editInfo(user);
			} else {
				System.out.println("Something went wrong. Please try again.");
				editInfo(user);
			}
			break;
		case "P":
			customerMenu(user);
			break;
		case "E":
			exitMessage();
			us.logout();
			beginApp();
			break;
		default:
			System.out.println("Option invalid. Please try again.");
			editInfo(user);
		}
	}

	private void createAccountOwnership(User user) {
		String username = user.getUsername();
		List<User> owners = new ArrayList<User>();
		owners.add(user);

		System.out.println("Is this a joint account? Enter [yes]/[no]");

		if (scan.nextLine().toLowerCase().equals("yes")) {

			System.out.println("Enter the first name, last name, and date of birth (yyyy-mm-dd) of the second owner.");
			String fname2 = scan.nextLine();
			String lname2 = scan.nextLine();
			String dob2 = scan.nextLine();

			System.out.println("Is " + fname2 + " " + lname2 + " a registered customer with the bank? [yes]/[no] \n"
					+ "enter [P] to return to the customer menu.");
			String ans = scan.nextLine().toLowerCase();

			if (ans.equals("yes")) {
				User owner2 = us.findByName(fname2, lname2);
				owners.add(owner2);
				createAccount(user, owners);
			} else if (ans.equals("no")) {
				System.out.println("Please create a username for the second owner.");
				String username2 = scan.nextLine();
				System.out.println("Create a password. (Minimum 6 characters)");
				String pw2 = scan.nextLine();
				User owner2 = us.registerCustomer(username2, pw2, fname2, lname2, dob2);
				owners.add(owner2);
				createAccount(user, owners);
			} else if (ans.equals("p")) {
				customerMenu(user);
			} else {
				createAccountOwnership(user);
			}
		} else {
			createAccount(user, owners);
		}
	}

	private void createAccount(User user, List<User> owners) {
		System.out.println("Enter [1] to open a new Checking account \n" + "[2] to open a new Savings account \n"
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
			customerMenu(user);
			break;
		default:
			System.out.println("The account type is not available. Please try again.");
			createAccount(user, owners);
			break;
		}

		Account a = new Account(type, owners, 0.00, "pending");

		if (as.insertAccount(a, owners)) {
			as.insertOwnership(owners);
			int newAccNum = as.pullNewAccNum(user, owners);
			System.out.println("Thanking you for opening a new " + type + " Account. "
					+ "Your new bank account number is: "+newAccNum +"\n"
					+ "Please allow up to 24 hours for your account to be approved.");
			System.out.println("================================================");
			System.out.println("Enter 'P' to return to the previous menu, \n" + "'E' to exit.");
			String o = scan.nextLine().toUpperCase();
			switch (o) {
			case "P":
				customerMenu(user);
				break;
			case "E":
				exitMessage();
				us.logout();
				beginApp();
				break;
			default:
				System.out.println("Please enter a valid option.");
				createAccount(user, owners);
				break;
			}
		} else {
			System.out.println("Something went wrong, please try again.");
			createAccountOwnership(user);
		}

	}

	private void manageAccount(User user) {
		List<Account> allAccounts = as.findByAccOwner(user.getUsername());
		List<Integer> allAccountNums = new ArrayList<Integer>();
		List<Integer> activeAccNums = new ArrayList<Integer>();
		for (Account a : allAccounts) {
			System.out.println(a);
			allAccountNums.add(a.getAccountNumber());
			if (a.getAccountStatus().toLowerCase().equals("approved")) {
				activeAccNums.add(a.getAccountNumber());
			}
		}
		
		System.out.println("===========================================");
		System.out.println("What would you like to do?");
		System.out.println("1. View Account Info");
		System.out.println("2. View Account Balance in All Accounts");
		System.out.println("3. Deposit, Withdraw, or Transfer Funds");
		System.out.println("P. Return to previous menu");
		System.out.println("E. Exit");
		String option = scan.nextLine().toUpperCase();
		
		switch(option) { 
	case "2": // viewBalance
		viewBalance(user);
		System.out.println("===================================================");
		System.out.println("Enter 'P' to return to the previous menu, \n" + "'E' to exit.");
		String o = scan.nextLine().toUpperCase();
		switch (o) {
		case "P":
			manageAccount(user);
		case "E":
			exitMessage();
			us.logout();
			beginApp();
			break;
		default:
			System.out.println("Please enter a valid option.");
			manageAccount(user);
			break;
		}
		break;
	case "1":	//view account info
		System.out.println("Enter account number.");
		int j = scan.nextInt();
		scan.nextLine();
		if (allAccountNums.contains(j)) {
			Account a = as.findByAccNum(j);
			showAccInfo(a);
			System.out.println("===================================================");
			System.out.println("Enter 'P' to return to the previous menu, \n" + "'E' to exit.");
			String o1 = scan.nextLine().toUpperCase();
			switch (o1) {
			case "P":
				manageAccount(user);
				break;
			case "E":
				exitMessage();
				us.logout();
				beginApp();
				break;
			default:
				System.out.println("Please enter a valid option.");
				manageAccount(user);
				break;
			}
		} else {
			System.out.println("Account does not exist / You do not have access to that account.");
			manageAccount(user);
		}
		break;
	case "3":
		if (activeAccNums.size() == 0) {
		System.out.println("You do not have any active accounts at this time.");
		customerMenu(user);
	}
		
		System.out.println("Enter the account number for the account you would like to access;");
		System.out.println("[0] to return to the main menu");
		int accNum = scan.nextInt();
		scan.nextLine();

		if (activeAccNums.contains(accNum)) {
			
			Account a1 = as.findByAccNum(accNum);
			accountTransactionMenu(a1, user);

		} else if (accNum == 0) {
			customerMenu(user);
		} else {
			System.out.println("Account does not exist / You do not have access to that account.");
			manageAccount(user);
		}
		
		break;
	case "P":
		customerMenu(user);
		break;
	case "E":
		exitMessage();
		us.logout();
		beginApp();
		break;
	default:
		System.out.println("Please enter a valid option.");
		manageAccount(user);
		break;
		
		}
			}

	private void manageAccountA(User user) {
		System.out.println("Enter the account number for the account you would like to access.");
		int accNum = scan.nextInt();
		scan.nextLine();
		Account a1 = as.findByAccNum(accNum);

		if (a1 != null) {

			accountTransactionMenu(a1, user);

		} else {
			System.out.println("Account does not exist.");
			manageAccountA(user);
		}
	}

	private void accountTransactionMenu(Account a1, User user) {
		int userType = user.getUserType();

		System.out.println("What would you like to do?");
		System.out.println("1. Deposit");
		System.out.println("2. Withdraw");
		System.out.println("3. Transfer Funds");
		System.out.println("P. Return to previous menu");
		System.out.println("E. Exit");
		String option = scan.nextLine().toUpperCase();

		double b = a1.getAccountBalance();
		System.out.println("Current account balance: $" + b);

		switch (option) {
		case "1": // deposit
			System.out.println("How much would you like to deposit?");
			double amt = scan.nextDouble();
			scan.nextLine();
			
			
			if (as.deposit(a1, amt) == -1) {
				accountTransactionMenu(a1, user);
			} else {
				System.out.println("================================================");
				System.out.println("Enter 'P' to return to the previous menu, \n" + "'E' to exit.");
				String o2 = scan.nextLine().toUpperCase();
				switch (o2) {
				case "P":
					accountTransactionMenu(a1, user);
					break;
				case "E":
					exitMessage();
					us.logout();
					beginApp();
					break;
				default:
					System.out.println("Please enter a valid option.");
					accountTransactionMenu(a1, user);
					break;
				}
			}
			break;
		case "2": // withdraw
			System.out.println("How much would you like to withdraw?");
			double amtW = scan.nextDouble();
			scan.nextLine();

			if (as.withdraw(a1, amtW) == -1) {
				accountTransactionMenu(a1, user);

			} else {
				System.out.println("================================================");
				System.out.println("Enter 'P' to return to the previous menu, \n" + "'E' to exit.");
				String o3 = scan.nextLine().toUpperCase();
				switch (o3) {
				case "P":
					accountTransactionMenu(a1, user);
					break;
				case "E":
					exitMessage();
					us.logout();
					beginApp();
					break;
				default:
					System.out.println("Please enter a valid option.");
					accountTransactionMenu(a1, user);
					break;
				}
			}

			break;
		case "3": // transfer
			System.out.println("How much would you like to transfer?");
			double amtT = scan.nextDouble();
			scan.nextLine();
			System.out.println("Enter the account number for the destination account");
			int accTo = scan.nextInt();
			scan.nextLine();
			Account a2 = as.findByAccNum(accTo);
			
			if (as.transferMoney(a1, a2, amtT) == null) {
				accountTransactionMenu(a1, user);

			} else {
				System.out.println("================================================");
				System.out.println("Enter 'P' to return to the previous menu, \n" + "'E' to exit.");
				String o4 = scan.nextLine().toUpperCase();
				switch (o4) {
				case "P":
					accountTransactionMenu(a1, user);
					break;
				case "E":
					exitMessage();
					us.logout();
					beginApp();
					break;
				default:
					System.out.println("Please enter a valid option.");
					accountTransactionMenu(a1, user);
					break;
				}
			}
			break;
		case "P":
			if (userType == 3) {
				adminMenu(user);
			} else {
				manageAccount(user);
			}
			break;
		case "E":
			exitMessage();
			us.logout();
			beginApp();
			break;
		default:
			System.out.println("Invalid option. Please select an option from the menu.");
			accountTransactionMenu(a1, user);
			break;
		}

	}

	private void showAccInfo(Account a) {
		System.out.println("Account Number: " + a.getAccountNumber());
		System.out.println("Account Type: " + a.getAccountType());
		System.out.println("Account Balance: $" + a.getAccountBalance());
		List<String> owners = a.listOwnerNames();
		// iterate over owner(s)
		for (int i = 1; i <= owners.size(); i++) {
			System.out.println("Account Owner" + i + ": " + owners.get(i-1));
		}

		System.out.println("Account status: " + a.getAccountStatus());

	}

	private void viewPendingApps(User user) {
		List<Account> list = as.viewPending();

		if (list.size() == 0) {
			System.out.println("There are no pending applications at this time.");
			System.out.println("=========================================================");
			employeeMenu(user);
		} else {
			for (int i = 0; i < list.size(); i++) {
				System.out.println(list.get(i));
			}

			System.out.println("=========================================================");
			System.out.println("1. Approve all \n" + "2. Deny all \n" + "3. Approve/Deny by account number \n"
					+ "4. Approve/Deny by account owner \n" + "P. Return to previous menu \n" + "E. Exit");
			String action = scan.nextLine().toUpperCase();

			switch (action) {
			case "1": // approve all
				if (as.changeAllPending("approved")) {
					employeeMenu(user);
				}
				break;
			case "2": // deny all
				if (as.changeAllPending("denied")) {
					employeeMenu(user);
				}
				break;
			case "3": // approve by account num
				System.out.println("Enter account number");
				int i = scan.nextInt();
				scan.nextLine();
				System.out.println("Enter 1 to approve, 2 to deny account application");
				String i2 = scan.nextLine();
				switch (i2) {
				case "1":
					as.changeStatusByNum(i, "approved");
					viewPendingApps(user);
					break;
				case "2":
					as.changeStatusByNum(i, "denied");
					viewPendingApps(user);
					break;
				default:
					viewPendingApps(user);
					break;
				}
			case "4": // approve by account owner
				System.out.println("Enter username of account owner");
				String username = scan.nextLine();
				System.out.println("Enter 1 to approve, 2 to deny user's account application(s)");
				String i3 = scan.nextLine();
				switch (i3) {
				case "1":
					as.changeStatusByOwner(username, "approved");
					viewPendingApps(user);
					break;
				case "2":
					as.changeStatusByOwner(username, "denied");
					viewPendingApps(user);
					break;
				default:
					viewPendingApps(user);
					break;
				}
				break;
			case "P":
				employeeMenu(user);
				break;
			case "E":
				System.out.println("Adios!");
				us.logout();
				beginApp();
				break;
			default:
				System.out.println("Invalid option. Please select an option from the menu.");
				viewPendingApps(user);
				break;
			}
		}

	}

	private void viewPendingAppsA(User user) {
		List<Account> list = as.viewPending();

		if (list.size() == 0) {
			System.out.println("There are no pending applications at this time.");
			System.out.println("=========================================================");
			adminMenu(user);
		} else {
			for (int i = 0; i < list.size(); i++) {
				System.out.println(list.get(i));
			}

			System.out.println("=========================================================");
			System.out.println("1. Approve all \n" + "2. Deny all \n" + "3. Approve/Deny by account number \n"
					+ "4. Approve/Deny by account owner \n" + "P. Return to previous menu \n" + "E. Exit");
			String action = scan.nextLine().toUpperCase();

			switch (action) {
			case "1":
				if (as.changeAllPending("approved")) {
					adminMenu(user);
				}
				break;
			case "2":
				if (as.changeAllPending("denied")) {
					adminMenu(user);
				}
				break;
			case "3":
				System.out.println("Enter account number");
				int i = scan.nextInt();
				scan.nextLine();
				System.out.println("Enter 1 to approve, 2 to deny account application");
				String i2 = scan.nextLine();
				switch (i2) {
				case "1":
					as.changeStatusByNum(i, "approved");
					viewPendingApps(user);
					break;
				case "2":
					as.changeStatusByNum(i, "denied");
					viewPendingApps(user);
					break;
				default:
					viewPendingApps(user);
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
					viewPendingApps(user);
					break;
				case "2":
					as.changeStatusByOwner(username, "denied");
					viewPendingApps(user);
					break;
				default:
					viewPendingApps(user);
					break;
				}
				break;
			case "P":
				adminMenu(user);
				break;
			case "E":
				System.out.println("Adios!");
				us.logout();
				beginApp();
				break;
			default:
				System.out.println("Invalid option. Please select an option from the menu.");
				viewPendingApps(user);
				break;
			}
		}

	}

	private void viewBalance(User user) {
		double totBalance = 0;

		List<Account> list = as.findByAccOwner(user.getUsername());
		for (Account a : list) {
			int accNum = a.getAccountNumber();
			String accType = a.getAccountType();
			double balance = a.getAccountBalance();
			totBalance += balance;

			System.out.println("The balance in " + accType + " account *****" + Integer.toString(accNum).substring(5)
					+ " is $" + balance);
		}
		System.out.println("------------------------------------------------");
		System.out.println("The total balance in all account(s) is $" + totBalance);
	}

	private void exitMessage() {

		String a = "Wealth consists not in having great possessions, but in having few wants. \n" + "-- Epictetus";
		String b = "Everyday is a bank account, and time is our currency. No one is rich, no one is poor, we've got 24 hours each. \n"
				+ "-- Christopher Rice";
		String c = "Believe you can and you're halfway there. \n" + "--Theodore Roosevelt";
		String d = "Frugality includes all the other virtues. \n" + "--Cicero";
		String e = "Annual income twenty pounds, annual expenditure nineteen six, result happiness. \n"
				+ "Annual income twenty pounds, annual expenditure twenty pound ought and six, result misery. \n"
				+ "-- Charles Dickens";
		String f = "A nickel ain't worth a dime anymore. \n" + "--Yogi Berra";
		String g = "It is not the man who has too little, but the man who craves more, that is poor. \n" + "-- Seneca";

		String[] quotes = { a, b, c, d, e, f, g };

		int rand = (int) (Math.random() * 7) + 0;

		String msg = quotes[rand];
		
		System.out.println("\n");
		System.out.println("We appreciate your business. Have a wonderful day! \n \n \n" + msg);
		System.out.println("\n\n");

	}

}
