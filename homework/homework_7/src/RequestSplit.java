import com.oocourse.elevator3.PersonRequest;

public class RequestSplit {
    private int[][] ele;
    
    RequestSplit(int[] eleaFloors, int[] elebFloors, int[] elecFloors) {
        ele = new int[3][];
        ele[0] = eleaFloors;
        ele[1] = elebFloors;
        ele[2] = elecFloors;
    }
    
    int[][] getEle() { return ele; }
    
    PersonRequest[] split(PersonRequest request) {
        PersonRequest[] afterSplit = new PersonRequest[2];
        int fromFloor = request.getFromFloor();
        int toFloor = request.getToFloor();
        String ele = chooseEle(fromFloor, toFloor);
        if (ele.equals("0")) {
            afterSplit[0] = request;
            afterSplit[1] = null;
            return afterSplit;
        }
        int transferFloor = Integer.parseInt(ele);
        int personId = request.getPersonId();
        afterSplit[0] = new PersonRequest(fromFloor, transferFloor, personId);
        afterSplit[1] = new PersonRequest(transferFloor, toFloor, personId);
        return afterSplit;
    }
    
    private String chooseEle(int fromFloor, int toFloor) {
        String chosenEle = "Split Wrong!";
        int i;
        int j;
        for (i = 0;i < 3;i++) {
            if (arrayContains(ele[i], fromFloor) &&
                    arrayContains(ele[i], toFloor)) {
                return "0";
            }
        }
        int transferFloor;
        int transferDistance;
        int bestTransferFloor = 0;
        int minTransferDistance = 44;
        for (i = 0;i < 3;i++) {
            for (j = 0;j < 3;j++) {
                if (j == i) { continue; }
                for (transferFloor = -3; transferFloor <= 20; transferFloor++) {
                    //transferFloor != fromFloor && transferFloor != toFloor
                    if (transferFloor == 0) { continue; }
                    if (arrayContains(ele[i], fromFloor)
                            && arrayContains(ele[i], transferFloor)
                            && arrayContains(ele[j], transferFloor)
                            && arrayContains(ele[j], toFloor)) {
                        transferDistance = calculateDistance(
                                fromFloor, transferFloor) +
                                calculateDistance(transferFloor, toFloor);
                        if (transferDistance < minTransferDistance) {
                            minTransferDistance = transferDistance;
                            bestTransferFloor = transferFloor;
                            chosenEle = String.valueOf(bestTransferFloor);
                        }
                    }
                }
            }
        }
        return chosenEle;
    }
    
    static boolean arrayContains(int[] array, int ele) {
        int i;
        for (i = 0;i < array.length;i++) {
            if (array[i] == ele) {
                return true;
            }
        }
        return false;
    }
    
    private int calculateDistance(int floor1, int floor2) {
        int distance = Math.abs(floor1 - floor2);
        if (floor1 * floor2 < 0) {
            distance--;
        }
        return distance;
    }
}
/*
class RequestSplitTest {
    public static void main(String[] args) {
        int[] eleaFloors = {-3, -2, -1, 1, 15, 16, 17, 18, 19, 20};
        int[] elebFloors = {-2, -1, 1, 2, 4, 5, 6, 7, 8, 9, 10, 11,
                12, 13, 14, 15};
        int[] elecFloors = {1, 3, 5, 7, 9, 11, 13, 15};
        RequestSplit requestSplit = new RequestSplit(eleaFloors,
                elebFloors, elecFloors);
        
        PersonRequest request = new PersonRequest(3,
                20, 1);
        PersonRequest[] twoRequests = requestSplit.split(request);
        System.out.println(twoRequests[0] + "\n" + twoRequests[1]);
        
        int [][] ele = requestSplit.getEle();
        System.out.println(String.format("%d %d %d",
                ele[0].length, ele[1].length, ele[2].length));
    }
}
*/
/*
class Test {

}
*/