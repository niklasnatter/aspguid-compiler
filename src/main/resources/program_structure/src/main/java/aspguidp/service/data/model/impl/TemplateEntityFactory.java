package aspguidp.service.data.model.impl;

import aspguidp.exception.InvalidEntityRepresentationException;
import aspguidp.helper.TemplateHelper;
import aspguidp.service.data.model.Entity;
import aspguidp.service.data.model.EntityFactory;
import aspguidp.service.data.template.TemplatePart;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Implementation of the {@link EntityFactory} interface which uses sequences of {@link TemplatePart} instances to
 * construct {@link Entity} instances.
 * <p>
 * An instance of the class is initializes with a sequence of display template parts and a sequence of atom template
 * parts. These representation templates are used to create a respective entity from a given display representation
 * or a given atom representation.
 */
public class TemplateEntityFactory implements EntityFactory {
    private final List<TemplatePart> atomTemplateParts;
    private final List<TemplatePart> displayTemplateParts;

    /**
     * Create a new service instance for the given display template parts and atom template parts.
     *
     * @param displayTemplateParts display representation template which is used to create a new entity from a given
     *                             display representation.
     * @param atomTemplateParts    atom representation template which is used to create a new entity from a given atom
     *                             representation
     */
    public TemplateEntityFactory(List<TemplatePart> displayTemplateParts, List<TemplatePart> atomTemplateParts) {
        this.displayTemplateParts = displayTemplateParts;
        this.atomTemplateParts = atomTemplateParts;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Entity fromDisplayRepresentation(String displayRepresentation) throws InvalidEntityRepresentationException {
        return this.fromPlaceholderValues(this.placeholderValuesFromString(displayRepresentation, this.displayTemplateParts));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Entity fromAtomRepresentation(String atomRepresentation) throws InvalidEntityRepresentationException {
        return this.fromPlaceholderValues(this.placeholderValuesFromString(atomRepresentation, this.atomTemplateParts));
    }

    /**
     * Create a new entity from the given placeholder values by using the representation templates of the manager
     * instance.
     *
     * @param placeholderValues placeholder values which are used to create the entity from
     * @return entity extracted from the given placeholder values
     * @throws InvalidEntityRepresentationException if the given placeholder values are not valid
     */
    private Entity fromPlaceholderValues(Map<String, String> placeholderValues) throws InvalidEntityRepresentationException {
        String displayRepresentation = this.stringFromPlaceholderValues(placeholderValues, this.displayTemplateParts);
        String atomRepresentation = this.stringFromPlaceholderValues(placeholderValues, this.atomTemplateParts);

        return new SimpleEntity(placeholderValues, displayRepresentation, atomRepresentation);
    }

    /**
     * Extract the placeholder values of the placeholder template parts of the given representation template from the
     * given string.
     *
     * @param stringValue   string from which the placeholder values are extracted
     * @param templateParts representation template which defines which placeholder values are extracted
     * @return map containing the placeholder id and the respective value which was extracted from the string
     * @throws InvalidEntityRepresentationException if the given string does not match the given representation template
     */
    private Map<String, String> placeholderValuesFromString(String stringValue, List<TemplatePart> templateParts) throws InvalidEntityRepresentationException {
        Matcher m = Pattern.compile(TemplateHelper.getPattern(templateParts)).matcher(stringValue);
        if (!m.matches()) throw new InvalidEntityRepresentationException(stringValue, templateParts);

        Integer currentMatcherGroup = 1;
        Map<String, String> placeholderValues = new HashMap<>();
        for (TemplatePart part : templateParts) {
            if (!part.isPlaceholderPart()) continue;
            placeholderValues.put(part.getPlaceholderIdentifier(), m.group(currentMatcherGroup++));
        }
        return placeholderValues;
    }

    /**
     * Generate a string by setting the value of the placeholder template parts of the given representation template
     * to the given placeholder values.
     *
     * @param placeholderValues map containing the placeholder id and the value which should be set to the respective
     *                          placeholder
     * @param templateParts     representation template which is filled with the given placeholder values and converted
     *                          to a string
     * @return string value of the given representation template after setting the given placeholder values
     * @throws InvalidEntityRepresentationException if the given placeholder values do not match the placeholder
     *                                              template parts of the given representation template
     */
    private String stringFromPlaceholderValues(Map<String, String> placeholderValues, List<TemplatePart> templateParts) throws InvalidEntityRepresentationException {
        String stringValue = "";

        for (TemplatePart part : templateParts) {
            if (part.isPlaceholderPart()) {
                if (!placeholderValues.containsKey(part.getPlaceholderIdentifier())) {
                    throw new InvalidEntityRepresentationException(placeholderValues, templateParts);
                }
                stringValue = stringValue + placeholderValues.get(part.getPlaceholderIdentifier());
            } else {
                stringValue = stringValue + part.getDisplayValue();
            }
        }

        return stringValue;
    }

}
