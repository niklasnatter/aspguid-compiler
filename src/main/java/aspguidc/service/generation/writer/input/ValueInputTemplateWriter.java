package aspguidc.service.generation.writer.input;

import aspguidc.helper.FileHelper;
import aspguidc.model.input.value.InputValueDefinition;
import aspguidc.model.input.value.ValueInputDefinition;
import org.jtwig.JtwigModel;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Writer class which provides methods for generation source code files which depend on an value input element of a
 * gui definition.
 * An instance of this class is assigned to a single target directory, which is used to store the generated source
 * code files.
 */
public class ValueInputTemplateWriter {
    private static final String templateFilePath = "src/main/resources/fxml/input/element/value_input.fxml";
    private static final String controllerTemplateFilePath = "src/main/java/aspguidp/controller/input/ValueInputController.java";
    private static final String serviceFactoryTemplateFilePath = "src/main/java/aspguidp/service/input/ValueDataServicePool.java";

    private final File sourceCodeDirectory;

    /**
     * Create a new writer instance with the given directory as output directory.
     *
     * @param sourceCodeDirectory directory which is used to write the generated source code into
     */
    public ValueInputTemplateWriter(File sourceCodeDirectory) {
        this.sourceCodeDirectory = sourceCodeDirectory;
    }

    /**
     * Generate and write the fxml file and the controller file for the given value input definition.
     * Additionally generate and write the service pool files of the input value definitions of the value input element.
     *
     * @param inputElement value input definition which is used for source code generation
     * @return relative path of the generated fxml file
     * @throws IOException
     */
    public String writeValueInputElement(ValueInputDefinition inputElement) throws IOException {
        Logger.getGlobal().info("[generating] write files for value input element '" + inputElement.getIdentifier() + "'");

        this.writeFxml(inputElement);
        this.writeController(inputElement);
        for (InputValueDefinition v : inputElement.getValueElements().values()) this.writeServicePool(inputElement, v);

        return this.getRelativeFxmlPath(inputElement);
    }

    /**
     * Generate and write the fxml file for the given value input definition.
     *
     * @param inputElement value input definition which is used for source code generation
     * @throws IOException
     */
    private void writeFxml(ValueInputDefinition inputElement) throws IOException {
        String absoluteTargetPath = "src/main/resources/fxml/input/" + this.getRelativeFxmlPath(inputElement);

        JtwigModel model = JtwigModel.newModel()
                .with("title", inputElement.getTitle())
                .with("displayFormatParts", inputElement.getGuiRepresentation().getTemplateParts())
                .with("valueElements", inputElement.getValueElements())
                .with("controllerName", this.getControllerName(inputElement));

        FileHelper.writeTemplateFile(templateFilePath, model, this.sourceCodeDirectory, absoluteTargetPath);
    }

    /**
     * @param inputElement value input definition which is used to generate the relative fxml path
     * @return relative path for the fxml file for the given value input definition
     */
    private String getRelativeFxmlPath(ValueInputDefinition inputElement) {
        return String.format("element/%s_value_input.fxml", inputElement.getIdentifier());
    }

    /**
     * Generate and write the controller source code file for the given value input definition.
     *
     * @param inputElement value input definition which is used for source code generation
     * @throws IOException
     */
    private void writeController(ValueInputDefinition inputElement) throws IOException {
        String absoluteTargetPath = "src/main/java/aspguidp/controller/input/element/impl/" + this.getControllerName(inputElement) + ".java";

        Map<String, String> servicePoolNames = new HashMap<>();
        for (InputValueDefinition v : inputElement.getValueElements().values()) {
            servicePoolNames.put(v.getIdentifier(), this.getServicePoolName(inputElement, v));
        }

        JtwigModel model = JtwigModel.newModel()
                .with("controllerName", this.getControllerName(inputElement))
                .with("servicePoolNames", servicePoolNames)
                .with("valueElements", inputElement.getValueElements().values());

        FileHelper.writeTemplateFile(controllerTemplateFilePath, model, this.sourceCodeDirectory, absoluteTargetPath);
    }

    /**
     * @param inputElement value input definition which is used to generate the controller class name
     * @return controller class name for the given value input definition
     */
    private String getControllerName(ValueInputDefinition inputElement) {
        String id = inputElement.getIdentifier();
        return id.substring(0, 1).toUpperCase() + id.substring(1) + "ValueInputController";
    }

    /**
     * Generate and write the service pool source code file for the input value definition of the value input element.
     *
     * @param inputElement value input definition which is used for source code generation
     * @param valueElement input value definition of the given value input element which is used for source code
     *                     generation
     * @throws IOException
     */
    private void writeServicePool(ValueInputDefinition inputElement, InputValueDefinition valueElement) throws IOException {
        String absoluteTargetPath = "src/main/java/aspguidp/service/input/" + this.getServicePoolName(inputElement, valueElement) + ".java";

        JtwigModel model = JtwigModel.newModel()
                .with("servicePoolName", this.getServicePoolName(inputElement, valueElement))
                .with("valueIdentifier", valueElement.getIdentifier())
                .with("valueName", valueElement.getName())
                .with("valueSource", valueElement.getValueSource().getSourceRelation())
                .with("atomRepresentation", valueElement.getAtomRepresentation().getTemplateString());

        FileHelper.writeTemplateFile(serviceFactoryTemplateFilePath, model, this.sourceCodeDirectory, absoluteTargetPath);
    }

    /**
     * @param inputElement value input element which is used to generate service pool class name
     * @param valueElement input value definition of the given value input element which is used to generate service
     *                     pool class name
     * @return service pool class name for the given input value definition of the given value input element
     */
    private String getServicePoolName(ValueInputDefinition inputElement, InputValueDefinition valueElement) {
        String inputId = inputElement.getIdentifier();
        inputId = inputId.substring(0, 1).toUpperCase() + inputId.substring(1);
        String elementId = valueElement.getIdentifier();
        elementId = elementId.substring(0, 1).toUpperCase() + elementId.substring(1);
        return inputId + elementId + "ValueDataServicePool";
    }
}
