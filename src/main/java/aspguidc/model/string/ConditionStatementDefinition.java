package aspguidc.model.string;

import aspguidc.exception.format.InvalidArgumentFormatException;
import aspguidc.exception.parsing.InvalidConditionStatementFormatException;
import aspguidc.helper.NormalizationHelper;
import aspguidc.helper.PatternHelper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Model for a condition statement property of a graphical user interface definition.
 */
public class ConditionStatementDefinition {
    private final String conditionAtom;

    private ConditionStatementDefinition(String conditionAtom) {
        this.conditionAtom = conditionAtom;
    }

    /**
     * Create a new condition statement model instance from the given string representation.
     *
     * @param s valid string representation of an condition statement according to the gui definition language
     *          specification
     * @return model instance
     * @throws InvalidConditionStatementFormatException if the given string representation is not valid according to
     *                                                  the gui definition language specification
     */
    public static ConditionStatementDefinition fromString(String s) throws InvalidConditionStatementFormatException {
        String conditionStatementPattern = "^\\?(" + PatternHelper.getAtomPattern() + ")$";
        Matcher matcher = Pattern.compile(conditionStatementPattern).matcher(s);
        if (!matcher.matches()) throw new InvalidConditionStatementFormatException(s);

        try {
            return new ConditionStatementDefinition(NormalizationHelper.normalizeAtomString(matcher.group(1)));
        } catch (InvalidArgumentFormatException e) {
            throw new InvalidConditionStatementFormatException(s);
        }
    }

    /**
     * Create a new condition statement model instance which represents an empty condition statement.
     *
     * @return model instance which represents an empty condition statement
     */
    public static ConditionStatementDefinition emptyDefinition() {
        return new ConditionStatementDefinition("");
    }

    public String getConditionAtom() {
        return this.conditionAtom;
    }
}
