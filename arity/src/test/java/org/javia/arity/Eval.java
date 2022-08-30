package org.javia.arity;

import org.junit.Test;

import static junit.framework.Assert.assertTrue;

public class Eval {
  private static class EvalCase {
    String expr;
    double result;
    Complex cResult;

    static final double ERR = -2, FUN = -3;

    EvalCase(String expr, double result) {
      this.expr = expr;
      this.result = result;
    }

    EvalCase(String expr, Complex result) {
      this.expr = expr;
      this.cResult = result;
    }
  }

  private static final EvalCase[] cases = {
          new EvalCase(".", 0),
          new EvalCase("1+.", 1),
          new EvalCase("1", 1),
          new EvalCase("\u03c0", Math.PI),
          new EvalCase("2\u00d73", 6), //2*3
          new EvalCase("1+\u221a9*2", 7), //1+sqrt(9)*2
          new EvalCase("3\u221a 4", 6), //3*sqrt(4)
          new EvalCase("\u221a16sin(2\u03c0/4)", 4), //sqrt(16)*sin(2pi/4)
          new EvalCase("1+", EvalCase.ERR),
          new EvalCase("1+1", 2),
          new EvalCase("1+-1", 0),
          new EvalCase("-0.5", -.5),
          new EvalCase("+1e2", 100),
          new EvalCase("1e-1", .1),
          new EvalCase("1e\u22122", .01), //unicode minus
          new EvalCase("-2^3!", -64),
          new EvalCase("(-2)^3!", 64),
          new EvalCase("-2^1^2", -2),
          new EvalCase("--1", 1),
          new EvalCase("-3^--2", -9),
          new EvalCase("1+2)(2+3", 15),
          new EvalCase("1+2)!^-2", 1./36),
          new EvalCase("sin(0)", 0),
          new EvalCase("cos(0)", 1),
          new EvalCase("sin(-1--1)", 0),
          new EvalCase("-(2+1)*-(4/2)", 6),
          new EvalCase("-.5E-1", -.05),
          new EvalCase("1E1.5", EvalCase.ERR),
          new EvalCase("2 3 4", 24),
          new EvalCase("pi", Math.PI),
          new EvalCase("e", Math.E),
          new EvalCase("sin(pi/2)", 1),
          new EvalCase("f=sin(2x)", EvalCase.FUN),
          new EvalCase("f(pi/2)", 0),
          new EvalCase("a=3", 3),
          new EvalCase("b=a+1", 4),
          new EvalCase("f(x, y) = x*(y+1)", EvalCase.FUN),
          new EvalCase("=", EvalCase.ERR),
          new EvalCase("f(a, b-a)", 6),
          new EvalCase(" f(a pi/4)", -1),
          new EvalCase("f (  1  +  1  , a+1)", 10),
          new EvalCase("g(foo) = f (f(foo, 1)pi/2)", EvalCase.FUN),
          new EvalCase("g(.5*2)", 0),
          new EvalCase("NaN", Double.NaN),
          new EvalCase("Inf", Double.POSITIVE_INFINITY),
          new EvalCase("Infinity", Double.POSITIVE_INFINITY),
          new EvalCase("-Inf", Double.NEGATIVE_INFINITY),
          new EvalCase("0/0", Double.NaN),

          new EvalCase("comb(11, 9)", 55),
          new EvalCase("perm(11, 2)", 110),
          new EvalCase("comb(1000, 999)", 1000),
          new EvalCase("perm(1000, 1)", 1000),

          new EvalCase("c(x)=1+x^2", EvalCase.FUN),
          new EvalCase("c(3-1)", 5),
          new EvalCase("abs(3-4i)", 5),
          new EvalCase("exp(pi*i)", -1),

          new EvalCase("5%", 0.05),
          new EvalCase("200+5%", 210),
          new EvalCase("200-5%", 190),
          new EvalCase("100/200%", 50),
          new EvalCase("100+200%+5%", 315),
          new EvalCase("p1(x)=200+5%+x", EvalCase.FUN),
          new EvalCase("p1(0)", 210),
          new EvalCase("p2(x,y)=x+y%+(2*y)%", EvalCase.FUN),
          new EvalCase("p2(200,5)", 231),

          new EvalCase("mod(5,3)", 2),
          new EvalCase("5.2 # 3.2", 2),

          new EvalCase("f(x)=3", EvalCase.FUN),
          new EvalCase("g(x)=f(x)", EvalCase.FUN),
          new EvalCase("g(1)", 3),

          new EvalCase("a(x)=i+x-x", EvalCase.FUN),
          new EvalCase("b(x)=a(x)*a(x)", EvalCase.FUN),
          new EvalCase("b(5)", -1),

          new EvalCase("h(x)=sqrt(-1+x-x)", EvalCase.FUN),
          new EvalCase("k(x)=h(x)*h(x)", EvalCase.FUN),
          new EvalCase("k(5)", -1),

          new EvalCase("pi=4", 4),
          new EvalCase("pi", Math.PI),

          new EvalCase("fc(x)=e^(i*x^2", EvalCase.FUN),
          new EvalCase("fc(0)", 1),
          new EvalCase("aa(x)=sin(x)^1+sin(x)^0", EvalCase.FUN),
          new EvalCase("aa(0)", 1),
          new EvalCase("null(x)=0", EvalCase.FUN),
          new EvalCase("n(x)=null(sin(x))", EvalCase.FUN),
          new EvalCase("n(1)", 0),
          new EvalCase("(2,", EvalCase.ERR),

          new EvalCase("100.1-100-.1", 0),
          new EvalCase("1.1-1+(-.1)", 0),

          new EvalCase("log(2,8)", 3),
          new EvalCase("log(9,81)", 2),
          new EvalCase("log(4,2)", .5),

          new EvalCase("sin'(0)", 1),
          new EvalCase("cos'(0)", 0),
          new EvalCase("cos'(pi/2)", -1),
          new EvalCase("f(x)=2*x^3+x^2+100", EvalCase.FUN),
          new EvalCase("f'(1)", 8),
          new EvalCase("f'(2)", 28),
          new EvalCase("abs'(2)", 1),
          new EvalCase("abs'(-3)", -1),

          new EvalCase("0x0", 0),
          new EvalCase("0x100", 256),
          new EvalCase("0X10", 16),
          new EvalCase("0b10", 2),
          new EvalCase("0o10", 8),
          new EvalCase("0o8", EvalCase.ERR),
          new EvalCase("0xg", EvalCase.ERR),
          new EvalCase("0b20", EvalCase.ERR),
          new EvalCase("sin(0x1*pi/2)", 1),

          new EvalCase("ln(e)", 1),
          new EvalCase("log(10)", 1),
          new EvalCase("log10(100)", 2),
          new EvalCase("lg(.1)", -1),
          new EvalCase("log2(2)", 1),
          new EvalCase("lb(256)", 8),

          new EvalCase("rnd()*0", 0),
          new EvalCase("rnd(5)*0", 0),

          new EvalCase("max(2,3)", 3),
          new EvalCase("min(2,3)", 2),
          new EvalCase("fm(x)=max(2, x)", EvalCase.FUN),
          new EvalCase("fm(6)", 6),
          new EvalCase("fmin(x)=min(2, x)", EvalCase.FUN),
          new EvalCase("fmin(1)", 1),
          new EvalCase("fmin(3)", 2),
          new EvalCase("cbrt(8)", 2),
          new EvalCase("cbrt(-8)", -2),

          new EvalCase("s=sign(x)", EvalCase.FUN),
          new EvalCase("s(2)", 1),
          new EvalCase("s(-2)", -1),
          new EvalCase("s(0)", 0),
          new EvalCase("s(nan)", Double.NaN),

          new EvalCase("real(8.123)", 8.123),
          new EvalCase("imag(8.123)", 0),
          new EvalCase("im(sqrt(-1))", 1),
          new EvalCase("im(nan)", Double.NaN),
  };

