package bankSystem;

public class CheckingAccount extends BankAccount{
private int overdraftFee;

public CheckingAccount(int customerID) {
	super("Checking", customerID);
}

public boolean debit(int pennies){
   if(pennies > balance){
	   balance -= overdraftFee;
   }                     
   balance -= pennies;      
   return true;
}

public int getFee(){
   return overdraftFee;
}

public void setFee(int fee){
   overdraftFee = fee;
}

public void applyInterest(){
   if(balance > 0){
	   balance += balance * interestRate;
   }
}

public String accountInfo(){
   return "Type of Account : Checking" +
          "\nAccount ID      : " + numID + 
          "\nCurrent Balance : " + String.format("$%.2f", balance / 100.0)  + 
          "\nInterest rate   : " + String.format("%.2f%%", interestRate * 100.0) +
          "\nOverdraft Fee   : " + String.format("$%.2f", overdraftFee / 100.0);
          
}
}
