public class Employee implements sumSalary{
    String Id;
    String type;
    double baseSalary = 50000;
    double bonusSalary;
    double sumSalary;
    String reimbursementType;
    double reimbursementMoney;
    String reimbursementColor;
    int timePerWeek;
    final int requiredTimePerWeek = 40;
    String check;
    double getBaseSalary() {
        return baseSalary;
    }
    void setTimePerWeek(int temp) {
        timePerWeek = temp;
    }
    public void calSalary() { }
}

interface sumSalary {
    void calSalary();
}


class Reaserch extends Employee implements sumSalary{
    double years;
    double myBaseSalary;
    Reaserch(String Id, String type, double years) {
        this.Id = Id;
        this.type = type;
        this.years = years;
    }
    public void calSalary() {
        myBaseSalary = baseSalary + years * 1000.0;
        if (timePerWeek >= requiredTimePerWeek) {
            check = "A";
            bonusSalary = 0.3 * baseSalary;
        } else {
            check = "B";
            bonusSalary = 0.1 * baseSalary;
        }
        sumSalary = myBaseSalary + bonusSalary;
    }
}

class Finance extends  Employee {
    double myBaseSalary = baseSalary;
    Finance(String Id, String type) {
        this.Id = Id;
        this.type = type;
    }
    public void calSalary() {
        if (timePerWeek >= requiredTimePerWeek) {
            check = "A";
            bonusSalary = 0.3 * baseSalary;
        } else {
            check = "B";
            bonusSalary = 0.1 * baseSalary;
        }
        sumSalary = myBaseSalary + bonusSalary;
    }
    
}

class Market extends  Employee {
    double myBaseSalary = baseSalary + 15000;
    Market(String Id, String type) {
        this.Id = Id;
        this.type = type;
    }
    public void calSalary() {
        if (timePerWeek >= requiredTimePerWeek) {
            check = "A";
            bonusSalary = 0.3 * baseSalary;
        } else {
            check = "B";
            bonusSalary = 0.1 * baseSalary;
        }
        sumSalary = myBaseSalary + bonusSalary;
    }
}

class Sales extends  Market {
    double sumDeals = 300000;
    double mySumDeals;
    Sales(String Id, String type) {
        super(Id, type);
    }
    void setMySumDeals(double temp) {
        mySumDeals = temp;
    }
    public void calSalary() {
        if (mySumDeals >= sumDeals) {
            check = "A";
            bonusSalary = 0.2 * (mySumDeals - sumDeals);
        } else {
            myBaseSalary = 0.8 * myBaseSalary;
            bonusSalary = 0;
        }
        sumSalary = myBaseSalary + bonusSalary;
    }
}