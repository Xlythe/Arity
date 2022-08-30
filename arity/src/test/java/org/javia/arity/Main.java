package org.javia.arity;

import org.junit.Assert;
import org.junit.Test;

import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

public class Main {
  @Test
  public void runUnitTests() {
    Assert.assertEquals("-Infinity", Util.doubleToString(Double.NEGATIVE_INFINITY, 5));
    Assert.assertEquals("NaN", Util.doubleToString(Double.NaN, 5));

    Complex c = new Complex();
    Complex d = new Complex();
    Complex e = new Complex();

    Assert.assertEquals("-i", Util.complexToString(c.set(0, -1), 10, 1));
    Assert.assertEquals("2.1", Util.complexToString(c.set(2.123, 0), 3, 0));
    Assert.assertEquals("i", Util.complexToString(c.set(0, 1.0000000000001), 20, 3));
    Assert.assertEquals("1-i", Util.complexToString(c.set(1, -1), 10, 1));
    Assert.assertEquals("1+i", Util.complexToString(c.set(1, 1), 10, 1));
    Assert.assertEquals("1.12+1.1i", Util.complexToString(c.set(1.12, 1.12), 9, 0));
    Assert.assertEquals("1.123-i", Util.complexToString(c.set(1.12345, -1), 7, 0));

    assertEquals(c.set(-1, 0).pow(d.set(0, 1)), e.set(0.04321391826377226, 0));
    assertEquals(c.set(-1, 0).pow(d.set(1, 1)), e.set(-0.04321391826377226, 0));

    assertEquals(c.set(-1, 0).abs(), 1);
    assertEquals(c.set(Math.E * Math.E, 0).log(), d.set(2, 0));
    assertEquals(c.set(-1, 0).log(),              d.set(0, Math.PI));

    assertEquals(c.set(2, 0).exp(),       d.set(Math.E*Math.E, 0));
    assertEquals(c.set(0, Math.PI).exp(), d.set(-1, 0));

    assertEquals(MoreMath.lgamma(1), 0);
    assertEquals(c.set(1, 0).lgamma(), d.set(0, 0));

    assertEquals(c.set(0, 0).factorial(), d.set(1, 0));
    assertEquals(c.set(1, 0).factorial(), d.set(1, 0));
    assertEquals(c.set(0, 1).factorial(), d.set(0.49801566811835596, -0.1549498283018106));
    assertEquals(c.set(-2, 1).factorial(), d.set(-0.17153291990834815, 0.32648274821006623));
    assertEquals(c.set(4, 0).factorial(), d.set(24, 0));
    assertEquals(c.set(4, 3).factorial(), d.set(0.016041882741649555, -9.433293289755953));

    assertEquals(Math.log(-1), Double.NaN);
    assertEquals(Math.log(-0.03), Double.NaN);
    assertEquals(MoreMath.intLog10(-0.03), 0);
    assertEquals(MoreMath.intLog10(0.03), -2);
    assertEquals(MoreMath.intExp10(3), 1000);
    assertEquals(MoreMath.intExp10(-1), 0.1);

    assertEquals(Util.shortApprox( 1.235, 0.02),  1.24);
    assertEquals(Util.shortApprox( 1.235, 0.4),   1.2000000000000002);
    assertEquals(Util.shortApprox(-1.235, 0.02), -1.24);
    assertEquals(Util.shortApprox(-1.235, 0.4),  -1.2000000000000002);
  }

  @Test
  public void testFrame() {
    try {
      Symbols symbols = new Symbols();
      symbols.define("a", 1);
      assertEquals(1, symbols.eval("a"));

      symbols.pushFrame();
      assertEquals(1, symbols.eval("a"));
      symbols.define("a", 2);
      assertEquals(2, symbols.eval("a"));
      symbols.define("a", 3);
      assertEquals(3, symbols.eval("a"));

      symbols.popFrame();
      assertEquals(1, symbols.eval("a"));

      // ----

      symbols = new Symbols();
      symbols.pushFrame();
      symbols.add(Symbol.makeArg("base", 0));
      symbols.add(Symbol.makeArg("x", 1));
      assertEquals(VM.LOAD1, symbols.lookupConst("x").op);
      symbols.pushFrame();
      assertEquals(VM.LOAD0, symbols.lookupConst("base").op);
      assertEquals(VM.LOAD1, symbols.lookupConst("x").op);
      symbols.popFrame();
      assertEquals(VM.LOAD0, symbols.lookupConst("base").op);
      assertEquals(VM.LOAD1, symbols.lookupConst("x").op);
      symbols.popFrame();
      assertEquals(VM.LOAD0, symbols.lookupConst("x").op);
    } catch (SyntaxException e) {
      fail(e.message);
    }
  }

  @Test
  public void testRecursiveEval() {
    Symbols symbols = new Symbols();
    symbols.define("myfun", new MyFun());
    try {
      Function f = symbols.compile("1+myfun(x)");
      assertEquals(2, f.eval(0));
      assertEquals(1, f.eval(1));
      assertEquals(0, f.eval(2));
      assertEquals(-1, f.eval(3));
    } catch (SyntaxException e) {
      fail(e.message);
    }
  }

  private static class MyFun extends Function {
    Symbols symbols = new Symbols();
    Function f;

    MyFun() {
      try {
        f = symbols.compile("1-x");
      } catch (SyntaxException e) {
        System.out.println("" + e);
      }
    }

    public double eval(double x) {
      return f.eval(x);
    }

    public int arity() {
      return 1;
    }
  }

  private static void assertEquals(Complex a, Complex b) {
    assertEquals(a.re, b.re);
    assertEquals(a.im, b.im);
  }

  private static void assertEquals(double a, Complex c) {
    assertEquals(a, c.re);
    if (!(Double.isNaN(a) && Double.isNaN(c.im))) {
      assertEquals(0, c.im);
    }
  }

  private static void assertEquals(double a, double b) {
    if (a == b) {
      return;
    }

    if (Double.isNaN(a) && Double.isNaN(b)) {
      return;
    }

    assertTrue(Math.abs((a - b) / b) < 1E-15 || Math.abs(a - b) < 1E-15);
  }
}
