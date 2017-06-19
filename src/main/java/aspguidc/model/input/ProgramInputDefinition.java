package aspguidc.model.input;

import aspguidc.exception.parsing.DefinitionFormatException;
import aspguidc.exception.parsing.InvalidJsonObjectException;
import aspguidc.model.input.entity.EntityInputDefinition;
import aspguidc.model.input.value.ValueInputDefinition;
import com.google.gson.JsonElement;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Model for the program input object of a graphical user interface definition.
 */
public class ProgramInputDefinition {
    private static final String objectName = "program_input";

    private final Map<String, InputElementDefinition> inputElements = new LinkedHashMap<>();

    private ProgramInputDefinition() {
    }

    /**
     * Create a new program input model instance from the given json element.
     * The model is created by extracting each property of the given json element and setting it to the
     * model.
     *
     * @param j valid program input json element according to the gui definition language specification
     * @return model instance
     * @throws DefinitionFormatException if the given json element is not valid according to the gui definition
     *                                   language specification
     */
    public static ProgramInputDefinition fromJsonElement(JsonElement j) throws DefinitionFormatException {
        Logger.getGlobal().info("[parsing] read program input object");

        if (!j.isJsonObject()) throw new InvalidJsonObjectException(objectName);

        ProgramInputDefinition programInput = new ProgramInputDefinition();
        for (Map.Entry<String, JsonElement> entry : j.getAsJsonObject().entrySet()) {
            programInput.setValueByJsonProperty(entry.getKey(), entry.getValue());
        }

        return programInput;
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
        if (propertyKey.startsWith("@value_input:")) {
            String[] keyParts = propertyKey.split(":", 2);
            this.inputElements.put(keyParts[1], ValueInputDefinition.fromJsonElement(keyParts[1], propertyValue));

        } else if (propertyKey.startsWith("@entity_input:")) {
            String[] keyParts = propertyKey.split(":", 2);
            this.inputElements.put(keyParts[1], EntityInputDefinition.fromJsonElement(keyParts[1], propertyValue));

        } else {
            Logger.getGlobal().warning(String.format("unsupported property '%s' in '%s' object", propertyKey, objectName));
        }
    }

    public Map<String, InputElementDefinition> getInputElements() {
        return this.inputElements;
    }
}
