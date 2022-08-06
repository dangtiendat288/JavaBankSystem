package bankSystem;

import javax.swing.JFrame;
import javax.swing.JList;
//import java.awt.BorderLayout;
import javax.swing.JLabel;
import java.awt.Font;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.JButton;

public class CustomerScreen {

	private JFrame frame;
	private JTextField edtAmount;
	
	static ArrayList<BankAccount> bankAccounts = new ArrayList<>();
	static Customer currentCustomer;
	static Connection currentConn;

	public CustomerScreen(Connection currentConn, Customer currentCustomer) {
		
		this.currentCustomer = currentCustomer;
		this.currentConn = currentConn;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 450);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JList list = new JList();
		list.setBounds(20, 79, 410, 205);
		frame.getContentPane().add(list);
		
		JLabel lblCustomerName = new JLabel(currentCustomer.getUsername());
		lblCustomerName.setHorizontalAlignment(SwingConstants.CENTER);
		lblCustomerName.setFont(new Font("Arial", Font.BOLD, 25));
		lblCustomerName.setBounds(20, 29, 410, 30);
		frame.getContentPane().add(lblCustomerName);
		
		edtAmount = new JTextField();
		edtAmount.setFont(new Font("Arial", Font.BOLD, 15));
		edtAmount.setBounds(20, 311, 142, 40);
		frame.getContentPane().add(edtAmount);
		edtAmount.setColumns(10);
		
		JButton btnDeposit = new JButton("Deposit");
		btnDeposit.setFont(new Font("Arial", Font.BOLD, 15));
		btnDeposit.setBounds(186, 308, 117, 45);
		frame.getContentPane().add(btnDeposit);
		
		JButton btnWithdraw = new JButton("Withdraw");
		btnWithdraw.setFont(new Font("Arial", Font.BOLD, 15));
		btnWithdraw.setBounds(318, 308, 117, 45);
		frame.getContentPane().add(btnWithdraw);
		
		JButton btnTransfer = new JButton("Transfer");
		btnTransfer.setFont(new Font("Arial", Font.BOLD, 15));
		btnTransfer.setBounds(69, 364, 117, 45);
		frame.getContentPane().add(btnTransfer);
		
		JButton btnNewButton_1_1 = new JButton("New Account");
		btnNewButton_1_1.setFont(new Font("Arial", Font.BOLD, 15));
		btnNewButton_1_1.setBounds(271, 364, 117, 45);
		frame.getContentPane().add(btnNewButton_1_1);
		
		frame.setVisible(true);
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

}
