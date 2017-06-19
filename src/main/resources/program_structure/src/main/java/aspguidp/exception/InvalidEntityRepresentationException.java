package aspguidp.exception;

import aspguidp.service.data.template.TemplatePart;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Signals that a given string value or a given placeholder value map is not a string representation of an
 * {@link aspguidp.service.data.model.Entity}. This exception is thrown, if the conversion of a string value or a
 * placeholder map to an entity fails.
 */
public class InvalidEntityRepresentationException extends Exception {
    public InvalidEntityRepresentationException(String stringValue, List<TemplatePart> templateParts) {
        super(String.format("invalid representation string \"%s\" for template \"%s\"",
                stringValue,
                templateParts.stream().map(TemplatePart::getDisplayValue).collect(Collectors.joining())));
    }

    public InvalidEntityRepresentationException(Map<String, String> placeholderValues, List<TemplatePart> templateParts) {
        super(String.format("invalid placeholder value map \"%s\" for template \"%s\"",
                placeholderValues.toString(),
                templateParts.stream().map(TemplatePart::getDisplayValue).collect(Collectors.joining())));
    }
}
