package cz.cvut.fit.run.interpreter;

import cz.cvut.fit.run.interpreter.context.VMMachine;
import cz.cvut.fit.run.interpreter.core.exceptions.VMException;
import cz.cvut.fit.run.interpreter.traversion.BasicVisitor;
import cz.cvut.fit.run.parser.JavaLexer;
import cz.cvut.fit.run.parser.JavaParser;
import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.apache.commons.cli.*;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException, VMException, ParseException {
        PosixParser cliParser = new PosixParser();
        Options parseOptions = new Options();
        parseOptions.addOption("f", "file", true, "The source code to interpret.");
        parseOptions.addOption("c", "class", true, "Name of the main class containing main() method.");

        CommandLine cl = cliParser.parse(parseOptions, args);

        String mainClassName = cl.getOptionValue("class");
        String sourceFilename = cl.getOptionValue("file");

        if (mainClassName == null || sourceFilename == null) {
            HelpFormatter helpFormatter = new HelpFormatter();
            helpFormatter.printHelp("-f \"Path to source\" -c \"Main class name\"", parseOptions);
            return;
        }

        System.out.println("VMMachine Starting up...");
        System.out.println("***************************");

        ANTLRInputStream input = new ANTLRFileStream(sourceFilename);
        JavaLexer lexer = new JavaLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        JavaParser parser = new JavaParser(tokens);

        JavaParser.CompilationUnitContext compilationUnit = parser.compilationUnit();

        JavaParser.TypeDeclarationContext mainClass = null;

        for (JavaParser.TypeDeclarationContext type : compilationUnit.typeDeclaration()) {
            VMMachine.getInstance().registerType(type); // TODO inner classes -> visitor?
            if (type.classDeclaration().Identifier().getText().equals(mainClassName))
                mainClass = type;
        }

        BasicVisitor visitor = new BasicVisitor();
        visitor.visit(mainClass);
    }
}
