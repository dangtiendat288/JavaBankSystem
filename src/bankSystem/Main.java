package bankSystem;

import java.awt.BorderLayout;
import java.awt.Color;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.*;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Main {
	static ArrayList<BankAccount> bankAccounts = new ArrayList<>();
	static Customer currentCustomer;
	static Connection currentConn;
	static JFrame frame;
	
	public static void main(String[] args) throws Exception {			
		Connection connection;
		String jdbcURL = "jdbc:postgresql://localhost:5432/customer";
		String username = "postgres";
		String password = "meomeomeo123";
		
		
		JLabel lbUsername = new JLabel("Username");
		lbUsername.setBounds(20, 0, 75, 75);
		
//		JLabel lbPassword = new JLabel("Password");
//		lbUsername.setBounds(40, 0, 75, 75);
		
		JPanel firstPanel = new JPanel();
		firstPanel.setBackground(Color.RED);
		firstPanel.setBounds(0, 0, 420, 140);
		firstPanel.add(lbUsername);
//		firstPanel.add(lbPassword);
		firstPanel.setLayout(new BorderLayout());
		
//		JPanel secondPanel = new JPanel();
//		secondPanel.setBackground(Color.BLUE);
//		secondPanel.setBounds(0, 140, 420, 140);
//		secondPanel.add(lbPassword);
//		secondPanel.setLayout(new BorderLayout());
		
		frame = new JFrame();
		frame.setTitle("TD Bank");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(null);
//		frame.setResizable(false);
		frame.setSize(420,420);
		frame.setVisible(true);		
		frame.add(firstPanel);
//		frame.add(secondPanel);
//		frame.add(lbUsername);
		
//		frame.pack();
		
		try {
			connection = DriverManager.getConnection(jdbcURL, username, password);
			currentConn = connection;
			System.out.println("Connected\n");				
			
			createCustomersTable(connection);
			createAccountsTable(connection);
			
//			Customer a = new Customer("Minie.Nguyen", "1p2a3s4s5");		
//			if(createCustomer(a, connection)) {
//				System.out.println("Inserted successfully!\n");
//			};
			
			ArrayList<Customer> customers = getCustomers();
			for(Customer customer : customers) {
				System.out.println(customer.toString());
			}
			
			if(login("Minnie.Nguyen","54321")) {
				System.out.println(currentCustomer.toString());
//				bankAccounts = getAccounts();
//				for(BankAccount ba : bankAccounts) {
//					System.out.println(ba.toString());
//				}
			}
			
//			BankAccount a = getAccount(1);
//			System.out.println(a.toString());
			
//			if(deposit(1, 500)) {
//				BankAccount a = getAccount(1);
//				System.out.println(a.toString());
//			};
			
			if(transfer(1, 2, 500)) {
				BankAccount fromBa = getAccount(1);
				BankAccount toBa = getAccount(2);
				System.out.println(fromBa.toString());
				System.out.println(toBa.toString());
			};
			
//			if(removeAccount(1)){
//				for(BankAccount ba : bankAccounts) {
//					System.out.println(ba.toString());
//				}
//			}
			
//			if(updateName("Minnie.Nguyen", currentCustomer.getID())){
//				System.out.println(currentCustomer.toString());
//			}
			
//			if(updatePassword("54321", currentCustomer.getID())){
//				System.out.println(currentCustomer.toString());
//			}
//			currentCustomer = customers.get(0);
//			
//			BankAccount b = currentCustomer.createAccount("checking", currentCustomer.getID());
//			BankAccount c = currentCustomer.createAccount("savings", currentCustomer.getID());
//			
//			if(createAccount(b, connection)) {
//				System.out.println("Created account successfully!\n");
//			};
//			
//			if(createAccount(c, connection)) {
//				System.out.println("Created account successfully!\n");
//			};
//			
//			ArrayList<BankAccount> accounts = getAccounts(connection, currentCustomer);
//			for(BankAccount ba : accounts) {
//				System.out.println(ba.toString());
//			}
			
		} catch (SQLException e) {
			System.out.print("Fail to connect to server!");
		}
	}
	
	
	public static void createCustomersTable(Connection connection) {
		String query = "CREATE TABLE IF NOT EXISTS customers("
				+ "id serial PRIMARY KEY,"
				+ "username text UNIQUE NOT NULL,"
				+ "password text NOT NULL"						
				+ ");";
		
		try {
			Statement statement = connection.createStatement();
			statement.executeUpdate(query);
			System.out.println("Customer Table created\n");
		} catch (SQLException e) {
			e.printStackTrace();		
		}
	}
	
	
	public static void createAccountsTable(Connection connection) {
		String query = "CREATE TABLE IF NOT EXISTS accounts("
				+ "numID serial PRIMARY KEY,"
				+ "balance float(2),"
				+ "type text NOT NULL,"		
				+ "customer_id INTEGER REFERENCES customers(id) ON DELETE CASCADE"
				+ ");";
		
		try {
			Statement statement = connection.createStatement();
			statement.executeUpdate(query);
			System.out.println("Accounts Table created\n");
		} catch (SQLException e) {
			e.printStackTrace();		
		}
	}
	
	
	public static ArrayList<BankAccount> getAccounts(){
		ArrayList<BankAccount> res = new ArrayList<>();
		String query = "SELECT * FROM accounts WHERE customer_id = " + currentCustomer.getID();
		try {
			Statement statement = currentConn.createStatement();
			
			ResultSet resultSet = statement.executeQuery(query);
			while(resultSet.next()) {
				int numID = resultSet.getInt("numid");
				Float balance = resultSet.getFloat("balance");
				String type = resultSet.getString("type");
				int customerID = resultSet.getInt("customer_id");
				BankAccount ba = type.equals("Checking") ? new CheckingAccount(customerID) : new SavingsAccount(customerID);
				ba.setBalance(balance);
				ba.setNumber(numID);
				res.add(ba);
			}
			return res;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}			
	}
	
	public static BankAccount getAccount(int accountNumID){
		BankAccount res = null;
		String query = String.format("SELECT * FROM accounts WHERE customer_id = %d AND numid = %d", currentCustomer.getID(), accountNumID);
		try {
			Statement statement = currentConn.createStatement();
			
			ResultSet resultSet = statement.executeQuery(query);
			while(resultSet.next()){
				int numID = resultSet.getInt("numid");
				float balance = resultSet.getFloat("balance");
				String type = resultSet.getString("type");
				int customerID = resultSet.getInt("customer_id");
				BankAccount ba = type.equals("Checking") ? new CheckingAccount(customerID) : new SavingsAccount(customerID);
				ba.setBalance(balance);
				ba.setNumber(numID);
				res = ba;
			}
			return res;
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Account not found!");
			return null;
		}			
	}
	
	
	public static ArrayList<Customer> getCustomers(){
		ArrayList<Customer> res = new ArrayList<>();
		String query = "SELECT * FROM customers";
		try {
			Statement statement = currentConn.createStatement();
			
			ResultSet resultSet = statement.executeQuery(query);
			while(resultSet.next()) {
				int ID = resultSet.getInt("id");
				String username = resultSet.getString("username");
				String password = resultSet.getString("password");				
				Customer a = new Customer(ID, username, password);
//				a.setPassword(password);
				res.add(a);
			}
			return res;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}			
	}
	
	public static Customer getCustomer(int IDInput){		
		String query = "SELECT * FROM customers WHERE id = " + IDInput;
		try {
			Statement statement = currentConn.createStatement();
			Customer res = null;
			ResultSet resultSet = statement.executeQuery(query);
			while(resultSet.next()) {
				int ID = resultSet.getInt("id");
				String username = resultSet.getString("username");
				String password = resultSet.getString("password");				
				res = new Customer(ID, username, password);				
			}
			return res;
		
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Customer not found!");
			return null;
		}			
	}
	
	
	public static boolean createCustomer(Customer customer) {
//		String sql = "INSERT INTO customers (username, password)"
//				+ " VALUES ('" + customer.getUsername() 
//				+ "', '" + customer.getPassword()
//				+ "')";
		
		String query = "INSERT INTO customers (username, password) VALUES (?, ?)";
		
		try {
			PreparedStatement statement = currentConn.prepareStatement(query);
			statement.setString(1, customer.getUsername());
			statement.setString(2, customer.getPassword());			
			statement.executeUpdate();
			return true;
		} catch (SQLException e) {			 
			e.printStackTrace();
			return false;
		}	
	}
	
	
	public static boolean createAccount(BankAccount account) {
		String query = "INSERT INTO accounts (balance, type, customer_id) VALUES (?, ?, ?)";
		
		try {
			PreparedStatement statement = currentConn.prepareStatement(query);
			statement.setFloat(1, account.getBalance());
			statement.setString(2, account.getType());
			statement.setInt(3, account.getCustomerID());
			statement.executeUpdate();
			return true;
		} catch (SQLException e) {			 
			e.printStackTrace();
			return false;
		}	
	}	
	
	public static boolean login(String usernameInput, String passwordInput) {
		String query = String.format("SELECT * FROM customers WHERE username = '%s' AND password = '%s'",usernameInput, passwordInput);
		try {
			Statement statement = currentConn.createStatement();
			
			ResultSet resultSet = statement.executeQuery(query);
			while(resultSet.next()) {
				int ID = resultSet.getInt("id");			
				currentCustomer = new Customer(ID, usernameInput, passwordInput);
				System.out.println("Login succesfully as " + usernameInput + "!");
				return true;
			}
			System.out.println("Customer not found!");
			return false;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}						
	}

	public static boolean updateName(String newName, int ID) {
		String query = String.format("UPDATE customers SET username = '%s' WHERE id = %d", newName, ID);
		
		try {
			Statement statement = currentConn.createStatement();
			statement.executeUpdate(query);
			System.out.println("Update successfully!");
			currentCustomer = getCustomer(ID);
			return true;
		} catch (SQLException e) {			 
			e.printStackTrace();
			System.out.println("Update unsuccessfully!");
			return false;
		}		
	}
	
	public static boolean updatePassword(String newPassword, int ID) {
		String query = String.format("UPDATE customers SET password = '%s' WHERE id = %d", newPassword, ID);
		
		try {
			Statement statement = currentConn.createStatement();
			statement.executeUpdate(query);
			System.out.println("Update successfully!");
			currentCustomer = getCustomer(ID);
			return true;
		} catch (SQLException e) {			 
			e.printStackTrace();
			System.out.println("Update unsuccessfully!");
			return false;
		}		
	}
	
	public static boolean removeAccount(int accountID) {
		String query = String.format("DELETE FROM accounts WHERE numid = %d", accountID);
		
		try {
			Statement statement = currentConn.createStatement();
			statement.executeUpdate(query);
			System.out.println("Remove successfully!");
			bankAccounts = getAccounts();
			return true;
		} catch (SQLException e) {			 
			e.printStackTrace();
			System.out.println("Remove unsuccessfully!");
			return false;
		}		
	}

	public static boolean transfer(int fromNumID, int toNumID, float amount) {
		BankAccount fromBa = getAccount(fromNumID);
		BankAccount toBa = getAccount(toNumID);
		
		if(fromBa.withdraw(amount)){
			toBa.deposit(amount);			
		} else {
			System.out.println("Insufficient fund. Transfer unsuccesfully!");
			return false;
		}
		
		if(updateBalance(fromBa.getBalance(), fromNumID) && updateBalance(toBa.getBalance(), toNumID)){
			System.out.println("Transfer succesfully!");			
			return true;
			
		} else {
			System.out.println("Transfer unsuccesfully!");
			return false;
		}
	}
	
																																									
	public static boolean deposit(int accountNumID, float amount) {
		BankAccount ba = getAccount(accountNumID);
		ba.deposit(amount);
		if(updateBalance(ba.getBalance(), accountNumID)) {
			System.out.println("Deposit succesfully!");			
			return true;
			
		} else {
			System.out.println("Deposit unsuccesfully!");
			return false;
		}
	}
	
	
	public static boolean withdraw(int accountNumID, float amount) {
		BankAccount ba = getAccount(accountNumID);
		ba.withdraw(amount);
		if(updateBalance(ba.getBalance(), accountNumID)) {
			System.out.println("Withdraw succesfully!");			
			return true;
			
		} else {
			System.out.println("Withdraw unsuccesfully!");
			return false;
		}
	}
	
	
	public static boolean updateBalance(float newBalance, int accountNumID) {
		String query = String.format("UPDATE accounts SET balance = '%f' WHERE numid = %d", newBalance, accountNumID);
		
		try {
			Statement statement = currentConn.createStatement();
			statement.executeUpdate(query);
			System.out.println("Update balance successfully!");
			bankAccounts = getAccounts();
			return true;
		} catch (SQLException e) {			 
			e.printStackTrace();
			System.out.println("Update balance unsuccessfully!");
			return false;
		}		
	}
}

