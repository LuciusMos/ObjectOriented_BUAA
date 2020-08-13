package bank;

public class TestDeadLock {
    public static void main(String[] args) throws InterruptedException {
        Account a = new Account(1000, "A");
        Account b = new Account(3000, "B");
        DeadLock t1 = new DeadLock(a, b, 100);
        DeadLock t2 = new DeadLock(b, a, 200);
        t1.start();
        t2.start();
    }
}