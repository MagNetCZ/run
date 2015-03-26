package cz.cvut.fit.run.interpreter.traversion;

import cz.cvut.fit.run.interpreter.context.VMMachine;
import cz.cvut.fit.run.interpreter.core.VMMethod;
import cz.cvut.fit.run.interpreter.core.exceptions.VMException;
import cz.cvut.fit.run.interpreter.core.modifiers.Modifiers;
import cz.cvut.fit.run.interpreter.core.modifiers.Scope;
import cz.cvut.fit.run.interpreter.core.types.classes.VMClass;
import cz.cvut.fit.run.interpreter.core.types.type.VMType;
import cz.cvut.fit.run.parser.JavaBaseVisitor;
import cz.cvut.fit.run.parser.JavaParser.*;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.LinkedList;

/**
 * Created by MagNet on 20. 3. 2015.
 */
public class MethodInitializeVisitorBuilder extends JavaBaseVisitor<VMException> {
    private VMClass buildObject;

    public MethodInitializeVisitorBuilder(@NotNull VMClass buildObject) {
        this.buildObject = buildObject;
    }

    private Modifiers getModifiers(ClassBodyDeclarationContext ctx) {
        return Modifiers.getFor(ctx.modifier());
    }

    @Override
    public VMException visitClassBodyDeclaration(@NotNull ClassBodyDeclarationContext ctx) {
        try {
            Modifiers modifiers = getModifiers(ctx);
            evalMethodDeclaration(ctx.memberDeclaration().methodDeclaration(), modifiers);
        } catch (VMException ex) {
            return ex;
        }

        return super.visitClassBodyDeclaration(ctx);
    }

    private void evalMethodDeclaration(MethodDeclarationContext ctx, Modifiers modifiers)
            throws VMException {
        if (ctx == null)
            return;

        VMMachine vm = VMMachine.getInstance();

        TypeContext type = ctx.type();
        VMType returnType = type == null ? VMType.VOID : vm.getType(type);

        String methodName = ctx.getChild(1).getText();

        LinkedList<VMType> argTypes = new LinkedList<>();
        LinkedList<String> argNames = new LinkedList<>();

        if (!modifiers.isStatic()) {
            argTypes.add(buildObject.getType());
            argNames.add("this");
        }

        if (ctx.formalParameters().formalParameterList() != null)
            for (FormalParameterContext parameter : ctx.formalParameters().formalParameterList().formalParameter()) {
                VMType argType = vm.getType(parameter.type());
                argTypes.add(argType);

                String argName = parameter.variableDeclaratorId().getText();
                argNames.add(argName);
            }

        MethodBodyContext methodSource = ctx.methodBody();

        VMType[] argTypeArray = argTypes.toArray(new VMType[argTypes.size()]);
        String[] argNameArray = argNames.toArray(new String[argNames.size()]);

        VMMethod method = new VMMethod(methodName, modifiers, returnType, methodSource, argTypeArray, argNameArray);

        buildObject.declareMethod(method);
    }
}
