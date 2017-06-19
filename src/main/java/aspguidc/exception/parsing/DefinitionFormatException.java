package aspguidc.exception.parsing;

/**
 * Signals that the input is not valid according to the graphical user interface definition format.
 * This is the base class for all definition format exceptions.
 */
public abstract class DefinitionFormatException extends Exception {
    public DefinitionFormatException(String s) {
        super(s);
    }
}
