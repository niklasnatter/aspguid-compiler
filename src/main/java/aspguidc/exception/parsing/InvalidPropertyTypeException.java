package aspguidc.exception.parsing;

/**
 * Signals that a property of the gui definition has an invalid type.
 */
public class InvalidPropertyTypeException extends DefinitionFormatException {
    public InvalidPropertyTypeException(String objectName, String propertyName, String requiredType) {
        super(String.format("property %s in %s object must be of type %s", propertyName, objectName, requiredType));
    }
}
