package aspguidp.service.core.asp;

import aspguidp.service.core.atom.Atom;

import java.io.IOException;
import java.util.Collection;

/**
 * Interface for an answer set programming solver service.
 * <p>
 * An answer set solver programming service is responsible for executing a logic program with an logic program solver.
 * An instance of an answer set programming solver service manages the content of a logic program and provides a method
 * to execute the logic program with a given collection of {@link Atom} instances as input.
 * <p>
 * Usually, only one answer set programming solver service instance is used in the application.
 */
public interface AspSolverService {
    /**
     * Execute the logic program which is managed by the answer set programming solver service instance.
     * Pass the given logic atoms to the logic program as input and store the output of the logic program solver to
     * the given solver message handler and the given answer set manager.
     *
     * @param atoms                logic atoms which are passed to the logic program as input
     * @param solverMessageManager solver message manager to which the output of the logic program solver is forwarded
     * @param answerSetManager     answer set manager to which the output of the logic program solver is forwarded
     * @throws IOException
     */
    void executeLogicProgram(Collection<Atom> atoms, SolverMessageManager solverMessageManager, AnswerSetManager answerSetManager) throws IOException;

    /**
     * @return content of the logic program which is managed by the answer set programming solver service instance
     */
    String getLogicProgramContent();
}
