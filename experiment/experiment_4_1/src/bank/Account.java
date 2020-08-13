package bank;

public class Account {
    
    private int balance;
    private final String acctNo;
    private boolean status;
    
    public Account(int  money, String id) {
        acctNo = id;
        balance = money;
        status = true;
    }
    
    public void debit(int money) {
        balance = this.balance + money;
        
    }
    
    public void credit(int money) {
        balance = this.balance - money;
    }
    
    public int getBalance() {
        return balance;
    }
    
    public String getAcctNo() {
        return acctNo;
    }
    
    public boolean getStatus() {
        return this.status;
    }
    
    public void disableTransfer() {
        this.status = false;
    }
    
    public void enableTransfer() {
        this.status = true;
    }
}