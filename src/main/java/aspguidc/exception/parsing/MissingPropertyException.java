package aspguidc.exception.parsing;

/**
 * Signals that a required property of the gui definition was not found.
 */
public class MissingPropertyException extends DefinitionFormatException {
    public MissingPropertyException(String objectName, String propertyName) {
        super(String.format("missing property %s in %s object", propertyName, objectName));
    }
}
