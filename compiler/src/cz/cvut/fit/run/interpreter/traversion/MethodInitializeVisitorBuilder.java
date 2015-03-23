package cz.cvut.fit.run.interpreter.traversion;

import cz.cvut.fit.run.interpreter.context.VMMachine;
import cz.cvut.fit.run.interpreter.core.VMMethod;
import cz.cvut.fit.run.interpreter.core.exceptions.VMException;
import cz.cvut.fit.run.interpreter.core.modifiers.Modifiers;
import cz.cvut.fit.run.interpreter.core.modifiers.Scope;
import cz.cvut.fit.run.interpreter.core.types.classes.VMClass;
import cz.cvut.fit.run.interpreter.core.types.classes.VMType;
import cz.cvut.fit.run.interpreter.core.types.instances.VMIdentifierInstance;
import cz.cvut.fit.run.parser.JavaBaseVisitor;
import cz.cvut.fit.run.parser.JavaParser;
import cz.cvut.fit.run.parser.JavaParser.*;
import org.antlr.v4.runtime.misc.NotNull;

/**
 * Created by MagNet on 20. 3. 2015.
 */
public class MethodInitializeVisitorBuilder extends JavaBaseVisitor<VMException> {
    private VMClass buildObject;

    public MethodInitializeVisitorBuilder(@NotNull VMClass buildObject) {
        this.buildObject = buildObject;
    }

    private Modifiers getModifiers(ClassBodyDeclarationContext ctx) {
        // TODO move to some helper class

        Modifiers modifiers = new Modifiers();
        for (ModifierContext modifier : ctx.modifier()) {
            switch (modifier.getText()) {
                case "public":
                    modifiers.setScope(Scope.PUBLIC);
                    break;
                case "protected":
                    modifiers.setScope(Scope.PROTECTED);
                    break;
                case "private":
                    modifiers.setScope(Scope.PRIVATE);
                    break;
                case "static":
                    modifiers.setStaticFlag(true);
                    break;

                // TODO final
            }
        }

        return modifiers;
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

    private void evalMethodDeclaration(MethodDeclarationContext methodDeclarationContext, Modifiers modifiers)
            throws VMException {
        if (methodDeclarationContext == null)
            return;

        VMMachine vm = VMMachine.getInstance();

        String returnTypeName = methodDeclarationContext.getChild(0).getText();
        VMType returnType = vm.getType(returnTypeName);

        String methodName = methodDeclarationContext.getChild(1).getText();

        // TODO args

        MethodBodyContext methodSource = methodDeclarationContext.methodBody();

        VMMethod method = new VMMethod(methodName, modifiers, returnType, methodSource);

        buildObject.declareMethod(method);
    }
}
