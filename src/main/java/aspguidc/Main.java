package aspguidc;

import aspguidc.exception.compilation.CompilationFailedException;
import aspguidc.exception.input.InvalidProgramArgumentsException;
import aspguidc.exception.parsing.DefinitionFormatException;
import aspguidc.helper.LoggingHelper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.FileAlreadyExistsException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    /**
     * Entry point of the program.
     * <p>
     * This method sets up the logger which is used for program status output, parses the given program arguments,
     * sets up a compiler object for the given input file argument and executes the logic of the compiler for the given
     * program mode argument.
     * <p>
     * If a not recoverable error happens during logic execution, the exception is catched in this method and
     * a respective error message is outputted.
     *
     * @param args program arguments which were passed to the program on execution
     */
    public static void main(String[] args) {
        // set logging configuration
        LoggingHelper.initializeLoggerConfiguration();

        try {
            // parse arguments
            if (args.length != 2) throw new InvalidProgramArgumentsException();
            ProgramMode programMode = ProgramMode.fromOption(args[0]);
            File inputFile = new File(args[1]);

            // execute logic
            AspGuiDC aspguidc = new AspGuiDC(inputFile);
            if (programMode.equals(ProgramMode.Generate)) aspguidc.generate();
            else if (programMode.equals(ProgramMode.Compile)) aspguidc.compile();
            else if (programMode.equals(ProgramMode.Execute)) aspguidc.execute();

        } catch (InvalidProgramArgumentsException e) {
            Logger.getGlobal().info(usage());
        } catch (DefinitionFormatException e) {
            Logger.getGlobal().severe(e.getMessage());
            System.exit(1);
        } catch (CompilationFailedException e) {
            Logger.getGlobal().severe("compilation failed: '" + e.getMessage() + "'");
            System.exit(1);
        } catch (FileNotFoundException e) {
            Logger.getGlobal().severe("file/directory not found: '" + e.getMessage() + "'");
            System.exit(1);
        } catch (FileAlreadyExistsException e) {
            Logger.getGlobal().severe("file/directory already exists: '" + e.getMessage() + "'");
            System.exit(1);
        } catch (IOException e) {
            Logger.getGlobal().log(Level.SEVERE, "unexpected io exception:", e);
            System.exit(1);
        }
    }

    /**
     * @return string containing the usage description for the program.
     */
    private static String usage() {
        String programName = "aspguidc";
        try {
            // try set program name to current executable
            File f = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
            if (f.isFile()) programName = f.getName();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        String usage = String.format("USAGE: %s {-g|-c|-e} input_file", programName);
        usage = usage + "\n  -g  generate java source code for the annotated asp encoding";
        usage = usage + "\n  -c  compile annotated asp encoding into executable .jar";
        usage = usage + "\n  -e  execute annotated asp encoding";
        return usage;
    }

    /**
     * Enum representing the available program modes.
     */
    private enum ProgramMode {
        Generate, Compile, Execute;

        /**
         * Return the program mode enum for the given program option string.
         *
         * @param option option string which was passed to the program as argument
         * @return program mode enum
         * @throws InvalidProgramArgumentsException if the given option string does not match a program mode
         */
        public static ProgramMode fromOption(String option) throws InvalidProgramArgumentsException {
            if ("-g".equals(option)) {
                return Generate;
            } else if ("-c".equals(option)) {
                return Compile;
            } else if ("-e".equals(option)) {
                return Execute;
            } else {
                throw new InvalidProgramArgumentsException();
            }
        }
    }
}
