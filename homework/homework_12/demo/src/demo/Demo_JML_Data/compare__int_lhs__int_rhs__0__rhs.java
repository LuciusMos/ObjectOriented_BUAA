/*
 * Test data strategy for demo.Demo.
 *
 * Generated by JMLUnitNG 1.4 (116/OpenJML-20131218-REV3178), 2019-05-21 23:36 +0800.
 * (do not modify this comment, it is used by JMLUnitNG clean-up routines)
 */

 
package demo.Demo_JML_Data;

import org.jmlspecs.jmlunitng.iterator.ObjectArrayIterator;
import org.jmlspecs.jmlunitng.iterator.RepeatedAccessIterator;

/**
 * Test data strategy for demo.Demo. Provides
 * test values for parameter "int rhs" 
 * of method "int compare(int, int)". 
 * 
 * @author JMLUnitNG 1.4 (116/OpenJML-20131218-REV3178)
 * @version 2019-05-21 23:36 +0800
 */
public /*@ nullable_by_default */ class compare__int_lhs__int_rhs__0__rhs
  extends ClassStrategy_int {
  /**
   * @return local-scope values for parameter 
   *  "int rhs".
   */
  public RepeatedAccessIterator<?> localValues() {
    return new ObjectArrayIterator<Object>
    (new Object[]
     { /* add local-scope int values or generators here */ });
  }
}
