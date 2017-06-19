package aspguidp.helper;

import aspguidp.exception.InvalidAtomRepresentationException;
import aspguidp.service.core.atom.Atom;
import aspguidp.service.core.atom.impl.SimpleAtom;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Helper class which provides static methods regarding to the management of {@link Atom} instances.
 */
public class AtomHelper {
    /**
     * Extract string representations of atoms from the given program code.
     *
     * @param programCodeString code of the program to extract atom strings from
     * @return list of string representations fo atoms extracted from the given program code.
     */
    public static List<String> extractAtomStringsFromProgramCode(String programCodeString) {
        List<String> atomStrings = new ArrayList<>();
        String pattern = "(?:^|\\R|\\.)\\s*(" + PatternHelper.getAtomPattern() + ")(?=\\.)";
        Matcher atomMatcher = Pattern.compile(pattern).matcher(programCodeString);
        while (atomMatcher.find()) {
            atomStrings.add(atomMatcher.group(1));
        }
        return atomStrings;
    }

    /**
     * Extract string representations of atoms from the given answer set string.
     *
     * @param answerSetString string representation of the answer set to extract atom strings from
     * @return list of string representations of atoms extracted from the given answer set
     */
    public static List<String> extractAtomStringsFromAnswerSet(String answerSetString) {
        List<String> atomStrings = new ArrayList<>();
        Matcher atomMatcher = Pattern.compile("(" + PatternHelper.getAtomPattern() + "),?").matcher(answerSetString);
        while (atomMatcher.find()) {
            atomStrings.add(atomMatcher.group(1));
        }
        return atomStrings;
    }

    /**
     * Get a list of atom instances from a list of string representations of atoms.
     * If an atom string representation from the given list is invalid, an exception is printed to stderr.
     * The returned list will contain atom instances for every atom string representation which was valid.
     *
     * @param atomStrings list of string representations of atoms
     * @return list of atom instances
     */
    public static List<Atom> atomsFromStringRepresentations(List<String> atomStrings) {
        List<Atom> atoms = new ArrayList<>();
        for (String s : atomStrings) {
            try {
                atoms.add(SimpleAtom.fromStringRepresentation(s));
            } catch (InvalidAtomRepresentationException e) {
                e.printStackTrace();
            }
        }
        return atoms;
    }

    /**
     * Group a given collection of atoms by the predicate names of the atoms
     *
     * @param atoms collection of atom instances which is grouped by their predicate name
     * @return map which contains predicate names as key and lists of respective atoms as values
     */
    public static Map<String, List<Atom>> groupAtomsByPredicateName(Collection<Atom> atoms) {
        return atoms.stream().collect(Collectors.groupingBy(Atom::getPredicateName, Collectors.toList()));
    }

    /**
     * Generate the program code which can be used in an asp program for the given atoms.
     *
     * @param atoms atoms which are set to the program code
     * @return string containing the program code for the given atom instances
     */
    public static String getProgramCode(Collection<Atom> atoms) {
        List<Atom> sortedAtoms = new ArrayList<>(atoms);
        sortedAtoms.sort(Comparator.comparing(Atom::getStringRepresentation));
        return sortedAtoms.stream().map(Atom::getStringRepresentation).map(s -> s + ". ").collect(Collectors.joining());
    }

    /**
     * Generate the program code which can be used in an asp program for the given atom instances.
     * Each list in the list of lists are written to the program code as one line.
     * This enables the generation of formatted program code (for example: write atoms with the same predicate name
     * to the same program code line).
     *
     * @param atomLines list of list of atom instances. each list of atom instances will be set to the
     *                  program code as single line
     * @return string containing the program code for the given atom instances
     */
    public static List<String> getProgramCodeLines(Collection<List<Atom>> atomLines) {
        return atomLines.stream().map(AtomHelper::getProgramCode).collect(Collectors.toList());
    }
}
