/*
 * Test data strategy for demo.Demo.
 *
 * Generated by JMLUnitNG 1.4 (116/OpenJML-20131218-REV3178), 2019-05-22 20:27 +0800.
 * (do not modify this comment, it is used by JMLUnitNG clean-up routines)
 */

package demo.Demo_JML_Data;


import org.jmlspecs.jmlunitng.iterator.ObjectArrayIterator;
import org.jmlspecs.jmlunitng.iterator.RepeatedAccessIterator;
import demo.PackageStrategy_double;
/**
 * Test data strategy for demo.Demo. Provides
 * class-scope test values for type double.
 * 
 * @author JMLUnitNG 1.4 (116/OpenJML-20131218-REV3178)
 * @version 2019-05-22 20:27 +0800
 */
public /*@ nullable_by_default */ class ClassStrategy_double 
  extends PackageStrategy_double {
  /**
   * @return class-scope values for type double.
   */
  public RepeatedAccessIterator<?> classValues() {
    return new ObjectArrayIterator<Object>
    (new Object[] 
     { /* add class-scope double values or generators here */ });
  }
}