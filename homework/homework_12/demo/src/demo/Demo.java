// demo/Demo.java
package demo;

public class Demo {
    /*@ public normal_behaviour
      @ ensures \result == lhs - rhs;
    */
    public static int compare(int lhs, int rhs) {
        return lhs - rhs;
    }
   
    public static void main(String[] args) {
        compare(114514,1919810);
    }
}