//	public static Customer register(String username, String password, int pin) {
//		Customer customer = new Customer(username, pin);
//		customer.setPassword(password);
//		pw.println("username: " + customer.getUsername());
//		pw.println("password: " + customer.getPassword());
//		return customer;
//	};


//os = Files.newOutputStream(Paths.get("writer.txt"), CREATE, APPEND);
//
//
//pw = new PrintWriter(os);
//cusPw = new PrintWriter(Files.newOutputStream(Paths.get("customers.txt"), CREATE, APPEND));

//Customer a = new Customer("Dan.D", 111);
//
//System.out.println("username: " + a.getUsername());
//pw.println("username: " + a.getUsername());
//
//a.setPassword("Meomeomeo123@");
//System.out.println("password: " + a.getPassword());
//pw.println("password: " + a.getPassword());
//BankAccount c = a.createAccount("checking");
//c.deposit(10000);
//System.out.println(c.accountInfo() + "\n");
//
//
//BankAccount s = a.createAccount("savings");
//s.deposit(100000);			
//System.out.println(s.accountInfo() + "\n");
//
//a.transfer(10000, 0, 1);
//
//System.out.println(c.accountInfo() + "\n");
//System.out.println(s.accountInfo() + "\n");
//
//a.transfer(110000, 1, 0);
//
//System.out.println(c.accountInfo() + "\n");
//System.out.println(s.accountInfo() + "\n");
//		
//System.out.println(a.getAccounts().toString());
//pw.println(a.getAccounts().toString());
//pw.close();
//cusPw.println(a.toString());
//cusPw.close();

//try {
//fis = new FileInputStream("customers.txt");
//scnr = new Scanner(fis);
//while(scnr.hasNextLine()) {
//}
//} catch (Exception e) {
//throw new FileNotFoundException();
//}

//static FileInputStream fis;
//static OutputStream os;
//static PrintWriter pw;
//static PrintWriter cusPw;
//static Scanner scnr;