package aspguidp.helper;

import aspguidp.service.core.atom.Atom;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Helper class which provides static methods for extracting values for a value source from a collection of atoms.
 * <p>
 * A value source is either a predicate value source like 'node' or 'edge[0]' or a type like '#int'.
 * A predicate value source consists of a predicate name and an optional argument index. For example, the predicate
 * value source 'edge[0]' contains the predicate name 'edge' and the argument index '0'.
 * Values for a predicate value source are the values of the argument respective to the argument index of atoms with
 * the respective predicate name. If a predicate value source contains no argument index, the argument index is set
 * to 0 per default.
 * If the value source is a type, values for the type value source are values of the respective type.
 */
public class ValueSourceHelper {
    /**
     * Extract values for the given value source from the given collection of atoms.
     * If the given starting string is not empty, the returned values are filtered, so that every returned value starts
     * with the content of the given string
     * <p>
     * This method is used to generate suggestions for possible values. For example: when generating suggestions
     * for the 'from' attribute of an 'edge' entity, the 'from' attribute has the value source 'node[0]'.
     * In this case, this method is called with all the atoms of the program, the value source 'node[0]' and the
     * current user input. The returned list of values will contain all values from the value source 'node[0]', which
     * start with the given user input.
     *
     * @param sourceAtoms collection of atoms which is used to extract values for the given value source
     * @param valueSource value source for which the values are extracted
     * @param startsWith  string which must be the beginning of returned values
     * @return list of values for the given value source extracted from the given atoms which begin with the given
     * start string.
     */
    public static List<String> getValues(Collection<Atom> sourceAtoms, String valueSource, String startsWith) {
        if (valueSource.startsWith("#")) return getValuesForType(valueSource.substring(1), startsWith);

        String relationName = getRelationNameFromPredicateValueSource(valueSource);
        Integer argumentIndex = getArgumentIndexFromPredicateValueSource(valueSource);
        return sourceAtoms.stream()
                .filter(a -> a.getPredicateName().equals(relationName))
                .filter(a -> a.getArguments().size() > argumentIndex)
                .map(a -> a.getArguments().get(argumentIndex))
                .filter(s -> s.startsWith(startsWith))
                .distinct()
                .collect(Collectors.toList());
    }

    /**
     * @param valueSource predicate value source from which the relation name is extracted
     * @return relation name for the given predicate value source
     */
    private static String getRelationNameFromPredicateValueSource(String valueSource) {
        Matcher argumentIndexMatcher = Pattern.compile("\\[(\\d+)]$").matcher(valueSource);
        if (argumentIndexMatcher.find()) return valueSource.replace(argumentIndexMatcher.group(0), "");
        return valueSource;
    }

    /**
     * @param valueSource predicate value source from which the argument index is extracted
     * @return argument index for the given predicate value source
     */
    private static Integer getArgumentIndexFromPredicateValueSource(String valueSource) {
        Matcher argumentIndexMatcher = Pattern.compile("\\[(\\d+)]$").matcher(valueSource);
        if (argumentIndexMatcher.find()) return Integer.parseInt(argumentIndexMatcher.group(1));
        return 0;
    }

    /**
     * Return values for the given type value source. Currently, only the '#int' type values source is supported.
     * <p>
     * This method is used to generate suggestion for possible values with a type value source.
     *
     * @param typeValueSource type value source
     * @param startsWith      string which must be the beginning of returned values
     * @return list of values for the given type value source which begin with the given start string
     */
    private static List<String> getValuesForType(String typeValueSource, String startsWith) {
        List<String> validTypeValues = new ArrayList<>();

        if ("int".equals(typeValueSource)) {
            // add current value and possible next values, if current value is an integer
            if (startsWith.isEmpty()) startsWith = "0";
            try {
                Integer current = new Integer(startsWith);
                validTypeValues.add(Integer.toString(current));
                for (int i = 0; i < 10; i++) validTypeValues.add(Integer.toString(current * 10 + i));
            } catch (NumberFormatException e) {}
        }

        return validTypeValues.stream().distinct().collect(Collectors.toList());
    }
}
