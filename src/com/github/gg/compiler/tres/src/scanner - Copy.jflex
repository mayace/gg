package com.github.gg.tres;

import java_cup.runtime.Symbol;


%%

%public
%class Scanner
%cup
%line
%column


%{
  StringBuffer string = new StringBuffer();

  private Symbol symbol(int type) {
    return new Symbol(type, yyline, yycolumn);
  }
  private Symbol symbol(int type, Object value) {
    return new Symbol(type, yyline, yycolumn, value);
  }
%}

NEWLINE         =        \n|\r|\r\n
SPACE           =        [ \t]|{NEWLINE}
DIGIT           =        [0-9]
INT             =        "-"?{DIGIT}+
ID              =        [:jletter:] [:jletterdigit:]*
BOOLEAN         =        "true"|"false"

SIMPLE_COMMENT  =        "//"[^\*\n\r]*{NEWLINE}

%%

<YYINITIAL>
{
    {SPACE}             {}
    {SIMPLE_COMMENT}    {}

    "="                 {return symbol(Sym.EQUAL);}
    ":"                 {return symbol(Sym.DPUNTOS);}
    ";"                 {return symbol(Sym.PCOMA);}

    "=="                {return symbol(Sym.DEQUAL);}
    "!="                {return symbol(Sym.NEQUAL);}
    ">="                {return symbol(Sym.BTHAN);}
    "<="                {return symbol(Sym.LTHAN);}

    "!"                 {return symbol(Sym.NOT);}
    "||"                {return symbol(Sym.OR);}
    "&&"                {return symbol(Sym.AND);}
    
    "("                 {return symbol(Sym.LP);}
    ")"                 {return symbol(Sym.RP);}
    "["                 {return symbol(Sym.LS);}
    "]"                 {return symbol(Sym.RS);}

    "+"                 {return symbol(Sym.PLUS);}
    "-"                 {return symbol(Sym.MINUS);}
    "*"                 {return symbol(Sym.MULT);}
    "/"                 {return symbol(Sym.DIV);}
    
    "goto"              {return symbol(Sym.GOTO);}
    "if"                {return symbol(Sym.IF);}
    "then"              {return symbol(Sym.THEN);}
    "print"             {return symbol(Sym.PRINT);}
    "println"           {return symbol(Sym.PRINTLN);}


    {INT}               {return symbol(Sym.INT,yytext());}
    {ID}                {return symbol(Sym.ID,yytext());}
}

[^]                     { throw new Error("Illegal character <" + yytext() + ">"); }

<<EOF>>                 {return symbol(Sym.EOF);}