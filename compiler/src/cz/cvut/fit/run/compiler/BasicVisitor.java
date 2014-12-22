package cz.cvut.fit.run.compiler;

import cz.cvut.fit.run.parser.JavaBaseVisitor;
import cz.cvut.fit.run.parser.JavaParser;
import cz.cvut.fit.run.parser.JavaVisitor;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;
import org.antlr.v4.runtime.tree.TerminalNode;

/**
 * Created by MagNet on 22. 12. 2014.
 */
public class BasicVisitor extends JavaBaseVisitor<String> {
    @Override
    public String visitExpression(@NotNull JavaParser.ExpressionContext ctx) {
        System.out.println("expression");
        return super.visitExpression(ctx);
    }
}
