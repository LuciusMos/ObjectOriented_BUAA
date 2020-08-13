public class Problem1 {
    public class IntSet {
        
        //@ public model non_null int[] ia;
        //@ public invariant ia != null && (\forall int i,j; 0 <=i && i<j && j < ia.length; ia[i] != ia[j]);
        //@ public constraint Math.abs(ia.length - \old(ia.length)) <= 1;
        
        //@ ensures ia.length == 0;
        public IntSet() {} //构造操作
        
        /*@ assignable ia
          @ ensures (\exists int j; 0 <= j < ia.length; ia[j] == x);
          @*/
        public void insert(int x) {} //更新操作
        
        /*@ assignable ia
          @ ensures (\forall int j; 0 <= j <ia.length; ia[j] != x);
          @*/
        public void delete(int x) {} //更新操作
        
        //@ ensures \result == (\exist int i; 0 <= i < ia.length; ia[i] == x);
        public /*@ pure @*/ boolean isIn(int x) {} //观察操作
        
        //@ ensures \result == ia.length;
        public /*@ pure @*/ int size(){} //观察操作
        
        /* 该方法完成两个IntSet对象所包含元素的交换, 例如：
           IntSet对象a中的元素为{1，2，3}，IntSet对象b中的元素为{4，5，6}
           经过交换操作后，a中的元素应为{4, 5, 6}, b中的元素为{1, 2, 3}
           两个IntSet对象中元素的数量可以不相同，例如：
           IntSet对象a中的元素为{1，2，3，4}，IntSet对象b中的元素为{5，6}
           经过交换操作后，a中的元素应为{5, 6}, b中的元素为{1, 2, 3，4}
           该方法无返回值
          */
        /*@ assignable ia, a.ia
          @ ensures (\forall int j; 0 <= j < \old(ia.length); a.isIn(ia[j]));
          @ ensures (\forall int j; 0 <= j < \old(a.ia.length); this.isIn(ia[j]));
          @ ensures (\forall int j; \old(!this.isIn(i)); !a.isIn(j));
          @ ensures (\forall int j; \old(!a.isIn(i)); !this.isIn(j));
          @ ensures ia.length == \old(a.ia.length);
          @ ensures a.ia.length == \old(ia.length);
         */
        public void elementSwap(IntSet a) {}
        
        /* 该方法返回两个IntSet对象的对称差运算结果
           数学上，两个集合的对称差是只属于其中一个集合，而不属于另一个集合的元素组成的集合
           集合论中的这个运算相当于布尔逻辑中的异或运算，如 A,B两个集合的对称差记为A⊕B，
           则 A⊕B = (A-B)∪(B-A) = (A∪B)-(A∩B)
           例如：集合{1,2,3}和{3,4}的对称差为{1,2,4}
           如果对空的IntSet对象进行对称差的运算，将抛出NullPointerException的异常
          */
        /* @ public normal_behavior
           @ requires a.ia != null && ia != null;
           @ assignable \nothing;
           @ ensures (\forall int i; this.isIn(i) && a.isIn(i); !\result.isIn(this.isIn(i)));
           @ ensures (\forall int i; !this.isIn(i) && a.isIn(i); \result.isIn(this.isIn(i)));
           @ ensures (\forall int i; this.isIn(i) && !a.isIn(i); \result.isIn(this.isIn(i)));
           @ ensures (\forall int i; !this.isIn(i) && !a.isIn(i); !\result.isIn(this.isIn(i)));
           @ ensures (\forall int i, j; 0<= i < ia.length && 0 <= j < a.ia.length; (this.ia[i] == a.ia.[j]) ==> !\result.isIn(this.isIn(i)));
           @ ensures (\forall int i; 0<= i < \result.length; this.isIn(\result.ia[i]) || a.isIn(\result.ia[i]));
           @ ensures (\forall int i, j; 0<= i < ia.length && 0 <= j < a.ia.length; (!this.isIn(i) && a.isIn(j)) ==> \result.isIn(a.isIn(j)));
           @ ensures (\forall int i, j; 0<= i < ia.length && 0 <= j < a.ia.length; (!this.isIn(i) && a.isIn(j)) ==> \result.isIn(a.isIn(j)));
           @ also
           @ public exceptional_behavior
           @ requires a.ia == null || ia == null;
           @ assignable \nothing
           @ signals_only NullPointerException;
         */
        public IntSet symmetricDifference(IntSet a) throws NullPointerException {}
        
        
        //@ ensures \result == invariant(this);
        public /*@ pure @*/ boolean repOK(){}//针对不变式的检查
        
    }
}
