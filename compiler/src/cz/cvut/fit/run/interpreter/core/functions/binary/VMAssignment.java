package cz.cvut.fit.run.interpreter.core.functions.binary;

import cz.cvut.fit.run.interpreter.context.VMMachine;
import cz.cvut.fit.run.interpreter.core.exceptions.VMException;
import cz.cvut.fit.run.interpreter.core.functions.VMStackFunction;
import cz.cvut.fit.run.interpreter.core.types.instances.VMIdentifierInstance;
import cz.cvut.fit.run.interpreter.core.types.instances.VMObject;

/**
 * Created by MagNet on 12. 3. 2015.
 */
public class VMAssignment implements VMStackFunction {
    @Override
    public Object call() throws VMException {
        VMObject value = VMMachine.pop();
        VMObject identifier = VMMachine.pop();

        VMMachine.logger.info("Assigning " + identifier + " = " + value);

        VMMachine.getFrame().assignVariable((VMIdentifierInstance) identifier, value);
        return null;
    }
}
