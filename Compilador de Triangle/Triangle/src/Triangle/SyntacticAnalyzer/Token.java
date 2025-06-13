/*
 * @(#)Token.java                        2.1 2003/10/07
 *
 * Copyright (C) 1999, 2003 D.A. Watt and D.F. Brown
 * Dept. of Computing Science, University of Glasgow, Glasgow G12 8QQ Scotland
 * and School of Computer and Math Sciences, The Robert Gordon University,
 * St. Andrew Street, Aberdeen AB25 1HG, Scotland.
 * All rights reserved.
 *
 * This software is provided free for educational use only. It may
 * not be used for commercial purposes without the prior written permission
 * of the authors.
 */

package Triangle.SyntacticAnalyzer;


final class Token extends Object {

  protected int kind;
  protected String spelling;
  protected SourcePosition position;

  public Token(int kind, String spelling, SourcePosition position) {

    if (kind == Token.IDENTIFIER) {
      int currentKind = firstReservedWord;
      boolean searching = true;

      while (searching) {
        int comparison = tokenTable[currentKind].compareTo(spelling);
        if (comparison == 0) {
          this.kind = currentKind;
          searching = false;
        } else if (comparison > 0 || currentKind == lastReservedWord) {
          this.kind = Token.IDENTIFIER;
          searching = false;
        } else {
          currentKind ++;
        }
      }
    } else
      this.kind = kind;

    this.spelling = spelling;
    this.position = position;

  }

  public static String spell (int kind) {
    return tokenTable[kind];
  }

  public String toString() {
    return "Kind=" + kind + ", spelling=" + spelling +
      ", position=" + position;
  }

  // Token classes...

  public static final int

    // literals, identifiers, operators...
    INTLITERAL	= 0,
    CHARLITERAL	= 1,
    IDENTIFIER	= 2,
    OPERATOR	= 3,

    // reserved words - must be in alphabetical order...
    ARRAY		= 4,
    BEGIN		= 5,
    CASE                = 6,  //case
    CONST		= 7,
    DELETE              = 8,  // delete
    DO			= 9,
    DOWNTO              = 10, //downto
    ELSE		= 11,
    END			= 12,
    FOR                 = 13, //for
    FROM                = 14, //from
    FUNC		= 15,
    GETCHAR             = 16, // Getchar     
    IF                  = 17,
    IN			= 18,
    LET			= 19,
    MATCH               = 20,
    NEW                 = 21, // new
    NIL                 = 22, // nil
    OF			= 23,
    OTHERWISE           = 24, //otherwise
    PROC		= 25,
    PUSH                = 26, //push
    RECORD		= 27,
    REPEAT              = 28, //repeat
    RETURN              = 29, //return
    STORE               = 30, //store
    THEN		= 31,
    TO                  = 32, //to
    TYPE		= 33,
    UNTIL               = 34, //until 
    VAR			= 35,
    WHILE		= 36,

    // punctuation...
    DOT			= 37,
    COLON		= 38,
    SEMICOLON           = 39,
    COMMA		= 40,
    BECOMES		= 41,
    IS			= 42,
    CARET               = 43, //caret
    HASH                = 44, //hash AAAAA
    
    // brackets...
    LPAREN		= 45,
    RPAREN		= 46,
    LBRACKET	        = 47,
    RBRACKET	        = 48,
    LCURLY		= 49,
    RCURLY		= 50,

    // special tokens...
    EOT			= 51,
    ERROR		= 52;

  private static String[] tokenTable = new String[] {
    "<int>",
    "<char>",
    "<identifier>",
    "<operator>",
    "array",
    "begin",
    "case", //case
    "const",
    "delete", // delete
    "do",
    "downto", //downTo :'D
    "else",
    "end",
    "for", //for
    "from", //from
    "func",
    "getchar", //getchar
    "if",
    "in",
    "let",
    "match", //match
    "new", // new
    "nil", // nil
    "of",
    "otherwise", //otherwise
    "proc",
    "push", //push
    "record",
    "repeat", //repeat
    "return", //return
    "store", //store
    "then",
    "to", //to
    "type",
    "until", //until
    "var",
    "while",
    
    ".",
    ":",
    ";",
    ",",
    ":=",
    "~",
    "^", //caret
    "#", //hash
    
    "(",
    ")",
    "[",
    "]",
    "{",
    "}",
    "",
    "<error>",
  };

  private final static int	firstReservedWord = Token.ARRAY,
                  lastReservedWord  = Token.WHILE;

}
