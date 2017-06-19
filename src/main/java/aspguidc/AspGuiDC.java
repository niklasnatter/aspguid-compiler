package aspguidc;

import aspguidc.exception.compilation.CompilationFailedException;
import aspguidc.exception.parsing.DefinitionFormatException;
import aspguidc.helper.FileHelper;
import aspguidc.helper.JarHelper;
import aspguidc.model.GraphicalUserInterfaceDefinition;
import aspguidc.service.compilation.SourceCodeCompilationService;
import aspguidc.service.generation.SourceCodeGenerationService;
import aspguidc.service.parsing.DefinitionParsingService;
import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;

/**
 * Answer Set Programming GUI Definition Compiler
 * <p>
 * An instance of this class manages a single answer set program which is annotated with a gui definition.
 * An instance of this class provides methods for generating the source code, compiling an executable program and
 * execute a program respective to the assigned annotated logic program.
 * <p>
 * An object of this class is created on program start with the input file from the program arguments.
 * This object represents the central control point of the program.
 */
public class AspGuiDC {
    private final File logicProgramFile;
    private final File outputDir;
    private final String jarFileName;
    private final String sourceCodeDirName;
    private final GraphicalUserInterfaceDefinition parsedGuiDefinition;

    /**
     * Create a new gui definition compiler for the given logic program file.
     *
     * @param logicProgramFile
     * @throws IOException               if an error occurs while accessing the given logic program file
     * @throws DefinitionFormatException if the given logic program file does not contain a valid gui definition
     *                                   according to the gui definition language specification.
     */
    public AspGuiDC(File logicProgramFile) throws IOException, DefinitionFormatException {
        this.logicProgramFile = logicProgramFile.getAbsoluteFile();
        this.outputDir = this.logicProgramFile.getParentFile();
        this.jarFileName = this.logicProgramFile.getName() + ".jar";
        this.sourceCodeDirName = this.logicProgramFile.getName() + "-source";

        DefinitionParsingService parsingService = new DefinitionParsingService();
        this.parsedGuiDefinition = parsingService.parseGuiDefinition(this.logicProgramFile);
    }

    /**
     * Generate the program source code for the annotated logic program which is assigned to the compiler instance.
     * The source code is generated in a new directory inside the directory of the assigned logic program.
     *
     * @throws IOException
     */
    public void generate() throws IOException {
        // generate code in output directory
        File sourceCodeTargetDir = new File(this.outputDir, this.sourceCodeDirName);
        SourceCodeGenerationService generationService = new SourceCodeGenerationService(sourceCodeTargetDir);
        generationService.generateSourceCode(this.parsedGuiDefinition, this.logicProgramFile, this.jarFileName);
    }

    /**
     * Compile an executable jar file for the annotated logic program which is assigned to the compiler instance.
     * The executable jar file is compiled into the directory of the assigned logic program.
     *
     * @throws IOException
     * @throws CompilationFailedException if an error occurs while compiling the executable jar
     */
    public void compile() throws IOException, CompilationFailedException {
        // generate code in temp directory
        File sourceCodeTargetDir = Files.createTempDir();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> FileHelper.deleteFileRecursively(sourceCodeTargetDir)));
        SourceCodeGenerationService generationService = new SourceCodeGenerationService(sourceCodeTargetDir);
        generationService.generateSourceCode(this.parsedGuiDefinition, this.logicProgramFile, this.jarFileName);

        // compile into output directory
        SourceCodeCompilationService compilationService = new SourceCodeCompilationService(sourceCodeTargetDir);
        compilationService.buildJarFile(this.outputDir, this.jarFileName);
    }

    /**
     * Execute the program for the annotated logic program which is assigned to the compiler instance.
     *
     * @throws IOException
     * @throws CompilationFailedException if an error occurs while compiling the program which is executed
     */
    public void execute() throws IOException, CompilationFailedException {
        // generate code in temp directory
        File sourceCodeTargetDir = Files.createTempDir();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> FileHelper.deleteFileRecursively(sourceCodeTargetDir)));
        SourceCodeGenerationService generationService = new SourceCodeGenerationService(sourceCodeTargetDir);
        generationService.generateSourceCode(this.parsedGuiDefinition, this.logicProgramFile, this.jarFileName);

        // compile jar into temp directory
        File jarFileDir = Files.createTempDir();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> FileHelper.deleteFileRecursively(jarFileDir)));
        SourceCodeCompilationService compilationService = new SourceCodeCompilationService(sourceCodeTargetDir);
        File jarFile = compilationService.buildJarFile(jarFileDir, this.jarFileName);

        // execute jar
        JarHelper.executeJarFile(jarFile);
    }
}
