package bank;

public class TestDynamicOrderLock {
    public static void main(String[] args) throws InterruptedException {
        Account a = new Account(1000, "A");
        Account b = new Account(3000, "B");
        DynamicOrderLock t1 = new DynamicOrderLock(a, b, 100);
        DynamicOrderLock t2 = new DynamicOrderLock(b, a, 200);
        t1.start();
        t2.start();
    }
}