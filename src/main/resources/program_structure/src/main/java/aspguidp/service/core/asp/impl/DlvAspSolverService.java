package aspguidp.service.core.asp.impl;

import aspguidp.Main;
import aspguidp.helper.AtomHelper;
import aspguidp.service.core.asp.AnswerSetManager;
import aspguidp.service.core.asp.AspSolverService;
import aspguidp.service.core.asp.SolverMessageManager;
import aspguidp.service.core.atom.Atom;

import java.io.*;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Implementation of the {@link AspSolverService} interface which implements the execution of the dlv logic program
 * solver.
 */
public class DlvAspSolverService implements AspSolverService {
    private final String logicProgramContent;

    /**
     * Create a new solver service instance for the logic program which can be accessed in the program resources with
     * the given path.
     *
     * @param logicProgramPath path of the logic program which is managed by the created solver service instance
     */
    public DlvAspSolverService(String logicProgramPath) {
        InputStream programFileStream = Main.class.getResourceAsStream(logicProgramPath);
        BufferedReader programFileReader = new BufferedReader(new InputStreamReader(programFileStream));
        this.logicProgramContent = programFileReader.lines().collect(Collectors.joining("\n"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void executeLogicProgram(Collection<Atom> atoms, SolverMessageManager solverMessageManager, AnswerSetManager answerSetManager) throws IOException {
        // instantiate dlv process
        ProcessBuilder pb = new ProcessBuilder("dlv", "-silent", "--");
        Process p = pb.start();

        // write program content
        OutputStreamWriter osw = new OutputStreamWriter(p.getOutputStream());
        osw.write(this.logicProgramContent);
        osw.write(AtomHelper.getProgramCode(atoms));
        osw.close(); // no need to flush() manually, close() will do it automatically

        // collect dlv output
        BufferedReader stdoutReader = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String stdoutContent = stdoutReader.lines().collect(Collectors.joining("\n"));
        BufferedReader stderrReader = new BufferedReader(new InputStreamReader(p.getErrorStream()));
        String stderrContent = stderrReader.lines().collect(Collectors.joining("\n"));
        String solverOutput = stdoutContent + stderrContent;

        // write to managers
        solverMessageManager.setPropertiesBySolverOutput(solverOutput);
        answerSetManager.setPropertiesBySolverOutput(solverOutput);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getLogicProgramContent() {
        return this.logicProgramContent;
    }
}
