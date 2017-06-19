package aspguidp.service.core.atom;

import java.util.List;

/**
 * Interface for a logic atom.
 * <p>
 * Atoms are basic data structures which are passed as input of the logic program to the logic program solver and
 * which are read as output from the logic program from the resulting answer sets of the execution of the logic
 * program.
 * An atom consists of a predicate name and an optional list of arguments. For example, the atom 'edge(alpha,beta)'
 * consists of the predicate name 'edge' and the arguments ['alpha', 'beta'].
 * <p>
 * Atoms which are used in the application are usually passed through an {@link AtomHub} instance.
 */
public interface Atom {
    /**
     * @return predicate name of the atom
     */
    String getPredicateName();

    /**
     * @return list of arguments of the atom
     */
    List<String> getArguments();

    /**
     * @return number of arguments of the atom
     */
    Integer getArity();

    /**
     * @return string representation of the atom which is set as input to the logic program
     */
    String getStringRepresentation();

    /**
     * {@inheritDoc}
     */
    @Override
    boolean equals(Object obj);

    /**
     * {@inheritDoc}
     */
    @Override
    int hashCode();
}
