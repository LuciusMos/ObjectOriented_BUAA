/*
 * Test data strategy for demo.Demo.
 *
 * Generated by JMLUnitNG 1.4 (116/OpenJML-20131218-REV3178), 2019-05-21 23:36 +0800.
 * (do not modify this comment, it is used by JMLUnitNG clean-up routines)
 */

package demo.Demo_JML_Data;


import org.jmlspecs.jmlunitng.iterator.ObjectArrayIterator;
import org.jmlspecs.jmlunitng.iterator.RepeatedAccessIterator;
import demo.PackageStrategy_java_lang_String;
/**
 * Test data strategy for demo.Demo. Provides
 * class-scope test values for type java.lang.String.
 * 
 * @author JMLUnitNG 1.4 (116/OpenJML-20131218-REV3178)
 * @version 2019-05-21 23:36 +0800
 */
public /*@ nullable_by_default */ class ClassStrategy_java_lang_String 
  extends PackageStrategy_java_lang_String {
  /**
   * @return class-scope values for type java.lang.String.
   */
  public RepeatedAccessIterator<?> classValues() {
    return new ObjectArrayIterator<Object>
    (new Object[] 
     { /* add class-scope java.lang.String values or generators here */ });
  }
}