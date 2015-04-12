#!/bin/bash

PATH_TO_JAR="out/artifacts/interpreter_jar/interpreter.jar"
echo "** Running examples **"
java -jar $PATH_TO_JAR -f examples/testcode.java
java -jar $PATH_TO_JAR -f examples/tests/test_constructor.java
java -jar $PATH_TO_JAR -f examples/tests/test_exceptions.java
java -jar $PATH_TO_JAR -f examples/tests/test_file.java
java -jar $PATH_TO_JAR -f examples/tests/test_inheritance.java
java -jar $PATH_TO_JAR -f examples/tests/test_methods.java
java -jar $PATH_TO_JAR -f examples/tests/satsolver/Main.java -a examples/tests/satsolver/sat_2.inst.dat