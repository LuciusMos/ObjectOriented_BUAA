package ticket.ticket2;

public class SaleTicket2 extends Thread {
    
    static final int sleepTime = 1000;
    
    private Station st;
    private String name;
    
    public SaleTicket2(Station st, String name) {
        this.st = st;
        this.name = name;
    }
    
    public String getname() {
        return this.name;
    }
    
    // 重写run方法，实现卖票操作
    @Override
    public void run() {
        while (st.getTicket() > 0) {
            st.saleTicket(this);
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
}