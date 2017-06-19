package aspguidc.model;

import aspguidc.exception.parsing.DefinitionFormatException;
import aspguidc.exception.parsing.InvalidJsonObjectException;
import aspguidc.exception.parsing.MissingPropertyException;
import aspguidc.model.information.ProgramInformationDefinition;
import aspguidc.model.input.ProgramInputDefinition;
import aspguidc.model.output.ProgramOutputDefinition;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Model for root gui definition object which defines the graphical user interface of a logic program.
 * Instances of this model are created from the gui definition comment content which is stated inside a logic program.
 */
public class GraphicalUserInterfaceDefinition {
    private static final String objectName = "gui-definition-object";

    private ProgramInformationDefinition programInformation;
    private ProgramInputDefinition programInput;
    private ProgramOutputDefinition programOutput;

    private GraphicalUserInterfaceDefinition() {
    }

    /**
     * Create a new gui definition model instance from the given string.
     *
     * @param s string containing the content of the gui definition comment
     * @return model instance
     * @throws DefinitionFormatException if the given string is not valid according to the gui definition language
     *                                   specification.
     */
    public static GraphicalUserInterfaceDefinition fromString(String s) throws DefinitionFormatException {
        try {
            return fromJsonElement(new JsonParser().parse(s));
        } catch (JsonParseException e) {
            String explanation = (e.getCause() != null) ? e.getCause().getMessage() : e.getMessage();
            throw new InvalidJsonObjectException(objectName, explanation);
        }
    }

    /**
     * Create a new gui definition model instance from the given json element.
     * The model is created by extracting each property of the given json element and setting it to the
     * model.
     *
     * @param j valid json element according to the gui definition language specification
     * @return model instance
     * @throws DefinitionFormatException if the given json element is not valid according to the gui definition
     *                                   language specification
     */
    private static GraphicalUserInterfaceDefinition fromJsonElement(JsonElement j) throws DefinitionFormatException {
        Logger.getGlobal().info("[parsing] read gui definition object");

        if (!j.isJsonObject()) throw new InvalidJsonObjectException(objectName);

        List<String> requiredProperties = new ArrayList<>(Arrays.asList("program_information", "program_input", "program_output"));
        GraphicalUserInterfaceDefinition guiDefinition = new GraphicalUserInterfaceDefinition();

        for (Map.Entry<String, JsonElement> entry : j.getAsJsonObject().entrySet()) {
            requiredProperties.remove(entry.getKey());
            guiDefinition.setValueByJsonProperty(entry.getKey(), entry.getValue());
        }

        if (!requiredProperties.isEmpty()) {
            throw new MissingPropertyException(objectName, requiredProperties.get(0));
        }

        return guiDefinition;
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
        if ("program_information".equals(propertyKey)) {
            this.programInformation = ProgramInformationDefinition.fromJsonElement(propertyValue);

        } else if ("program_input".equals(propertyKey)) {
            this.programInput = ProgramInputDefinition.fromJsonElement(propertyValue);

        } else if ("program_output".equals(propertyKey)) {
            this.programOutput = ProgramOutputDefinition.fromJsonElement(propertyValue);

        } else {
            Logger.getGlobal().warning(String.format("unsupported property '%s' in '%s' object", propertyKey, objectName));
        }
    }

    public ProgramInformationDefinition getProgramInformation() {
        return this.programInformation;
    }

    public ProgramInputDefinition getProgramInput() {
        return this.programInput;
    }

    public ProgramOutputDefinition getProgramOutput() {
        return this.programOutput;
    }
}
