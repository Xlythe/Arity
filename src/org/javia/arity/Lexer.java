/*
 * Copyright (C) 2007-2008 Mihai Preda.
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

class Lexer {
    static final int
        ADD = 1, 
        SUB = 2, 
        MUL = 3, 
        DIV = 4, 
        MOD = 5,
        UMIN   = 6, 
        POWER  = 7, 
        FACT   = 8,
        NUMBER = 9, 
        CONST  = 10,
        CALL   = 11, 
        COMMA  = 12, 
        LPAREN = 13, 
        RPAREN = 14,
        END    = 15,
        SQRT   = 16,
        PERCENT = 17;
        
    static final Token
        TOK_ADD    = new Token(ADD, 4, Token.LEFT, VM.ADD),
        TOK_SUB    = new Token(SUB, 4, Token.LEFT, VM.SUB),

        TOK_MUL    = new Token(MUL, 5, Token.LEFT, VM.MUL),
        TOK_DIV    = new Token(DIV, 5, Token.LEFT, VM.DIV),
        TOK_MOD    = new Token(MOD, 5, Token.LEFT, VM.MOD),

        TOK_UMIN   = new Token(UMIN, 6, Token.PREFIX, VM.UMIN),

        TOK_POWER  = new Token(POWER, 7, Token.RIGHT, VM.POWER),

        TOK_FACT   = new Token(FACT,  8, Token.SUFIX, VM.FACT),
        TOK_PERCENT = new Token(PERCENT, 9, Token.SUFIX, VM.PERCENT),

        TOK_SQRT   = new Token(SQRT,  10, Token.PREFIX, VM.SQRT),

        TOK_LPAREN = new Token(LPAREN, 1, Token.PREFIX, 0),
        TOK_RPAREN = new Token(RPAREN, 3, 0, 0),
        TOK_COMMA  = new Token(COMMA,  2, 0, 0),
        TOK_END    = new Token(END,    0, 0, 0),

        TOK_NUMBER = new Token(NUMBER, 20, 0, 0),
        TOK_CONST  = new Token(CONST,  20, 0, 0);

    private static final char
        UNICODE_MINUS = '\u2212',
        UNICODE_MUL   = '\u00d7',
        UNICODE_DIV   = '\u00f7',
        UNICODE_SQRT  = '\u221a';

    private static final String WHITESPACE = " \n\r\t";
    private static final char END_MARKER = '$';
    private char[] input = new char[32];
    private int pos;
    private SyntaxException exception;

    Lexer(SyntaxException exception) {
        this.exception = exception;
    }

    void scan(String str, TokenConsumer consumer) throws SyntaxException {
        exception.expression = str;
        if (str.indexOf(END_MARKER) != -1) {
            throw exception.set("Invalid character '" + END_MARKER + '\'', str.indexOf(END_MARKER));
        }
        init(str);
        consumer.start();
        Token token;
        do {
            int savePos = pos;
            token = nextToken();
            token.position = savePos;
            consumer.push(token);
        } while (token != TOK_END);
    }

    private void init(String str) {
        int len = str.length();
        if (input.length < len + 1) {
            input = new char[len+1];
        }
        str.getChars(0, len, input, 0);
        input[len] = END_MARKER;
        pos = 0;
    }

    Token nextToken() throws SyntaxException {
        while (WHITESPACE.indexOf(input[pos]) != -1) {
            ++pos;
        }

        char c = input[pos];
        int begin = pos++;

        switch (c) {
        case '!': return TOK_FACT;
        case END_MARKER: return TOK_END;
        case '%': return TOK_PERCENT;
        case '#': return TOK_MOD;
        case '(': return TOK_LPAREN;
        case ')': return TOK_RPAREN;
        case '*': return TOK_MUL;
        case '+': return TOK_ADD;
        case ',': return TOK_COMMA;
        case '-': return TOK_SUB;
        case '/': return TOK_DIV;
        }

        int p  = pos;
        if (('0' <= c && c <= '9') || c == '.') {
            if (c == '0') {
                char cc = Character.toLowerCase(input[p]);
                int base = (cc=='x') ? 16 : (cc=='b') ? 2 : (cc=='o') ? 8 : 0;
                if (base > 0) {
                    p++;
                    do { 
                        c = input[p++];
                    } while (('a' <= c && c <= 'z') ||
                             ('A' <= c && c <= 'Z') ||
                             ('0' <= c && c <= '9'));
                    String coded = String.valueOf(input, begin+2, p-3-begin);
                    pos = p-1;
                    try {
                        return TOK_NUMBER.setValue(Integer.parseInt(coded, base));
                    } catch (NumberFormatException e) {
                        throw exception.set("invalid number '" + String.valueOf(input, begin, p-1-begin) + "'", begin);
                    }
                }
            }
            
            while (('0' <= c && c <= '9') || c == '.' || c == 'E' || c == 'e') {
                //accept '-' only after E
                if ((c == 'E' || c == 'e') && (input[p] == '-' || input[p] == UNICODE_MINUS)) {
                    input[p] = '-'; //replace unicode with plain minus, for Double.parseDouble()
                    ++p; 
                }
                c = input[p++];
            } 
            pos = p-1;
            String nbStr = String.valueOf(input, begin, p-1-begin);
            try {
                // parse single dot as 0
                if (nbStr.equals(".")) {
                    return TOK_NUMBER.setValue(0);
                } else {
                    double numberValue = Double.parseDouble(nbStr);
                    return TOK_NUMBER.setValue(numberValue);
                }
            } catch (NumberFormatException e) {
                throw exception.set("invalid number '" + nbStr + "'", begin);
            }
        } else if (('a' <= c && c <= 'z') ||
                   ('A' <= c && c <= 'Z')) {
            do {
                c = input[p++];
            } while (('a' <= c && c <= 'z') ||
                     ('A' <= c && c <= 'Z') ||
                     ('0' <= c && c <= '9'));
            if (c == '\'') {
                c = input[p++];
            }
            String nameValue = String.valueOf(input, begin, p-1-begin);
            while (WHITESPACE.indexOf(c) != -1) {
                c = input[p++];
            }
            if (c == '(') {
                pos = p;
                return (new Token(CALL, 0, Token.PREFIX, 0)).setAlpha(nameValue);
            } else {
                pos = p-1;                
                return TOK_CONST.setAlpha(nameValue);
            }
        } else if ((c >= '\u0391' && c <= '\u03a9') || (c >= '\u03b1' && c <= '\u03c9')
                   || c == '\u221e') {
            return TOK_CONST.setAlpha(""+c);
        } else { 
            switch (c) {
            case '^':
                return TOK_POWER;
            case UNICODE_MUL:
                return TOK_MUL;
            case UNICODE_DIV:
                return TOK_DIV;
            case UNICODE_MINUS:
                return TOK_SUB;
            case UNICODE_SQRT:
                return TOK_SQRT;
            default:
                throw exception.set("invalid character '" + c + "'", begin); 
            }
        }
    }
}
