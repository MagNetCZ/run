#!/bin/bash

PATH_TO_JAR="out/artifacts/interpreter_jar/interpreter.jar"
echo "** Running examples **"
java -jar $PATH_TO_JAR -f examples/testcode.java -l 2
java -jar $PATH_TO_JAR -f examples/tests/test_constructor.java  -l 2
java -jar $PATH_TO_JAR -f examples/tests/test_exceptions.java  -l 2
java -jar $PATH_TO_JAR -f examples/tests/test_file.java  -l 2
java -jar $PATH_TO_JAR -f examples/tests/test_inheritance.java  -l 2
java -jar $PATH_TO_JAR -f examples/tests/test_methods.java  -l 2
java -jar $PATH_TO_JAR -f examples/tests/satsolver/Main.java  -l 2 -a examples/tests/satsolver/sat_2.inst.dat