/*
 * Test data strategy for demo.Demo.
 *
 * Generated by JMLUnitNG 1.4 (116/OpenJML-20131218-REV3178), 2019-05-21 23:36 +0800.
 * (do not modify this comment, it is used by JMLUnitNG clean-up routines)
 */

package demo;

import java.util.LinkedList;
import java.util.List;

import org.jmlspecs.jmlunitng.iterator.InstantiationIterator;
import org.jmlspecs.jmlunitng.iterator.IteratorAdapter;
import org.jmlspecs.jmlunitng.iterator.NonNullMultiIterator;
import org.jmlspecs.jmlunitng.iterator.ObjectArrayIterator;
import org.jmlspecs.jmlunitng.iterator.RepeatedAccessIterator;
import org.jmlspecs.jmlunitng.strategy.ObjectStrategy;

/**
 * Test data strategy for demo.Demo. Provides
 * instances of demo.Demo for testing, using
 * parameters from constructor tests.
 * 
 * @author JMLUnitNG 1.4 (116/OpenJML-20131218-REV3178)
 * @version 2019-05-21 23:36 +0800
 */
public /*@ nullable_by_default */ class Demo_InstanceStrategy extends ObjectStrategy {
  /**
   * @return local-scope instances of Demo.
   */
  public RepeatedAccessIterator<?> localValues() {
    return new ObjectArrayIterator<Object>
    (new Object[]
     { /* add demo.Demo values or generators here */ });
  }

  /**
   * @return default instances of Demo, generated
   *  using constructor test parameters.
   */ 
  public RepeatedAccessIterator<demo.Demo> defaultValues() {
    final List<RepeatedAccessIterator<demo.Demo>> iters = 
      new LinkedList<RepeatedAccessIterator<demo.Demo>>();

    // an instantiation iterator for the default constructor
    // (if there isn't one, it will fail silently)
    iters.add(new InstantiationIterator<demo.Demo>
      (demo.Demo.class, 
       new Class<?>[0], 
       new ObjectArrayIterator<Object[]>(new Object[][]{{}})));

    return new NonNullMultiIterator<demo.Demo>(iters);
  }

  /**
   * Constructor. The boolean parameter to <code>setReflective</code>
   * determines whether or not reflection will be used to generate
   * test objects, and the int parameter to <code>setMaxRecursionDepth</code>
   * determines how many levels reflective generation of self-referential classes
   * will recurse.
   *
   * @see ObjectStrategy#setReflective(boolean)
   * @see ObjectStrategy#setMaxRecursionDepth(int)
   */
  public Demo_InstanceStrategy() {
    super(demo.Demo.class);
    setReflective(false);
    // uncomment to control the maximum reflective instantiation
    // recursion depth, 0 by default
    // setMaxRecursionDepth(0);
  }
}
