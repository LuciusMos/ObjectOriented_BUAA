public class Main {
    public static void main(String []args) {
        Expression exp = new Expression();
        exp.expressionInput();
        exp.expressionFormatCheck();
        exp.expressionDerivation();
        exp.hm2Al(1);
        exp.simplify();
        exp.hm2Al(2);
        exp.print();
    }

}
