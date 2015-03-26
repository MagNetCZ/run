package cz.cvut.fit.run.interpreter.context;

import cz.cvut.fit.run.interpreter.core.TypeValuePair;
import cz.cvut.fit.run.interpreter.core.VMBaseObject;
import cz.cvut.fit.run.interpreter.core.exceptions.*;
import cz.cvut.fit.run.interpreter.core.functions.VMExpressionType;
import cz.cvut.fit.run.interpreter.core.functions.VMStackFunction;
import cz.cvut.fit.run.interpreter.core.helpers.LiteralParser;
import cz.cvut.fit.run.interpreter.core.types.IDType;
import cz.cvut.fit.run.interpreter.core.types.classes.*;
import cz.cvut.fit.run.interpreter.core.types.instances.*;
import cz.cvut.fit.run.interpreter.core.types.type.VMType;
import cz.cvut.fit.run.parser.JavaParser.*;
import org.antlr.v4.runtime.tree.ParseTree;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.HashMap;
import java.util.LinkedList;
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
//        currentFrame = new VMFrame();
        functions = new HashMap<>();
        classes = new HashMap<>();

        try {
            registerBuiltinClasses();
        } catch (VMException ex) {
            throw new RuntimeException(ex);
        }

        logger.setLevel(Level.INFO);
