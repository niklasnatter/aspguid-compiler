package aspguidc.model.input.value;

import aspguidc.exception.parsing.DefinitionFormatException;
import aspguidc.exception.parsing.InvalidIdentifierFormatException;
import aspguidc.exception.parsing.InvalidJsonObjectException;
import aspguidc.exception.parsing.MissingRepresentationTemplatePlaceholderException;
import aspguidc.helper.JsonHelper;
import aspguidc.model.string.RepresentationTemplateDefinition;
import aspguidc.model.string.SourceSelectorDefinition;
import com.google.gson.JsonElement;

import java.util.Collections;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Model for an input value element of a graphical user interface definition.
 */
public class InputValueDefinition {
    private static final String objectName = "@input_value";

    private final String identifier;
    private String defaultValue;
    private SourceSelectorDefinition valueSource;
    private RepresentationTemplateDefinition atomRepresentation;
    private String name;

    private InputValueDefinition(String identifier) throws InvalidIdentifierFormatException {
        JsonHelper.assertIdentifierIsValid(objectName, identifier);
        this.identifier = identifier;
    }

    /**
     * Create a new input value model instance with the given identifier from the given json element.
     * The model is created by extracting each property of the given json element and setting it to the
     * model.
     *
     * @param identifier identifier of the input value element
     * @param j          valid input value json element according to the gui definition language specification
     * @return model instance
     * @throws DefinitionFormatException if the given json element is not valid according to the gui definition
     *                                   language specification
     */
    public static InputValueDefinition fromJsonElement(String identifier, JsonElement j) throws DefinitionFormatException {
        Logger.getGlobal().info("[parsing] read input value element '" + identifier + "'");

        if (!j.isJsonObject()) throw new InvalidJsonObjectException(objectName);

        InputValueDefinition inputValue = new InputValueDefinition(identifier);
        for (Map.Entry<String, JsonElement> entry : j.getAsJsonObject().entrySet()) {
            inputValue.setValueByJsonProperty(entry.getKey(), entry.getValue());
        }

        inputValue.assertRepresentationTemplateIntegrity();

        return inputValue;
    }

    /**
     * Set a specific value of the model instance by the given json property key and json property value.
     * The value which is set is selected based on the given json property key.
     * <p>
     * This method is the central point when creating a new model from a json element, as it is called for each
     * property of the json element.
     * <p>
     * If the given json property key is not defined by the gui definition language specification, a warning is
     * logged.
     *
     * @param propertyKey   key of the json property which is set to the model
     * @param propertyValue value of the json property which is set to the model
     * @throws DefinitionFormatException if the given property key is malformed or the given property value is
     *                                   not valid according to the gui definition language specification.
     */
    private void setValueByJsonProperty(String propertyKey, JsonElement propertyValue) throws DefinitionFormatException {
        if ("name".equals(propertyKey)) {
            JsonHelper.assertPropertyIsString(objectName, propertyKey, propertyValue);
            this.name = propertyValue.getAsJsonPrimitive().getAsString();

        } else if ("default_value".equals(propertyKey)) {
            JsonHelper.assertPropertyIsString(objectName, propertyKey, propertyValue);
            this.defaultValue = propertyValue.getAsJsonPrimitive().getAsString();

        } else if ("value_source".equals(propertyKey)) {
            JsonHelper.assertPropertyIsString(objectName, propertyKey, propertyValue);
            this.valueSource = SourceSelectorDefinition.fromString(propertyValue.getAsJsonPrimitive().getAsString());

        } else if ("atom_representation".equals(propertyKey)) {
            JsonHelper.assertPropertyIsString(objectName, propertyKey, propertyValue);
            this.atomRepresentation = RepresentationTemplateDefinition.fromAtomRepresentationString(propertyValue.getAsJsonPrimitive().getAsString());

        } else {
            Logger.getGlobal().warning(String.format("unsupported property '%s' in '%s' object", propertyKey, objectName));
        }
    }

    /**
     * Throw a {@link MissingRepresentationTemplatePlaceholderException} if a representation template of the input
     * value model contains a placeholder identifier which is not defined.
     *
     * @throws MissingRepresentationTemplatePlaceholderException
     */
    private void assertRepresentationTemplateIntegrity() throws MissingRepresentationTemplatePlaceholderException {
        for (RepresentationTemplateDefinition t : Collections.singletonList(this.getAtomRepresentation())) {
            for (String placeholderId : t.getPlaceholderIds()) {
                if (!this.identifier.equals(placeholderId)) {
                    throw new MissingRepresentationTemplatePlaceholderException(placeholderId, t.getTemplateString());
                }
            }
        }
    }

    public String getIdentifier() {
        return this.identifier;
    }

    public String getName() {
        if (this.name == null) {
            return this.identifier;
        }
        return this.name;
    }

    public String getDefaultValue() {
        return this.defaultValue;
    }

    public SourceSelectorDefinition getValueSource() {
        if (this.valueSource == null) return SourceSelectorDefinition.emptyDefinition();
        return this.valueSource;
    }

    public RepresentationTemplateDefinition getAtomRepresentation() {
        if (this.atomRepresentation == null) {
            return RepresentationTemplateDefinition.defaultTemplate(this.identifier, Collections.singletonList(this.identifier));
        }
        return this.atomRepresentation;
    }
}
