import java.math.BigInteger;

class Expo/* implements Comparable*/ {
    private BigInteger xpowEx;
    private BigInteger sinEx;
    private BigInteger cosEx;
    
    Expo(BigInteger a, BigInteger b, BigInteger c) {
        xpowEx = a;
        sinEx = b;
        cosEx = c;
    }
    
    BigInteger getXpowEx() {
        return xpowEx;
    }
    
    BigInteger getSinEx() {
        return sinEx;
    }
    
    BigInteger getCosEx() {
        return cosEx;
    }
    
    @Override
    public int hashCode() {
        return this.xpowEx.hashCode() *
                this.sinEx.hashCode() * this.cosEx.hashCode();
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Expo)) {
            return false;
        }
        Expo expo = (Expo)o;
        return expo.cosEx.equals(this.cosEx) &&
                expo.sinEx.equals(this.sinEx) &&
                expo.xpowEx.equals(this.xpowEx);
    }
    
    public int compareTo(Expo o) {
        if (this.xpowEx.compareTo(o.xpowEx) > 0) {
            return 1;
        } else if (this.xpowEx.compareTo(o.xpowEx) == 0 &&
                this.sinEx.compareTo(o.sinEx) > 0) {
            return 1;
        } else if (this.sinEx.compareTo(o.sinEx) == 0 &&
                this.cosEx.compareTo(o.cosEx) > 0) {
            return 1;
        } else if (this.cosEx.compareTo(o.cosEx) == 0) {
            return 0;
        } else {
            return -1;
        }
    }
    
}
