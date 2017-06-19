package aspguidp.controller.output.element;

import aspguidp.service.data.DataServicePool;
import aspguidp.service.data.model.Entity;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;

import java.util.Comparator;

/**
 * Base controller class for an entity output component. This class implements functionality which is used by all
 * entity output components of the application.
 */
public abstract class EntityOutputController extends OutputElementController {
    /**
     * Initialize the given list view to display the entities of the entity data manager of the given data service pool.
     * The list is set up to display the entities in an alphabetically ascending order.
     *
     * @param dataServicePool service pool which is used to access the entity data manager which holds the entities
     * @param entityList      list which is set up to display the entities
     */
    protected void initializeListView(DataServicePool dataServicePool, ListView<Entity> entityList) {
        // set list items and selection mode
        ObservableList<Entity> entityItems = dataServicePool.getEntityDataManager().getEntities();
        entityList.setItems(new SortedList<>(entityItems, Comparator.comparing(Entity::getDisplayRepresentation)));
        entityList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }
}
