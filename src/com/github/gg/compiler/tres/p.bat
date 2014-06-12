@echo off

set basedir=.
set srcdir=%basedir%/src
set libdir=%basedir%/lib

set cuplib=%libdir%/java-cup-11b.jar
set jflexlib=%libdir%/jflex-1.5.1.jar
set parser_src=%srcdir%/parser.cup
set scanner_src=%srcdir%/scanner.jflex


java -jar %cuplib% -parser Parser -symbols Sym %parser_src%
echo "parser compiled..."

rem java -jar %jflexlib% %scanner_src%
rem echo "scanner compiled..."

@pause