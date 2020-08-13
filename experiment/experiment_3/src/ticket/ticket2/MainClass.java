package ticket.ticket2;

public class MainClass {
    
    public static void main(String[] args) {
        
        Station st = new Station();
        
        SaleTicket2 st1 = new SaleTicket2(st,"Window 1 is selling/");
        SaleTicket2 st2 = new SaleTicket2(st,"Window 2 is selling/");
        SaleTicket2 st3 = new SaleTicket2(st,"Window 3 is selling/");
        SaleTicket2 st4 = new SaleTicket2(st,"Window 4 is selling/");
        SaleTicket2 st5 = new SaleTicket2(st,"Window 5 is selling/");
        // 五个售票点开始工作
        st1.start();
        st2.start();
        st3.start();
        st4.start();
        st5.start();
        
    }
}