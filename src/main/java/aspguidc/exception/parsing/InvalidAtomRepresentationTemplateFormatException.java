package aspguidc.exception.parsing;

/**
 * Signals that an atom representation template in the gui definition not a valid.
 */
public class InvalidAtomRepresentationTemplateFormatException extends DefinitionFormatException {
    public InvalidAtomRepresentationTemplateFormatException(String sourceSelector) {
        super(String.format("%s is not a valid code representation template", sourceSelector));
    }
}
