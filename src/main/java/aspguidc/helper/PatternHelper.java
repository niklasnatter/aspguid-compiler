package aspguidc.helper;

/**
 * Helper class which provides static methods for accessing regex patterns which are used throughout the program.
 */
public class PatternHelper {
    private static final String identifierPattern = "[a-z][A-Za-z0-9_]*";
    private static final String stringPattern = "\"(?:[^\"]|\\\\\")*\"";
    private static final String numberPattern = "0|[1-9][0-9]*";

    /**
     * @return pattern string which matches a gui definition comment inside a logic program
     */
    public static String getGuiDefinitionCommentPattern() {
        return "%\\*::(.+?)::\\*%";
    }

    /**
     * @return pattern string which matches a valid identifier
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
     * @return pattern string which matches a value which can be used as an argument for an atom template
     */
    public static String getAtomTemplateValuePattern() {
        return "((?:::)?" + identifierPattern + "|" + stringPattern + "|" + numberPattern + ")";
    }

    /**
     * @return pattern string which matches an atom
     */
    public static String getAtomPattern() {
        String relationName = "(-?" + identifierPattern + ")";
        String argumentValue = "\\s*" + getValuePattern() + "\\s*";
        String argumentsPart = "\\(((" + argumentValue + ",?)+)\\)";
        return relationName + "(?:" + argumentsPart + ")?";
    }

    /**
     * @return pattern string which matches an atom template
     */
    public static String getAtomTemplatePattern() {
        String relationName = "(-?(?:::)?" + identifierPattern + ")";
        String argumentValue = " *" + getAtomTemplateValuePattern() + " *";
        String argumentsPart = "\\(((" + argumentValue + ",?)+)\\)";
        return relationName + "(?:" + argumentsPart + ")?";
    }
}
