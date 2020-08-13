import com.oocourse.elevator1.ElevatorInput;
import com.oocourse.elevator1.PersonRequest;

import java.io.IOException;

public class RequestInput extends Thread {
    private RequestQueue requestqueue;
    
    RequestInput(RequestQueue requestqueue) {
        this.requestqueue = requestqueue;
    }
    
    @Override
    public void run() {
        //System.out.println(Thread.currentThread() + "starts");
        ElevatorInput elevatorInput = new ElevatorInput(System.in);
        while (true) {
            PersonRequest request = elevatorInput.nextPersonRequest();
            if (request == null) {
                RequestQueue.setHasInput(false);
                break;
            } else {
                requestqueue.requestsAdd(request);
            }
        }
        try {
            elevatorInput.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
    }
}
