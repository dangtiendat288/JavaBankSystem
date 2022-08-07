package bankSystem;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import java.awt.Color;

public class LoginScreen {

	static private JFrame frame;
	static private JTextField edtUsername;
	static private JTextField edtPassword;
	
	static ArrayList<BankAccount> bankAccounts = new ArrayList<>();
	static Customer currentCustomer;
	static Connection currentConn;

	public static void main(String[] args) {
		Connection connection;
		String jdbcURL = "jdbc:postgresql://localhost:5432/customer";
		String username = "postgres";
		String password = "meomeomeo123";
		
		try {
			connection = DriverManager.getConnection(jdbcURL, username, password);
			currentConn = connection;
			System.out.println("Connected\n");				
			
			createCustomersTable(connection);
			createAccountsTable(connection);
			
		} catch (SQLException e) {
			System.out.print("Fail to connect to server!");
		}		
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginScreen window = new LoginScreen();
					LoginScreen.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public LoginScreen() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblUsername = new JLabel("Username");
		lblUsername.setFont(new Font("Arial", Font.BOLD, 20));
		lblUsername.setBounds(77, 67, 128, 43);
		frame.getContentPane().add(lblUsername);
		
		JLabel lblPassword = new JLabel("Password");
		lblPassword.setFont(new Font("Arial", Font.BOLD, 20));
		lblPassword.setBounds(76, 123, 128, 43);
		frame.getContentPane().add(lblPassword);
		
		edtUsername = new JTextField();
		edtUsername.setFont(new Font("Arial", Font.PLAIN, 20));
		edtUsername.setBounds(215, 75, 155, 26);
		frame.getContentPane().add(edtUsername);
		edtUsername.setColumns(10);
		
		edtPassword = new JPasswordField();
		edtPassword.setFont(new Font("Arial", Font.PLAIN, 20));
		edtPassword.setColumns(10);
		
		edtPassword.setBounds(215, 131, 155, 26);
		frame.getContentPane().add(edtPassword);
		
		JButton btnRegister = new JButton("Register");
		btnRegister.addActionListener((ActionListener) new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				createCustomer(edtUsername.getText().toString().trim(), edtPassword.getText().toString().trim());
			}
		});
		btnRegister.setFont(new Font("Arial", Font.BOLD, 20));
		btnRegister.setBounds(60, 188, 141, 58);
		frame.getContentPane().add(btnRegister);
		
		JButton btnLogin = new JButton("Login");
		btnLogin.addActionListener((ActionListener) new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(login(edtUsername.getText().toString().trim(), edtPassword.getText().toString().trim())) {
					frame.dispose();
					CustomerScreen cScreen = new CustomerScreen(currentConn, currentCustomer);
				};
			}
		});
		btnLogin.setFont(new Font("Arial", Font.BOLD, 20));
		btnLogin.setBounds(241, 188, 141, 58);
		frame.getContentPane().add(btnLogin);
		
		JLabel lblTdBank = new JLabel("TD BANK");
		lblTdBank.setForeground(new Color(0, 191, 255));
		lblTdBank.setHorizontalAlignment(SwingConstants.CENTER);
		lblTdBank.setFont(new Font("Arial", Font.BOLD, 23));
		lblTdBank.setBounds(169, 11, 128, 43);
		frame.getContentPane().add(lblTdBank);
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
	
	public static boolean createCustomer(String username, String password) {
//		String sql = "INSERT INTO customers (username, password)"
//				+ " VALUES ('" + customer.getUsername() 
//				+ "', '" + customer.getPassword()
//				+ "')";
		
		String query = "INSERT INTO customers (username, password) VALUES (?, ?)";
		
		try {
			PreparedStatement statement = currentConn.prepareStatement(query);
			statement.setString(1, username);
			statement.setString(2, password);			
			statement.executeUpdate();
			JOptionPane.showMessageDialog(frame, "Register succesfully!");
			System.out.println("Register succesfully!");
			return true;
		} catch (SQLException e) {			 
			e.printStackTrace();
			JOptionPane.showMessageDialog(frame, "Register unsuccesfully! Name already taken!");
			System.out.println("Register unsuccesfully! Name already taken!");
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
				JOptionPane.showMessageDialog(frame, "Login succesfully as " + usernameInput + "!");
				System.out.println("Login succesfully as " + usernameInput + "!");
				return true;
			}
			JOptionPane.showMessageDialog(frame, "Customer not found!");
			System.out.println("Wrong username or password. Please try again!");
			return false;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}						
	}

}
