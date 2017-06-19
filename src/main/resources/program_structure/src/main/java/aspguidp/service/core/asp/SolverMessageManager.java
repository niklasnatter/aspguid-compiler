package aspguidp.service.core.asp;

import javafx.beans.property.ReadOnlyStringProperty;

/**
 * Interface for a solver message manager.
 * <p>
 * A solver message manager is responsible for extracting messages from the output of a logic program solver. The
 * message manager only extracts output of the logic program solver, which is not an answer set. Therefore, a solver
 * message manager is used to extract warnings or errors from the output of a logic program solver.
 * <p>
 * An instance of a solver message manager is passed to an {@link AspSolverService} instance, when the logic
 * program of the solver service is executed. The solver message manager instance provides a observable property which
 * is set to the messages from the logic program solver after execution.
 * <p>
 * Usually, only one solver message manager instance is used in the application.
 */
public interface SolverMessageManager {
    /**
     * Extract the logic program solver messages from the given solver output and set it to the observable property
     * of the solver message manager instance.
     * This method is called by the answer set programming solver service with the output of the solver after the
     * execution of the logic program.
     *
     * @param solverOutput output of the executed logic program solver
     */
    void setPropertiesBySolverOutput(String solverOutput);

    /**
     * @return observable property which contains the message of the logic program solver (default is null).
     */
    ReadOnlyStringProperty solverMessageProperty();
}
