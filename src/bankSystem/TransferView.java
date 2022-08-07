package bankSystem;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

import javax.swing.SwingConstants;
import javax.swing.JButton;

public class TransferView {

	private JFrame frame;
	private JTextField edtAmount;
	private JTextField edtToAccount;

	static Customer currentCustomer;
	static Connection currentConn;
	static int fromAccount;

	public TransferView(Connection connection, Customer customer, int fromAccount) {
		currentConn = connection;
		currentCustomer = customer;
		this.fromAccount = fromAccount;
		initialize();
	}

	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 186);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		edtAmount = new JTextField();
		edtAmount.setText("Amount");
		edtAmount.setBounds(22, 82, 130, 40);
		frame.getContentPane().add(edtAmount);
		edtAmount.setColumns(10);
		
		JLabel lblTransfer = new JLabel("Transfer");
		lblTransfer.setHorizontalAlignment(SwingConstants.CENTER);
		lblTransfer.setFont(new Font("Arial", Font.BOLD, 25));
		lblTransfer.setBounds(22, 23, 410, 30);
		frame.getContentPane().add(lblTransfer);
		
		edtToAccount = new JTextField();
		edtToAccount.setText("To (Account Number)");
		edtToAccount.setColumns(10);
		edtToAccount.setBounds(181, 82, 159, 40);
		frame.getContentPane().add(edtToAccount);
		
		JButton btnTransfer = new JButton("Transfer");
		btnTransfer.addActionListener((ActionListener) new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(edtAmount.getText().toString().length() != 0 && edtToAccount.getText().toString().length() != 0){
					if(transfer(fromAccount, 
							Integer.valueOf(edtToAccount.getText()), 
							Float.valueOf(edtAmount.getText()))) {
						frame.dispose();
						CustomerScreen.refreshAccounts();
					}
				} else {									
					if(edtAmount.getText().toString().length() == 0){
						JOptionPane.showMessageDialog(frame, "Please enter an amount!");
					}
					if(edtToAccount.getText().toString().length() == 0) {
						JOptionPane.showMessageDialog(frame, "Please enter an account!");
					}
				}
				
			}
		});
		btnTransfer.setBounds(352, 83, 90, 40);
		frame.getContentPane().add(btnTransfer);
		
		JLabel lbl$ = new JLabel("$");
		lbl$.setBounds(6, 94, 14, 16);
		frame.getContentPane().add(lbl$);
		
		JLabel lblNumber = new JLabel("#");
		lblNumber.setBounds(164, 94, 14, 16);
		frame.getContentPane().add(lblNumber);
		
		frame.setVisible(true);
	}
	
	public boolean transfer(int fromNumID, int toNumID, float amount) {
		BankAccount fromBa = CustomerScreen.getAccount(fromNumID);
		BankAccount toBa = CustomerScreen.getAccount(toNumID);
		
		if(fromBa.withdraw(amount)){
			toBa.deposit(amount);			
		} else {
			System.out.println("Insufficient fund. Transfer unsuccesfully!");
			JOptionPane.showMessageDialog(frame, "Insufficient fund. Transfer unsuccesfully!");
			return false;
		}
		
		if(CustomerScreen.updateBalance(fromBa.getBalance(), fromNumID) && CustomerScreen.updateBalance(toBa.getBalance(), toNumID)){
			System.out.println("Transfer succesfully!");
			JOptionPane.showMessageDialog(frame, "Transfer succesfully!");
			return true;
			
		} else {
			System.out.println("Transfer unsuccesfully!");
			JOptionPane.showMessageDialog(frame, "Transfer unsuccesfully!");
			return false;
		}
	}
}
