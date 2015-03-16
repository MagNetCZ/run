package cz.cvut.fit.run.interpreter.context;

import cz.cvut.fit.run.interpreter.core.types.classes.*;
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

        logger.setLevel(Level.INFO);
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
            for (BlockStatementContext statement : body.block().blockStatement()) {
                evalStatement(statement);
            }
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

    private void evalStatement(BlockStatementContext blockStatement) throws VMException {
        if (blockStatement.statement() != null) {
            ExpressionContext expression = blockStatement.statement().statementExpression().expression();
            evalExpression(expression);
        }

        if (blockStatement.localVariableDeclarationStatement() != null) {
            LocalVariableDeclarationContext localVariableDeclaration =
                    blockStatement.localVariableDeclarationStatement().localVariableDeclaration();

            evalLocalVariableDeclaration(localVariableDeclaration);
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
                String unOperator = expression.getChild(0).toString();
                VMObject unObject = popValue();
                unObject.callMethod(unOperator);
                break;
            case DOT_ACCESS:
                throw new NotImplementedException();
            case FUNCTION_CALL:
                evalFunctionCall(expression);
        }
    }

    private void evalFunctionCall(ExpressionContext expression) throws VMException {
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
