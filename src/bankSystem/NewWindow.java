package bankSystem;

import java.awt.*;
import java.sql.Connection;
import java.util.ArrayList;

import javax.swing.*;
public class NewWindow {
	
	private JTextField edtAmount;
	
	static ArrayList<BankAccount> bankAccounts = new ArrayList<>();
	static Customer currentCustomer;
	static Connection currentConn;
	
 JFrame frame = new JFrame();
 JLabel label = new JLabel("Hello!");
 
 NewWindow(){

  initialize();
  
  frame.setVisible(true);
  
 }
 
 private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 450);
		frame.setLayout(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		
		
		JList list = new JList();
		list.setBounds(20, 79, 410, 205);
		frame.add(list);
		
		JLabel lblCustomerName = new JLabel("Customer's name");
		lblCustomerName.setHorizontalAlignment(SwingConstants.CENTER);
		lblCustomerName.setFont(new Font("Arial", Font.BOLD, 25));
		lblCustomerName.setBounds(20, 29, 410, 30);
		frame.add(lblCustomerName);
		
		edtAmount = new JTextField();
		edtAmount.setFont(new Font("Arial", Font.BOLD, 15));
		edtAmount.setBounds(20, 311, 142, 40);
		frame.add(edtAmount);
		edtAmount.setColumns(10);
		
		JButton btnDeposit = new JButton("Deposit");
		btnDeposit.setFont(new Font("Arial", Font.BOLD, 15));
		btnDeposit.setBounds(186, 308, 117, 45);
		frame.add(btnDeposit);
		
		JButton btnWithdraw = new JButton("Withdraw");
		btnWithdraw.setFont(new Font("Arial", Font.BOLD, 15));
		btnWithdraw.setBounds(318, 308, 117, 45);
		frame.add(btnWithdraw);
		
		JButton btnTransfer = new JButton("Transfer");
		btnTransfer.setFont(new Font("Arial", Font.BOLD, 15));
		btnTransfer.setBounds(69, 364, 117, 45);
		frame.add(btnTransfer);
		
		JButton btnNewButton_1_1 = new JButton("New Account");
		btnNewButton_1_1.setFont(new Font("Arial", Font.BOLD, 15));
		btnNewButton_1_1.setBounds(271, 364, 117, 45);
		frame.add(btnNewButton_1_1);
	}

 
}
