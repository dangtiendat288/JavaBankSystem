package bankSystem;

public abstract class BankAccount{
	   protected int numID = -1;
	   protected String type = "";
	   protected double interestRate = 0.0;
	   protected float balance = 0.0f;
	   protected int customerID = -1;
	   	   
	   public BankAccount(String type, int customerID){
		      this.type = type;
		      this.customerID = customerID;
		      this.numID = (int) ((Math.random() * (99999 - 10000)) + 10000);		      
		   }
	   
	   public String toString() {
			return  String.format("%s Account #%d: $%.2f", type, numID, balance);									
		}
	   
	   public int getNumber(){		      
		      return numID;
	   }
	   
	   public void setNumber(int num){		      
		      this.numID = num;
	   }
	   
	   public String getType(){		      
		      return type;
	   }
	   
	   public void setBalance(float balance){
		   this.balance = balance;
	   }	   
	   public float getBalance(){
		      return balance;
	   }
	   
	   public boolean deposit(float amount){
	      balance += amount;
	      return true;
	   }
	   
	   public boolean withdraw(float amount){
		   if(amount <= balance) {
			   balance -= amount;
			   return true;
		   } else {
			   return false;
		   }
	   }   
	   
	   public abstract boolean debit(int pennies);
	   
//	   public double getInterestRate(){
//	      return interestRate;
//	   }
	   
//	   public void setInterestRate(double interestRate){
//	      this.interestRate = interestRate;
//	   }
	   
//	   public abstract void applyInterest();
	   
	   public abstract String accountInfo();

	public int getCustomerID() {		
		return customerID;
	}	   	   
	}