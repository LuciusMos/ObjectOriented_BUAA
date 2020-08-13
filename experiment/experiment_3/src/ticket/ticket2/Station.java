package ticket.ticket2;

public class Station {
    
    static final int numberOfTickets = 200;
    
    private static int ticket = numberOfTickets;
    private static int count = 0;
    
    public static int getTicket() {
        return ticket;
    }
    
    public synchronized  void saleTicket(SaleTicket2 st) {
        Station.count++;
        Station.ticket--;
        System.out.println(st.getname() + " Current Ticket No: " + Station.count
                + "/ Tickets Available: " + Station.ticket);
    }
}