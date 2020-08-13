package bank;

public class DynamicOrderLock extends Thread {
    private Account fromAccount;
    private Account toAccount;
    private int amount;
    private static final Object tieLock = new Object();
    static final int sleepTime = 2000;
    
    public DynamicOrderLock(Account a, Account b, int amount) {
        this.fromAccount = a;
        this.toAccount = b;
        this.amount = amount;
    }
    
    public void transferMoney(Account fromAcct,
                              Account toAcct,
                              int amount) {
        class Helper {
            public void transfer() {
                fromAcct.disableTransfer();
                fromAcct.debit(amount);
                toAcct.credit(amount);
                System.out.println(String.format(
                        "%s is transfering money to %s : %d",
                        fromAccount.getAcctNo(), toAccount.getAcctNo(), amount));
            }
        }
        
        int fromHash = System.identityHashCode(fromAcct);
        int toHash = System.identityHashCode(toAcct);
        
        if (fromHash < toHash) {
            synchronized (fromAcct) {
                System.out.println(String.format("%s is locked by %s, waiting for %s",
                        fromAcct.getAcctNo(),
                        Thread.currentThread().getName(),
                        toAcct.getAcctNo()));
                
                synchronized (toAcct) {
                    new Helper().transfer();
                }
            }
            
        } else if (fromHash > toHash) {
            synchronized (toAcct) {
                System.out.println(String.format("%s is locked by %s, waiting for %s",
                        toAcct.getAcctNo(),
                        Thread.currentThread().getName(),
                        fromAcct.getAcctNo()));
                synchronized (fromAcct) {
                    new Helper().transfer();
                }
            }
        } else {
            synchronized (tieLock) {
                synchronized (fromAcct) {
                    synchronized (toAcct) {
                        new Helper().transfer();
                    }
                }
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