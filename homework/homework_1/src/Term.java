import java.math.BigInteger;
/*
import java.util.regex.Matcher;
import java.util.regex.Pattern;
*/

public class Term {
    /*
    String pattern1 = "[+-]?[+-]?\\d+\\*";
    Pattern p1 = Pattern.compile(pattern1);
    String pattern2 = "\\^[+-]?\\d+";
    Pattern p2 = Pattern.compile(pattern2);
    */
    private String termString;
    
    public Term(String temp) {
        termString = temp;
    }
    
    BigInteger[] term2Nums() {
        BigInteger[] nums = new BigInteger[2];
        /*
        Matcher m1 = p1.matcher(termString);
        if(m1.find()) {
            String temp1 = termString.replaceAll("+","");
            String temp2 = temp1.substring(0,temp1.length()-1);
            BigInteger num1 = new BigInteger(temp2);
        }
        */
        termString = termString.replaceAll("\\+","");
        boolean containsNegative = false;
        if (termString.charAt(0) == '-') {
            if (termString.charAt(1) == '-') {
                termString = termString.substring(2);
            } else {
                containsNegative = true;
                termString = termString.substring(1);
            }
        }
        String split = "[*^]";
        String[] numsString = termString.split(split);
        int numOfNumsString = numsString.length;
        if (numOfNumsString == 1) {
            if (termString.contains("x")) {
                nums[0] = BigInteger.ONE;//nums[0]--coef
                nums[1] = BigInteger.ONE;//nums[1]--expo
            } else {
                nums[0] = new BigInteger(termString);
                nums[1] = BigInteger.ZERO;
            }
        } else if (numOfNumsString == 2) {
            if (numsString[0].equals("x")) { //x^__
                nums[0] = BigInteger.ONE;
                nums[1] = new BigInteger(numsString[1]);
            } else { //__*x
                nums[0] = new BigInteger(numsString[0]);
                nums[1] = BigInteger.ONE;
            }
        } else if (numOfNumsString == 3) {
            nums[0] = new BigInteger(numsString[0]);
            nums[1] = new BigInteger(numsString[2]);
        }
        if (containsNegative) {
            nums[0] = nums[0].negate();
        }
        if (nums[1].compareTo(BigInteger.ZERO) == 0) {
            nums[0] = new BigInteger("0");
            nums[1] = new BigInteger("0");
        } else {
            nums[0] = nums[0].multiply(nums[1]);
            nums[1] = nums[1].subtract(BigInteger.ONE);
        }
        return nums;
    }
}
