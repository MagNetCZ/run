package cz.cvut.fit.run.interpreter.context;

import cz.cvut.fit.run.interpreter.core.exceptions.BreakException;
import cz.cvut.fit.run.interpreter.core.exceptions.ContinueException;
import cz.cvut.fit.run.interpreter.core.types.classes.*;
import cz.cvut.fit.run.interpreter.core.types.instances.VMBooleanInstance;
import cz.cvut.fit.run.interpreter.core.types.instances.VMIdentifierInstance;
import cz.cvut.fit.run.interpreter.core.types.instances.VMObject;
import cz.cvut.fit.run.interpreter.core.exceptions.VMException;
import cz.cvut.fit.run.interpreter.core.functions.VMExpressionType;
import cz.cvut.fit.run.interpreter.core.functions.VMStackFunction;
import cz.cvut.fit.run.interpreter.core.functions.binary.VMAssignment;
import cz.cvut.fit.run.interpreter.core.helpers.LiteralParser;
import cz.cvut.fit.run.parser.JavaParser.*;
import org.antlr.v4.runtime.tree.ParseTree;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.beans.Expression;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
/**
 * Created by MagNet on 12. 3. 2015.
 */
public class VMMachine {
    private static VMMachine instance = null;
    private HashMap<VMExpressionType, VMStackFunction> functions;
    private HashMap<String, VMClass> classes;

    private VMIdentifier IDClass;

    public static final Logger logger = Logger.getLogger("VMMachine");

    private VMFrame currentFrame;

    private VMMachine() {
        currentFrame = new VMFrame();
        functions = new HashMap<>();
        classes = new HashMap<>();

        loadBuiltinFunctions();
        loadBuiltinClasses();

//        logger.setLevel(Level.INFO);
        logger.setLevel(Level.SEVERE);
    }

    public static VMMachine getInstance() {
        if (instance == null) {
            instance = new VMMachine();
        }

        return instance;
    }

    /** VM INIT **/

    private void loadBuiltinFunctions() {
        functions.put(VMExpressionType.ASSIGNMENT, new VMAssignment());
    }

    private void loadBuiltinClasses() {
        classes.put("Integer", new VMInteger());
        classes.put("Boolean", new VMBoolean());
        classes.put("String", new VMString());
        IDClass = new VMIdentifier();
        classes.put("ID", IDClass);
    }

    /** ACCESSORS **/

    public static VMFrame getFrame() {
        return getInstance().getCurrentFrame();
    }

    public static VMObject pop() {
        return getFrame().pop();
    }

    public static VMObject popValue() throws VMException {
        VMObject object = pop();
        if (object.getType() == VMType.ID) {
            VMObject value = getFrame().getVariable((VMIdentifierInstance)object);
            logger.log(Level.INFO, "Looked up " + value);
            return value;
        }

        return object;
    }

    public static void push(VMObject object) {
        getFrame().push(object);
    }

    public VMIdentifierInstance getID(String id) {
        return IDClass.createInstance(id);
    }

    public VMFrame getCurrentFrame() {
        return currentFrame;
    }

    public void evalMethod(MethodBodyContext body) {
        try {
            evalBlock(body.block());
        } catch (VMException e) {
            throw new RuntimeException(e);
        }
    }

    private VMExpressionType getExpressionType(ExpressionContext expression) {
        if (expression.getChildCount() == 4)
            return VMExpressionType.FUNCTION_CALL;

        String operator = expression.getChild(expression.getChildCount() - 2).toString();

        // Binary expressions
        switch (operator) {
            case "=": return VMExpressionType.ASSIGNMENT;
            case ".": return VMExpressionType.DOT_ACCESS;
            default:
                if (expression.getChildCount() == 3)
                    return VMExpressionType.DIRECT_BINARY_METHOD;
                if (expression.getChildCount() == 2)
                    return VMExpressionType.DIRECT_UNARY_METHOD;
        }

        throw new NotImplementedException();
    }

