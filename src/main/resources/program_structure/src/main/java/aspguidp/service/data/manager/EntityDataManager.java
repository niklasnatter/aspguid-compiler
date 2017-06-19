package aspguidp.service.data.manager;

import aspguidp.service.data.model.Entity;
import javafx.collections.ObservableList;

/**
 * Interface for an entity data manager.
 * <p>
 * An entity data manager is responsible for managing a set of {@link Entity} instances. This is used by the entity
 * input and entity output components of the application to store and access the entities which are inputted by the
 * user or outputted to the user ({@link aspguidp.controller.input.element.EntityInputController}, {@link aspguidp.controller.input.element.EntityInputController}).
 */
public interface EntityDataManager extends DataManager {

    /**
     * Add an entity to the entities of the manager by the display representation of the entity.
     * This method is called when an entity input component tries to store the user input to the entity data manager.
     *
     * @param displayRepresentation display representation of the entity which is added to the entities of the manager
     * @return true, if the display representation was successfully converted to an entity and added to the entities
     * of the manager
     */
    boolean addByUserInput(String displayRepresentation);

    /**
     * Remove all managed entities from the manager instance.
     */
    void clear();

    /**
     * @return observable list which contains the entities which are managed by the manager instance.
     */
    ObservableList<Entity> getEntities();
}
