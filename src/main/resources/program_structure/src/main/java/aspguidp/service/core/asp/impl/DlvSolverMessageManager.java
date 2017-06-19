package aspguidp.service.core.asp.impl;

import aspguidp.helper.PatternHelper;
import aspguidp.service.core.asp.SolverMessageManager;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Implementation of the {@link SolverMessageManager} interface which extracts messages from the output of the dlv
 * logic program solver.
 */
public class DlvSolverMessageManager implements SolverMessageManager {
    private final StringProperty solverMessageProperty = new SimpleStringProperty(null);

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPropertiesBySolverOutput(String solverOutput) {
        // extract solver output which is not a valid answer set
        String remainingOutput = solverOutput;
        Matcher m = Pattern.compile(PatternHelper.getAnswerSetPattern()).matcher(solverOutput);
        while (m.find()) remainingOutput = remainingOutput.replaceAll(Pattern.quote(m.group()), "");

        if (!remainingOutput.trim().isEmpty()) this.solverMessageProperty.set(remainingOutput.trim());
        else this.solverMessageProperty.set(null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ReadOnlyStringProperty solverMessageProperty() {
        return this.solverMessageProperty;
    }
}
