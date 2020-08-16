package com.revature.models;

import java.util.Scanner;

public class Account {
	
	private int accountNumber;
	private String accountType;
	private User accountOwner;
	private double accountBalance;
	private String accountStatus = "pending";
	
	public Account() {
		super();
	}

	
	public Account(String accountType, User accountOwner, double accountBalance,
			String accountStatus) {
		super();
		this.accountType = accountType;
		this.accountOwner = accountOwner;
		this.accountBalance = accountBalance;
		this.accountStatus = accountStatus;
	}
	
	public Account(int accountNumber, String accountType, User accountOwner, double accountBalance,
			String accountStatus) {
		super();
		this.accountNumber = accountNumber;
		this.accountType = accountType;
		this.accountOwner = accountOwner;
		this.accountBalance = accountBalance;
		this.accountStatus = accountStatus;
	}

	public int getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(int accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public User getAccountOwner() {
		return accountOwner;
	}

	public void setAccountOwner(User user) {
		this.accountOwner = user;
	}

	public double getAccountBalance() {
		return accountBalance;
	}

	public void setAccountBalance(double accountBalance) {
		this.accountBalance = accountBalance;
	}

	public String getAccountStatus() {
		return accountStatus;
	}

	public void setAccountStatus(String accountStatus) {
		this.accountStatus = accountStatus;
	}
	
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(accountBalance);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + accountNumber;
		result = prime * result + ((accountOwner == null) ? 0 : accountOwner.hashCode());
		result = prime * result + ((accountStatus == null) ? 0 : accountStatus.hashCode());
		result = prime * result + ((accountType == null) ? 0 : accountType.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Account other = (Account) obj;
		if (Double.doubleToLongBits(accountBalance) != Double.doubleToLongBits(other.accountBalance))
			return false;
		if (accountNumber != other.accountNumber)
			return false;
		if (accountOwner == null) {
			if (other.accountOwner != null)
				return false;
		} else if (!accountOwner.equals(other.accountOwner))
			return false;
		if (accountStatus == null) {
			if (other.accountStatus != null)
				return false;
		} else if (!accountStatus.equals(other.accountStatus))
			return false;
		if (accountType == null) {
			if (other.accountType != null)
				return false;
		} else if (!accountType.equals(other.accountType))
			return false;
		return true;
	}


	@Override
	public String toString() {
		return "Account [accountNumber=" + accountNumber + ", accountType=" + accountType + ", accountOwner="
				+ accountOwner.getFirstName() + " " + accountOwner.getLastName() + ", accountBalance=" + accountBalance + ", accountStatus=" + accountStatus + "]";
	}

	

}
	
	
	
	