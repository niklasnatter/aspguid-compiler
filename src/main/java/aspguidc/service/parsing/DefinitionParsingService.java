package aspguidc.service.parsing;

import aspguidc.exception.input.WrongNumberOfGuiCommentsException;
import aspguidc.exception.parsing.DefinitionFormatException;
import aspguidc.helper.PatternHelper;
import aspguidc.model.GraphicalUserInterfaceDefinition;
import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Service class which provides methods regarding to parsing the gui definition of an annotated logic program.
 */
public class DefinitionParsingService {
    /**
     * Parse and return the gui definition from the given annotated logic program file.
     *
     * @param inputFile annotated logic program file which is parsed
     * @return gui definition model respective to the given annotated logic program
     * @throws IOException
     * @throws DefinitionFormatException if the given logic program file does not contain a valid gui definition
     *                                   according to the gui definition language specification.
     */
    public GraphicalUserInterfaceDefinition parseGuiDefinition(File inputFile) throws IOException, DefinitionFormatException {
        Logger.getGlobal().info("[parsing] extract gui definition from file: '" + inputFile.getPath() + "'");

        String fileContent = Files.toString(inputFile, Charset.defaultCharset());
        List<String> guiDefinitionCommentContents = this.extractGuiDefinitionCommentContents(fileContent);
        if (guiDefinitionCommentContents.size() != 1) {
            throw new WrongNumberOfGuiCommentsException(guiDefinitionCommentContents.size());
        }

        return GraphicalUserInterfaceDefinition.fromString(guiDefinitionCommentContents.get(0));
    }

    /**
     * Extract gui definition comment contents from the given file content.
     * A gui definition comment is an asp multiline comment in the format '%*:: _content_ ::*%'
     *
     * @param fileContent content which is used to extract gui definition comment contents from
     * @return list of extracted gui definition comment contents
     */
    private List<String> extractGuiDefinitionCommentContents(CharSequence fileContent) {
        Pattern commentPattern = Pattern.compile(PatternHelper.getGuiDefinitionCommentPattern(), Pattern.DOTALL);
        Matcher guiDefinitionCommentMatcher = commentPattern.matcher(fileContent);
        List<String> contents = new ArrayList<>();
        while (guiDefinitionCommentMatcher.find()) contents.add(guiDefinitionCommentMatcher.group(1).trim());

        return contents;
    }
}
