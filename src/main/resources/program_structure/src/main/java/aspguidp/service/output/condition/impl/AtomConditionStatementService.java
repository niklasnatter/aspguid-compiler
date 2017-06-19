package aspguidp.service.output.condition.impl;

import aspguidp.exception.InvalidAtomRepresentationException;
import aspguidp.service.core.CoreServicePool;
import aspguidp.service.core.atom.Atom;
import aspguidp.service.core.atom.AtomConsumer;
import aspguidp.service.core.atom.impl.SimpleAtom;
import aspguidp.service.output.condition.ConditionStatementService;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.util.Collection;

/**
 * Implementation of the {@link ConditionStatementService} interface which manages the status of an atom condition
 * statement.
 * <p>
 * An instance of this class is assigned to a condition atom. The instance provides a method to access a observable
 * property, which signals if the condition atom is contained in the currently displayed answer set.
 * <p>
 * This class registers itself as atom consumer to the output atom hub and therefore implements the {@link AtomConsumer}
 * interface ({@link aspguidp.service.core.atom.AtomHub}).
 */
public class AtomConditionStatementService implements ConditionStatementService, AtomConsumer {
    private final Atom conditionAtom;
    private final BooleanProperty fulfilledProperty = new SimpleBooleanProperty(false);

    /**
     * Create a new service instance which manages the status of the given atom.
     *
     * @param atomString string representation of the atom which will be managed by the service
     */
    public AtomConditionStatementService(String atomString) {
        CoreServicePool.getInstance().getOutputAtomHub().registerAtomConsumer(this);

        // try parse atom, print exception if wrong format
        Atom tempAtom = null;
        try { tempAtom = (!atomString.isEmpty()) ? SimpleAtom.fromStringRepresentation(atomString) : null;
        } catch (InvalidAtomRepresentationException e) { e.printStackTrace(); }

        this.conditionAtom = tempAtom;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setAtoms(Collection<Atom> atoms) {
        // check if the condition atom of the service is contained in the atoms of the output atom hub
        this.fulfilledProperty.set(this.conditionAtom == null || atoms.contains(this.conditionAtom));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ReadOnlyBooleanProperty fulfilledProperty() {
        return this.fulfilledProperty;
    }
}
