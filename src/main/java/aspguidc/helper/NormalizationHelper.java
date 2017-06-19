package aspguidc.helper;

import aspguidc.exception.format.InvalidArgumentFormatException;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Helper class which provides static methods regarding to the normalisation of various strings.
 * The provided methods are used to normalize user input to a uniform format when generating the source code
 * of a logic program.
 */
public class NormalizationHelper {
    /**
     * Normalize the string representation of an logical atom.
     * The normalized representation of an atom does not contain spaces between the arguments of the relation.
     *
     * @param atomString string representation of an atom which is normalized
     * @return normalized string representation of the given atom
     * @throws InvalidArgumentFormatException if the given string is not a valid atom
     */
    public static String normalizeAtomString(String atomString) throws InvalidArgumentFormatException {
        Matcher m = Pattern.compile(PatternHelper.getAtomPattern()).matcher(atomString);
        if (!m.matches()) throw new InvalidArgumentFormatException();
        String relationName = m.group(1);
        String termString = m.group(2);

        List<String> termValues = new ArrayList<>();
        if (termString != null) {
            Matcher termsMatcher = Pattern.compile(PatternHelper.getValuePattern()).matcher(termString);
            while (termsMatcher.find()) {
                termValues.add(termsMatcher.group(0));
            }
        }

        String normalizedTermString = (!termValues.isEmpty()) ? "(" + String.join(",", termValues) + ")" : "";
        return relationName + normalizedTermString;
    }

    /**
     * Normalize the string representation of an atom template.
     * The normalized representation of an atom template does not contain spaces between the arguments of the relation.
     *
     * @param atomRepresentation string representation of an atom template which is normalized
     * @return normalized string representation of the given atom template
     * @throws InvalidArgumentFormatException if the given string is not a valid atom template
     */
    public static String normalizeAtomTemplate(String atomRepresentation) throws InvalidArgumentFormatException {
        Matcher m = Pattern.compile(PatternHelper.getAtomTemplatePattern()).matcher(atomRepresentation);
        if (!m.matches()) throw new InvalidArgumentFormatException();
        String relationName = m.group(1);
        String termString = m.group(2);

        List<String> termValues = new ArrayList<>();
        if (termString != null) {
            Matcher termsMatcher = Pattern.compile(PatternHelper.getAtomTemplateValuePattern()).matcher(termString);
            while (termsMatcher.find()) {
                termValues.add(termsMatcher.group(0));
            }
        }

        String normalizedTermString = (!termValues.isEmpty()) ? "(" + String.join(",", termValues) + ")" : "";
        return relationName + normalizedTermString;
    }
}