    private boolean checkExpression(ExpressionContext expression) throws VMException {
        evalExpression(expression);
        VMBooleanInstance expressionResult = (VMBooleanInstance)popValue();
        return expressionResult.getValue();
    }

    private boolean checkParExpression(StatementContext statement) throws VMException {
        return checkExpression(statement.parExpression().expression());
    }

    private void evalIf(StatementContext statement) throws VMException {
        if (checkParExpression(statement)) {
            evalStatement(statement.statement(0));
        } else {
            if (statement.statement(1) != null)
                evalStatement(statement.statement(1));
        }
    }

    private void evalWhile(StatementContext statement) throws VMException {
        try {
            while (checkParExpression(statement)) {
                try {
                    evalStatement(statement.statement(0));
                } catch (ContinueException ex) {}
            }
        } catch (BreakException ex) {

        }
    }

    private void evalDoWhile(StatementContext statement) throws VMException {
        try {
            try {
                evalStatement(statement.statement(0));
            } catch (ContinueException ex) {}
            evalWhile(statement);
        } catch (BreakException ex) {}
    }

    private void evalSwitch(StatementContext statement) throws VMException {
        evalExpression(statement.parExpression().expression());
        VMObject switchValue = popValue();

        boolean caseEquals = false;
        try {
            for (SwitchBlockStatementGroupContext switchBlockStatementGroup : statement.switchBlockStatementGroup()) {
                if (!caseEquals) {
                    for (SwitchLabelContext switchLabel : switchBlockStatementGroup.switchLabel()) {
                        evalExpression(switchLabel.constantExpression().expression());
                        VMObject caseValue = popValue();

                        switchValue.callMethod("==", caseValue); // TODO use equals
                        VMBooleanInstance compResult = (VMBooleanInstance)popValue();
                        if (compResult.getValue()) {
                            caseEquals = true;
                            break;
                        }
                    }
                }

                if (caseEquals) {
                    for (BlockStatementContext blockStatement : switchBlockStatementGroup.blockStatement()) {
                        evalBlockStatement(blockStatement);
                    }
                }
            }
        } catch (BreakException ex) {
        }

        // TODO finally scope exit
    }


    private void evalFor(StatementContext statement) throws VMException {
        ForControlContext forControl = statement.forControl();

        if (forControl.forInit().localVariableDeclaration() != null) {
            evalLocalVariableDeclaration(forControl.forInit().localVariableDeclaration());
        }

        try {
            while (checkExpression(forControl.expression())) {
                try {
                    evalStatement(statement.statement(0));
                } catch (ContinueException ex) {}
                evalExpressionList(forControl.forUpdate().expressionList());
            }
        } catch (BreakException ex) {}

        // TODO finally scope exit
    }

    private void evalFlowControl(StatementContext statement) throws VMException {
        String controlKeyword = statement.getChild(0).getText();

        switch (controlKeyword) {
            // TODO dynamic order
            case "if":
                evalIf(statement);
                break;
            case "while":
                evalWhile(statement);
                break;
            case "switch":
                evalSwitch(statement);
                break;
            case "do":
                evalDoWhile(statement);
                break;

            default:
                throw new NotImplementedException();
        }
    }

    private void evalBlock(BlockContext block) throws VMException {
        for (BlockStatementContext blockStatement : block.blockStatement()) {
            evalBlockStatement(blockStatement);
        }
    }
    private void evalBlockStatement(BlockStatementContext blockStatement) throws VMException {
        StatementContext statement = blockStatement.statement();
        if (statement != null)
            evalStatement(blockStatement.statement());

        if (blockStatement.localVariableDeclarationStatement() != null) {
            LocalVariableDeclarationContext localVariableDeclaration =
                    blockStatement.localVariableDeclarationStatement().localVariableDeclaration();

            evalLocalVariableDeclaration(localVariableDeclaration);
        }
    }

