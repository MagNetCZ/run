package cz.cvut.fit.run.interpreter.context;

/**
 * Created by MagNet on 12. 3. 2015.
 */
public class VMMachine {
    private static VMMachine instance = null;

    private VMFrame currentFrame;

    private VMMachine() {
        currentFrame = new VMFrame();
    }

    public static VMMachine getInstance() {
        if (instance == null) {
            instance = new VMMachine();
        }

        return instance;
    }

    public VMFrame getCurrentFrame() {
        return currentFrame;
    }
}
