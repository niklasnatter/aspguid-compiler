package aspguidc.exception.compilation;

import org.apache.tools.ant.BuildException;

import java.util.List;

/**
 * Signals that the compilation of the generated source code failed.
 * <p>
 * This exception is thrown in the {@link aspguidc.service.compilation.SourceCodeCompilationService} to signal that
 * the compilation was not successful.
 */
public class CompilationFailedException extends Exception {
    private final BuildException buildException;
    private final List<String> buildLoggerOutput;

    /**
     * Construct a new CompilationFailedException from the given build exception and buildLoggerOutput
     *
     * @param buildException    exception which was thrown by the ant builder
     * @param buildLoggerOutput output of the logger which logged the building process
     */
    public CompilationFailedException(BuildException buildException, List<String> buildLoggerOutput) {
        this.buildException = buildException;
        this.buildLoggerOutput = buildLoggerOutput;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getMessage() {
        if (this.buildLoggerOutput.isEmpty()) return this.buildException.getMessage();
        return this.buildLoggerOutput.get(0);
    }
}
