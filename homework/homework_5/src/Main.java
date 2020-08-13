public class Main {
    public static void main(String[] args) {
        RequestQueue requestQueue = new RequestQueue();
        Elevator elevator = new Elevator(requestQueue);
        RequestInput requestInput = new RequestInput(requestQueue);
        elevator.start();
        requestInput.start();
    }
}