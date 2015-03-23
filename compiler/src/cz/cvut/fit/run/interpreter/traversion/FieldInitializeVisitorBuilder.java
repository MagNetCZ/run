package cz.cvut.fit.run.interpreter.traversion;

import cz.cvut.fit.run.interpreter.context.VMMachine;
import cz.cvut.fit.run.interpreter.core.VMBaseObject;
import cz.cvut.fit.run.interpreter.core.exceptions.VMException;
import cz.cvut.fit.run.interpreter.core.types.classes.VMType;
import cz.cvut.fit.run.interpreter.core.types.instances.VMIdentifierInstance;
import cz.cvut.fit.run.parser.JavaBaseVisitor;
import cz.cvut.fit.run.parser.JavaParser.*;
import org.antlr.v4.runtime.misc.NotNull;

/**
 * Created by MagNet on 20. 3. 2015.
 */
public class FieldInitializeVisitorBuilder extends JavaBaseVisitor<VMException> {
    private VMBaseObject buildObject;
    private ModifierFilter modifierFilter = null;

    public FieldInitializeVisitorBuilder(@NotNull VMBaseObject buildObject, ModifierFilter modifierFilter) {
        this.buildObject = buildObject;
        this.modifierFilter = modifierFilter;
    }

    @Override
    public VMException visitClassBodyDeclaration(@NotNull ClassBodyDeclarationContext ctx) {
        if (!passesThroughFilter(ctx))
            return null;

        try {
            evalFieldDeclaration(ctx.memberDeclaration().fieldDeclaration());
            evalMethodDeclaration(ctx.memberDeclaration().methodDeclaration());

        } catch (VMException ex) {
            return ex;
        }

        return super.visitClassBodyDeclaration(ctx);
    }

    private void evalMethodDeclaration(MethodDeclarationContext methodDeclarationContext) {
        if (methodDeclarationContext == null)
            return;
    }

    private void evalFieldDeclaration(FieldDeclarationContext fieldDeclaration)
        throws VMException {
        if (fieldDeclaration == null)
            return;

        VMMachine vm = VMMachine.getInstance();

        VMType type = vm.getClazz(fieldDeclaration.type()).getType();

        for (VariableDeclaratorContext variableDeclarator
                : fieldDeclaration.variableDeclarators().variableDeclarator()) {
            String variableId = variableDeclarator.variableDeclaratorId().getChild(0).toString();

            VMIdentifierInstance identifier = vm.getID(variableId);

            buildObject.declareField(identifier, type);

            // TODO initializer

//            ExpressionContext initExpression = variableDeclarator.variableInitializer().expression();
//            evalExpression(initExpression);
//
//            assignValue();
        }
    }


    private boolean passesThroughFilter(ClassBodyDeclarationContext ctx) {
        if (modifierFilter == null)
            return true;

        for (ModifierContext modifier : ctx.modifier()) {
            if (modifier.getText().equals(modifierFilter))
                return !modifierFilter.isReversed();
        }

        return modifierFilter.isReversed();
    }


}
