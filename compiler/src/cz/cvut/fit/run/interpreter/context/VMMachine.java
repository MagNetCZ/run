package cz.cvut.fit.run.interpreter.context;

import cz.cvut.fit.run.interpreter.core.VMObject;
import cz.cvut.fit.run.interpreter.core.exceptions.NotDeclaredException;
import cz.cvut.fit.run.interpreter.core.exceptions.VMException;
import cz.cvut.fit.run.interpreter.core.functions.VMExpressionType;
import cz.cvut.fit.run.interpreter.core.functions.VMStackFunction;
import cz.cvut.fit.run.interpreter.core.functions.binary.VMAssignment;
import cz.cvut.fit.run.interpreter.core.types.VMIdentifier;
import cz.cvut.fit.run.interpreter.core.types.VMInteger;
import cz.cvut.fit.run.interpreter.core.types.VMString;
import cz.cvut.fit.run.interpreter.core.types.VMType;
import cz.cvut.fit.run.parser.JavaParser.*;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.HashMap;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by MagNet on 12. 3. 2015.
 */
public class VMMachine {
    private static VMMachine instance = null;
    private HashMap<VMExpressionType, VMStackFunction> functions;

    public static final Logger logger = Logger.getLogger("VMMachine");

    private VMFrame currentFrame;

    private VMMachine() {
        currentFrame = new VMFrame();
        functions = new HashMap<>();

        loadFunctions();

        logger.setLevel(Level.INFO);
    }

    private void loadFunctions() {
        functions.put(VMExpressionType.ASSIGNMENT, new VMAssignment());
    }

    public static VMMachine getInstance() {
        if (instance == null) {
            instance = new VMMachine();
        }

        return instance;
    }

    public static VMFrame getFrame() {
        return getInstance().getCurrentFrame();
    }

    public static VMObject pop() {
        return getFrame().pop();
    }

    public static VMObject popValue() throws VMException {
        VMObject object = pop();
        if (object instanceof VMIdentifier) {
            VMObject value = getFrame().getVariable((VMIdentifier)object);
            logger.log(Level.INFO, "Looked up " + value);
            return value;
        }

        return object;
    }

    public static void push(VMObject object) {
        getFrame().push(object);
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
        String middleNode = expression.getChild(1).toString();

        // Binary expressions
        switch (middleNode) {
            case "=": return VMExpressionType.ASSIGNMENT;
            case "+": return VMExpressionType.DIRECT_METHOD;
            case "-": return VMExpressionType.DIRECT_METHOD;
            case "*": return VMExpressionType.DIRECT_METHOD;
            case "/": return VMExpressionType.DIRECT_METHOD;
            case ".": return VMExpressionType.DOT_ACCESS;
            default: throw new NotImplementedException();
        }
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

    private void evalExpression(ExpressionContext expression) throws VMException {
        // Primary expression
        if (expression.children.size() == 1) {
            VMObject value;

            PrimaryContext primary = expression.primary();
            // Literal?
            if (primary.literal() != null) {
                value = new VMInteger(primary.literal().getChild(0).toString()); // TODO proper type
            } else {
                value = new VMIdentifier(primary.getChild(0).toString()); // TODO identifier type
            }

            push(value);
            return;
        }

        VMExpressionType expressionType = getExpressionType(expression);

        for (ParseTree child : expression.children) {
            if (child instanceof ExpressionContext)
                evalExpression((ExpressionContext)child);
        }

        switch (expressionType) {
            case ASSIGNMENT:
                functions.get(expressionType).call();
                break;
            case DIRECT_METHOD:
                String methodName = expression.getChild(1).toString();
                VMObject operand = popValue();
                VMObject object = popValue();
                object.callMethod(methodName, operand);
                break;
            case DOT_ACCESS:
                throw new NotImplementedException();
        }
    }



    private void evalLocalVariableDeclaration(LocalVariableDeclarationContext variableDeclaration)
        throws VMException {
        String typeString = variableDeclaration.type().primitiveType().getChild(0).toString();
        VMType type = VMType.valueOf(typeString.toUpperCase());

        for (VariableDeclaratorContext variableDeclarator
                : variableDeclaration.variableDeclarators().variableDeclarator()) {
            String variableId = variableDeclarator.variableDeclaratorId().getChild(0).toString();

            VMIdentifier identifier = new VMIdentifier(variableId);

            getFrame().declareVariable(identifier, type);

            push(identifier);

            ExpressionContext initExpression = variableDeclarator.variableInitializer().expression();
            evalExpression(initExpression);

            functions.get(VMExpressionType.ASSIGNMENT).call();
        }
    }
}
