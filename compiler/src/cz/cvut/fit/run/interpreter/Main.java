package cz.cvut.fit.run.interpreter;

import cz.cvut.fit.run.interpreter.traversion.BasicVisitor;
import cz.cvut.fit.run.parser.JavaLexer;
import cz.cvut.fit.run.parser.JavaParser;
import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        System.out.println("VMMachine Starting up...");
        System.out.println("***************************");

        // TODO load file based on args
        ANTLRInputStream input = new ANTLRFileStream("examples/testcode.java");
        JavaLexer lexer = new JavaLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        JavaParser parser = new JavaParser(tokens);

        ParseTree tree = parser.compilationUnit().getChild(1);

        BasicVisitor visitor = new BasicVisitor();
        visitor.visit(tree);
    }
}
