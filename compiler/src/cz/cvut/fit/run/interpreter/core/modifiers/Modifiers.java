package cz.cvut.fit.run.interpreter.core.modifiers;

import cz.cvut.fit.run.parser.JavaParser;

import java.util.List;

/**
 * Created by MagNet on 23. 3. 2015.
 */
public class Modifiers {
    private boolean staticFlag;
    private boolean finalFlag;
    private Scope scope;

    public Modifiers(boolean staticFlag, boolean finalFlag, Scope scope) {
        this.staticFlag = staticFlag;
        this.finalFlag = finalFlag;
        this.scope = scope;
    }

    public Modifiers() {
        this(false, false, Scope.DEFAULT);
    }

    public boolean isStatic() {
        return staticFlag;
    }

    public void setStatic(boolean staticFlag) {
        this.staticFlag = staticFlag;
    }

    public Scope getScope() {
        return scope;
    }

    public void setScope(Scope scope) {
        this.scope = scope;
    }

    public boolean isFinal() {
        return finalFlag;
    }

    public void setFinal(boolean finalFlag) {
        this.finalFlag = finalFlag;
    }

    public static Modifiers getFor(List<JavaParser.ModifierContext> modifierList) {
        Modifiers modifiers = new Modifiers();
        for (JavaParser.ModifierContext modifier : modifierList) {
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
                    modifiers.setStatic(true);
                    break;
                case "final":
                    modifiers.setFinal(true);
                    break;
            }
        }

        return modifiers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Modifiers)) return false;

        Modifiers modifiers = (Modifiers) o;

        if (finalFlag != modifiers.finalFlag) return false;
        if (staticFlag != modifiers.staticFlag) return false;
        if (scope != modifiers.scope) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (staticFlag ? 1 : 0);
        result = 31 * result + (finalFlag ? 1 : 0);
        result = 31 * result + (scope != null ? scope.hashCode() : 0);
        return result;
    }
}
