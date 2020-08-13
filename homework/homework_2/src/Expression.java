import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Expression {
    private String input;
    private String factor = "\\s*(([+-]?\\d+)|" +
            "x\\s*(\\^\\s*[+-]?\\d+)?|" +
            "(sin|cos)\\s*\\(\\s*x\\s*\\)\\s*(\\^\\s*[+-]?\\d+)?)\\s*";
    private String termString1 = "\\s*[+-]?\\s*[+-]?\\s*(" + factor +
            "\\*\\s*)*" + factor;
    private String termString2 = "\\s*[+-]\\s*[+-]?\\s*(" + factor +
            "\\*\\s*)*" + factor;
    private Pattern termPattern1 = Pattern.compile(termString1);
    private Pattern termPattern2 = Pattern.compile(termString2);
    private String illegalCharString = "[^ \\t)(0-9sincox*^+-]";
    private Pattern illegalCharPattern = Pattern.compile(illegalCharString);
    private HashMap<Expo, BigInteger> expoAndCoef = new HashMap<>();
    private Set keysSet;
    private ArrayList<Expo> keys = new ArrayList<>();
    private ArrayList<BigInteger> values = new ArrayList<>();
    private String outputBef;
    private String outputAft;
    
    void expressionInput() {
        Scanner scan = new Scanner(System.in);
        if (scan.hasNextLine()) {
            input = scan.nextLine();
        } else {
            wrongFormat();
        }
    }
    
    void expressionFormatCheck() {
        Matcher m3 = illegalCharPattern.matcher(input);
        if (m3.find()) {
            wrongFormat();
        }
        Matcher m1 = termPattern1.matcher(input);
        int bj = 0;
        if (m1.lookingAt()) {
            bj = m1.end();
        } else {
            wrongFormat();
        }
        Matcher m2 = termPattern2.matcher(input);
        while (m2.find(bj)) {
            if (m2.start() != bj) {
                break;
            }
            bj = m2.end();
        }
        if (bj != input.length()) {
            wrongFormat();
        }
    }
    
    void expressionDerivation() {
        Matcher m1 = termPattern1.matcher(input);
        Term term;
        while (m1.find()) {
            term = new Term(m1.group().
                    replaceAll("\\s",""));
            BigInteger[][] nums = term.termDerivation();
            int i;
            for (i = 0;i < 3; i++) {
                BigInteger value = nums[i][0];
                if (value.compareTo(BigInteger.ZERO) != 0) {
                    Expo key = new Expo(nums[i][1],nums[i][2],nums[i][3]);
                    hmMerge(key,value);
                }
            }
        }
    }
    
    void simplify() {
        boolean bjSimplify;
        for (int times = 0;times < 50;times++) {
            bjSimplify = false;
            keysSet = expoAndCoef.keySet();
            for (Object key1 : keysSet) {
                for (Object key2 : keysSet) {
                    if (key1 == key2) { continue; }
                    if (((Expo) key1).getXpowEx()
                            .equals(((Expo) key2).getXpowEx())) {
                        bjSimplify = simplify1((Expo) key1,(Expo) key2);
                        if (bjSimplify) { break; }
                        bjSimplify = simplify2((Expo) key1,(Expo) key2);
                        if (bjSimplify) { break; }
                    }
                }
                if (bjSimplify) { break; }
            }
        }
    }
    
    private boolean simplify1(Expo key1,Expo key2) {
        BigInteger a1 = expoAndCoef.get(key1);
        BigInteger a2 = expoAndCoef.get(key2);
        BigInteger b  = key1.getXpowEx();
        BigInteger c1 = key1.getSinEx();
        BigInteger c2 = key2.getSinEx();
        BigInteger d1 = key1.getCosEx();
        BigInteger d2 = key2.getCosEx();
        BigInteger bi2 = new BigInteger("2");
        int choice = simplify1Choice(c1,c2,d1,d2);
        if (c1.subtract(c2).equals(bi2) &&
                d2.subtract(d1).equals(bi2)) { //c1=c2+2 d2=d1+2
            expoAndCoef.remove(key1,a1);
            expoAndCoef.remove(key2,a2);
            if (choice == 2) { //a1<=a2 c2<c1 d1<d2
                hmMerge(new Expo(b,c2,d1),a1);
                hmMerge(new Expo(b,c2,d2),a2.subtract(a1));
            } else { //a1>a2 c2<c1 d1<d2
                hmMerge(new Expo(b,c2,d1),a2);
                hmMerge(new Expo(b,c1,d1), a1.subtract(a2));
            }
            return true;
        }
        if (c2.subtract(c1).equals(bi2) &&
                d1.subtract(d2).equals(bi2)) { //c2=c1+2 d1=d2+2
            expoAndCoef.remove(key1,a1);
            expoAndCoef.remove(key2,a2);
            if (choice == 2) { //a1<=a2 c1<c2 d2<d1
                hmMerge(new Expo(b,c1,d2),a1);
                hmMerge(new Expo(b,c2,d2), a2.subtract(a1));
            } else { //a1>a2 c1<c2 d2<d1
                hmMerge(new Expo(b,c1,d2),a2);
                hmMerge(new Expo(b,c1,d1), a1.subtract(a2));
            }
            return true;
        }
        return false;
    }
    
    private boolean simplify2(Expo key1,Expo key2) {
        BigInteger a1 = expoAndCoef.get(key1);
        BigInteger a2 = expoAndCoef.get(key2);
        BigInteger b = key1.getXpowEx();
        BigInteger c1 = key1.getSinEx();
        BigInteger c2 = key2.getSinEx();
        BigInteger d1 = key1.getCosEx();
        BigInteger d2 = key2.getCosEx();
        BigInteger bi2 = new BigInteger("2");
        if (a1.equals(a2.negate())) {
            if (c1.equals(c2) && d1.subtract(d2).equals(bi2)) {
                expoAndCoef.remove(key1,a1);
                expoAndCoef.remove(key2,a2);
                hmMerge(new Expo(b,c1.add(bi2),d2),a1.negate());
                return true;
            }
            if (c1.equals(c2) && d2.subtract(d1).equals(bi2)) {
                expoAndCoef.remove(key1,a1);
                expoAndCoef.remove(key2,a2);
                hmMerge(new Expo(b,c1.add(bi2),d1),a1);
                return true;
            }
            if (d1.equals(d2) && c1.subtract(c2).equals(bi2)) {
                expoAndCoef.remove(key1,a1);
                expoAndCoef.remove(key2,a2);
                hmMerge(new Expo(b,c2,d1.add(bi2)),a1.negate());
                return true;
            }
            if (d1.equals(d2) && c2.subtract(c1).equals(bi2)) {
                expoAndCoef.remove(key1,a1);
                expoAndCoef.remove(key2,a2);
                hmMerge(new Expo(b,c1,d1.add(bi2)),a1);
                return true;
            }
        }
        return false;
    }
    
    void hm2Al(int time) {
        keysSet = expoAndCoef.keySet();
        keys = new ArrayList<>();
        values = new ArrayList<>();
        int i;
        for (Object key : keysSet) {
            if (!expoAndCoef.get(((Expo)key)).equals(BigInteger.ZERO)) {
                for (i = 0;i < keys.size();i++) {
                    if (((Expo)key).compareTo(keys.get(i)) < 0) {
                        break;
                    }
                }
                keys.add(i,(Expo)key);
                values.add(i,expoAndCoef.get(key));
            }
        }
        al2Str(time);
    }
 
    private void al2Str(int time) {
        beginPositive(); //begins with positive
        StringBuffer outputBuffer = new StringBuffer();
        for (int i = 0;i < keys.size();i++) {
            if (values.get(i).compareTo(BigInteger.ZERO) == 0) { continue; }
            if (values.get(i).compareTo(BigInteger.ZERO) > 0 && i != 0) {
                outputBuffer.append("+");
            }
            if (!keys.get(i).getXpowEx().equals(BigInteger.ZERO) ||
                    !keys.get(i).getSinEx().equals(BigInteger.ZERO) ||
                    !keys.get(i).getCosEx().equals(BigInteger.ZERO)) {
                if (values.get(i).compareTo(BigInteger.ZERO) > 0) {
                    if (!values.get(i).equals(BigInteger.ONE)) {
                        outputBuffer.append(values.get(i));
                        outputBuffer.append("*");
                    }
                } else {
                    if (values.get(i).negate().equals(BigInteger.ONE)) {
                        outputBuffer.append("-");
                    } else {
                        outputBuffer.append(values.get(i));
                        outputBuffer.append("*");
                    }
                }
                if (!keys.get(i).getXpowEx().equals(BigInteger.ZERO)) {
                    outputBuffer.append("x");
                    if (!keys.get(i).getXpowEx().equals(BigInteger.ONE)) {
                        outputBuffer.append("^");
                        outputBuffer.append(keys.get(i).getXpowEx());
                    }
                    if (!keys.get(i).getSinEx().equals(BigInteger.ZERO) ||
                            !keys.get(i).getCosEx().equals(BigInteger.ZERO)) {
                        outputBuffer.append("*");
                    }
                }
                if (!keys.get(i).getSinEx().equals(BigInteger.ZERO)) {
                    outputBuffer.append("sin(x)");
                    if (!keys.get(i).getSinEx().equals(BigInteger.ONE)) {
                        outputBuffer.append("^");
                        outputBuffer.append(keys.get(i).getSinEx());
                    }
                    if (!keys.get(i).getCosEx().equals(BigInteger.ZERO)) {
                        outputBuffer.append("*");
                    }
                }
                if (!keys.get(i).getCosEx().equals(BigInteger.ZERO)) {
                    outputBuffer.append("cos(x)");
                    if (!keys.get(i).getCosEx().equals(BigInteger.ONE)) {
                        outputBuffer.append("^");
                        outputBuffer.append(keys.get(i).getCosEx());
                    }
                }
            } else { outputBuffer.append(values.get(i)); }
        }
        if (outputBuffer.toString().equals("")) {
            outputBuffer = new StringBuffer("0");
        }
        if (time == 1) { outputBef = outputBuffer.toString(); }
        else { outputAft = outputBuffer.toString(); }
    }
    
    void print() {
        long lengthBef = outputBef.length();
        long lengthAft = outputAft.length();
        if (lengthBef <= lengthAft) {
            System.out.println(outputBef);
        } else {
            System.out.println(outputAft);
        }
        
    }
    
    private void wrongFormat() {
        System.out.println("WRONG FORMAT!");
        System.exit(0);
    }
    
    private void hmMerge(Expo key,BigInteger value) {
        if (expoAndCoef.containsKey(key)) {
            expoAndCoef.put(key,value.add(expoAndCoef.get(key)));
        } else {
            expoAndCoef.put(key,value);
        }
    }
    
    private int simplify1Choice(BigInteger c1,BigInteger c2,
                               BigInteger d1,BigInteger d2) {
        long length1 = 0;
        if (c1.equals(BigInteger.ZERO)) { ; }
        else if (c1.equals(BigInteger.ONE)) { length1 += 6; }
        else { length1 += 8; }
        if (d1.equals(BigInteger.ZERO)) { ; }
        else if (d1.equals(BigInteger.ONE)) { length1 += 6; }
        else { length1 += 8; }
        long length2 = 0;
        if (c2.equals(BigInteger.ZERO)) { ; }
        else if (c2.equals(BigInteger.ONE)) { length2 += 6; }
        else { length2 += 8; }
        if (d2.equals(BigInteger.ZERO)) { ; }
        else if (d2.equals(BigInteger.ONE)) { length2 += 6; }
        else { length2 += 8; }
        if (length1 <= length2) { return 1; }
        else { return 2; }
    }
    
    private void beginPositive() {
        for (int i = 0;i < keys.size();i++) {
            if (values.get(i).compareTo(BigInteger.ZERO) > 0) {
                keys.add(0,keys.get(i));
                keys.remove(i + 1);
                values.add(0,values.get(i));
                values.remove(i + 1);
                break;
            }
        }
    }
    
    /*
            if (expoAndCoef.get(key).compareTo(BigInteger.ONE) != 0 &&
                    expoAndCoef.get(key).negate()
                            .compareTo(BigInteger.ONE) != 0) {
                output.append(expoAndCoef.get(key));
                if (!(key.getXpowEx().equals(BigInteger.ZERO) &&
                        key.getSinEx().equals(BigInteger.ZERO) &&
                        key.getCosEx().equals(BigInteger.ZERO))) {
                    output.append("+");
                }
            } else if (expoAndCoef.get(key).negate().equals(BigInteger.ONE)) {
                output.append("-");
            }
            if ((expoAndCoef.get(key).equals(BigInteger.ONE) ||
                    expoAndCoef.get(key).negate().equals(BigInteger.ONE))
                    && key.equals(std)) {
                output.append("1");
            }
            output.append("" +
                    "" +
                    "")
            */
    
    
    /*
    void simplify() {
        //sin(x)^2+cos(x)^2
        int i;
        int j;
        for (i = 0;i < keys.size();i++) {
            for (j = 0;j < keys.size();j++) {
                if (i == j) {
                    continue;
                }
                if (keys.get(i).getXpowEx() == keys.get(j).getXpowEx()) {
                    BigInteger a1 = values.get(i);
                    BigInteger a2 = values.get(j);
                    BigInteger b = keys.get(i).getXpowEx();
                    BigInteger c1 = keys.get(i).getSinEx();
                    BigInteger c2 = keys.get(j).getSinEx();
                    BigInteger d1 = keys.get(i).getCosEx();
                    BigInteger d2 = keys.get(j).getCosEx();
                    BigInteger bi2 = new BigInteger("2");
                    if (c1.subtract(c2).equals(bi2) &&
                            d2.subtract(d1).equals(bi2)) { //c1=c2+2 d2=d1+2
                        keys.remove(i);
                        keys.remove(j);
                        values.remove(i);
                        values.remove(j);
                        if (a1.compareTo(a2) <= 0) { //a1<=a2 c2<c1 d1<d2
                            //newa1 = a1; //a1 b c2 d1
                            //newa2 = a2.subtract(a1); //a2-a1 b c2 d2
                            values.add(a1);
                            keys.add(new Expo(b,c2,d1));
                            values.add(a2.subtract(a1));
                            keys.add(new Expo(b,c2,d2));
                        } else { //a1>a2 c2<c1 d1<d2
                            //newa1 = a2; //a2 b c2 d1
                            //newa2 = a1.subtract(a2); //a1-a2 b c2 d2
                            values.add(a2);
                            keys.add(new Expo(b,c2,d1));
                            values.add(a1.subtract(a2));
                            keys.add(new Expo(b,c1,d1));
                        }
                    }
                    if (c2.subtract(c1).equals(bi2) &&
                            d1.subtract(d2).equals(bi2)) { //c2=c1+2 d1=d2+2
                        keys.remove(i);
                        keys.remove(j);
                        values.remove(i);
                        values.remove(j);
                        if (a1.compareTo(a2) <= 0) { //a1<=a2 c1<c2 d2<d1
                            //newa1 = a1; //a1 b c1 d2
                            //newa2 = a2.subtract(a1); //a2-a1 b c2 d2
                            values.add(a1);
                            keys.add(new Expo(b,c1,d2));
                            values.add(a2.subtract(a1));
                            keys.add(new Expo(b,c2,d2));
                        } else { //a1>a2 c1<c2 d2<d1
                            //newa1 = a2; //a2 b c1 d2
                            //newa2 = a1.subtract(a2); //a1-a2 b c2 d2
                            values.add(a2);
                            keys.add(new Expo(b,c1,d2));
                            values.add(a1.subtract(a2));
                            keys.add(new Expo(b,c1,d1));
                        }
                    }
                }
            }
        }
        //begins with positive
        for (i = 0;i < keys.size();i++) {
            if (values.get(i).compareTo(BigInteger.ZERO) > 0) {
                keys.add(0,keys.get(i));
                keys.remove(i+1);
                values.add(0,values.get(i));
                values.remove(i+1);
                break;
            }
        }
    }
    */
    
    
    /*
            if (values.get(i).compareTo(BigInteger.ZERO) > 0 && i != 0) {
                outputBuffer.append("+");
            }
            if (values.get(i).compareTo(BigInteger.ZERO) != 0) {
                outputBuffer.append(values.get(i));
                outputBuffer.append(" *x^");
                outputBuffer.append(keys.get(i).getXpowEx());
                outputBuffer.append(" ");
                outputBuffer.append(" *sin(x)^");
                outputBuffer.append(keys.get(i).getSinEx());
                outputBuffer.append(" ");
                outputBuffer.append(" *cos(x)^");
                outputBuffer.append(keys.get(i).getCosEx());
                outputBuffer.append(" ");
            }
            
            /*
        String output = outputBuffer.toString();
        output = output.replace("^1","");
        output = output.replace("*x^0","");
        output = output.replace("*sin(x)^0","");
        output = output.replace("*cos(x)^0","");
        output = output.replace("1*","");
        */
}
