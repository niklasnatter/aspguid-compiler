package aspguidp.service.core.asp;

import aspguidp.service.core.atom.Atom;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyObjectProperty;

import java.util.List;

/**
 * Interface for an answer set manager.
 * <p>
 * An answer set manager is responsible for extracting the answer sets from the output of a logic program solver.
 * <p>
 * An instance of a answer set manager is passed to an {@link AspSolverService} instance, when the logic program
 * of the solver service is executed. The answer set manager instance provides a observable property which is set to
 * the resulting answer sets of the logic program after execution. Additionally, the instance provides a observable
 * property which signals, if the execution of the logic program did not lead to any answer sets.
 * <p>
 * Usually, only one answer set manager instance is used in the application.
 */
public interface AnswerSetManager {
    /**
     * Extract the answer sets from the given solver output and set it to the observable property of the answer set
     * manager instance.
     * This method is called by the answer set programming solver service with the output of the solver after the
     * execution of the logic program.
     *
     * @param solverOutput output of the executed logic program solver
     */
    void setPropertiesBySolverOutput(String solverOutput);

    /**
     * Resets the answer set property and the no valid answer set property of the answer set manager to the default
     * values.
     */
    void resetProperties();

    /**
     * @return observable property which contains the answer sets of the executed logic program. (default is null)
     */
    ReadOnlyObjectProperty<List<List<Atom>>> answerSetsProperty();

    /**
     * @return observable property which signals, if the execution of the logic program lead to no answer sets. (default
     * is false)
     */
    ReadOnlyBooleanProperty noValidAnswerSetProperty();
}
