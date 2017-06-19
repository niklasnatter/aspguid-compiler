package aspguidp.exception;

/**
 * Signals that a given string value is not a valid string representation of an {@link aspguidp.service.core.atom.Atom}.
 * This exception is thrown, if the conversion of a string value to an atom fails.
 */
public class InvalidAtomRepresentationException extends Exception {
    public InvalidAtomRepresentationException(String invalidAtomString) {
        super(String.format("invalid atom string: \"%s\"", invalidAtomString));
    }
}
