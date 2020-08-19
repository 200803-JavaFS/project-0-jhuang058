import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.revature.models.Account;
import com.revature.services.AccountService;


public class AccountTest {

	public static AccountService as;
	public static Account a;
	public static Account a2;
	double amtD;
	double amtW;
	static double result;
	static double[] resultT;

	@BeforeClass
	public static void makeServices() {
		System.out.println("In BeforeClass");
		as = new AccountService();
		
	}

	@Before
	public void setAmts() {
		System.out.println("In Before");
		a = new Account(111111111, "Checking", null, 100.00, "approved");
		a2 = new Account(222222222, "Savings", null, 100.00, "approved");
		amtD = 100.00;
		amtW = 50.00;
	}

	@After
	public void clearResult() {
		System.out.println("In After");
		result = 0;
		resultT = null;
	}

	@AfterClass
	public static void clearMO() {
		System.out.println("In AfterClass");
		as = null;
		a = null;

	}

	@Test
	public void testDeposit() {
		System.out.println("Testing deposit");
		result = as.deposit(a, amtD);
		assertTrue(result == 200.00);
	}

	@Test
	public void testWithdraw() {
		System.out.println("Testing withdraw");
		result = as.withdraw(a, amtW);
		assertTrue(result == 50.00);
	}
	
	@Test
	public void testTransfer() {
		System.out.println("Testing transfer");
		resultT = as.transferMoney(a, a2, amtW);
		double[] expectedOutput = {50.00, 150.00};
		assertTrue(Arrays.equals(expectedOutput, resultT));
	}

}
