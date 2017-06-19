package aspguidc.model.information;

import aspguidc.exception.parsing.DefinitionFormatException;
import aspguidc.exception.parsing.InvalidJsonObjectException;
import aspguidc.exception.parsing.MissingPropertyException;
import aspguidc.helper.JsonHelper;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Model for the program information object of a graphical user interface definition.
 */
public class ProgramInformationDefinition {
    private static final String objectName = "program_information";

    private String name;
    private String description;
    private String author;
    private String version;

    private ProgramInformationDefinition() {
    }

    /**
     * Create a new program information model instance from the given json element.
     * The model is created by extracting each property of the given json element and setting it to the
     * model.
     *
     * @param j valid program information json element according to the gui definition language specification
     * @return model instance
     * @throws DefinitionFormatException if the given json element is not valid according to the gui definition
     *                                   language specification
     */
    public static ProgramInformationDefinition fromJsonElement(JsonElement j) throws DefinitionFormatException {
        Logger.getGlobal().info("[parsing] read program information object");

        if (!j.isJsonObject()) throw new InvalidJsonObjectException(objectName);

        List<String> requiredProperties = new ArrayList<>(Collections.singletonList("name"));
        ProgramInformationDefinition programInformation = new ProgramInformationDefinition();

        for (Map.Entry<String, JsonElement> entry : j.getAsJsonObject().entrySet()) {
            requiredProperties.remove(entry.getKey());
            programInformation.setValueByJsonProperty(entry.getKey(), entry.getValue());
        }

        if (!requiredProperties.isEmpty()) {
            throw new MissingPropertyException(objectName, requiredProperties.get(0));
        }

        return programInformation;
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

        } else if ("author".equals(propertyKey)) {
            JsonHelper.assertPropertyIsString(objectName, propertyKey, propertyValue);
            this.author = propertyValue.getAsJsonPrimitive().getAsString();

        } else if ("version".equals(propertyKey)) {
            JsonHelper.assertPropertyIsString(objectName, propertyKey, propertyValue);
            this.version = propertyValue.getAsJsonPrimitive().getAsString();

        } else {
            Logger.getGlobal().warning(String.format("unsupported property '%s' in '%s' object", propertyKey, objectName));
        }
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public String getAuthor() {
        return this.author;
    }

    public String getVersion() {
        return this.version;
    }
}
