package aspguidp.service.input.validation.impl;

import aspguidp.helper.TemplateHelper;
import aspguidp.helper.ValueSourceHelper;
import aspguidp.service.core.CoreServicePool;
import aspguidp.service.core.atom.Atom;
import aspguidp.service.data.template.TemplatePart;
import aspguidp.service.input.validation.ValidationService;

import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Implementation of the {@link ValidationService} interface which validates the given input based on a sequence of
 * {@link TemplatePart} instances.
 * <p>
 * An instance of this class is initialized with a sequence of template parts and validates a given input value against
 * these template parts. A given input validation is not only validated against the format of the template, but also
 * the placeholder values of the given input are validated against the value source of the respective template part.
 */
public class TemplateValidationService implements ValidationService {
    private final List<TemplatePart> templateParts;

    /**
     * Create a new service instance for the given sequence of template parts.
     *
     * @param inputTemplateParts sequence of template parts which is used to validate the input passed to the service
     */
    public TemplateValidationService(List<TemplatePart> inputTemplateParts) {
        this.templateParts = inputTemplateParts;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ValidationStatus validate(String input) {
        List<TemplatePart> matchingParts = TemplateHelper.getMatchingTemplateParts(input, this.templateParts);

        ValidationStatus notMatchedEndStatus = this.validateNotMatchedEnd(input, matchingParts);
        if (notMatchedEndStatus != ValidationStatus.VALID) return notMatchedEndStatus;

        ValidationStatus placeholderStatus = this.validatePlaceholderValues(input, matchingParts);
        if (placeholderStatus != ValidationStatus.VALID) return placeholderStatus;

        // check if input matches all parts
        if (matchingParts.size() != this.templateParts.size()) return ValidationStatus.INCOMPLETE;

        return ValidationStatus.VALID;
    }

    /**
     * Validate the end of the given string which is not matched by the given sequence of template parts.
     *
     * @param input         input which is validated against the given template parts
     * @param matchingParts template parts which are used to validate the given input
     * @return validation status of the end of the given string which is not matched by the given template parts
     */
    private ValidationStatus validateNotMatchedEnd(String input, List<TemplatePart> matchingParts) {
        // check if not matched input is valid
        String notMatchedInput = input.substring(TemplateHelper.getMatchEndPosition(input, matchingParts));

        if (!notMatchedInput.isEmpty()) {
            // all parts are already matched
            if (matchingParts.size() == this.templateParts.size()) return ValidationStatus.INVALID;

            TemplatePart nextPart = this.templateParts.get(matchingParts.size());
            // not matched input is invalid as placeholder value, otherwise it would have been matched
            if (nextPart.isPlaceholderPart()) return ValidationStatus.INVALID;
            // not matched input is invalid, because it is not the beginning of the next part
            if (!nextPart.getDisplayValue().startsWith(notMatchedInput)) return ValidationStatus.INVALID;
        }

        return ValidationStatus.VALID;
    }

    /**
     * Validate the placeholder values of the given string by the given sequence of template parts.
     * This method extracts the placeholder values from the given string based on the given sequence of template parts
     * and validates every placeholder value against the respective value source of the placeholder template part.
     *
     * @param input         string of which the placeholder values are validated against the given template parts
     * @param matchingParts template parts which are used to extract the placeholder values from the given string
     * @return validation status of the placeholder values of the given string
     */
    private ValidationStatus validatePlaceholderValues(String input, List<TemplatePart> matchingParts) {
        String matchedPattern = "^" + TemplateHelper.getPattern(matchingParts);
        Matcher matcher = Pattern.compile(matchedPattern).matcher(input);
        if (!matcher.find()) return ValidationStatus.INVALID;

        List<TemplatePart> matchingPlaceholderParts = matchingParts.stream()
                .filter(TemplatePart::isPlaceholderPart).collect(Collectors.toList());
        for (int i = 0; i < matchingPlaceholderParts.size(); i++) {
            String placeholderValue = matcher.group(i + 1);
            String valueSource = matchingPlaceholderParts.get(i).getPlaceholderValueSource();
            Boolean lastInputPart = input.endsWith(placeholderValue);

            ValidationStatus placeholderStatus = this.validatePlaceholderValue(placeholderValue, valueSource, lastInputPart);
            if (placeholderStatus != ValidationStatus.VALID) return placeholderStatus;
        }

        return ValidationStatus.VALID;
    }

    /**
     * Validate the given placeholder value against the given value source based on if the given value is the last part
     * of the input.
     * <p>
     * A placeholder value is valid if it is a value of the value source, otherwise it is invalid. If the placeholder
     * value is the last part of the input of the user, it is validated as incomplete, if there exists a value of the
     * value source which starts with the given input.
     *
     * @param value             placeholder value which is validated
     * @param valueSource       value source which is used for validation. if empty, every value is considered as valid
     * @param isLastPartOfInput true, if the given placeholder value is the last part of the user input
     * @return validation status of the given placeholder value
     */
    private ValidationStatus validatePlaceholderValue(String value, String valueSource, Boolean isLastPartOfInput) {
        // if value source is empty, ever value is valid
        if (valueSource.isEmpty()) return ValidationStatus.VALID;

        Collection<Atom> currentInputAtoms = CoreServicePool.getInstance().getInputAtomHub().getAtomsFromSuppliers();
        List<String> possibleValidValues = ValueSourceHelper.getValues(currentInputAtoms, valueSource, value);

        if (possibleValidValues.contains(value)) return ValidationStatus.VALID;
        // if value is last part of input and there is a possible valid value left, status is incomplete
        if (isLastPartOfInput && !possibleValidValues.isEmpty()) return ValidationStatus.INCOMPLETE;

        return ValidationStatus.INVALID;
    }

}
