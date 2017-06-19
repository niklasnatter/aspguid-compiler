package aspguidp.service.data.manager;

import aspguidp.service.core.atom.AtomConsumer;
import aspguidp.service.core.atom.AtomSupplier;

/**
 * Interface for a data manager.
 * <p>
 * A data manager is responsible for managing an arbitrary number of {@link aspguidp.service.data.model.Entity} instances.
 * This is used by the input and output components of the application, to store access the data which is inputted by
 * the user or outputted to the user ({@link aspguidp.controller.input.element.InputElementController}, {@link aspguidp.controller.output.element.OutputElementController}).
 * <p>
 * The data manager interface extends the {@link AtomSupplier} interface and the {@link AtomConsumer} interface.
 * This enables to register the data manager of an input component as an atom supplier to the input atom hub of the
 * application ({@link aspguidp.service.core.atom.AtomHub}). Therefore, the entities which are managed by the data manager
 * are passed to the logic program solver on logic program execution.
 * Also, this enables to register the data manager of an output component as an atom consumer to the output atom hub
 * of the application. Therefore, the atoms of the currently displayed answer set are passed to the data manager
 * and can be outputted to the user in the respective output component.
 */
public interface DataManager extends AtomSupplier, AtomConsumer {
}
