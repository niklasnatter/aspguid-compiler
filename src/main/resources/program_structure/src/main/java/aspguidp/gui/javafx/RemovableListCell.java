package aspguidp.gui.javafx;

import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;

/**
 * Custom javafx list cell, which can be used in a javafx list view.
 * The list cell displays an delete icon on the right side of the cell, which enables the deletion of the list cell
 * from the list.
 * <p>
 * Instances of this class are used in the list view of the entity input components of the application
 * ({@link aspguidp.controller.input.element.EntityInputController}).
 *
 * @param <T> type of the items which are managed by the list view in which the custom list cell is used
 */
public class RemovableListCell<T> extends ListCell<T> {
    private final HBox cellNode = new HBox();
    private final Label cellLabel = new Label("");

    /**
     * Create a new removable list cell instance for the list which displays the given list items.
     * The item of the list cell is removed from the given list items, when the delete icon is clicked.
     *
     * @param listItems items of the list view in which the list cell is used. the item of the list cell is removed
     *                  from this list, when the delete icon is clicked.
     */
    public RemovableListCell(ObservableList<T> listItems) {
        super();

        Pane filler = new Pane();
        HBox.setHgrow(filler, Priority.ALWAYS);
        Pane deleteButton = new Pane(new Label("\uf014"));
        deleteButton.getStyleClass().add("list-delete-button");
        deleteButton.setOnMouseClicked(event -> listItems.remove(this.getItem()));
        this.cellNode.getChildren().addAll(this.cellLabel, filler, deleteButton);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void updateItem(T item, boolean empty) {
        super.updateItem(item, empty);
        this.setText(null);
        this.setGraphic(null);

        if (item != null && !empty) {
            this.cellLabel.setText(item.toString());
            this.setGraphic(this.cellNode);
        }
    }
}
