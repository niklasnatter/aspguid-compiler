package aspguidc.exception.parsing;

/**
 * Signals that a condition statement in the gui definition is not valid.
 */
public class InvalidConditionStatementFormatException extends DefinitionFormatException {
    public InvalidConditionStatementFormatException(String conditionStatement) {
        super(String.format("%s is not a valid condition statement", conditionStatement));
    }
}
