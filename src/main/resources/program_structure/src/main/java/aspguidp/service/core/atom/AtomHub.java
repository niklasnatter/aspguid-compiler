package aspguidp.service.core.atom;

import java.util.Collection;

/**
 * Interface for an atom hub.
 * <p>
 * An atom hub is responsible for passing atoms through the application.
 * Therefore, {@link AtomSupplier} instances and {@link AtomConsumer} instances can register themselves to an instance
 * of the atom hub. The atom hub instance then provides a method to get all atoms from the registered atom suppliers
 * and a method to set a collection of atoms to the registered atom consumers.
 * Usually, the application uses two atom hub instances; an input atom hub and an output atom hub.
 * <p>
 * The input atom hub is used to set and access the atoms which are passed to the logic program as input.
 * Every input component of the application registers its data manager as an atom supplier to the input atom hub
 * ({@link aspguidp.controller.input.element.InputElementController}, {@link aspguidp.service.data.manager.DataManager}).
 * When the logic program is executed, the atoms of these data managers are passed as input to the logic program.
 * Additionally, the data managers of the input components of the applications are registered as atom consumers to
 * the input atom hub. This enables to set the data of the input components when a problem instance is loaded.
 * <p>
 * The output atom hub is used to set and access the atoms of the currently displayed answer set.
 * The {@link aspguidp.controller.output.ProgramOutputController} instance of the application registers itself as atom
 * provider to the output atom hub and provides the atoms of the currently displayed answer set.
 * Additionally, if the currently displayed answer set is changed, the atoms of new displayed answer set is set to the
 * consumers of the output atom hub.
 * Also, every output component of the application registers its data manager as an atom consumer to the atom output
 * hub ({@link aspguidp.controller.output.element.OutputElementController}, {@link aspguidp.service.data.manager.DataManager}).
 * So when the currently displayed answer set by the output controller, the atoms of the answer set are passed to the
 * data managers of the output components.
 */
public interface AtomHub {
    /**
     * Register an atom supplier to the atom hub instance.
     *
     * @param atomSupplier atom supplier which is registered.
     */
    void registerAtomSupplier(AtomSupplier atomSupplier);

    /**
     * Register an atom consumer to the atom hub instance.
     *
     * @param atomConsumer atom consumer which is registered
     */
    void registerAtomConsumer(AtomConsumer atomConsumer);

    /**
     * @return Collection of atoms which contains all atoms which are provided by the registered atom providers of the
     * atom hub instance.
     */
    Collection<Atom> getAtomsFromSuppliers();

    /**
     * Set the given collection of atoms to the registered consumers of the atom hub instance.
     *
     * @param atoms collection of atoms which is set to the registered consumers
     */
    void setAtomsToConsumers(Collection<Atom> atoms);
}
