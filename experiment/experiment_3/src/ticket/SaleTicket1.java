package ticket;

//实现Runnable接口去实现线程
public class SaleTicket1 implements Runnable {
    
    static final int numberOfTickets = 200;
    static final int numberOfThreads = 5;
    static final int sleepTime = 1000;
    
    private static int count = 0;
    private static int avaliable = numberOfTickets;
    
    //实现Runnable接口中的run方法
    public void run() {
        while (avaliable > 0) {
            synchronized (this) {
                if (avaliable > 0) {
                    count++;
                    avaliable--;
                    System.out.println(Thread.currentThread().getName() +" is selling/" + " Current Ticket No: " + count
                            + "/ Tickets Available: " + avaliable);
                }
            }
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
    }
    
    public static void main(String[] args) {
        SaleTicket1 st = new SaleTicket1();
        for (int i = 1; i <= numberOfThreads; i++) {
            //使用new Thread(Runnable)这种方式来生成线程对象
            Thread thread = new Thread(st, "Window " + i);
            //启动线程
            thread.start();
        }
    }
}