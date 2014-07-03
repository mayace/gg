package com.github.gg.compiler.tres;

import java_cup.runtime.Symbol;


%%

%public
%class Scanner
%cup
%char
%line
%column


%{
  private Symbol symbol(int type) {
    return new Symbol(type, yyline, yycolumn,yytext());
  }
  private Symbol symbol(int type, Object value) {
    return new Symbol(type, yyline, yycolumn, value);
  }
%}

NEWLINE         =   \n|\r|\r\n
SPACE           =   [ \t]|{NEWLINE}
DIGIT           =   [0-9]
INT             =   {DIGIT}+
FLOAT           =   {INT}"."{INT}
ID              =   [:jletter:] [:jletterdigit:]*

SIMPLE_COMMENT  =   "//"[^\*\n\r]*{NEWLINE}

%%

<YYINITIAL>
{
    {SPACE}             {}
    {SIMPLE_COMMENT}    {return symbol(Sym.COMMENT);}

    "="                 {return symbol(Sym.EQUAL);}
    ":"                 {return symbol(Sym.DPUNTOS);}
    ";"                 {return symbol(Sym.PCOMA);}
    ","                 {return symbol(Sym.COMA);}

    "=="                {return symbol(Sym.DEQUAL);}
    "!="                {return symbol(Sym.NEQUAL);}
    ">="                {return symbol(Sym.BETHAN);}
    "<="                {return symbol(Sym.LETHAN);}
    ">"                 {return symbol(Sym.BTHAN);}
    "<"                 {return symbol(Sym.LTHAN);}
    
    "("                 {return symbol(Sym.LP);}
    ")"                 {return symbol(Sym.RP);}
    "["                 {return symbol(Sym.LS);}
    "]"                 {return symbol(Sym.RS);}
    "{"                 {return symbol(Sym.LL);}
    "}"                 {return symbol(Sym.RL);}

    "+"                 {return symbol(Sym.PLUS);}
    "-"                 {return symbol(Sym.MINUS);}
    "*"                 {return symbol(Sym.MULT);}
    "/"                 {return symbol(Sym.DIV);}
    "%"                 {return symbol(Sym.MOD);}
    "^"                 {return symbol(Sym.EXP);}
    
    "char"              {return symbol(Sym.KW_CHAR);}
    "void"              {return symbol(Sym.KW_VOID);}
    // "int"               {return symbol(Sym.KW_INT);}
    "p"                 {return symbol(Sym.S);}
    "h"                 {return symbol(Sym.H);}
    "pila"              {return symbol(Sym.STACK);}
    "heap"              {return symbol(Sym.HEAP);}
    "goto"              {return symbol(Sym.GOTO);}
    "if"                {return symbol(Sym.IF);}
    "print"             {return symbol(Sym.PRINT);}
    "println"           {return symbol(Sym.PRINTLN);}


    {FLOAT}             {return symbol(Sym.FLOAT);}
    {INT}               {return symbol(Sym.INT);}
    {ID}                {return symbol(Sym.ID);}
}

[^]                     {return symbol(Sym.ERROR);}

<<EOF>>                 {return symbol(Sym.EOF);}