import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Expression {
    private String expression;
    private HashMap<BigInteger,BigInteger> coefAndExpo = new HashMap<>();
    private String pattern1 = "\\s*[+-]?\\s*(([+-]?(\\d+\\s*\\*)?" +
            "\\s*x\\s*(\\^\\s*[+-]?\\d+)?)|([+-]?\\d+))\\s*";
    private Pattern p1 = Pattern.compile(pattern1);
    private String pattern2 = "\\s*[+-]\\s*(([+-]?(\\d+\\s*\\*)?" +
            "\\s*x\\s*(\\^\\s*[+-]?\\d+)?)|([+-]?\\d+))\\s*";
    private Pattern p2 = Pattern.compile(pattern2);
    private String pattern3 = "[^ \\t0-9x*^+-]";
    private Pattern p3 = Pattern.compile(pattern3);
    
    void expressionInput() {
        Scanner scan = new Scanner(System.in);
        if (scan.hasNextLine()) {
            expression = scan.nextLine();
        } else {
            System.out.println("WRONG FORMAT!");
            System.exit(0);
        }
        scan.close();
    }
    
    void expressionFormatCheck() {
        Matcher m3 = p3.matcher(expression);
        if (m3.find()) {
            System.out.println("WRONG FORMAT!");
            System.exit(0);
        }
        Matcher m1 = p1.matcher(expression);
        int bj = 0;
        if (m1.lookingAt()) {
            bj = m1.end();
        } else {
            System.out.println("WRONG FORMAT!");
            System.exit(0);
        }
        Matcher m2 = p2.matcher(expression);
        while (m2.find(bj)) {
            if (m2.start() != bj) {
                break;
            }
            bj = m2.end();
        }
        if (bj != expression.length()) {
            System.out.println("WRONG FORMAT!");
            System.exit(0);
        }
    }
    
    void termMatch() {
        Matcher m1 = p1.matcher(expression);
        Term term;
        while (m1.find()) {
            term = new Term(m1.group().replaceAll("\\s*",""));
            BigInteger[] valueKey = term.term2Nums();
            BigInteger key = valueKey[1];
            BigInteger value = valueKey[0];
            if (coefAndExpo.containsKey(key)) {
                value = value.add(coefAndExpo.get(key));
                coefAndExpo.put(key,value);
            } else {
                coefAndExpo.put(key,value);
            }
        }
    }
    
    void printHashMap() {
        Set exposSet = coefAndExpo.keySet();
        ArrayList<BigInteger> expos = new ArrayList<>();
        boolean bjFirstPositive = false;
        for (Object expo : exposSet) {
            if (!bjFirstPositive && coefAndExpo.get(expo).
                    compareTo(BigInteger.ZERO) > 0) {
                expos.add(0,(BigInteger)expo);
                bjFirstPositive = true;
            } else if (coefAndExpo.get(expo).compareTo(BigInteger.ZERO) != 0) {
                expos.add((BigInteger)expo);
            }
        }
        boolean bjPrint = false;
        for (BigInteger expo : expos) {
            if (coefAndExpo.get(expo).compareTo(BigInteger.ZERO) != 0) {
                if (coefAndExpo.get(expo).compareTo(BigInteger.ZERO) > 0
                        && expos.indexOf(expo) != 0) {
                    System.out.print("+");
                    bjPrint = true;
                }
                if (coefAndExpo.get(expo).compareTo(BigInteger.ONE) != 0) {
                    System.out.print(coefAndExpo.get(expo));
                    bjPrint = true;
                    if (expo.compareTo(BigInteger.ZERO) != 0) {
                        System.out.print("*");
                    }
                }
                if (expo.compareTo(BigInteger.ONE) == 0) {
                    System.out.print("x");
                    bjPrint = true;
                } else if (expo.compareTo(BigInteger.ZERO) == 0) {
                    if (coefAndExpo.get(expo).compareTo(BigInteger.ONE) == 0) {
                        System.out.print("1");
                        bjPrint = true;
                    }
                } else {
                    System.out.print("x^" + expo);
                    bjPrint = true;
                }
            }
        }
        if (!bjPrint) {
            System.out.print("0");
        }
    }
}
