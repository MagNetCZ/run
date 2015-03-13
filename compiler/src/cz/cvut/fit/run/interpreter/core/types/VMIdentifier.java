package cz.cvut.fit.run.interpreter.core.types;

/**
 * Created by MagNet on 12. 3. 2015.
 */
public class VMIdentifier extends VMBuiltinType<String> {
    public VMIdentifier(String contents) {
        super(contents);
    }

    @Override
    public VMType getType() {
        return VMType.ID;
    }
}
