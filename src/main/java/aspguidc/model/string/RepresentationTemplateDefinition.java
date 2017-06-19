package aspguidc.model.string;

import aspguidc.exception.format.InvalidArgumentFormatException;
import aspguidc.exception.parsing.InvalidAtomRepresentationTemplateFormatException;
import aspguidc.helper.NormalizationHelper;
import aspguidc.helper.PatternHelper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Model for a representation template property of a graphical user interface definition.
 */
public class RepresentationTemplateDefinition {
    private final String templateString;

    private RepresentationTemplateDefinition(String templateString) {
        this.templateString = templateString;
    }

    /**
     * Create a new gui representation template model instance from the given string.
     *
     * @param s string representation of the gui representation template
     * @return model instance
     */
    public static RepresentationTemplateDefinition fromGuiRepresentationString(String s) {
        return new RepresentationTemplateDefinition(s);
    }

    /**
     * Create a new atom representation template model instance from the given string.
     *
     * @param s string representation of the atom representation template
     * @return model instance
     * @throws InvalidAtomRepresentationTemplateFormatException if the given string is not a valid atom representation
     *                                                          template
     */
    public static RepresentationTemplateDefinition fromAtomRepresentationString(String s) throws InvalidAtomRepresentationTemplateFormatException {
        try {
            return new RepresentationTemplateDefinition(NormalizationHelper.normalizeAtomTemplate(s));
        } catch (InvalidArgumentFormatException e) {
            throw new InvalidAtomRepresentationTemplateFormatException(s);
        }
    }

    /**
     * Create a new default representation template model for the given entity name and attribute identifiers.
     * <p>
     * The default representation template for the entity "edge" with the attributes ["from", "to"] is:
     * edge(::from,::to)
     *
     * @param entityName           entity name which is used to generate the default representation template
     * @param attributeIdentifiers attribute identifiers which is used to generate the default representation template
     * @return model instance
     */
    public static RepresentationTemplateDefinition defaultTemplate(String entityName, Collection<String> attributeIdentifiers) {
        Collection<String> attributePlaceholders = attributeIdentifiers.stream().map("::"::concat).collect(Collectors.toList());
        String templateString = entityName + "(" + String.join(",", attributePlaceholders) + ")";
        return new RepresentationTemplateDefinition(templateString);
    }

    public String getTemplateString() {
        return this.templateString;
    }

    /**
     * Return a list which contains each word and each placeholder of the template as single list entry.
     * This method is used by the value writer classes to generate the content of the fxml file.
     *
     * @return list containing each word and each placeholder of the template as single list entry.
     */
    public List<String> getTemplateParts() {
        List<String> templateParts = new ArrayList<>();
        String placeholderPattern = " |(::(" + PatternHelper.getIdentifierPattern() + "))";
        Matcher placeholderMatcher = Pattern.compile(placeholderPattern).matcher(this.templateString);

        Integer lastEnd = 0;
        while (placeholderMatcher.find()) {
            templateParts.add(this.templateString.substring(lastEnd, placeholderMatcher.start()));
            templateParts.add(this.templateString.substring(placeholderMatcher.start(), placeholderMatcher.end()));
            lastEnd = placeholderMatcher.end();
        }
        templateParts.add(this.templateString.substring(lastEnd));

        return templateParts.stream().filter(s -> !s.isEmpty()).collect(Collectors.toList());
    }

    /**
     * @return list of placeholder identifiers which are used in the template
     */
    public List<String> getPlaceholderIds() {
        String placeholderPattern = "::(" + PatternHelper.getIdentifierPattern() + ")";
        Matcher placeholderMatcher = Pattern.compile(placeholderPattern).matcher(this.templateString);

        List<String> placeholderIds = new ArrayList<>();
        while (placeholderMatcher.find()) placeholderIds.add(placeholderMatcher.group(1));

        return placeholderIds;
    }
}
