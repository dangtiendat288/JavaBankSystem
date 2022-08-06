package bankSystem;

public class SavingsAccount extends BankAccount{
	
public SavingsAccount(int customerID) {
	super("Savings", customerID);
}
	
public boolean debit(int pennies){
   if(pennies <= balance){
      balance -= pennies;
      return true;
   }
   return false;
}

public void applyInterest(){
   if(balance > 0){
      balance += (balance * interestRate);
   }
}

public String accountInfo(){
   return "Type of Account : Savings" +
          "\nAccount ID      : " + numID + 
          "\nCurrent Balance : " + String.format("$%.2f", balance / 100.0)  + 
          "\nInterest rate   : " + String.format("%.2f%%", interestRate * 100.0);
}
}
