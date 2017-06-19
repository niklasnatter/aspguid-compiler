package aspguidc.exception.parsing;

/**
 * Signals that a section of the gui definition is not a valid json object.
 */
public class InvalidJsonObjectException extends DefinitionFormatException {
    public InvalidJsonObjectException(String objectName) {
        super(String.format("%s is not a valid json object", objectName));
    }

    public InvalidJsonObjectException(String objectName, String explanation) {
        super(String.format("%s is not a valid json object: %s", objectName, explanation));
    }
}