//        logger.setLevel(Level.SEVERE);
    }

    public static VMMachine getInstance() {
        if (instance == null) {
            instance = new VMMachine();
        }

        return instance;
    }

    /** VM INIT **/

    private void registerClass(VMClass clazz) {
        classes.put(clazz.getType().getName(), clazz);
    }

    private void registerClassAlias(String className, String alias) {
        classes.put(alias, classes.get(className));
    }

    private void registerBuiltinClasses() throws VMException {
        registerClass(new VMInteger());
        registerClassAlias("Integer", "int");

        registerClass(new VMBoolean());
        registerClassAlias("Boolean", "boolean");

        registerClass(new VMString());
        registerClass(new VMFile());
        registerClass(new VMSystem());

        IDClass = new VMIdentifier();
        registerClass(IDClass);
    }

    public void registerType(TypeDeclarationContext typeDeclaration) throws VMException {
        ClassDeclarationContext classDeclaration = typeDeclaration.classDeclaration();
        String className = classDeclaration.Identifier().getText();

        VMType newType = new VMType(className);

        VMClass newClass = new VMClass(newType, null, classDeclaration.classBody());
        registerClass(newClass);
    }

    public void registerSuperType(TypeDeclarationContext typeDeclaration) throws VMException {
        TypeContext superTypeNode = typeDeclaration.classDeclaration().type();
        if (superTypeNode != null) {
            ClassDeclarationContext classDeclaration = typeDeclaration.classDeclaration();
            String className = classDeclaration.Identifier().getText();

            VMClass thisClass = getClazz(className);
            VMClass superClass = getClazz(superTypeNode);

            thisClass.setSuperClass(superClass);
        }
    }

    /** ACCESSORS **/

    public static VMFrame getFrame() {
        return getInstance().getCurrentFrame();
    }

    public static VMObject pop() {
        return getFrame().pop();
    }

    public static boolean isIdOnStack() {
        return getFrame().peek().getType() == VMType.ID;
    }

    public static VMObject popValue() throws VMException {
        if (isIdOnStack()) {
            return popPair().getValue();
        } else {
            return pop();
        }
    }

    private static VMBaseObject getObjectOrClass(VMIdentifierInstance id) throws VMException {
        try {
            VMObject accessedObject = getFrame().getVariable(id);
            return accessedObject;
        } catch (NotDeclaredException ex) {
            VMClass accessedClass = VMMachine.getInstance().getClazz(id.getValue());

            if (accessedClass == null)
                throw new NotDeclaredException("No such object or class - " + id.getValue());

            return accessedClass;
        }
    }

    public static TypeValuePair popPair() throws VMException {
        VMObject object = pop();
        if (object.getType() == VMType.ID) {
            VMIdentifierInstance id = (VMIdentifierInstance)object;
            IDType idType = id.getIDType();
            switch (idType) {
                case LOCAL_VARIABLE:
//                    logger.log(Level.INFO, "Looked up " + value);
                    return getFrame().getPair(id);
                case ARRAY_ACCESS:
                    VMArrayInstance array = (VMArrayInstance)getFrame().getVariable(id);
//                    logger.log(Level.INFO, "Looked up array " + value);
                    return array.get(id.getArrayIndex());
                case FIELD_ACCESS:
                    return getObjectOrClass(id).getField(id.getField());
            }
        }

        throw new NotImplementedException();
    }

    public static void push(VMObject object) {
        getFrame().push(object);
    }

    public VMIdentifierInstance getID(String id) throws VMException {
        return IDClass.createInstance(id);
    }

    public VMFrame getCurrentFrame() {
        return currentFrame;
    }

    public void evalMethod(BlockContext body) throws VMException {
        evalBlock(body);
        throw new ReturnException(null); // Implicit return
    }

    private VMExpressionType getExpressionType(ExpressionContext expression) {
        if (expression.getChildCount() == 4 || expression.getChildCount() == 3) {
            String bracket = expression.getChild(1).getText();
            switch (bracket) {
                case "(":
                    return VMExpressionType.FUNCTION_CALL;
                case "[":
                    return VMExpressionType.ARRAY_ACCESS;
                default:
                    if (expression.getChildCount() == 4)
                        throw new NotImplementedException();
            }
        }

        String operator = expression.getChild(expression.getChildCount() - 2).toString();

        // Binary expressions
        if (expression.creator() != null)
            return VMExpressionType.CREATOR;

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
    }


    private void evalFor(StatementContext statement) throws VMException {
        ForControlContext forControl = statement.forControl();

        getFrame().enterScope();

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

        getFrame().exitScope();
    }

    private void evalFlowControl(StatementContext statement) throws VMException {
        String controlKeyword = statement.getChild(0).getText();

        switch (controlKeyword) {
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
        getFrame().enterScope();

        for (BlockStatementContext blockStatement : block.blockStatement()) {
            evalBlockStatement(blockStatement);
        }

        getFrame().exitScope();
    }
    private void evalBlockStatement(BlockStatementContext blockStatement) throws VMException {
        StatementContext statement = blockStatement.statement();
        if (statement != null) {
            evalStatement(blockStatement.statement());
            currentFrame.clearStack();
        }

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
            case "return":
                evalReturn(statement);
            default:
                throw new NotImplementedException();
        }
    }

    private void evalReturn(StatementContext statement) throws VMException {
        ExpressionContext returnExpression = statement.expression(0);
        VMObject returnValue = null;
        if (returnExpression != null)
            returnValue = evalReturnExpressionValue(statement.expression(0));

        throw new ReturnException(returnValue);
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
            case CREATOR:
                evalCreator(expression.creator());
                break;
            case DOT_ACCESS:
                evalDotAccess(expression);
                break;
            case FUNCTION_CALL:
                evalFunctionCall(expression);
                break;
            case ARRAY_ACCESS:
                evalArrayAccess(expression);
                break;
        }
    }

    private void evalDotAccess(ExpressionContext expression) throws VMException {
//        String classOrInstanceName = expression.getChild(0).getText();

        String fieldOrMethodName = expression.getChild(2).getText();
        VMIdentifierInstance objectId = (VMIdentifierInstance)pop();//VMIdentifierInstance)(evalReturnExpression(expression.expression(0)));
        objectId.setField(getID(fieldOrMethodName));

        push(objectId);
    }

    private void evalArrayAccess(ExpressionContext expression) throws VMException {
        VMIntegerInstance arrayIndex = (VMIntegerInstance)(evalReturnExpressionValue(expression.expression(1)));
        VMIdentifierInstance arrayIdentifier = (VMIdentifierInstance)(evalReturnExpression(expression.expression(0)));

        arrayIdentifier.setArrayIndex(arrayIndex.getValue());

        push(arrayIdentifier);
    }

    private void evalObjectCreator(CreatorContext creator) throws VMException {
        String typeName = creator.createdName().getText();
        VMClass clazz = getClazz(typeName);

        ExpressionListContext argumentExpressionList = creator.classCreatorRest().arguments().expressionList();
        LinkedList<VMObject> argList = new LinkedList<>();
        if (argumentExpressionList != null)
            for (ExpressionContext expression : argumentExpressionList.expression()) {
                evalExpression(expression);
                argList.add(popValue());
            }

        push(clazz.createInstance(argList.toArray(new VMObject[argList.size()])));
    }

    private void evalArrayCreator(CreatorContext creator) throws VMException {
        String typeName = creator.createdName().getText();
        VMClass clazz = getClazz(typeName);

        evalExpression(creator.arrayCreatorRest().expression(0));
        // TODO check if there could be anything else than one expression

        VMIntegerInstance arraySizeInstance = (VMIntegerInstance)popValue();
        VMArray arrayClass = getArrayClazz(clazz.getType());

        VMObject newArray = arrayClass.createInstance(arraySizeInstance.getValue());
        push(newArray);
    }

    private void evalCreator(CreatorContext creator) throws VMException {
        if (creator.classCreatorRest() != null) {
            evalObjectCreator(creator);
            return;
        }

        evalArrayCreator(creator);
    }

    private void evalExpressionList(ExpressionListContext expressionList) throws VMException {
        for (ExpressionContext expression : expressionList.expression()) {
            evalExpression(expression);
        }
    }

    private void evalFunctionCall(ExpressionContext expression) throws VMException {
        VMIdentifierInstance id = (VMIdentifierInstance)pop();

//        if (id.getValue().equals("console")) {
//            ExpressionContext expressionToPrint = expression.expressionList().expression(0);
//            VMObject printContents = evalReturnExpressionValue(expressionToPrint);
//            System.out.println(printContents);
//            return;
//        }
        // TODO REMOVE

        // Method invocation
        LinkedList<VMObject> args = new LinkedList<>();
        if (expression.expressionList() != null)
            for (ExpressionContext argExpression : expression.expressionList().expression()) {
                args.add(evalReturnExpressionValue(argExpression));
            }

        VMObject[] argArray = args.toArray(new VMObject[args.size()]);

        String methodName = id.getField().getValue();
        getObjectOrClass(id).callMethod(methodName, argArray);
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
        VMObject value= popValue();
        TypeValuePair pair = popPair();

        pair.setValue(value);
    }

    private void evalLocalVariableDeclaration(LocalVariableDeclarationContext variableDeclaration)
        throws VMException {
        VMType type = getClazz(variableDeclaration.type()).getType();

        for (VariableDeclaratorContext variableDeclarator
                : variableDeclaration.variableDeclarators().variableDeclarator()) {
            String variableId = variableDeclarator.variableDeclaratorId().getChild(0).toString();

            VMIdentifierInstance identifier = getID(variableId);

            TypeValuePair newPair = getFrame().declareVariable(identifier, type);

            if (variableDeclarator.variableInitializer() == null)
                return;

            ExpressionContext initExpression = variableDeclarator.variableInitializer().expression();
            VMObject initValue = evalReturnExpressionValue(initExpression);

            newPair.setValue(initValue);
        }
    }

    public VMArray getArrayClazz(VMType contentType) throws VMException {
        return new VMArray(contentType);
        // TODO register array class
    }

    public VMClass getClazz(TypeContext type) throws VMException {
        String typeName = type.getChild(0).getText();
        VMClass clazz = getClazz(typeName);

        if (clazz == null)
            throw new NotDeclaredException("No such type - " + typeName);

        if (type.getChildCount() == 1)
            return clazz;

        // Array
        return getArrayClazz(clazz.getType());
    }

    public VMClass getClazz(String name) throws VMException {
        VMClass clazz = classes.get(name);
        clazz.initialize();
        return clazz;
    }

    public VMType getType(TypeContext type) throws VMException {
        VMClass clazz = getClazz(type);
        return clazz.getType();
    }

    /**
     * Evaluate expression and return result. The default evalExpression leaves its result on the stack.
     * @param expression
     * @return
     * @throws VMException
     */
    public VMObject evalReturnExpression(ExpressionContext expression) throws VMException {
        evalExpression(expression);
        return pop();
    }

    public VMObject evalReturnExpressionValue(ExpressionContext expression) throws VMException {
        evalExpression(expression);
        return popValue();
    }

    public void enterFrame() {
        VMFrame lastFrame = currentFrame;
        currentFrame = new VMFrame(lastFrame);
    }

    public void exitFrame() throws VMException {
        logger.info("Exiting frame, stack size " + currentFrame.stackSize());

        currentFrame = currentFrame.parent;
        if (currentFrame == null)
            throw new ProgramEndException();
    }
}
