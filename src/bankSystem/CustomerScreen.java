package bankSystem;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.JTextField;
import javax.swing.DefaultListModel;
import javax.swing.JButton;

public class CustomerScreen {

	private static JFrame frame;
	private JTextField edtAmount;
	
	static ArrayList<BankAccount> bankAccounts = new ArrayList<>();
	static Customer currentCustomer;
	static Connection currentConn;
	static DefaultListModel<BankAccount> listAccountModel;
	static JList<BankAccount> accountList;
	
	
	public CustomerScreen(Connection currentConn, Customer currentCustomer) {
		
		this.currentCustomer = currentCustomer;
		this.currentConn = currentConn;		
		initialize();
		refreshAccounts();
		
	}

	/**
	 * Initialize the contents of the frame.
	 */
	
	public static void refreshAccounts() {
		listAccountModel.removeAllElements();
		bankAccounts = getAccounts();
		for(BankAccount ba : bankAccounts) {
			listAccountModel.addElement(ba);
			System.out.println("Adding account:" + ba.toString());
		}
	};
	
	private void initialize() {
		
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 450);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		accountList = new JList<>();
		listAccountModel = new DefaultListModel<>();
		accountList.setModel(listAccountModel);
		accountList.addListSelectionListener( new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				System.out.println(accountList.getSelectedValue());
			}
		});
		accountList.setBounds(20, 79, 410, 205);
		frame.getContentPane().add(accountList);
		
		
		JLabel lblCustomerName = new JLabel(currentCustomer.getUsername());
		lblCustomerName.setHorizontalAlignment(SwingConstants.CENTER);
		lblCustomerName.setFont(new Font("Arial", Font.BOLD, 25));
		lblCustomerName.setBounds(20, 29, 410, 30);
		frame.getContentPane().add(lblCustomerName);
		
		edtAmount = new JTextField();
		edtAmount.setText("Amount");
		edtAmount.setFont(new Font("Arial", Font.PLAIN, 15));
		edtAmount.setBounds(30, 311, 132, 40);
		frame.getContentPane().add(edtAmount);
		edtAmount.setColumns(10);
		
		JButton btnDeposit = new JButton("Deposit");
		btnDeposit.addActionListener((ActionListener) new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(accountList.getSelectedValue() != null && edtAmount.getText().toString().length() != 0){
					deposit(accountList.getSelectedValue().getNumber(), Float.valueOf(edtAmount.getText()));
					edtAmount.setText("");
					refreshAccounts();
				} else {				
					if(accountList.getSelectedValue() == null) {
						JOptionPane.showMessageDialog(frame, "Please select an account!");
					} 
					if(edtAmount.getText().toString().length() == 0){
						JOptionPane.showMessageDialog(frame, "Please enter an amount!");
					}
				}				
			}
		});
		btnDeposit.setFont(new Font("Arial", Font.BOLD, 15));
		btnDeposit.setBounds(186, 308, 117, 45);
		frame.getContentPane().add(btnDeposit);
		
		JButton btnWithdraw = new JButton("Withdraw");
		btnWithdraw.addActionListener((ActionListener) new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(accountList.getSelectedValue() != null && edtAmount.getText().toString().length() != 0){
					withdraw(accountList.getSelectedValue().getNumber(), Float.valueOf(edtAmount.getText()));
					edtAmount.setText("");
					refreshAccounts();
				} else {
					if(accountList.getSelectedValue() == null) {
						JOptionPane.showMessageDialog(frame, "Please select an account!");
					} 
					if(edtAmount.getText().toString().length() == 0){
						JOptionPane.showMessageDialog(frame, "Please enter an amount!");
					}
				}
			}
		});
		btnWithdraw.setFont(new Font("Arial", Font.BOLD, 15));
		btnWithdraw.setBounds(318, 308, 117, 45);
		frame.getContentPane().add(btnWithdraw);
		
		JButton btnTransfer = new JButton("Transfer");
		btnTransfer.addActionListener((ActionListener) new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(accountList.getSelectedValue() == null) {
					JOptionPane.showMessageDialog(frame, "Please select a sender account!");
				} else {
					TransferView transferView = new TransferView(currentConn, currentCustomer, accountList.getSelectedValue().getNumber());
				}
			}
		});
		btnTransfer.setFont(new Font("Arial", Font.BOLD, 15));
		btnTransfer.setBounds(20, 365, 117, 45);
		frame.getContentPane().add(btnTransfer);
		
		JButton btnNewAccount = new JButton("New Account");
		btnNewAccount.addActionListener((ActionListener) new ActionListener() {
			public void actionPerformed(ActionEvent e) {
					CreateNewAccountView newAccView = new CreateNewAccountView(currentConn, currentCustomer);				
			}
		});
		btnNewAccount.setFont(new Font("Arial", Font.BOLD, 15));
		btnNewAccount.setBounds(170, 365, 117, 45);
		frame.getContentPane().add(btnNewAccount);
		
		JLabel lbl$ = new JLabel("$");
		lbl$.setBounds(20, 323, 20, 16);
		frame.getContentPane().add(lbl$);
		
		JButton btnRemoveAcc = new JButton("Remove Acc");
		btnRemoveAcc.addActionListener((ActionListener) new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(accountList.getSelectedValue() == null) {
					JOptionPane.showMessageDialog(frame, "Please select an account to remove!");
				} else {
					removeAccount(accountList.getSelectedValue().getNumber());
					refreshAccounts();
				}					
			}
		});
		btnRemoveAcc.setFont(new Font("Arial", Font.BOLD, 15));
		btnRemoveAcc.setBounds(318, 365, 117, 45);
		frame.getContentPane().add(btnRemoveAcc);
		
		frame.setVisible(true);
	}

	public static ArrayList<BankAccount> getAccounts(){
		ArrayList<BankAccount> res = new ArrayList<>();
		String query = "SELECT * FROM accounts WHERE customer_id = " + currentCustomer.getID();
		try {
			Statement statement = currentConn.createStatement();
			
			ResultSet resultSet = statement.executeQuery(query);
			while(resultSet.next()) {
				int numID = resultSet.getInt("accnum");
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
		String query = String.format("SELECT * FROM accounts WHERE customer_id = %d AND accnum = %d", currentCustomer.getID(), accountNumID);
		try {
			Statement statement = currentConn.createStatement();
			
			ResultSet resultSet = statement.executeQuery(query);
			while(resultSet.next()){
				int numID = resultSet.getInt("accnum");
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
	
	public ArrayList<Customer> getCustomers(){
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
	
	public static boolean deposit(int accountNumID, float amount) {
		BankAccount ba = getAccount(accountNumID);
		System.out.println("Deposit to:" + ba);
		ba.deposit(amount);
		if(updateBalance(ba.getBalance(), accountNumID)) {
			System.out.println("Deposit succesfully!");
			JOptionPane.showMessageDialog(frame, "Deposit succesfully!");
			return true;
			
		} else {
			System.out.println("Deposit unsuccesfully!");
			JOptionPane.showMessageDialog(frame, "Deposit unsuccesfully!");
			return false;
		}
	}

	public static boolean withdraw(int accountNumID, float amount) {
		BankAccount ba = getAccount(accountNumID);		
		if(ba.withdraw(amount)) {
			updateBalance(ba.getBalance(), accountNumID); 
			System.out.println("Withdraw succesfully!");
			JOptionPane.showMessageDialog(frame, "Withdraw succesfully!");
			return true;
			
		} else {
			System.out.println("Withdraw unsuccesfully!");
			JOptionPane.showMessageDialog(frame, "Insufficient fund. Withdraw unsuccesfully!");
			return false;
		}
	}
	
	public static boolean updateBalance(float newBalance, int accountNumID) {
		String query = String.format("UPDATE accounts SET balance = '%f' WHERE accnum = %d", newBalance, accountNumID);
		
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
	
	public boolean removeAccount(int accountID) {
		String query = String.format("DELETE FROM accounts WHERE accnum = %d", accountID);
		
		try {
			Statement statement = currentConn.createStatement();
			statement.executeUpdate(query);
			JOptionPane.showMessageDialog(frame, "Remove account successfully!");
			System.out.println("Remove account successfully!");
			bankAccounts = getAccounts();
			return true;
		} catch (SQLException e) {			 
			e.printStackTrace();
			JOptionPane.showMessageDialog(frame, "Remove account unsuccessfully!");
			System.out.println("Remove account unsuccessfully!");
			return false;
		}		
	}
}
