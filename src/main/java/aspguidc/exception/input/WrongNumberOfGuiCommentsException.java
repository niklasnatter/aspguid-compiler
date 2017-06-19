package aspguidc.exception.input;

import aspguidc.exception.parsing.DefinitionFormatException;

/**
 * Signals that the logic program contains a wrong number of gui definition comments.
 */
public class WrongNumberOfGuiCommentsException extends DefinitionFormatException {
    public WrongNumberOfGuiCommentsException(int numberOfGuiComments) {
        super("invalid number of gui definition comments found: " + numberOfGuiComments);
    }
}
