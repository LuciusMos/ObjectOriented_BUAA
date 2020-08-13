import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String []args) {
        ArrayList<Employee> emps = new ArrayList<>();
        emps.add(new Reaserch( "001","R",5.0));
        emps.add(new Reaserch( "002","R",1.2));
        emps.add(new Finance( "003","F"));
        emps.add(new Finance( "004","F"));
        emps.add(new Market( "005","M"));
        emps.add(new Market( "006","M"));
        emps.add(new Sales( "007","S"));
        emps.add(new Sales( "008","S"));
        Scanner scan = new Scanner(System.in);
        try {
            String query = scan.nextLine();
            String instrs[] = query.split(" ");
            boolean bjFound = false;
            if (instrs[0].equals("01")) {
                for (Employee emp : emps) {
                    if (emp.Id.equals(instrs[1])) {
                        bjFound = true;
                        if (!emp.type.equals("S")) {
                            emp.setTimePerWeek(Integer.valueOf(instrs[2]));
                            emp.calSalary();
                            System.out.println(emp.check + " " + emp.sumSalary);
                            System.exit(0);
                        } else {
                            ((Sales) emp).
                                    setMySumDeals(Integer.valueOf(instrs[3]));
                            ((Sales) emp).calSalary();
                            System.out.println(emp.check + " " + emp.sumSalary);
                            System.exit(0);
                        }
                    }
                }
                if (!bjFound) {
                    System.out.println("Invalid Query!");
                    System.exit(0);
                }
            } else {
                System.out.println("Invalid Query!");
                System.exit(0);
            }
        } catch (Exception ex) {
            System.out.println("Invalid Query!");
            System.exit(0);
        }
        
        scan.close();
    }
    
}
