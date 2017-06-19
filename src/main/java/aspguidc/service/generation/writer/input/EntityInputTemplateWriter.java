package aspguidc.service.generation.writer.input;

import aspguidc.helper.FileHelper;
import aspguidc.model.input.entity.EntityInputDefinition;
import org.jtwig.JtwigModel;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * Writer class which provides methods for generation source code files which depend on an entity input element of a
 * gui definition.
 * An instance of this class is assigned to a single target directory, which is used to store the generated source
 * code files.
 */
public class EntityInputTemplateWriter {
    private static final String templateFilePath = "src/main/resources/fxml/input/element/entity_input.fxml";
    private static final String controllerTemplateFilePath = "src/main/java/aspguidp/controller/input/EntityInputController.java";
    private static final String serviceFactoryTemplateFilePath = "src/main/java/aspguidp/service/input/EntityDataServicePool.java";

    private final File sourceCodeDirectory;

    /**
     * Create a new writer instance with the given directory as output directory.
     *
     * @param sourceCodeDirectory directory which is used to write the generated source code into
     */
    public EntityInputTemplateWriter(File sourceCodeDirectory) {
        this.sourceCodeDirectory = sourceCodeDirectory;
    }

    /**
     * Generate and write the fxml file and the controller and service pool files for the given entity input definition.
     *
     * @param inputElement entity input element definition which is used for source code generation
     * @return relative path of the generated fxml file
     * @throws IOException
     */
    public String writeEntityInputElement(EntityInputDefinition inputElement) throws IOException {
        Logger.getGlobal().info("[generating] write files for entity input element '" + inputElement.getIdentifier() + "'");

        this.writeFxml(inputElement);
        this.writeController(inputElement);
        this.writeServicePool(inputElement);

        return this.getRelativeFxmlPath(inputElement);
    }

    /**
     * Generate and write the fxml file for the given entity input definition.
     *
     * @param inputElement entity input element definition which is used for source code generation
     * @throws IOException
     */
    private void writeFxml(EntityInputDefinition inputElement) throws IOException {
        String absoluteTargetPath = "src/main/resources/fxml/input/" + this.getRelativeFxmlPath(inputElement);

        JtwigModel model = JtwigModel.newModel()
                .with("title", inputElement.getTitle())
                .with("description", inputElement.getDescription())
                .with("attributes", inputElement.getAttributeElements().values())
                .with("controllerName", this.getControllerName(inputElement));

        FileHelper.writeTemplateFile(templateFilePath, model, this.sourceCodeDirectory, absoluteTargetPath);
    }

    /**
     * @param inputElement entity input definition which is used to generate the relative fxml path
     * @return relative path for the fxml file for the given entity input definition
     */
    private String getRelativeFxmlPath(EntityInputDefinition inputElement) {
        return String.format("element/%s_entity_input.fxml", inputElement.getIdentifier());
    }

    /**
     * Generate and write the controller source code file for the given entity input definition.
     *
     * @param inputElement entity input element definition which is used for source code generation
     * @throws IOException
     */
    private void writeController(EntityInputDefinition inputElement) throws IOException {
        String absoluteTargetPath = "src/main/java/aspguidp/controller/input/element/impl/" + this.getControllerName(inputElement) + ".java";

        JtwigModel model = JtwigModel.newModel()
                .with("controllerName", this.getControllerName(inputElement))
                .with("servicePoolName", this.getServicePoolName(inputElement))
                .with("minCount", inputElement.getInputCountMin())
                .with("maxCount", inputElement.getInputCountMax());

        FileHelper.writeTemplateFile(controllerTemplateFilePath, model, this.sourceCodeDirectory, absoluteTargetPath);
    }

    /**
     * @param inputElement entity input definition which is used to generate the controller class name
     * @return controller class name for the given entity input definition
     */
    private String getControllerName(EntityInputDefinition inputElement) {
        String id = inputElement.getIdentifier();
        return id.substring(0, 1).toUpperCase() + id.substring(1) + "EntityInputController";
    }

    /**
     * Generate and write the service pool source code file for the given entity input definition.
     *
     * @param inputElement entity input element definition which is used for source code generation
     * @throws IOException
     */
    private void writeServicePool(EntityInputDefinition inputElement) throws IOException {
        String absoluteTargetPath = "src/main/java/aspguidp/service/input/" + this.getServicePoolName(inputElement) + ".java";

        JtwigModel model = JtwigModel.newModel()
                .with("servicePoolName", this.getServicePoolName(inputElement))
                .with("atomRepresentation", inputElement.getAtomRepresentation().getTemplateString())
                .with("guiRepresentation", inputElement.getGuiRepresentation().getTemplateString())
                .with("attributes", inputElement.getAttributeElements());

        FileHelper.writeTemplateFile(serviceFactoryTemplateFilePath, model, this.sourceCodeDirectory, absoluteTargetPath);
    }

    /**
     * @param inputElement entity input definition which is used to generate the service pool class name
     * @return service pool class name for the given entity input definition
     */
    private Object getServicePoolName(EntityInputDefinition inputElement) {
        String id = inputElement.getIdentifier();
        return id.substring(0, 1).toUpperCase() + id.substring(1) + "EntityDataServicePool";
    }
}
