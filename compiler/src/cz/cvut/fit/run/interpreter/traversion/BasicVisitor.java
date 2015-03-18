package cz.cvut.fit.run.interpreter.traversion;

import cz.cvut.fit.run.interpreter.context.VMMachine;
import cz.cvut.fit.run.parser.JavaBaseVisitor;
import cz.cvut.fit.run.parser.JavaParser;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTree;

/**
 * Created by MagNet on 22. 12. 2014.
 */
public class BasicVisitor extends JavaBaseVisitor<String> {
    @Override
    public String visitFieldDeclaration(@NotNull JavaParser.FieldDeclarationContext ctx) {
//        System.out.println("field");
//
//        String type = ctx.type().primitiveType().getChild(0).toString();
//
//        for (ParseTree child : ctx.variableDeclarators().children) {
//            if (!(child instanceof JavaParser.VariableDeclaratorContext))
//                continue;
//
//            JavaParser.VariableDeclaratorIdContext id = ((JavaParser.VariableDeclaratorContext) child).variableDeclaratorId();
//
//            System.out.println(id.getChild(0).toString());
//        }

        return super.visitFieldDeclaration(ctx);
    }

    public String visitMethodDeclaration(@NotNull JavaParser.MethodDeclarationContext ctx) {
        System.out.println(ctx.Identifier());

        String identifier = ctx.Identifier().toString();
//        if (identifier.equals("testMethod")) {
//        if (identifier.equals("testIf")) {
//        if (identifier.equals("testFor")) {
        if (identifier.equals("testCompare")) {
//        if (identifier.equals("testSwitch")) {
            VMMachine.getInstance().evalMethod(ctx.methodBody());
        }

        return super.visitMethodDeclaration(ctx);
    }

    @Override
    public String visitExpression(@NotNull JavaParser.ExpressionContext ctx) {
//        System.out.println("expression");
        return super.visitExpression(ctx);
    }
}
