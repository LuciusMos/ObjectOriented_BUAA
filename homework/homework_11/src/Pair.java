import java.util.Objects;

public class Pair {
    private Integer left;
    private Integer right;
    
    Pair() {}
    
    Pair(Integer l, Integer r) {
        left = l;
        right = r;
    }
    
    Integer getLeft() {
        return left;
    }
    
    Integer getRight() {
        return right;
    }
    
    void swapLeftAndRight() {
        int temp = left;
        left = right;
        right = temp;
    }
    
    void set(int left, int right) {
        this.right = right;
        this.left = left;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Pair)) {
            return false;
        }
        Pair pair = (Pair) o;
        return Objects.equals(getLeft(), pair.getLeft()) &&
                Objects.equals(getRight(), pair.getRight());
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(getLeft(), getRight());
    }
}
