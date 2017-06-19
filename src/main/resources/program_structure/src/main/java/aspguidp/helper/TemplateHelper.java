package aspguidp.helper;

import aspguidp.service.data.template.TemplatePart;
import aspguidp.service.data.template.impl.FixedTemplatePart;
import aspguidp.service.data.template.impl.PlaceholderTemplatePart;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Helper class which provides static methods for the handling of representation templates.
 * <p>
 * Representation templates are used to map an entity to another representation (for example: map an entity of
 * the type 'edge' with the attributes 'from' and 'to' to the string atom representation edge(from,to)).
 * The respective template for this conversion would be 'edge(::from,::to)'.
 * <p>
 * Representation templates are stored in the application as sequences of instances of the class {@link TemplatePart}.
 */
public class TemplateHelper {
    private static final Pattern placeholderPartPattern = Pattern.compile("::" + PatternHelper.getIdentifierPattern());

    /**
     * Extract template parts from the given string representation of a template.
     * Properties of placeholder template parts are gathered from the given supplier functions.
     *
     * @param templateString                 string representation of the template
     * @param placeholderNameSupplier        function which supplies the name for a given placeholder id
     * @param placeholderValueSourceSupplier function which supplies the value source for a given placeholder id
     * @return list of template part instances for the given string representation of a template
     */
    public static List<TemplatePart> getTemplateParts(String templateString,
                                                      Function<String, String> placeholderNameSupplier,
                                                      Function<String, String> placeholderValueSourceSupplier) {
        List<TemplatePart> templateParts = new ArrayList<>();
        Matcher placeholderPartMatcher = placeholderPartPattern.matcher(templateString);

        Integer lastMatchEnd = 0;
        while (placeholderPartMatcher.find(lastMatchEnd)) {
            if (placeholderPartMatcher.start() > lastMatchEnd) {
                String fixedPart = templateString.substring(lastMatchEnd, placeholderPartMatcher.start());
                templateParts.add(new FixedTemplatePart(fixedPart));
            }

            String placeholderPart = placeholderPartMatcher.group();
            String placeholderId = placeholderPart.replace("::", "");
            String placeholderName = placeholderNameSupplier.apply(placeholderId);
            String placeholderValueSource = placeholderValueSourceSupplier.apply(placeholderId);
            templateParts.add(new PlaceholderTemplatePart(placeholderId, placeholderName, placeholderValueSource));

            lastMatchEnd = placeholderPartMatcher.end();
        }

        if (lastMatchEnd < templateString.length()) {
            String fixedPart = templateString.substring(lastMatchEnd);
            templateParts.add(new FixedTemplatePart(fixedPart));
        }

        return templateParts;
    }

    /**
     * Try to match the given string against the given template parts. The returned list contains the maximum sub
     * sequence of the given template parts, which can be matched against the given string.
     * The returned sequence will always start with the first template part of the given template parts.
     *
     * @param s             sequence which is matched against the given template parts
     * @param templateParts template parts which are matched against the given string
     * @return maximum sequence of given template parts which matches against the given string
     */
    public static List<TemplatePart> getMatchingTemplateParts(String s, List<TemplatePart> templateParts) {
        List<TemplatePart> matchingParts = new ArrayList<>();
        String currentPatternString = "^";

        for (TemplatePart part : templateParts) {
            currentPatternString = currentPatternString + part.getPattern();
            Matcher m = Pattern.compile(currentPatternString).matcher(s);
            if (!m.find()) break;
            matchingParts.add(part);
        }

        return matchingParts;
    }

    /**
     * Returns the position of the end of the match, when the given string is matched against the given sequence of
     * template parts.
     *
     * @param s             string which is matched against the sequence of template parts
     * @param templateParts sequence of template parts which is matched against the given string
     * @return end position of the match
     */
    public static Integer getMatchEndPosition(String s, List<TemplatePart> templateParts) {
        String patternString = "^" + getPattern(templateParts);
        Matcher m = Pattern.compile(patternString).matcher(s);
        if (m.find()) return m.end();
        return 0;
    }

    /**
     * Returns the respective regex pattern for a sequence of template parts.
     *
     * @param templateParts sequence of template parts
     * @return regex pattern respective to the given sequence of template parts
     */
    public static String getPattern(List<TemplatePart> templateParts) {
        return templateParts.stream().map(TemplatePart::getPattern).collect(Collectors.joining());
    }
}
