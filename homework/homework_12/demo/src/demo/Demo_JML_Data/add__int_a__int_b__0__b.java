/*
 * Test data strategy for demo.Demo.
 *
 * Generated by JMLUnitNG 1.4 (116/OpenJML-20131218-REV3178), 2019-05-22 19:36 +0800.
 * (do not modify this comment, it is used by JMLUnitNG clean-up routines)
 */

 
package demo.Demo_JML_Data;

import org.jmlspecs.jmlunitng.iterator.ObjectArrayIterator;
import org.jmlspecs.jmlunitng.iterator.RepeatedAccessIterator;

/**
 * Test data strategy for demo.Demo. Provides
 * test values for parameter "int b" 
 * of method "int add(int, int)". 
 * 
 * @author JMLUnitNG 1.4 (116/OpenJML-20131218-REV3178)
 * @version 2019-05-22 19:36 +0800
 */
public /*@ nullable_by_default */ class add__int_a__int_b__0__b
  extends ClassStrategy_int {
  /**
   * @return local-scope values for parameter 
   *  "int b".
   */
  public RepeatedAccessIterator<?> localValues() {
    return new ObjectArrayIterator<Object>
    (new Object[]
     { /* add local-scope int values or generators here */ });
  }
}
