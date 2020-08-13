import com.oocourse.elevator3.ElevatorInput;
import com.oocourse.elevator3.PersonRequest;

import java.io.IOException;

public class Input implements Runnable {
    private final RequestQueue requestqueue;
    
    Input(RequestQueue requestqueue) {
        this.requestqueue = requestqueue;
    }
    
    @Override
    public void run() {
        //System.out.println(Thread.currentThread() + "starts");
        ElevatorInput elevatorInput = new ElevatorInput(System.in);
        while (true) {
            PersonRequest request = elevatorInput.nextPersonRequest();
            synchronized (requestqueue) {
                if (request == null) {
                    requestqueue.setHasInput(false);
                } else {
                    requestqueue.requestsAdd(request);
                }
                //System.out.println("input notify");
                requestqueue.notifyAll();
                if (!requestqueue.getHasInput()) { break; }
            }
        }
        try {
            elevatorInput.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
    }
}
