package aspguidc.service.generation.writer.output;

import aspguidc.helper.FileHelper;
import aspguidc.model.output.value.OutputValueDefinition;
import aspguidc.model.output.value.ValueOutputDefinition;
import org.jtwig.JtwigModel;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Writer class which provides methods for generation source code files which depend on an value output element of a
 * gui definition.
 * An instance of this class is assigned to a single target directory, which is used to store the generated source
 * code files.
 */
public class ValueOutputTemplateWriter {
    private static final String fxmlTemplateFilePath = "src/main/resources/fxml/output/element/value_output.fxml";
    private static final String controllerTemplateFilePath = "src/main/java/aspguidp/controller/output/ValueOutputController.java";
    private static final String serviceFactoryTemplateFilePath = "src/main/java/aspguidp/service/output/ValueDataServicePool.java";

    private final File sourceCodeDirectory;

    /**
     * Create a new writer instance with the given directory as output directory.
     *
     * @param sourceCodeDirectory directory which is used to write the generated source code into
     */
    public ValueOutputTemplateWriter(File sourceCodeDirectory) {
        this.sourceCodeDirectory = sourceCodeDirectory;
    }

    /**
     * Generate and write the fxml file and the controller file for the given value output definition.
     * Additionally generate and write the service pool files of the output value definitions of the value output element.
     *
     * @param outputElement value output definition which is used for source code generation
     * @return relative path of the generated fxml file
     * @throws IOException
     */
    public String writeValueOutputElement(ValueOutputDefinition outputElement) throws IOException {
        Logger.getGlobal().info("[generating] write files for value output element '" + outputElement.getIdentifier() + "'");

        this.writeFxml(outputElement);
        this.writeController(outputElement);
        for (OutputValueDefinition v : outputElement.getValueElements().values()) this.writeServicePool(outputElement, v);

        return this.getRelativeFxmlPath(outputElement);
    }

    /**
     * Generate and write the fxml file for the given value output definition.
     *
     * @param outputElement value output definition which is used for source code generation
     * @throws IOException
     */
    private void writeFxml(ValueOutputDefinition outputElement) throws IOException {
        String absoluteTargetPath = "src/main/resources/fxml/output/" + this.getRelativeFxmlPath(outputElement);

        JtwigModel model = JtwigModel.newModel()
                .with("title", outputElement.getTitle())
                .with("displayFormatParts", outputElement.getGuiRepresentation().getTemplateParts())
                .with("valueElements", outputElement.getValueElements())
                .with("controllerName", this.getControllerName(outputElement));

        FileHelper.writeTemplateFile(fxmlTemplateFilePath, model, this.sourceCodeDirectory, absoluteTargetPath);
    }

    /**
     * @param outputElement value output definition which is used to generate the relative fxml path
     * @return relative path for the fxml file for the given value output definition
     */
    private String getRelativeFxmlPath(ValueOutputDefinition outputElement) {
        return String.format("element/%s_value_output.fxml", outputElement.getIdentifier());
    }

    /**
     * Generate and write the controller source code file for the given value output definition.
     *
     * @param outputElement value output definition which is used for source code generation
     * @throws IOException
     */
    private void writeController(ValueOutputDefinition outputElement) throws IOException {
        String absoluteTargetPath = "src/main/java/aspguidp/controller/output/element/impl/" + this.getControllerName(outputElement) + ".java";

        Map<String, String> servicePoolNames = new HashMap<>();
        for (OutputValueDefinition v : outputElement.getValueElements().values()) {
            servicePoolNames.put(v.getIdentifier(), this.getServicePoolName(outputElement, v));
        }

        JtwigModel model = JtwigModel.newModel()
                .with("controllerName", this.getControllerName(outputElement))
                .with("servicePoolNames", servicePoolNames)
                .with("outputCondition", outputElement.getOutputCondition().getConditionAtom())
                .with("valueElements", outputElement.getValueElements().values());

        FileHelper.writeTemplateFile(controllerTemplateFilePath, model, this.sourceCodeDirectory, absoluteTargetPath);
    }

    /**
     * @param outputElement value output definition which is used to generate the controller class name
     * @return controller class name for the given value output definition
     */
    private String getControllerName(ValueOutputDefinition outputElement) {
        String id = outputElement.getIdentifier();
        return id.substring(0, 1).toUpperCase() + id.substring(1) + "ValueOutputController";
    }

    /**
     * Generate and write the service pool source code file for the output value definition of the value output element.
     *
     * @param outputElement value output definition which is used for source code generation
     * @param valueElement  output value definition of the given value output element which is used for source code
     *                      generation
     * @throws IOException
     */
    private void writeServicePool(ValueOutputDefinition outputElement, OutputValueDefinition valueElement) throws IOException {
        String absoluteTargetPath = "src/main/java/aspguidp/service/output/" + this.getServicePoolName(outputElement, valueElement) + ".java";

        JtwigModel model = JtwigModel.newModel()
                .with("servicePoolName", this.getServicePoolName(outputElement, valueElement))
                .with("valueIdentifier", valueElement.getIdentifier())
                .with("valueName", valueElement.getName())
                .with("atomRepresentation", valueElement.getAtomRepresentation().getTemplateString());

        FileHelper.writeTemplateFile(serviceFactoryTemplateFilePath, model, this.sourceCodeDirectory, absoluteTargetPath);
    }

    /**
     * @param outputElement value output element which is used to generate service pool class name
     * @param valueElement  output value definition of the given value output element which is used to generate service
     *                      pool class name
     * @return service pool class name for the given output value definition of the given value output element
     */
    private String getServicePoolName(ValueOutputDefinition outputElement, OutputValueDefinition valueElement) {
        String outputId = outputElement.getIdentifier();
        outputId = outputId.substring(0, 1).toUpperCase() + outputId.substring(1);
        String elementId = valueElement.getIdentifier();
        elementId = elementId.substring(0, 1).toUpperCase() + elementId.substring(1);
        return outputId + elementId + "ValueDataServicePool";
    }
}
