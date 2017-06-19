package aspguidc.exception.parsing;

/**
 * Signals that an identifier in the gui definition is not valid.
 */
public class InvalidIdentifierFormatException extends DefinitionFormatException {
    public InvalidIdentifierFormatException(String objectName, String identifier) {
        super(String.format("%s is not a valid identifier for an %s object", identifier, objectName));
    }
}