  private static final double ONE_SQRT2 = 0.7071067811865475; // sin(pi/4)

  private static final EvalCase[] casesComplex = {
          new EvalCase("sqrt(-1)^2", new Complex(-1, 0)),
          new EvalCase("i", new Complex(0, 1)),
          new EvalCase("sqrt(-1)", new Complex(0, 1)),

          new EvalCase("c(2+0i)", new Complex(5, 0)),
          new EvalCase("c(1+i)", new Complex(1, 2)),
          new EvalCase("ln(-1)", new Complex(0, -Math.PI)),
          new EvalCase("i^i", new Complex(0.20787957635076193, 0)),
          new EvalCase("gcd(135-14i, 155+34i)", new Complex(12, -5)),
          new EvalCase("comb(1+.5i, 1)", new Complex(1, .5)),
          new EvalCase("perm(2+i, 2)", new Complex(1, 3)),
          new EvalCase("fc(2)", new Complex(-0.6536436208636119, -0.7568024953079282)),

          new EvalCase("sign(2i)", new Complex(0, 1)),
          new EvalCase("sign(-i)", new Complex(0, -1)),
          new EvalCase("sign(nan)", new Complex(Double.NaN, 0)),
          new EvalCase("sign(nan i)", new Complex(Double.NaN, 0)),
          new EvalCase("sign(0)", new Complex(0, 0)),
          new EvalCase("sign(2-2i)", new Complex(ONE_SQRT2, -ONE_SQRT2)),

          // These functions have no imaginary part
          new EvalCase("real(8.123)", new Complex(8.123,0)),
          new EvalCase("imag(8.123)", new Complex(0,0)),
          new EvalCase("real(1+3i)", new Complex(1,0)),
          new EvalCase("imag(1+3i)", new Complex(3,0)),
          new EvalCase("re(1+3i)", new Complex(1,0)),
          new EvalCase("im(1+3i)", new Complex(3,0)),
  };

  @Test
  public void testEval() {
    Symbols symbols = new Symbols();
    for (EvalCase c : cases) {
      try {
        double actual, actual2;
        new Complex();
        Complex complex, complex2;

        FunctionAndName fan = symbols.compileWithName(c.expr);
        Function f = fan.function;
        symbols.define(fan);
        if (f.arity() == 0) {
          actual = f.eval();
          complex = f.evalComplex();
          assertEquals(actual, complex);

          if (!Symbols.isDefinition(c.expr)) {
            actual2 = symbols.eval(c.expr);
            complex2 = symbols.evalComplex(c.expr);
            assertEquals(actual, actual2);
            assertTrue(complex.equals(complex2));
          }
        } else {
          actual = EvalCase.FUN;
        }
        assertEquals(c.result, actual);
      } catch (SyntaxException e) {
        assertEquals(EvalCase.ERR, c.result);
      }
    }

    for (EvalCase complex : casesComplex) {
      try {
        Complex result = symbols.evalComplex(complex.expr);
        assertEquals(complex.cResult, result);
      } catch (SyntaxException ignored) {
      }
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
