package aspguidc.service.generation.writer.output;

import aspguidc.helper.FileHelper;
import aspguidc.model.output.OutputElementDefinition;
import aspguidc.model.output.ProgramOutputDefinition;
import aspguidc.model.output.entity.EntityOutputDefinition;
import aspguidc.model.output.value.ValueOutputDefinition;
import org.jtwig.JtwigModel;

import java.io.File;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Writer class which provides methods for generating source code files which depend on the program output section
 * of an gui definition.
 * An instance of this class is assigned to a single target directory, which is used to store the generated source
 * code files.
 */
public class ProgramOutputTemplateWriter {
    private final File sourceCodeDirectory;
    private final ValueOutputTemplateWriter valueOutputWriter;
    private final EntityOutputTemplateWriter entityOutputWriter;

    /**
     * Create a new writer instance with the given directory as output directory.
     *
     * @param sourceCodeDirectory directory which is used to write the generated source code into
     */
    public ProgramOutputTemplateWriter(File sourceCodeDirectory) {
        this.sourceCodeDirectory = sourceCodeDirectory;
        this.valueOutputWriter = new ValueOutputTemplateWriter(sourceCodeDirectory);
        this.entityOutputWriter = new EntityOutputTemplateWriter(sourceCodeDirectory);
    }

    /**
     * Generate an write the source code files for the given program output definition.
     * This method first generates the source code files for all output elements in the given program output definition,
     * then generates program output fxml file which contains the paths to the generated output elements.
     *
     * @param programOutput program output definition which is used for source code generation
     * @throws IOException
     */
    public void writeProgramOutput(ProgramOutputDefinition programOutput) throws IOException {
        Logger.getGlobal().info("[generating] write program output files");

        Map<String, String> fxmlPathTypeMap = new LinkedHashMap<>(); // use linked map to keep order

        // generate source code for output elements
        for (OutputElementDefinition outputElement : programOutput.getOutputElements().values()) {
            Map.Entry<String, String> elementPathTypeEntry = this.writeOutputElement(outputElement);
            fxmlPathTypeMap.put(elementPathTypeEntry.getKey(), elementPathTypeEntry.getValue());
        }

        // generate fxml including the output element fxml paths
        JtwigModel model = JtwigModel.newModel().with("outputElementFxmlPaths", fxmlPathTypeMap);
        FileHelper.writeTemplateFile("src/main/resources/fxml/output/program_output.fxml", model, this.sourceCodeDirectory);
    }

    /**
     * Generate and write the source code files for the given output element definition by calling the respective
     * writer for the output element type.
     *
     * @param outputElement output element definition which is used for source code generation
     * @return tuple containing the relative path of the generated fxml file and the type of the output element
     * @throws IOException
     */
    private Map.Entry<String, String> writeOutputElement(OutputElementDefinition outputElement) throws IOException {
        if (outputElement instanceof ValueOutputDefinition) {
            String path = this.valueOutputWriter.writeValueOutputElement((ValueOutputDefinition) outputElement);
            return new AbstractMap.SimpleEntry<>(path, "value");
        } else if (outputElement instanceof EntityOutputDefinition) {
            String path = this.entityOutputWriter.writeEntityOutputElement((EntityOutputDefinition) outputElement);
            return new AbstractMap.SimpleEntry<>(path, "entity");
        }

        throw new IllegalArgumentException("invalid type of output element while generating source code");
    }
}
