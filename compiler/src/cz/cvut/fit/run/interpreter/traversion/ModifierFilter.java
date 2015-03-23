package cz.cvut.fit.run.interpreter.traversion;

/**
 * Created by MagNet on 23. 3. 2015.
 */
public class ModifierFilter {
    String stringFilter;
    boolean reversed;

    public ModifierFilter(String stringFilter, boolean reversed) {
        this.stringFilter = stringFilter;
        this.reversed = reversed;
    }

    public String getStringFilter() {
        return stringFilter;
    }

    public boolean isReversed() {
        return reversed;
    }
}
