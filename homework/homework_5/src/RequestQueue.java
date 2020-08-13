import com.oocourse.TimableOutput;
import com.oocourse.elevator1.PersonRequest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class RequestQueue {
    private static List<PersonRequest> requests = Collections.synchronizedList(
            new ArrayList<>());
    private static final int timePerFloor = 500;
    private static final int doorTime = 250;
    private static boolean hasInput = true;
    
    static synchronized void setHasInput(boolean temp) {
        hasInput = temp;
    }
    
    static synchronized boolean getHasInput() {
        return hasInput;
    }
    
    synchronized List<PersonRequest> getRequests() {
        return requests;
    }
    
    synchronized void requestsAdd(PersonRequest newRequest) {
        //System.out.println("requestsAdd is used");
        RequestQueue.requests.add(newRequest);
    }
    
    synchronized int requestsProcess(int presentFloor) {
        //System.out.println("requestsProcess is used");
        PersonRequest presentRequest = RequestQueue.requests.get(0);
        final int fromFloor = presentRequest.getFromFloor();
        final int toFloor = presentRequest.getToFloor();
        final int personId = presentRequest.getPersonId();
        TimableOutput.initStartTimestamp();
        // pick up person
        elevatorMove(presentFloor, fromFloor);
        elevatorDoorOpen(fromFloor);
        TimableOutput.println(String.format("IN-%d-%d", personId, fromFloor));
        elevatorDoorClose(fromFloor);
        // drop off person
        elevatorMove(fromFloor, toFloor);
        elevatorDoorOpen(toFloor);
        TimableOutput.println(String.format("OUT-%d-%d", personId, toFloor));
        elevatorDoorClose(toFloor);
        
        RequestQueue.requests.remove(0);
        return toFloor;
    }
    
    void elevatorMove(int start, int end) {
        try {
            Thread.sleep(Math.abs(start - end) * timePerFloor);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        
    }
    
    void elevatorDoorOpen(int floor) {
        TimableOutput.println(String.format("OPEN-%d", floor));
        try {
            Thread.sleep(doorTime);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }
    
    void elevatorDoorClose(int floor) {
        try {
            Thread.sleep(doorTime);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        TimableOutput.println(String.format("CLOSE-%d", floor));
        
    }
}
