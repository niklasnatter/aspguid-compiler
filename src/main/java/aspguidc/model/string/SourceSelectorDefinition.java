package aspguidc.model.string;

import aspguidc.exception.parsing.InvalidSourceSelectorFormatException;
import aspguidc.helper.PatternHelper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Model for a source selector property of a graphical user interface definition.
 */
public class SourceSelectorDefinition {
    private final String sourceRelation;

    private SourceSelectorDefinition(String sourceRelation) {
        this.sourceRelation = sourceRelation;
    }

    /**
     * Create a new source selector model instance from the given string representation
     *
     * @param s valid string representation of an condition statement according to the gui definition language
     *          specification
     * @return model instance
     * @throws InvalidSourceSelectorFormatException if the given string representation is not valid according to
     *                                              the gui definition language specification
     */
    public static SourceSelectorDefinition fromString(String s) throws InvalidSourceSelectorFormatException {
        String identifierPattern = PatternHelper.getIdentifierPattern();
        String sourceSelectorPattern = "^\\$(#?" + identifierPattern + "(?:\\[\\d+])?)$";
        Matcher matcher = Pattern.compile(sourceSelectorPattern).matcher(s);
        if (!matcher.matches()) throw new InvalidSourceSelectorFormatException(s);

        return new SourceSelectorDefinition(matcher.group(1));
    }

    /**
     * Create a new source selector model instance which represents an empty source selector.
     *
     * @return model instance which represents an empty source selector
     */
    public static SourceSelectorDefinition emptyDefinition() {
        return new SourceSelectorDefinition("");
    }

    public String getSourceRelation() {
        return this.sourceRelation;
    }
}
