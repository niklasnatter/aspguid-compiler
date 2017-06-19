package aspguidc.exception.format;

/**
 * Signals that the an argument which was passed to a method is invalid for the method.
 * <p>
 * This exception is used in the {@link aspguidc.helper.NormalizationHelper} class to signal that the given input
 * cannot be normalized, because it is invalid.
 */
public class InvalidArgumentFormatException extends Exception {
}
