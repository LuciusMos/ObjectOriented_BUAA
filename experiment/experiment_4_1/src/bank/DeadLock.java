package bank;

public class DeadLock extends Thread {
    private Account fromAccount;
    private Account toAccount;
    private int amount;
    static final int sleepTime = 2000;
    
    public DeadLock(Account a, Account b, int amount) {
        this.fromAccount = a;
        this.toAccount = b;
        this.amount = amount;
    }
    
    public void transferMoney(Account fromAccount,
                              Account toAccount,
                              int amount) {
        
        //锁住汇款账户
        synchronized (fromAccount) {
            System.out.println(String.format("%s is locked by %s, waiting for %s",
                    fromAccount.getAcctNo(),
                    Thread.currentThread().getName(),
                    toAccount.getAcctNo()));
            //锁住收款帐户
            synchronized (toAccount) {
                fromAccount.disableTransfer();
                fromAccount.debit(amount);
                toAccount.credit(amount);
                System.out.println(
                        String.format(
                                "%s is transfering money to %s : %d",
                                fromAccount.getAcctNo(), toAccount.getAcctNo(), amount));
            }
        }
    }
    
    public void run() {
        if (fromAccount.getStatus()) {
            transferMoney(fromAccount, toAccount, amount);
            fromAccount.enableTransfer();
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        else {
            System.out.println(String.format("From account is not ready!"));
        }
    }
}