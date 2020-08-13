import com.oocourse.TimableOutput;
import com.oocourse.elevator3.PersonRequest;

import java.util.ArrayList;
import java.util.List;

public class Elevator implements Runnable {
    private final RequestQueue requestqueue;
    private final int doorTime = 400;
    private final String name;
    private int presentFloor = 1;
    private ArrayList<PersonRequest> elevatorRequests = new ArrayList<>();
    private ArrayList<Integer> inPerson = new ArrayList<>();
    private final int timePerFloor;
    private final int[] accessFloors;
    private boolean state; // true for "up", false for "down"
    private int targetFloor;
    private final int inPersonMax;
    private final Object outputLock;
    
    Elevator(String name, RequestQueue requests, int timePerFloor,
             int[] accessFloors, int inPersonMax, Object outputLock) {
        this.name = name;
        this.requestqueue = requests;
        this.timePerFloor = timePerFloor;
        this.accessFloors = accessFloors;
        this.inPersonMax = inPersonMax;
        this.outputLock = outputLock;
    }
    
    List<PersonRequest> getElevatorRequests() {
        return elevatorRequests;
    }
    
    int getPresentFloor() {
        return presentFloor;
    }
    
    boolean getState() { return state; }
    
    void setState(boolean state) { this.state = state; }
    
    int[] getAccessFloors() { return accessFloors; }
    
    int getTargetFloor() { return targetFloor; }
    
    void setTargetFloor(int targetFloor) { this.targetFloor = targetFloor; }
    
    int getInPersonMax() { return inPersonMax; }
    
    @Override
    public void run() {
        //System.out.println(Thread.currentThread() + "starts");
        while (!requestqueue.getRequests().isEmpty() ||
                !elevatorRequests.isEmpty() ||
                !requestqueue.getUninsertedRequests().isEmpty() ||
                requestqueue.getHasInput()) {
            //System.out.println(Thread.currentThread() + "has the key");
            if (!requestqueue.getRequests().isEmpty() ||
                    !elevatorRequests.isEmpty()) {
                synchronized (requestqueue) {
                    if (!requestqueue.requestAlloc(this) &&
                            elevatorRequests.isEmpty()) {
                        try {
                            //System.out.println(name + " is 1-waiting!");
                            //1-waiting: can't be allocated
                            requestqueue.wait();
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
                //System.out.println(Thread.currentThread() +
                // "door open or close");
                doorOpenAndClose();
                if (elevatorRequests.isEmpty()) {
                    continue;
                }
                determineState();
                elevatorMove();
            } else {
                synchronized (requestqueue) {
                    if (requestqueue.getRequests().isEmpty() &&
                            elevatorRequests.isEmpty()) {
                        try {
                            //System.out.println(name + " is 2-waiting!");
                            //2-waiting: wait for input or transfer
                            requestqueue.wait();
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                        //System.out.println(name + " is awakened!");
                    }
                }
                try {
                    Thread.sleep(10);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                //System.out.println(Thread.currentThread() +
                //"can notifyALL");
                //requestqueue.notifyAll();
            }
        }
        //System.out.println(name + " ends!");
    }
    
    private void doorOpenAndClose() {
        ArrayList<PersonRequest> outRequests = new ArrayList<>();
        ArrayList<PersonRequest> inRequests = new ArrayList<>();
        for (PersonRequest request : elevatorRequests) {
            if (request.getToFloor() == presentFloor &&
                    inPerson.contains(request.getPersonId())) {
                outRequests.add(request);
                inPerson.remove(new Integer(request.getPersonId()));
            }
            if (request.getFromFloor() == presentFloor &&
                    !inPerson.contains(request.getPersonId()) &&
                    inPerson.size() < inPersonMax) {
                inRequests.add(request);
                inPerson.add(request.getPersonId());
            }
        }
        boolean needOpenOrClose =  !inRequests.isEmpty()
                || !outRequests.isEmpty();
        if (needOpenOrClose) {
            synchronized (outputLock) {
                TimableOutput.println(String.format("OPEN-%d-%s",
                        presentFloor, name));
            }
            for (PersonRequest outRequest : outRequests) {
                synchronized (outputLock) {
                    TimableOutput.println(String.format("OUT-%d-%d-%s",
                            outRequest.getPersonId(), presentFloor, name));
                }
                elevatorRequests.remove(outRequest);
                synchronized (requestqueue) {
                    requestqueue.checkTransferRequest(outRequest);
                    //System.out.println("successfully split!" +
                    // requestqueue.getRequests());
                }
            }
            for (PersonRequest inRequest : inRequests) {
                synchronized (outputLock) {
                    TimableOutput.println(String.format("IN-%d-%d-%s",
                            inRequest.getPersonId(), presentFloor, name));
                }
            }
            try {
                Thread.sleep(doorTime);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            synchronized (outputLock) {
                TimableOutput.println(String.format("CLOSE-%d-%s",
                        presentFloor, name));
            }
        }
    }
    
    private void elevatorMove() {
        try {
            Thread.sleep(timePerFloor);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        if (state) {
            presentFloor++;
            if (presentFloor == 0) {
                presentFloor++;
            }
        } else {
            presentFloor--;
            if (presentFloor == 0) {
                presentFloor--;
            }
        }
        synchronized (outputLock) {
            /*if (name.equals("C")) {
                System.out.println(state + " " + presentFloor + " " +
                        inPerson + " " + elevatorRequests);
            }*/
            TimableOutput.println(String.format("ARRIVE-%d-%s",
                    presentFloor, name));
        }
    }
    
    private void determineState() {
        if (!inPerson.isEmpty() &&
                inPerson.contains(elevatorRequests.get(0).getPersonId())) {
            /*if (name.equals("C")) {
                System.out.println("not empty");
            }*/
            targetFloor = elevatorRequests.get(0).getToFloor();
        } else {
            targetFloor = elevatorRequests.get(0).getFromFloor();
        }
        state = (targetFloor > presentFloor);
        /*if (name.equals("C")) {
            System.out.println(state);
        }*/
    }
}
