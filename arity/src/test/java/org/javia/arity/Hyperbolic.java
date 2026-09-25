package org.javia.arity;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * The inverse hyperbolic functions near the point where they are zero.
 *
 * <p>Eval's cases can't catch this: they accept anything within 1E-15 of the answer, and every
 * answer here is smaller than that. These are held to a couple of ulps of the true value instead.
 */
public class Hyperbolic {
  private static final int ULPS = 2;

  @Test
  public void atanhOfSmallNumbers() throws SyntaxException {
    check("atanh(1e-20)", 1e-20);
    check("atanh(5.12e-16)", 5.12e-16);
    check("atanh(1e-10)", 1e-10);
    check("atanh(-1e-10)", -1e-10);
    check("atanh(1e-5)", 1.0000000000333334E-5);
  }

  @Test
  public void atanhAwayFromZero() throws SyntaxException {
    check("atanh(0)", 0);
    check("atanh(0.5)", 0.5493061443340549);
    check("atanh(-0.5)", -0.5493061443340549);
    check("atanh(0.9)", 1.4722194895832204);
  }

  @Test
  public void asinhOfSmallNumbers() throws SyntaxException {
    check("asinh(1e-20)", 1e-20);
    check("asinh(1e-10)", 1e-10);
    check("asinh(-1e-10)", -1e-10);
    check("asinh(1e-5)", 9.999999999833334E-6);
  }

  @Test
  public void asinhAwayFromZero() throws SyntaxException {
    check("asinh(0)", 0);
    check("asinh(1)", 0.881373587019543);
    check("asinh(-1)", -0.881373587019543);
    check("asinh(2)", 1.4436354751788103);
    check("asinh(1e10)", 23.7189981105004);
  }

  @Test
  public void acoshJustAboveOne() throws SyntaxException {
    check("acosh(1)", 0);
    check("acosh(1.0000000001)", 1.4142136208675862E-5);
    check("acosh(1.00001)", 0.004472132228242651);
  }

  @Test
  public void acoshAwayFromOne() throws SyntaxException {
    check("acosh(1.5)", 0.9624236501192069);
    check("acosh(2)", 1.3169578969248168);
    check("acosh(10)", 2.993222846126381);
    check("acosh(1e300)", 691.4686750787737);
    assertTrue(Double.isNaN(MoreMath.acosh(0.5)));
    assertTrue(Double.isNaN(MoreMath.acosh(-3)));
  }

  /** Checks both ways of evaluating an expression against the true answer. */
  private static void check(String expr, double expected) throws SyntaxException {
    Symbols symbols = new Symbols();
    near(expr, expected, symbols.eval(expr));
    Complex complex = symbols.evalComplex(expr);
    near(expr + " (complex)", expected, complex.re);
    assertTrue(expr + " has an imaginary part: " + complex.im, complex.im == 0);
  }

  private static void near(String what, double expected, double actual) {
    double error = Math.abs(expected - actual);
    assertTrue(what + " came to " + actual + " rather than " + expected,
        error <= ULPS * Math.ulp(expected));
  }
}
