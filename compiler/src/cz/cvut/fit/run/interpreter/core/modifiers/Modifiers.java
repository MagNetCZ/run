package cz.cvut.fit.run.interpreter.core.modifiers;

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
}
