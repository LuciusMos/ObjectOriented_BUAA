import com.oocourse.elevator3.PersonRequest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class RequestQueue {
    private static List<PersonRequest> requests = Collections.synchronizedList(
            new ArrayList<>());
    private static HashMap<PersonRequest, PersonRequest> uninsertedRequests
            = new HashMap<>();
    private boolean hasInput = true;
    private RequestSplit requestSplit;
    private Elevator eleA;
    
    RequestQueue(RequestSplit requestSplit) {
        this.requestSplit = requestSplit;
    }
    
    synchronized void setHasInput(boolean temp) {
        hasInput = temp;
    }
    
    synchronized boolean getHasInput() {
        return hasInput;
    }
    
    synchronized List<PersonRequest> getRequests() {
        return requests;
    }
    
    synchronized HashMap<PersonRequest, PersonRequest> getUninsertedRequests() {
        return uninsertedRequests;
    }
    
    synchronized void requestsAdd(PersonRequest newRequest) {
        PersonRequest[] twoRequests = requestSplit.split(newRequest);
        if (twoRequests[1] == null) {
            requests.add(newRequest);
            return;
        }
        requests.add(twoRequests[0]);
        uninsertedRequests.put(twoRequests[0], twoRequests[1]);
        /*System.out.println(String.format("%d-FROM-%d-TO-%d进入主队列",
                newRequest.getPersonId(),
                newRequest.getFromFloor(),
                newRequest.getToFloor()));*/
    }
    
    synchronized boolean requestAlloc(Elevator elevator) {
        boolean hasNewRequest = false;
        if (elevator.getElevatorRequests().isEmpty()) {
            hasNewRequest = determineMajorRequest(elevator);
        }
        int presentFloor = elevator.getPresentFloor();
        int targetFloor = elevator.getTargetFloor();
        boolean state = elevator.getState();
        ArrayList<PersonRequest> removedRequests = new ArrayList<>();
        for (PersonRequest request : requests) {
            if (elevator.getElevatorRequests().size() > 1.5 *
                    elevator.getInPersonMax()) { continue; }
            if (!canBeLifted(elevator, request)) { continue; }
            int newFromFloor = request.getFromFloor();
            int newToFloor = request.getToFloor();
            boolean newState;
            newState = (newFromFloor < newToFloor); //true : "up"
            boolean sameSide = (state == newState);
            boolean halfway;
            if (state) { //state==true : "up"
                halfway = (presentFloor <= newFromFloor) &&
                        (newFromFloor <= targetFloor);
            } else {
                halfway = (presentFloor >= newFromFloor) &&
                        (newFromFloor >= targetFloor);
            }
            boolean canBeTaken = sameSide && halfway;
            if (canBeTaken) {
                elevator.getElevatorRequests().add(request);
                removedRequests.add(request);
                /*System.out.println(String.format(
                        "%d-FROM-%d-TO-%d进入电梯队列",
                        request.getPersonId(),
                        request.getFromFloor(),
                        request.getToFloor()));*/
                hasNewRequest = true;
            }
        }
        for (PersonRequest removedRequest : removedRequests) {
            requests.remove(removedRequest);
        }
        return hasNewRequest;
    }
    
    synchronized void checkTransferRequest(PersonRequest request) {
        if (uninsertedRequests.containsKey(request)) {
            requests.add(uninsertedRequests.get(request));
            uninsertedRequests.remove(request);
        }
        synchronized (this) {
            //System.out.println("transfer notify");
            this.notifyAll();
        }
    }
    
    private synchronized boolean determineMajorRequest(Elevator elevator) {
        int i;
        for (i = 0;i < requests.size();i++) {
            if (canBeLifted(elevator, requests.get(i))) { break; }
        }
        if (i == requests.size()) { return false; }
        elevator.getElevatorRequests().add(requests.get(i));
        /*System.out.println(String.format("%d-FROM-%d-TO-%d进入电梯队列",
                requests.get(0).getPersonId(),
                requests.get(0).getFromFloor(),
                requests.get(0).getToFloor()));*/
        elevator.setTargetFloor(requests.get(i).getFromFloor());
        boolean state;
        if (elevator.getPresentFloor() < elevator.getTargetFloor()) {
            state = true;
        } else if (elevator.getPresentFloor() > elevator.getTargetFloor()) {
            state = false;
        } else {
            if (requests.get(i).getFromFloor() <
                    requests.get(i).getToFloor()) {
                state = true;
            } else {
                state = false;
            }
        }
        elevator.setState(state);
        requests.remove(i);
        return true;
    }
    
    private boolean canBeLifted(Elevator elevator, PersonRequest request) {
        int[] accessFloors = elevator.getAccessFloors();
        int fromFloor = request.getFromFloor();
        int toFloor = request.getToFloor();
        return (RequestSplit.arrayContains(accessFloors, fromFloor) &&
                    RequestSplit.arrayContains(accessFloors, toFloor));
    }
}
