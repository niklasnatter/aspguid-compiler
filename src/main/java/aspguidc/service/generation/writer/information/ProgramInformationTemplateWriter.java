package aspguidc.service.generation.writer.information;

import aspguidc.helper.FileHelper;
import aspguidc.model.information.ProgramInformationDefinition;
import org.jtwig.JtwigModel;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * Writer class which provides methods for generating source code files which depend on the program information section
 * of an gui definition.
 * An instance of this class is assigned to a single target directory, which is used to store the generated source
 * code files.
 */
public class ProgramInformationTemplateWriter {
    private static final String templateFilePath = "src/main/resources/fxml/information/program_information.fxml";
    private final File sourceCodeDirectory;

    /**
     * Create a new writer instance with the given directory as output directory.
     *
     * @param sourceCodeDirectory directory which is used to write the generated source code into
     */
    public ProgramInformationTemplateWriter(File sourceCodeDirectory) {
        this.sourceCodeDirectory = sourceCodeDirectory;
    }

    /**
     * Generate and write the program information fxml file to the directory which is assigned to the writer instance.
     *
     * @param programInformation program information definition which is used for source code generation
     * @throws IOException
     */
    public void writeProgramInformation(ProgramInformationDefinition programInformation) throws IOException {
        Logger.getGlobal().info("[generating] write program information files");

        JtwigModel programInfoModel = JtwigModel.newModel()
                .with("name", programInformation.getName())
                .with("author", programInformation.getAuthor())
                .with("version", programInformation.getVersion())
                .with("description", programInformation.getDescription());

        FileHelper.writeTemplateFile(templateFilePath, programInfoModel, this.sourceCodeDirectory);
    }
}
