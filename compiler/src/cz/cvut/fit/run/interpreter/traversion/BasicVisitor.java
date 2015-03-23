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
    public String visitMethodDeclaration(@NotNull JavaParser.MethodDeclarationContext ctx) {
        System.out.println(ctx.Identifier());

        String identifier = ctx.Identifier().toString();
//        if (identifier.equals("testMethod")) {
//        if (identifier.equals("testIf")) {
//        if (identifier.equals("testFor")) {
//        if (identifier.equals("testCompare")) {
//        if (identifier.equals("testSwitch")) {
//        if (identifier.equals("testBoolean")) {
//        if (identifier.equals("testVariableScope")) {
//        if (identifier.equals("testVariableScope2")) {
//        if (identifier.equals("testNew")) {
//        if (identifier.equals("testArray")) {
//        if (identifier.equals("testIntegerArray")) {
        if (identifier.equals("main")) {
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
