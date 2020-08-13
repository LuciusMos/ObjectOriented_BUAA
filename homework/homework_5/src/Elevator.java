public class Elevator extends Thread {
    private RequestQueue requestqueue;
    private int presentFloor;
    
    public Elevator(RequestQueue requests) {
        this.requestqueue = requests;
    }
    
    @Override
    public void run() {
        //System.out.println(Thread.currentThread() + "starts");
        presentFloor = 1;
        //System.out.println(requestqueue.getRequests().isEmpty());
        while (!requestqueue.getRequests().isEmpty() ||
                RequestQueue.getHasInput()) {
            if (!requestqueue.getRequests().isEmpty()) {
                presentFloor = requestqueue.requestsProcess(presentFloor);
            }
            //yield();
            /*
            try {
                Thread.sleep(0);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }*/
        }
    }
}
