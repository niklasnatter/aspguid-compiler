package aspguidp.service.core.atom;

import java.util.Collection;

/**
 * Interface for an atom consumer.
 * <p>
 * An atom consumer can be registered to an {@link AtomHub} instance. When registered to an atom hub, the set atoms
 * method of the atom consumer will be called, if the set atoms to consumers method of the atom hub is called.
 * <p>
 * Atom consumers are used in various parts of the program. For example, the data managers of the output components of
 * the application are registered as atom consumers to the output atom hub
 * ({@link aspguidp.controller.output.element.OutputElementController}, {@link aspguidp.service.data.manager.DataManager}).
 * Therefore, when the currently displayed answer set is changed by the
 * {@link aspguidp.controller.output.ProgramOutputController} instance of the application, the atoms of the answer set
 * are passed to the output components.
 */
public interface AtomConsumer {
    /**
     * Method which is called by the atom hub, if the set atoms to consumers method of the atom hub is called.
     *
     * @param atomCollection collection of atoms which should be set to the atom consumer
     */
    void setAtoms(Collection<Atom> atomCollection);
}
