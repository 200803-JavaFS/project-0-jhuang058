

import com.revature.models.Account;

public class AccountTest {
		
		public static Account acc;
		double balance;
		double amtToDeposit;
		double amtToWithdraw;
		
		@BeforeClass
		public static void makeAccount() {
			System.out.println("In BeforeClass");
			acc = new Account("Alice", "123456789", "11111111", 500.00);
		}
		
		@Before
		public void showMenu() {
			System.out.println("In Before");
			double amtToDeposit = 100.00;
			double amtToWithdraw = 50.00;
		}
		
		@After
		public void clearResult() {
			System.out.println("In After");
			balance = 500.00;
		}
		
		@AfterClass
		public static void clearMO() {
			System.out.println("In AfterClass");
			acc = null;
		}
		
		@Test
		public void testShowBalance() {
			System.out.println("Testing show balance");
			balance = acc.getBalance();
		}	
		
		@Test
		public void testDeposit() {
			System.out.println("Testing deposit");
			balance = acc.deposit(amtToDeposit);
			assertTrue(balance == 600.00);
		}
		
		@Test
		public void testWithdraw() {
			System.out.println("Testing withdraw");
			balance = acc.withdraw(amtToWithdraw);
			assertTrue(balance == 450.00);
		}
		
		
	}

	
}
