package aspguidc.helper;

import com.google.common.io.Files;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * Helper class which provides static methods regarding to the handling of files.
 */
public class FileHelper {
    /**
     * Delete the given file. If the given file is a directory, all files inside the directory are deleted too.
     *
     * @param file File which is deleted
     */
    public static void deleteFileRecursively(File file) {
        File[] contents = file.listFiles();

        if (contents != null) {
            for (File f : contents) {
                deleteFileRecursively(f);
            }
        }

        file.delete();
    }

    /**
     * Fill the template which is stored in the subdirectory of the program_template directory in the program resources
     * according to the given template path with the given model and write it according to the given template path to
     * the given target directory.
     *
     * @param relativeTemplatePath relative path of the template which is used to access the template and write
     *                             the filled template
     * @param model                model which is used to fill the template
     * @param targetDir            directory in which the filled template is written according to the given template path
     * @throws IOException if an error occurs while accessing or writing the template
     */
    public static void writeTemplateFile(String relativeTemplatePath, JtwigModel model, File targetDir) throws IOException {
        writeTemplateFile(relativeTemplatePath, model, targetDir, relativeTemplatePath);
    }

    /**
     * Fill the template which is stored in the subdirectory of the program_template directory in the program resources
     * according to the given template path with the given model and write it a subdirectory of the given target dir
     * according to the given target path.
     *
     * @param relativeTemplatePath relative path of the template from the program_template directory in the program
     *                             resources
     * @param model                model which is used to fill the template
     * @param targetDir            directory in which the filled template is written according to the given target path
     * @param relativeTargetPath   relative path from the given target dir which is used to write te filled template
     * @throws IOException if an error occurs while accessing or writing the template
     */
    public static void writeTemplateFile(String relativeTemplatePath, JtwigModel model, File targetDir, String relativeTargetPath) throws IOException {
        JtwigTemplate template = JtwigTemplate.classpathTemplate("/program_template/" + relativeTemplatePath + ".twig");
        writeFile(new File(targetDir, relativeTargetPath), template.render(model));
    }

    /**
     * Write the given content to the given file.
     *
     * @param targetFile  file in which the given content is written to
     * @param fileContent content which is written to the given file
     * @throws IOException if an error occurs while writing the content to the given file
     */
    public static void writeFile(File targetFile, CharSequence fileContent) throws IOException {
        Files.createParentDirs(targetFile);
        Files.write(fileContent, targetFile, Charset.defaultCharset());
    }
}
