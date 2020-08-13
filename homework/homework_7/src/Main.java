import com.oocourse.TimableOutput;

public class Main {
    public static void main(String[] args) {
        TimableOutput.initStartTimestamp();
        int[] eleaFloors = {-3, -2, -1, 1, 15, 16, 17, 18, 19, 20};
        int[] elebFloors = {-2, -1, 1, 2, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13,
            14, 15};
        int[] elecFloors = {1, 3, 5, 7, 9, 11, 13, 15};
        RequestSplit requestSplit = new RequestSplit(eleaFloors,
                elebFloors, elecFloors);
        RequestQueue requestQueue = new RequestQueue(requestSplit);
        Input in = new Input(requestQueue);
        new Thread(in, "input").start();
        Object outputLock = new Object();
        Elevator eleA = new Elevator("A", requestQueue,
                400, eleaFloors, 6, outputLock);
        Elevator eleB = new Elevator("B", requestQueue,
                500, elebFloors, 8, outputLock);
        Elevator eleC = new Elevator("C", requestQueue,
                600, elecFloors, 7, outputLock);
        new Thread(eleA, "elevatorA").start();
        new Thread(eleB, "elevatorB").start();
        new Thread(eleC, "elevatorC").start();
    }
}