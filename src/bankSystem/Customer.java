package bankSystem;

import java.util.ArrayList;

public class Customer {
	private int ID;
	private String username;
	private String password;	
	private ArrayList<BankAccount> accounts = new ArrayList<>();
	
	public Customer(String name) {
		username = name;		
	}
	
	public Customer(String name, String password) {
		username = name;
		this.password = password;		
	}
	
	public Customer(int ID, String name, String password) {
		this.ID = ID;
		username = name;
		this.password = password;
		
	}
	
	public String toString() {
		String res = "username: " +username + "\n" 
					+ "password: " + password + "\n";
		for(BankAccount account : accounts) {
			res += account.getNumber() + "\n"; 
		}
		return res;
	}
	
	public int getID() {
		return ID;
	}
	
	public String getUsername() {
		return username;
	}

	public void setName(String name) {
		username = name;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public ArrayList<BankAccount> getAccounts() {
		return accounts;
	}
	
	public BankAccount createAccount(String type, int customerID) {
		BankAccount ba = type.equals("checking")? new CheckingAccount(customerID): new SavingsAccount(customerID);
//		ba.setNumber(accounts.size());
//		accounts.add(ba);		
		return ba;
	}
	
	public boolean removeAccount(int num) {
		if (num > -1 && num < accounts.size()){
			accounts.remove(num);
			return true;
		}
		return false;
	}
	
	public boolean transfer(int amount, int from, int to) {
		BankAccount sender = accounts.get(from);
		BankAccount receiver = accounts.get(to);
		if(amount > sender.getBalance()) {
			return false;
		} else {
			sender.withdraw(amount);
			receiver.deposit(amount);
			return true;
		}
	}	
}
