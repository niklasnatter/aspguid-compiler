package aspguidc.model.output.entity;

import aspguidc.exception.parsing.DefinitionFormatException;
import aspguidc.exception.parsing.InvalidIdentifierFormatException;
import aspguidc.exception.parsing.InvalidJsonObjectException;
import aspguidc.helper.JsonHelper;
import com.google.gson.JsonElement;

import java.util.Map;
import java.util.logging.Logger;

/**
 * Model for an output attribute element of a graphical user interface definition.
 */
public class OutputAttributeDefinition {
    private static final String objectName = "@output_attribute";

    private final String identifier;
    private String name;
    private String description;

    private OutputAttributeDefinition(String identifier) throws InvalidIdentifierFormatException {
        JsonHelper.assertIdentifierIsValid(objectName, identifier);
        this.identifier = identifier;
    }

    /**
     * Create a new output attribute model instance with the given identifier from the given json element.
     * The model is created by extracting each property of the given json element and setting it to the
     * model.
     *
     * @param identifier identifier of the output attribute element
     * @param j          valid output attribute json element according to the gui definition language specification
     * @return model instance
     * @throws DefinitionFormatException if the given json element is not valid according to the gui definition
     *                                   language specification
     */
    public static OutputAttributeDefinition fromJsonElement(String identifier, JsonElement j) throws DefinitionFormatException {
        Logger.getGlobal().info("[parsing] read output attribute element '" + identifier + "'");

        if (!j.isJsonObject()) throw new InvalidJsonObjectException(objectName);

        OutputAttributeDefinition outputAttribute = new OutputAttributeDefinition(identifier);
        for (Map.Entry<String, JsonElement> entry : j.getAsJsonObject().entrySet()) {
            outputAttribute.setValueByJsonProperty(entry.getKey(), entry.getValue());
        }

        return outputAttribute;
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

        } else if ("description".equals(propertyKey)) {
            JsonHelper.assertPropertyIsString(objectName, propertyKey, propertyValue);
            this.description = propertyValue.getAsJsonPrimitive().getAsString();

        } else {
            Logger.getGlobal().warning(String.format("unsupported property '%s' in '%s' object", propertyKey, objectName));
        }
    }

    public String getIdentifier() {
        return this.identifier;
    }

    public String getName() {
        if (this.name == null) return this.identifier;
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }
}