    private void evalStatement(StatementContext statement) throws VMException {
        ParExpressionContext parExpression = statement.parExpression();
        if (parExpression != null) {
            // Flow control
            evalFlowControl(statement);
            return;
        }

        if (statement.forControl() != null) {
            evalFor(statement);
            return;
        }

        StatementExpressionContext statementExpression = statement.statementExpression();
        if (statementExpression != null) {
            ExpressionContext expression = statementExpression.expression();
            evalExpression(expression);
            return;
        }

        if (statement.block() != null) {
            evalBlock(statement.block());
            return;
        }

        // Keywords
        String keyword = statement.getChild(0).getText();
        switch (keyword) {
            case "break":
                throw new BreakException();
            case "continue":
                throw new ContinueException();
            // TODO return
            default:
                throw new NotImplementedException();
        }
    }

    private void evalPrimaryExpression(ExpressionContext expression) throws VMException {
        VMObject value;

        PrimaryContext primary = expression.primary();
        // Literal?
        if (primary.literal() != null) {
            value = LiteralParser.parseLiteral(primary.getText());
        } else {
            value = getID(primary.getText());
        }

        push(value);
        return;
    }

    private void evalSecondaryExpression(ExpressionContext expression) throws VMException {
        VMExpressionType expressionType = getExpressionType(expression);

        for (ParseTree child : expression.children) {
            if (child instanceof ExpressionContext)
                evalExpression((ExpressionContext)child);
        }

        switch (expressionType) {
            case ASSIGNMENT:
                assignValue();
                break;
            case DIRECT_BINARY_METHOD:
                String binOperator = expression.getChild(1).toString();
                VMObject operand = popValue();
                VMObject object = popValue();
                object.callMethod(binOperator, operand);
                break;
            case DIRECT_UNARY_METHOD:
                int operandIndex = expression.getChild(0) instanceof ExpressionContext ? 1 : 0;
                String unOperator = expression.getChild(operandIndex).getText();
                VMObject unObject = popValue();
                unObject.callMethod(unOperator);
                break;
            case DOT_ACCESS:
                throw new NotImplementedException();
            case FUNCTION_CALL:
                evalFunctionCall(expression);
        }
    }

    private void evalExpressionList(ExpressionListContext expressionList) throws VMException {
        for (ExpressionContext expression : expressionList.expression()) {
            evalExpression(expression);
        }
    }

    private void evalFunctionCall(ExpressionContext expression) throws VMException {
        // TODO ...
        evalExpression(expression.expressionList().expression(0));
        System.out.println(popValue());
    }

    private void evalExpression(ExpressionContext expression) throws VMException {
        // Primary expression
        if (expression.getChildCount() == 1) {
            evalPrimaryExpression(expression);
        } else {
            evalSecondaryExpression(expression);
        }
    }

    private void assignValue() throws VMException {
        VMObject assignValue= popValue();
        VMObject objectId = pop();
        push(objectId);
        push(assignValue);
        functions.get(VMExpressionType.ASSIGNMENT).call();
    }

    private void evalLocalVariableDeclaration(LocalVariableDeclarationContext variableDeclaration)
        throws VMException {
        String typeString = variableDeclaration.type().primitiveType().getChild(0).toString();
        VMType type = VMType.valueOf(typeString.toUpperCase());

        for (VariableDeclaratorContext variableDeclarator
                : variableDeclaration.variableDeclarators().variableDeclarator()) {
            String variableId = variableDeclarator.variableDeclaratorId().getChild(0).toString();

            VMIdentifierInstance identifier = getID(variableId);

            getFrame().declareVariable(identifier, type);

            push(identifier);

            ExpressionContext initExpression = variableDeclarator.variableInitializer().expression();
            evalExpression(initExpression);

            assignValue();
        }
    }

    public VMClass getClazz(String name) {
        return classes.get(name);
        // TODO source lookup
    }
}
