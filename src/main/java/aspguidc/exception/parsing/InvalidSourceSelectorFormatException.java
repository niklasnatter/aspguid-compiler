package aspguidc.exception.parsing;

/**
 * Signals that a source selector in the gui definition is not valid.
 */
public class InvalidSourceSelectorFormatException extends DefinitionFormatException {
    public InvalidSourceSelectorFormatException(String sourceSelector) {
        super(String.format("%s is not a valid source selector", sourceSelector));
    }
}
