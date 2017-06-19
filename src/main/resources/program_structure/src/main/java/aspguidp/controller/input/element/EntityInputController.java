package aspguidp.controller.input.element;

import aspguidp.gui.component.AutocompletePopupAssistant;
import aspguidp.gui.javafx.RemovableListCell;
import aspguidp.gui.status.DataStatusManager;
import aspguidp.gui.status.ExecutionStatusManager;
import aspguidp.helper.ObservableHelper;
import aspguidp.service.data.DataServicePool;
import aspguidp.service.data.model.Entity;
import aspguidp.service.data.template.TemplatePart;
import aspguidp.service.input.suggestion.SuggestionService;
import aspguidp.service.input.suggestion.impl.TemplateSuggestionService;
import aspguidp.service.input.validation.ValidationService;
import aspguidp.service.input.validation.impl.TemplateValidationService;
import com.sun.javafx.scene.control.skin.BehaviorSkinBase;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;

import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;

/**
 * Base controller class for an entity input component. This class implements functionality which is used by all entity
 * input components of the application.
 */
public abstract class EntityInputController extends InputElementController {
    /**
     * Initialize an autocomplete popup for the given input field with the given data service pool.
     * <p>
     * This method creates a new suggestion service for the display template parts of the given service pool and
     * initializes an autocomplete popup with this suggestion service.
     * Additionally, the autocomplete popup is initialized in such a way, that suggestions are excluded, if they
     * are already displayed in the list view of the entity input component.
     *
     * @param dataServicePool service pool which holds the data services which are used to initialize the autocomplete
     *                        popup for the given input field
     * @param inputField      input field for which the autocomplete popup is initialized
     */
    protected void initializeInputFieldAutocompletePopup(DataServicePool dataServicePool, TextField inputField) {
        List<TemplatePart> displayTemplateParts = dataServicePool.getDisplayTemplateParts();
        TemplateValidationService validationService = new TemplateValidationService(displayTemplateParts);
        SuggestionService suggestionService = new TemplateSuggestionService(displayTemplateParts, validationService);

        // initialize autocomplete popup for input field, exclude suggestions which are already in list
        ObservableList<Entity> curItems = dataServicePool.getEntityDataManager().getEntities();
        ObservableList<String> excluded = ObservableHelper.observableList(curItems, Entity::getDisplayRepresentation);
        AutocompletePopupAssistant.initializeAutocompletePopup(inputField, suggestionService, excluded);
    }

