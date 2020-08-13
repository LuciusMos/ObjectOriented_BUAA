package ticket;

public class SaleTicket implements Runnable {
    static final int numberOfTickets = 200;
    static final int numberOfThreads = 5;
    static final int sleepTime = 1000;
    
    private int count = 0;
    private int avaliable = numberOfTickets;
    
    public SaleTicket() {
    }
    
    public void run() {
        while (avaliable > 0) {
            count++;
            avaliable--;
            System.out.println(Thread.currentThread().getName()
                    + " Current Ticket No: " + count
                    + "/ Tickets Available: " + avaliable);
        }
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        SaleTicket st = new SaleTicket();
        for (int i = 1; i <= numberOfThreads; i++) {
            new Thread(st, "Window " + i + " is selling/ ").start();
        }
    }
}