// This file is automatically generated by the build.py script. Do not edit!

/*
 * Copyright (C) 2008-2009 Mihai Preda.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.javia.arity;

class VM {

  static final byte
          RESERVED = 0,
          CONST = 1,
          CALL = 2,
          ADD = 3,
          SUB = 4,
          MUL = 5,
          DIV = 6,
          MOD = 7,
          RND = 8,
          UMIN = 9,
          POWER = 10,
          FACT = 11,
          PERCENT = 12,
          SQRT = 13,
          CBRT = 14,
          EXP = 15,
          LN = 16,
          SIN = 17,
          COS = 18,
          TAN = 19,
          ASIN = 20,
          ACOS = 21,
          ATAN = 22,
          SINH = 23,
          COSH = 24,
          TANH = 25,
          ASINH = 26,
          ACOSH = 27,
          ATANH = 28,
          ABS = 29,
          FLOOR = 30,
          CEIL = 31,
          SIGN = 32,
          MIN = 33,
          MAX = 34,
          GCD = 35,
          COMB = 36,
          PERM = 37,
          LOAD0 = 38,
          LOAD1 = 39,
          LOAD2 = 40,
          LOAD3 = 41,
          LOAD4 = 42,
          REAL = 43,
          IMAG = 44;

  static final String[] opcodeName = {"reserved", "const", "call", "add", "sub", "mul", "div", "mod", "rnd", "umin", "power", "fact", "percent", "sqrt", "cbrt", "exp", "ln", "sin", "cos", "tan", "asin", "acos", "atan", "sinh", "cosh", "tanh", "asinh", "acosh", "atanh", "abs", "floor", "ceil", "sign", "min", "max", "gcd", "comb", "perm", "load0", "load1", "load2", "load3", "load4", "real", "imag"};

  static final byte[] arity = {0, 0, -1, 2, 2, 2, 2, 2, 0, 1, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 0, 0, 0, 0, 0, 1, 1};

  static final byte[] builtins = {RND, SQRT, CBRT, SIN, COS, TAN, ASIN, ACOS, ATAN, SINH, COSH, TANH, ASINH, ACOSH, ATANH, EXP, LN, ABS, FLOOR, CEIL, SIGN, MIN, MAX, GCD, COMB, PERM, MOD, REAL, IMAG};

}
