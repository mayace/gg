@echo off

set basedir=.
set srcdir=%basedir%/src
set libdir=%basedir%/lib

set cuplib=%libdir%/java-cup-11b.jar
set jflexlib=%libdir%/jflex-1.5.1.jar
set parser_src=%srcdir%/parser.cup
set scanner_src=%srcdir%/scanner.jflex


rem java -jar %cuplib% -parser Parser -symbols Sym %parser_src%
rem echo "parser compiled..."

java -jar %jflexlib% -d %basedir% %scanner_src% 
echo "scanner compiled..."

@pause