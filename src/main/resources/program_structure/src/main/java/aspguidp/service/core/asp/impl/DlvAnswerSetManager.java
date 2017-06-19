package aspguidp.service.core.asp.impl;

import aspguidp.helper.AtomHelper;
import aspguidp.helper.PatternHelper;
import aspguidp.service.core.asp.AnswerSetManager;
import aspguidp.service.core.atom.Atom;
import javafx.beans.property.*;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Implementation of the {@link AnswerSetManager} interface which extracts answer sets from the output of the dlv logic
 * program solver.
 */
public class DlvAnswerSetManager implements AnswerSetManager {
    private final ObjectProperty<List<List<Atom>>> answerSetsProperty = new SimpleObjectProperty<>(null);
    private final BooleanProperty noValidAnswerSetProperty = new SimpleBooleanProperty(false);

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPropertiesBySolverOutput(String solverOutput) {
        // check if input lead to any answer sets
        // property is set to false on solver error, as string is not empty in that case
        this.noValidAnswerSetProperty.set(solverOutput.trim().isEmpty());

        // get single answer set strings
        List<String> answerSetStrings = new ArrayList<>();
        Matcher m = Pattern.compile(PatternHelper.getAnswerSetPattern()).matcher(solverOutput);
        while (m.find()) answerSetStrings.add(m.group(1));

        // fill answer set list with lists of answer set atoms
        List<List<Atom>> answerSets = new ArrayList<>();
        for (String s : answerSetStrings) {
            List<String> atomStrings = AtomHelper.extractAtomStringsFromAnswerSet(s);
            answerSets.add(AtomHelper.atomsFromStringRepresentations(atomStrings));
        }

        if (!answerSets.isEmpty()) this.answerSetsProperty.set(answerSets);
        else this.answerSetsProperty.set(null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void resetProperties() {
        this.answerSetsProperty.set(null);
        this.noValidAnswerSetProperty.set(false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ReadOnlyObjectProperty<List<List<Atom>>> answerSetsProperty() {
        return this.answerSetsProperty;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ReadOnlyBooleanProperty noValidAnswerSetProperty() {
        return this.noValidAnswerSetProperty;
    }
}
