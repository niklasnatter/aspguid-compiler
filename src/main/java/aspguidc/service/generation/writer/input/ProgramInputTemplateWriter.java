package aspguidc.service.generation.writer.input;

import aspguidc.helper.FileHelper;
import aspguidc.model.input.InputElementDefinition;
import aspguidc.model.input.ProgramInputDefinition;
import aspguidc.model.input.entity.EntityInputDefinition;
import aspguidc.model.input.value.ValueInputDefinition;
import org.jtwig.JtwigModel;

import java.io.File;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Writer class which provides methods for generating source code files which depend on the program input section
 * of an gui definition.
 * An instance of this class is assigned to a single target directory, which is used to store the generated source
 * code files.
 */
public class ProgramInputTemplateWriter {
    private final File sourceCodeDirectory;
    private final ValueInputTemplateWriter valueInputWriter;
    private final EntityInputTemplateWriter entityInputWriter;

    /**
     * Create a new writer instance with the given directory as output directory.
     *
     * @param sourceCodeDirectory directory which is used to write the generated source code into
     */
    public ProgramInputTemplateWriter(File sourceCodeDirectory) {
        this.sourceCodeDirectory = sourceCodeDirectory;
        this.valueInputWriter = new ValueInputTemplateWriter(sourceCodeDirectory);
        this.entityInputWriter = new EntityInputTemplateWriter(sourceCodeDirectory);
    }

    /**
     * Generate an write the source code files for the given program input definition.
     * This method first generates the source code files for all input elements in the given program input definition,
     * then generates program input fxml file which contains the paths to the generated input elements.
     *
     * @param programInput program input definition which is used for source code generation
     * @throws IOException
     */
    public void writeProgramInput(ProgramInputDefinition programInput) throws IOException {
        Logger.getGlobal().info("[generating] write program input files");

        Map<String, String> fxmlPathTypeMap = new LinkedHashMap<>(); // use linked map to keep order

        // generate source code for input elements
        for (InputElementDefinition inputElement : programInput.getInputElements().values()) {
            Map.Entry<String, String> elementPathTypeEntry = this.writeInputElement(inputElement);
            fxmlPathTypeMap.put(elementPathTypeEntry.getKey(), elementPathTypeEntry.getValue());
        }

        // generate fxml including the input element fxml paths
        JtwigModel model = JtwigModel.newModel().with("inputElementFxmlPaths", fxmlPathTypeMap);
        FileHelper.writeTemplateFile("src/main/resources/fxml/input/program_input.fxml", model, this.sourceCodeDirectory);
    }

    /**
     * Generate and write the source code files for the given input element definition by calling the respective
     * writer for the input element type.
     *
     * @param inputElement input element definition which is used for source code generation
     * @return tuple containing the relative path of the generated fxml file and the type of the input element
     * @throws IOException
     */
    private Map.Entry<String, String> writeInputElement(InputElementDefinition inputElement) throws IOException {
        if (inputElement instanceof ValueInputDefinition) {
            String path = this.valueInputWriter.writeValueInputElement((ValueInputDefinition) inputElement);
            return new AbstractMap.SimpleEntry<>(path, "value");
        } else if (inputElement instanceof EntityInputDefinition) {
            String path = this.entityInputWriter.writeEntityInputElement((EntityInputDefinition) inputElement);
            return new AbstractMap.SimpleEntry<>(path, "entity");
        }

        throw new IllegalArgumentException("invalid type of input element while generating source code");
    }

}
