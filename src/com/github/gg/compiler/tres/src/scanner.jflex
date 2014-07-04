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
    // "^"                 {return symbol(Sym.EXP);}
    
    // "char"              {return symbol(Sym.KW_CHAR);}
    "void"              {return symbol(Sym.KW_VOID);}
    "call"              {return symbol(Sym.KW_CALL);}
    // "int"               {return symbol(Sym.KW_INT);}
    "p"                 {return symbol(Sym.P);}
    "h"                 {return symbol(Sym.H);}
    "stack"              {return symbol(Sym.STACK);}
    "heap"              {return symbol(Sym.HEAP);}
    "goto"              {return symbol(Sym.GOTO);}
    "if"                {return symbol(Sym.IF);}
    "then"                {return symbol(Sym.THEN);}


    "printNum"          {return symbol(Sym.PRINT_NUM);}
    "printBoolean"      {return symbol(Sym.PRINT_BOOLEAN);}
    "printChar"         {return symbol(Sym.PRINT_CHAR);}
    "linea"             {return symbol(Sym.KW_LINEA);}
    "texto"             {return symbol(Sym.KW_TEXTO);}
    "arco"              {return symbol(Sym.KW_ARCO);}
    "rectangulo"        {return symbol(Sym.KW_RECTANGULO);}
    "ovalo"             {return symbol(Sym.KW_OVALO);}
    "poligono"          {return symbol(Sym.KW_POLIGONO);}
    "lienzo"            {return symbol(Sym.KW_LIENZO);}

    "int_to_char"       {return symbol(Sym.KW_INT2CHAR);}
    "int_to_float"      {return symbol(Sym.KW_INT2FLOAT);}
    "int_to_string"     {return symbol(Sym.KW_INT2STRING);}
    "int_to_boolean"    {return symbol(Sym.KW_INT2BOOLEAN);}
    "float_to_string"   {return symbol(Sym.KW_FLOAT2STRING);}
    "float_to_int"      {return symbol(Sym.KW_FLOAT2INT);}
    "char_to_int"       {return symbol(Sym.KW_CHAR2INT);}
    "char_to_string"    {return symbol(Sym.KW_CHAR2STRING);}
    "string_to_int"     {return symbol(Sym.KW_STRING2INT);}
    "string_to_float"   {return symbol(Sym.KW_STRING2FLOAT);}
    "string_to_char"    {return symbol(Sym.KW_STRING2CHAR);}
    "string_to_boolean" {return symbol(Sym.KW_STRING2BOOLEAN);}

    {FLOAT}             {return symbol(Sym.FLOAT);}
    {INT}               {return symbol(Sym.INT);}
    {ID}                {return symbol(Sym.ID);}
}

[^]                     {return symbol(Sym.ERROR);}

<<EOF>>                 {return symbol(Sym.EOF);}