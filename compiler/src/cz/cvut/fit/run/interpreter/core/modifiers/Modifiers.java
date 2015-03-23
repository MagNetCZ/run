package cz.cvut.fit.run.interpreter.core.modifiers;

/**
 * Created by MagNet on 23. 3. 2015.
 */
public class Modifiers {
    private boolean staticFlag;
    private Scope scope;

    // TODO final

    public Modifiers(boolean staticFlag, Scope scope) {
        this.staticFlag = staticFlag;
        this.scope = scope;
    }

    public Modifiers() {
        this(false, Scope.DEFAULT);
    }

    public boolean isStaticFlag() {
        return staticFlag;
    }

    public void setStaticFlag(boolean staticFlag) {
        this.staticFlag = staticFlag;
    }

    public Scope getScope() {
        return scope;
    }

    public void setScope(Scope scope) {
        this.scope = scope;
    }
}
