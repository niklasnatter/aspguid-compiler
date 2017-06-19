package aspguidp.service.core.atom.impl;

import aspguidp.exception.InvalidAtomRepresentationException;
import aspguidp.helper.PatternHelper;
import aspguidp.service.core.atom.Atom;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Basic implementation of the {@link Atom} interface which allows the creation of an atom instance by a raw predicate
 * name and a raw list of arguments.
 * <p>
 * Additionally, this class provides a static method to create an atom instance from the string representation of an
 * atom.
 */
public class SimpleAtom implements Atom {
    private static final Pattern atomPattern = Pattern.compile(PatternHelper.getAtomPattern());
    private static final Pattern argumentPattern = Pattern.compile(PatternHelper.getValuePattern());

    private final String predicateName;
    private final List<String> arguments;

    /**
     * Create a new atom instance with the given predicate name and list of arguments
     *
     * @param predicateName predicate name of the created atom instance
     * @param arguments     list of arguments of the created atom instance
     */
    public SimpleAtom(String predicateName, List<String> arguments) {
        this.predicateName = predicateName;
        this.arguments = arguments;
    }

    /**
     * Create a new atom instance from the string representation of an atom.
     *
     * @param stringRepresentation string representation from which the atom is extracted
     * @return atom instance for the given string representation of an atom
     * @throws InvalidAtomRepresentationException if the given string representation is not a valid atom string
     *                                            representation
     */
    public static Atom fromStringRepresentation(String stringRepresentation) throws InvalidAtomRepresentationException {
        Matcher atomMatcher = atomPattern.matcher(stringRepresentation);
        if (!atomMatcher.matches()) throw new InvalidAtomRepresentationException(stringRepresentation);
        String predicateName = atomMatcher.group(1);
        String termString = (atomMatcher.group(2) != null) ? atomMatcher.group(2) : "";

        List<String> arguments = new ArrayList<>();
        Matcher argumentMatcher = argumentPattern.matcher(termString);
        while (argumentMatcher.find()) arguments.add(argumentMatcher.group(0));

        return new SimpleAtom(predicateName, arguments);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPredicateName() {
        return this.predicateName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getArguments() {
        return this.arguments;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer getArity() {
        return (this.arguments != null && !this.arguments.isEmpty()) ? this.arguments.size() : 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getStringRepresentation() {
        Boolean hasArguments = this.arguments != null && !this.arguments.isEmpty();
        String argumentsString = (hasArguments) ? "(" + String.join(",", this.arguments) + ")" : "";
        return this.predicateName + argumentsString;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof SimpleAtom)) return false;
        if (obj == this) return true;

        Atom o = (Atom) obj;
        if (!o.getPredicateName().equals(this.getPredicateName())) return false;
        if (!o.getArguments().equals(this.getArguments())) return false;
        if (!o.getStringRepresentation().equals(this.getStringRepresentation())) return false;

        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return this.predicateName.hashCode() + this.arguments.hashCode();
    }
}