    /**
     * Initialize a key pressed handler for the given input field, which fires the given input button on an enter
     * key event if the button is not disabled and which traverses the focus to the next input element on an tab
     * key event.
     *
     * @param inputField  input field on which the handler is set up
     * @param inputButton input button which is fired on enter key press if it is not disabled
     */
    protected void initializeInputFieldKeyPressHandler(TextField inputField, Button inputButton) {
        // initialize key pressed handler
        inputField.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER) && !inputButton.isDisable()) {
                // fire add button on enter, if button is enabled
                inputButton.getOnAction().handle(null);
            } else if (event.getCode().equals(KeyCode.TAB) && inputField.getSkin() instanceof BehaviorSkinBase) {
                // enable traversing to next element when autocomplete popup is opened
                // https://stackoverflow.com/questions/29984025/focus-on-next-item-in-container
                ((BehaviorSkinBase) inputField.getSkin()).getBehavior().traverseNext();
            }
        });
    }

    /**
     * Initialize the validation of the given stacked field (which consists of an input field and a background field)
     * of the entity input component with the given service pool.
     * <p>
     * This method creates a new validation service for the display template parts of the given service pool and sets
     * up a handler which sets the respective css class to the given background field on text change of the given
     * input field.
     * Additionally, this method creates a new suggestion service for the template parts of the given service pool
     * and sets the prompt text of the given background field to the prompt text which is gathered from this suggestion
     * service on text change of the input field.
     *
     * @param servicePool     service pool which holds the data services which are used to initialize the validation of
     *                        the given input field
     * @param inputField      input field of the stacked field of which the validation which is initialized
     * @param backgroundField background field of the stacked field of which the validation which is initialized
     */
    protected void initializeInputFieldValidation(DataServicePool servicePool, TextField inputField, TextField backgroundField) {
        List<TemplatePart> displayTemplateParts = servicePool.getDisplayTemplateParts();
        ValidationService validationService = new TemplateValidationService(displayTemplateParts);
        SuggestionService suggestionService = new TemplateSuggestionService(displayTemplateParts, validationService);

        // validate input on textchange
        Consumer<String> textChangeHandler = newValue -> {
            backgroundField.setPromptText(suggestionService.getPromptText(newValue));
            backgroundField.getStyleClass().removeAll("validation-invalid", "validation-incomplete", "validation-valid");
            backgroundField.getStyleClass().add(validationService.validate(newValue).getCssClass());
        };
        inputField.textProperty().addListener((observable, oldValue, newValue) -> textChangeHandler.accept(newValue));

        // validate current input after gui is initialized
        Platform.runLater(() -> textChangeHandler.accept(inputField.getText()));
    }

    /**
     * Initialize the input button of the entity input component with the given service pool and the given stacked
     * field (which consists of an input field and a background field).
     * <p>
     * This method sets up a handler which disables the input button, when the given background field signals that it is
     * not validated as valid. Additionally, a handler is set up, which adds the input of the given input field
     * to the entity data manager of the service pool, when the button is clicked.
     *
     * @param servicePool     service pool which holds the data services which are used to initialize the input button
     * @param inputField      input field of which the value is added to the entity data manager of the given service pool
     * @param backgroundField background field which of which the validation status checked
     * @param inputButton     input button which is initialized
     */
    protected void initializeInputButton(DataServicePool servicePool, TextField inputField, TextField backgroundField, Button inputButton) {
        // activate button if input is valid
        ObservableList<String> fieldStyles = backgroundField.getStyleClass();
        Runnable enableHandler = () -> inputButton.setDisable(!fieldStyles.contains("validation-valid"));
        fieldStyles.addListener((ListChangeListener<String>) c -> enableHandler.run());
        enableHandler.run();

        // fire button on enter press if focused
        inputButton.setOnKeyPressed(e -> { if (e.getCode().equals(KeyCode.ENTER)) inputButton.fire(); });

        // add input to datamanager on buttonclick
        inputButton.setOnAction(event -> {
            servicePool.getEntityDataManager().addByUserInput(inputField.getText());
            inputField.clear();
            inputField.requestFocus();
        });
    }

    /**
     * Initialize the given list view count label of the entity input component with the given service pool and the
     * given min and max entity counts.
     * <p>
     * This method binds the text of the given count label to the count of entities which are displayed in the list
     * view of the entity input component. Additionally, a handler is set up which sets a validation invalid class,
     * if the count of entities displayed in the list view contradicts the given min and max entity count.
     *
     * @param servicePool service pool which holds the data services which are used to initialize the given count label
     * @param countLabel  count label which is initialized
     * @param minCount    minimum valid count of entities for the list view
     * @param maxCount    maximum valid count of entities for the list view
     */
    protected void initializeListViewCountLabel(DataServicePool servicePool, Label countLabel, int minCount, int maxCount) {
        ObservableList<Entity> curEntities = servicePool.getEntityDataManager().getEntities();

        // initialize entity count label below listview
        countLabel.textProperty().bind(Bindings.concat("number of entries: ", Bindings.size(curEntities)));

        // validate mincount and maxcount on item change
        Runnable countValidationHandler = () -> {
            countLabel.getStyleClass().remove("validation-invalid");
            if (curEntities.size() < minCount || curEntities.size() > maxCount) {
                countLabel.getStyleClass().add("validation-invalid");
            }
        };
        curEntities.addListener((ListChangeListener<Entity>) c -> countValidationHandler.run());
        countValidationHandler.run();
    }

    /**
     * Initialize the given list view of an entity input component with the given service pool.
     * <p>
     * This method sets the items of the given list to the entities of the entity data manger of the given service
     * pool. The list is set up to display the entities in an alphabetically ascending order.
     * The value factory of the given list view is set up to use removable list cells.
     * Additionally, a handler is initialized, which removes the currently selected items from the list when the
     * del key is pressed.
     *
     * @param dataServicePool service pool which holds the data services which are used to initialize the list view
     * @param entityList      list view which is initialized
     */
    protected void initializeListView(DataServicePool dataServicePool, ListView<Entity> entityList) {
        ObservableList<Entity> curEntities = dataServicePool.getEntityDataManager().getEntities();

        // set list items, selection mode and cellfactory
        entityList.setItems(new SortedList<>(curEntities, Comparator.comparing(Entity::getDisplayRepresentation)));
        entityList.setCellFactory(param -> new RemovableListCell<>(curEntities));
        entityList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        // delete selected items on keypress
        entityList.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.DELETE)) {
                curEntities.removeAll(entityList.getSelectionModel().getSelectedItems());
            }
        });
    }

    /**
     * Register the execution status and the data status of the entity component to the execution/data service manager
     * of the application.
     * <p>
     * The execution status of an entity input component is set to true, if the count of entities managed by the
     * data manager of the given service pool fulfills the given min and max entity counts.
     * The data status of an entity input component is set to true, if the entity data manager of the given service
     * pool holds at least one entity.
     *
     * @param dataServicePool service pool which holds the data services which are used to determine the execution/data
     *                        status of the entity input component
     * @param inputCountMin   minimum valid count of entities of the entity input component
     * @param inputCountMax   maximum valid count of entities of the entity input component
     */
    protected void registerInputElementStatus(DataServicePool dataServicePool, int inputCountMin, int inputCountMax) {
        ObservableList<Entity> entityItems = dataServicePool.getEntityDataManager().getEntities();

        // set contains data status if entity items are not empty
        DataStatusManager.registerElementContainsDataStatus(Bindings.isEmpty(entityItems).not());

        // set ready status if mincount and maxcount is fulfilled
        BooleanBinding minCount = Bindings.greaterThanOrEqual(Bindings.size(entityItems), inputCountMin);
        BooleanBinding maxCount = Bindings.lessThanOrEqual(Bindings.size(entityItems), inputCountMax);
        ExecutionStatusManager.registerElementReadyStatus(Bindings.and(minCount, maxCount));
    }
}
