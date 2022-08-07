package bankSystem;

import java.awt.EventQueue;

import javax.swing.JOptionPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.SwingConstants;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.JButton;

public class CreateNewAccountView {

	private JFrame frame;
	ButtonGroup G;
	
	static Customer currentCustomer;
	static Connection currentConn;
	

	/**
	 * Create the application.
	 */
	public CreateNewAccountView(Connection connection, Customer customer) {
		currentConn = connection;
		currentCustomer = customer;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 219);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblNewAccount = new JLabel("New Account");
		lblNewAccount.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewAccount.setFont(new Font("Arial", Font.BOLD, 25));
		lblNewAccount.setBounds(142, 28, 166, 30);
		frame.getContentPane().add(lblNewAccount);
		
		
		JRadioButton rdbtnChecking = new JRadioButton("Checking");
		rdbtnChecking.setFont(new Font("Arial", Font.PLAIN, 20));
		rdbtnChecking.setBounds(30, 104, 117, 23);
		frame.getContentPane().add(rdbtnChecking);
		
		JRadioButton rdbtnSavings = new JRadioButton("Savings");
		rdbtnSavings.setFont(new Font("Arial", Font.PLAIN, 20));
		rdbtnSavings.setBounds(168, 104, 121, 23);
		frame.getContentPane().add(rdbtnSavings);
		
		G = new ButtonGroup();
		G.add(rdbtnChecking);
		G.add(rdbtnSavings);
		
		JButton btnCreate = new JButton("Create");
		btnCreate.addActionListener(new ActionListener() {             
            public void actionPerformed(ActionEvent e)
            {
                
                String res = "";
  
                if(!rdbtnChecking.isSelected() && !rdbtnSavings.isSelected()) {                	
                	JOptionPane.showMessageDialog(frame, "Please select an account type");
                } else {
                	if (rdbtnChecking.isSelected()) {                    
                		BankAccount c = currentCustomer.createAccount("checking", currentCustomer.getID());
                		createAccount(c);
                		res = "New Checking Account Created";
                	}
                	else if (rdbtnSavings.isSelected()) {
                		BankAccount s = currentCustomer.createAccount("savings", currentCustomer.getID());
                		createAccount(s);
                		res = "New Savings Account Created";
                	}               
                	CustomerScreen.refreshAccounts();
                	JOptionPane.showMessageDialog(frame, res);
                	frame.dispose();
                }                
            }
        });
		btnCreate.setFont(new Font("Arial", Font.PLAIN, 20));
		btnCreate.setBounds(303, 97, 117, 40);
		frame.getContentPane().add(btnCreate);
		
		frame.setVisible(true);
	}

	public static boolean createAccount(BankAccount account) {
		String query = "INSERT INTO accounts (accnum, balance, type, customer_id) VALUES (?, ?, ?, ?)";
		
		try {
			PreparedStatement statement = currentConn.prepareStatement(query);
			statement.setInt(1, account.getNumber());
			statement.setFloat(2, account.getBalance());			
			statement.setString(3, account.getType());
			statement.setInt(4, account.getCustomerID());
			statement.executeUpdate();
			return true;
		} catch (SQLException e) {			 
			e.printStackTrace();
			return false;
		}	
	}	
}
