/*
 * Test data strategy for demo.Demo.
 *
 * Generated by JMLUnitNG 1.4 (116/OpenJML-20131218-REV3178), 2019-05-22 20:27 +0800.
 * (do not modify this comment, it is used by JMLUnitNG clean-up routines)
 */

 
package demo.Demo_JML_Data;

import org.jmlspecs.jmlunitng.iterator.ObjectArrayIterator;
import org.jmlspecs.jmlunitng.iterator.RepeatedAccessIterator;

/**
 * Test data strategy for demo.Demo. Provides
 * test values for parameter "String a" 
 * of method "boolean contains(String, String)". 
 * 
 * @author JMLUnitNG 1.4 (116/OpenJML-20131218-REV3178)
 * @version 2019-05-22 20:27 +0800
 */
public /*@ nullable_by_default */ class contains__String_a__String_b__20__a
  extends ClassStrategy_java_lang_String {
  /**
   * @return local-scope values for parameter 
   *  "String a".
   */
  public RepeatedAccessIterator<?> localValues() {
    return new ObjectArrayIterator<Object>
    (new Object[]
     { /* add local-scope java.lang.String values or generators here */ });
  }
}
