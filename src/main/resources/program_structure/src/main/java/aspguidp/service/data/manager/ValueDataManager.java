package aspguidp.service.data.manager;

import aspguidp.service.data.model.Entity;
import javafx.beans.property.ObjectProperty;

/**
 * Interface for a value data manager.
 * <p>
 * A value data manager is responsible for managing exactly one {@link Entity} instance. This is used by the value
 * input and value output components of the application to set and access the values which are inputted by the user or outputted
 * to the user ({@link aspguidp.controller.input.element.ValueInputController}, {@link aspguidp.controller.output.element.ValueOutputController}).
 */
public interface ValueDataManager extends DataManager {
    /**
     * Set the entity of the value data manager by the display representation of the entity.
     * This method is called when an value input component tries to set the user input to the value data manager.
     *
     * @param displayRepresentation display representation of the entity which is set to the manager
     * @return true, if the display representation was successfully converted to an entity and set to the manager
     */
    boolean setByUserInput(String displayRepresentation);

    /**
     * Clear the managed entity of the manager instance.
     */
    void clear();

    /**
     * @return observable property which contains the entity which is managed by the manager instance
     */
    ObjectProperty<Entity> getValue();
}
