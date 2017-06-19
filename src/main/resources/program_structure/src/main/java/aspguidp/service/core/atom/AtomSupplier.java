package aspguidp.service.core.atom;

import java.util.Collection;

/**
 * Interface for an atom supplier.
 * <p>
 * An atom supplier can be registered to an {@link AtomHub} instance. When registered to an atom hub, the get atom
 * method of the atom supplier will be called, if the get atoms from suppliers method of the atom hub is called.
 * <p>
 * Atom suppliers are used in various parts of the program. For example, the data manager instances of the input
 * components of the application are registered as atom suppliers to the input atom hub
 * ({@link aspguidp.controller.input.element.InputElementController}, {@link aspguidp.service.data.manager.DataManager}).
 * Therefore, when the logic program is executed by the {@link aspguidp.service.core.asp.AspSolverService} instance
 * of the application, the atoms of these data managers are passed as input to the logic program.
 */
public interface AtomSupplier {
    /**
     * Method which is called by the atom hub, if the get atoms from consumers method of the atom hub is called.
     *
     * @return collection of atoms which should be supplied by the atom supplier
     */
    Collection<Atom> getAtoms();
}
