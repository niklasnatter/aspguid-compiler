package aspguidp.helper;

/**
 * Helper class which provides static methods for accessing regex patterns which are used throughout the program.
 */
public class PatternHelper {
    private static final String identifierPattern = "[a-z][A-Za-z0-9_]*";
    private static final String stringPattern = "\"(?:[^\"]|\\\\\")*\"";
    private static final String numberPattern = "0|[1-9][0-9]*";

    /**
     * @return pattern string which matches a valid identifier which can be used as predicate name for an atom
     */
    public static String getIdentifierPattern() {
        return identifierPattern;
    }

    /**
     * @return pattern string which matches a value which can be used as an argument for an atom
     */
    public static String getValuePattern() {
        return "(" + identifierPattern + "|" + stringPattern + "|" + numberPattern + ")";
    }

    /**
     * @return pattern string which matches a string representation atom
     */
    public static String getAtomPattern() {
        String relationName = "(-?" + identifierPattern + ")";
        String argumentValue = "\\s*" + getValuePattern() + "\\s*";
        String argumentsPart = "\\(((" + argumentValue + ",?)+)\\)";
        return relationName + "(?:" + argumentsPart + ")?";
    }

    /**
     * @return pattern string which matches an answer set inside the output of the dlv solver
     */
    public static String getAnswerSetPattern() {
        // precise pattern leads to stack overflow error because of flawed regex implementation in java:
        // https://stackoverflow.com/questions/31676277/stackoverflowerror-in-regular-expression
        // return "\\{((?:\\s*" + getAtomPattern() + "\\s*,?)*)\\}";

        return "\\{(.+?)\\}";
    }
}
