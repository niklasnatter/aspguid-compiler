package aspguidc.exception.parsing;

/**
 * Signals that a placeholder which was used inside a representation template is not defined in the gui definition.
 */
public class MissingRepresentationTemplatePlaceholderException extends DefinitionFormatException {
    public MissingRepresentationTemplatePlaceholderException(String placeholderId, String templateString) {
        super(String.format("placeholder \"%s\" in representation template \"%s\" is not defined", placeholderId, templateString));
    }
}
