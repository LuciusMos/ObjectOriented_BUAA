import java.math.BigInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Term {
    private String termString;
    private String numString = "[+-]?\\d+";
    private String xpowString = "x(\\^([+-]?\\d+))?";
    private String sinString = "sin\\(x\\)(\\^([+-]?\\d+))?";
    private String cosStirng = "cos\\(x\\)(\\^([+-]?\\d+))?";
    private Pattern numPattern = Pattern.compile(numString);
    private Pattern xpowPattern = Pattern.compile(xpowString);
    private Pattern sinPattern = Pattern.compile(sinString);
    private Pattern cosPattern = Pattern.compile(cosStirng);
    private BigInteger[][] nums = new BigInteger[3][4];
    
    Term(String temp) {
        termString = temp.replaceAll("\\+","");
    }
    
    BigInteger[][] termDerivation() {
        boolean beginNegative = false;
        if (termString.charAt(0) == '-') {
            if (termString.length() > 1 && termString.charAt(1) == '-') {
                termString = termString.substring(2);
            } else {
                beginNegative = true;
                termString = termString.substring(1);
            }
        }
        String[] factors = termString.split("\\*");
        BigInteger a = BigInteger.ONE;
        BigInteger b = BigInteger.ZERO;
        BigInteger c = BigInteger.ZERO;
        BigInteger d = BigInteger.ZERO;
        for (String factor : factors) {
            Matcher num = numPattern.matcher(factor);
            Matcher xpow = xpowPattern.matcher(factor);
            Matcher sin = sinPattern.matcher(factor);
            Matcher cos = cosPattern.matcher(factor);
            if (num.matches()) {
                BigInteger temp = new BigInteger(num.group());
                a = a.multiply(temp);
            } else if (xpow.matches()) {
                if (xpow.group(2) != null) {
                    BigInteger temp = new BigInteger(xpow.group(2));
                    b = b.add(temp);
                } else {
                    b = b.add(BigInteger.ONE);
                }
            } else if (sin.matches()) {
                if (sin.group(2) != null) {
                    BigInteger temp = new BigInteger(sin.group(2));
                    c = c.add(temp);
                } else {
                    c = c.add(BigInteger.ONE);
                }
            } else if (cos.matches()) {
                if (cos.group(2) != null) {
                    BigInteger temp = new BigInteger(cos.group(2));
                    d = d.add(temp);
                } else {
                    d = d.add(BigInteger.ONE);
                }
            }
        }
        if (beginNegative) {
            a = a.negate();
        }
        derivation(a,b,c,d);
        return nums;
    }
    
    private void derivation(BigInteger a,BigInteger b,
                            BigInteger c,BigInteger d) {
        nums[0][0] = a.multiply(b);
        nums[0][1] = b.subtract(BigInteger.ONE);
        nums[0][2] = c;
        nums[0][3] = d;
        nums[1][0] = a.multiply(c);
        nums[1][1] = b;
        nums[1][2] = c.subtract(BigInteger.ONE);
        nums[1][3] = d.add(BigInteger.ONE);
        nums[2][0] = a.multiply(d).negate();
        nums[2][1] = b;
        nums[2][2] = c.add(BigInteger.ONE);
        nums[2][3] = d.subtract(BigInteger.ONE);
    }
    
}
