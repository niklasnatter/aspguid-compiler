package aspguidc.service.generation.writer.output;

import aspguidc.helper.FileHelper;
import aspguidc.model.output.entity.EntityOutputDefinition;
import org.jtwig.JtwigModel;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * Writer class which provides methods for generation source code files which depend on an entity output element of a
 * gui definition.
 * An instance of this class is assigned to a single target directory, which is used to store the generated source
 * code files.
 */
public class EntityOutputTemplateWriter {
    private static final String templateFilePath = "src/main/resources/fxml/output/element/entity_output.fxml";
    private static final String controllerTemplateFilePath = "src/main/java/aspguidp/controller/output/EntityOutputController.java";
    private static final String serviceFactoryTemplateFilePath = "src/main/java/aspguidp/service/output/EntityDataServicePool.java";

    private final File sourceCodeDirectory;

    /**
     * Create a new writer instance with the given directory as output directory.
     *
     * @param sourceCodeDirectory directory which is used to write the generated source code into
     */
    public EntityOutputTemplateWriter(File sourceCodeDirectory) {
        this.sourceCodeDirectory = sourceCodeDirectory;
    }

    /**
     * Generate and write the fxml file and the controller and service pool files for the given entity output definition.
     *
     * @param outputElement entity output element definition which is used for source code generation
     * @return relative path of the generated fxml file
     * @throws IOException
     */
    public String writeEntityOutputElement(EntityOutputDefinition outputElement) throws IOException {
        Logger.getGlobal().info("[generating] write files for entity output element '" + outputElement.getIdentifier() + "'");

        this.writeFxml(outputElement);
        this.writeController(outputElement);
        this.writeServicePool(outputElement);

        return this.getRelativeFxmlPath(outputElement);
    }

    /**
     * Generate and write the fxml file for the given entity output definition.
     *
     * @param outputElement entity output element definition which is used for source code generation
     * @throws IOException
     */
    private void writeFxml(EntityOutputDefinition outputElement) throws IOException {
        String absoluteTargetPath = "src/main/resources/fxml/output/" + this.getRelativeFxmlPath(outputElement);

        JtwigModel model = JtwigModel.newModel()
                .with("title", outputElement.getTitle())
                .with("description", outputElement.getDescription())
                .with("attributes", outputElement.getAttributeElements().values())
                .with("controllerName", this.getControllerName(outputElement));

        FileHelper.writeTemplateFile(templateFilePath, model, this.sourceCodeDirectory, absoluteTargetPath);
    }

    /**
     * @param outputElement entity output definition which is used to generate the relative fxml path
     * @return relative path for the fxml file for the given entity output definition
     */
    private String getRelativeFxmlPath(EntityOutputDefinition outputElement) {
        return String.format("element/%s_entity_output.fxml", outputElement.getIdentifier());
    }

    /**
     * Generate and write the controller source code file for the given entity output definition.
     *
     * @param outputElement entity output element definition which is used for source code generation
     * @throws IOException
     */
    private void writeController(EntityOutputDefinition outputElement) throws IOException {
        String absoluteTargetPath = "src/main/java/aspguidp/controller/output/element/impl/" + this.getControllerName(outputElement) + ".java";

        JtwigModel model = JtwigModel.newModel()
                .with("controllerName", this.getControllerName(outputElement))
                .with("servicePoolName", this.getServicePoolName(outputElement))
                .with("outputCondition", outputElement.getOutputCondition().getConditionAtom());

        FileHelper.writeTemplateFile(controllerTemplateFilePath, model, this.sourceCodeDirectory, absoluteTargetPath);
    }

    /**
     * @param outputElement entity output definition which is used to generate the controller class name
     * @return controller class name for the given entity output definition
     */
    private String getControllerName(EntityOutputDefinition outputElement) {
        String id = outputElement.getIdentifier();
        return id.substring(0, 1).toUpperCase() + id.substring(1) + "EntityOutputController";
    }

    /**
     * Generate and write the service pool source code file for the given entity output definition.
     *
     * @param outputElement entity output element definition which is used for source code generation
     * @throws IOException
     */
    private void writeServicePool(EntityOutputDefinition outputElement) throws IOException {
        String absoluteTargetPath = "src/main/java/aspguidp/service/output/" + this.getServicePoolName(outputElement) + ".java";

        JtwigModel model = JtwigModel.newModel()
                .with("servicePoolName", this.getServicePoolName(outputElement))
                .with("atomRepresentation", outputElement.getAtomRepresentation().getTemplateString())
                .with("guiRepresentation", outputElement.getGuiRepresentation().getTemplateString())
                .with("attributes", outputElement.getAttributeElements());

        FileHelper.writeTemplateFile(serviceFactoryTemplateFilePath, model, this.sourceCodeDirectory, absoluteTargetPath);
    }

    /**
     * @param outputElement entity output definition which is used to generate the service pool class name
     * @return service pool class name for the given entity output definition
     */
    private Object getServicePoolName(EntityOutputDefinition outputElement) {
        String id = outputElement.getIdentifier();
        return id.substring(0, 1).toUpperCase() + id.substring(1) + "EntityDataServicePool";
    }
}
