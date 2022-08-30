package org.javia.arity;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class Size {
  private static class SizeCase {
    public SizeCase(int size, String v, String s) {
      this.size = size;
      this.val = v;
      this.res = s;
    }

    public int size;
    public String val;
    public String res;
  }

  private static final SizeCase[] sizeCases = {
          new SizeCase(9,  "1111111110", "1.11111E9"),
          new SizeCase(10, "1111111110", "1111111110"),
          new SizeCase(10,"11111111110", "1.11111E10"),
          new SizeCase(10, "12.11111E9", "12.11111E9"),
          new SizeCase(9,  "12.34567E9", "12.3456E9"),
          new SizeCase(9,  "12345678E3", "1.2345E10"),
          new SizeCase(9, "-12345678E3", "-1.234E10"),

          new SizeCase(9, "-0.00000007", "-0.000000"),

          new SizeCase(5, "-1.23E123", "-1.23E123"),
          new SizeCase(5, "-1.2E123", "-1.2E123"),
          new SizeCase(5, "-1E123", "-1E123"),
          new SizeCase(2, "-1", "-1"),
          new SizeCase(1, "-1", "-1"),
          new SizeCase(1, "-0.02", "-0.02"),
          new SizeCase(1, "0.02", "0"),
  };

  @Test
  public void testSizeCases() {
    for (SizeCase c : sizeCases) {
      String truncated = Util.sizeTruncate(c.val, c.size);
      assertEquals(truncated, c.res);
    }
  }
}
