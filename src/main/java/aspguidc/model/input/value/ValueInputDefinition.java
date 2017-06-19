package aspguidc.model.input.value;

import aspguidc.exception.parsing.DefinitionFormatException;
import aspguidc.exception.parsing.InvalidIdentifierFormatException;
import aspguidc.exception.parsing.InvalidJsonObjectException;
import aspguidc.exception.parsing.MissingRepresentationTemplatePlaceholderException;
import aspguidc.helper.JsonHelper;
import aspguidc.model.input.InputElementDefinition;
import aspguidc.model.string.RepresentationTemplateDefinition;
import com.google.gson.JsonElement;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Model for an input element of the type value input of a graphical user interface definition.
 */
public class ValueInputDefinition implements InputElementDefinition {
    private static final String objectName = "@value_input";

    private final String identifier;
    private String title;
    private RepresentationTemplateDefinition guiRepresentation;
    private final Map<String, InputValueDefinition> valueElements = new LinkedHashMap<>();

    private ValueInputDefinition(String identifier) throws InvalidIdentifierFormatException {
        JsonHelper.assertIdentifierIsValid(objectName, identifier);
        this.identifier = identifier;
    }

    /**
     * Create a new value input model instance with the given identifier from the given json element.
     * The model is created by extracting each property of the given json element and setting it to the
     * model.
     *
     * @param identifier identifier of the value input element
     * @param j          valid value input json element according to the gui definition language specification
     * @return model instance
     * @throws DefinitionFormatException if the given json element is not valid according to the gui definition
     *                                   language specification
     */
    public static ValueInputDefinition fromJsonElement(String identifier, JsonElement j) throws DefinitionFormatException {
        Logger.getGlobal().info("[parsing] read value input element '" + identifier + "'");

        if (!j.isJsonObject()) throw new InvalidJsonObjectException(objectName);

        ValueInputDefinition valueInput = new ValueInputDefinition(identifier);
        for (Map.Entry<String, JsonElement> entry : j.getAsJsonObject().entrySet()) {
            valueInput.setValueByJsonProperty(entry.getKey(), entry.getValue());
        }

        valueInput.assertRepresentationTemplateIntegrity();

        return valueInput;
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
        if ("title".equals(propertyKey)) {
            JsonHelper.assertPropertyIsString(objectName, propertyKey, propertyValue);
            this.title = propertyValue.getAsJsonPrimitive().getAsString();

        } else if ("gui_representation".equals(propertyKey)) {
            JsonHelper.assertPropertyIsString(objectName, propertyKey, propertyValue);
            this.guiRepresentation = RepresentationTemplateDefinition.fromGuiRepresentationString(propertyValue.getAsJsonPrimitive().getAsString());

        } else if (propertyKey.startsWith("@input_value:")) {
            String[] keyParts = propertyKey.split(":", 2);
            this.valueElements.put(keyParts[1], InputValueDefinition.fromJsonElement(keyParts[1], propertyValue));

        } else {
            Logger.getGlobal().warning(String.format("unsupported property '%s' in '%s' object", propertyKey, objectName));
        }
    }

    /**
     * Throw a {@link MissingRepresentationTemplatePlaceholderException} if a representation template of the value
     * input model contains a placeholder identifier which is not defined.
     *
     * @throws MissingRepresentationTemplatePlaceholderException
     */
    private void assertRepresentationTemplateIntegrity() throws MissingRepresentationTemplatePlaceholderException {
        for (RepresentationTemplateDefinition t : Collections.singletonList(this.getGuiRepresentation())) {
            for (String placeholderId : t.getPlaceholderIds()) {
                if (!this.valueElements.containsKey(placeholderId)) {
                    throw new MissingRepresentationTemplatePlaceholderException(placeholderId, t.getTemplateString());
                }
            }
        }
    }

    public String getIdentifier() {
        return this.identifier;
    }

    public String getTitle() {
        return this.title;
    }

    public RepresentationTemplateDefinition getGuiRepresentation() {
        if (this.guiRepresentation == null) {
            return RepresentationTemplateDefinition.defaultTemplate(this.identifier, this.valueElements.keySet());
        }
        return this.guiRepresentation;
    }

    public Map<String, InputValueDefinition> getValueElements() {
        return this.valueElements;
    }
}
