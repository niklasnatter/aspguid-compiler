package aspguidp.controller.component;

import aspguidp.service.input.suggestion.SuggestionService;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.util.Collection;
import java.util.List;

/**
 * Controller class for the autocomplete popup content component.
 * The autocomplete popup component is used to display suggestion in a popup below input fields in the input elements
 * of the application ({@link aspguidp.controller.input.element.InputElementController}).
 * <p>
 * An instance of this class is assigned to the content of a single autocomplete popup and manages displayed
 * suggestions for a single input field. The instance is responsible only for managing displayed solutions,
 * update the height of the content of the popup and handle key events inside the popup.
 * <p>
 * The visibility and position of the popup must be managed by the class which creates the popup window. The
 * {@link aspguidp.gui.component.AutocompletePopupAssistant} class provides static methods to initialize an
 * autocomplete popup and manage the visibility and position of the popup for a given input field.
 */
public class AutocompletePopupController {
    private static final KeyEvent ENTER_PRESSED_EVENT = new KeyEvent(KeyEvent.KEY_PRESSED, "", "", KeyCode.ENTER, false, false, false, false);

    @FXML
    private AnchorPane rootNode;
    @FXML
    private ListView<String> suggestionList;

    /**
     * Initialization method which is called when the respective .fxml file is loaded.
     */
    @FXML
    private void initialize() {
        this.suggestionList.setItems(FXCollections.observableArrayList());
    }

    /**
     * Initialize the popup content to display suggestions from the given suggestion manager for the given input field.
     * <p>
     * The suggestions in the given excluded suggestions list are not displayed in the popup, even if the suggestion
     * manager suggests them. This is used to exclude suggestions which are already present in the entity list of
     * an entity input.
     * The list of excluded suggestions is not cloned, but used as a reference. If another class changes the content
     * of this list, the excluded suggestions will be updated in the popup too.
     *
     * @param inputField          input field for which content of the autocomplete popup is initialized
     * @param suggestionService   service which is used to gather suggestions for the current input field content
     * @param excludedSuggestions list of suggestions which should not be displayed in the popup window
     */
    public void initialize(TextField inputField, SuggestionService suggestionService, List<String> excludedSuggestions) {
        this.rootNode.prefWidthProperty().bind(inputField.widthProperty());
        this.initializeSuggestionUpdateHandler(inputField, suggestionService, excludedSuggestions);
        this.initializeHeightHandler();
        this.initializeKeyPressedHandler(inputField);
    }

    /**
     * Initialize the handler which is used to update the displayed suggestions. The initialized handler uses the
     * given suggestion service to gather displayed suggestions.
     * <p>
     * The handler is called when the given input field gets the focus, is clicked by mouse or when the content of the
     * input field changes.
     *
     * @param inputField          input field for which the handler is initialized
     * @param suggestionService   service which is used to gather displayed suggestions
     * @param excludedSuggestions list of suggestion which should not be displayed
     */
    private void initializeSuggestionUpdateHandler(TextField inputField, SuggestionService suggestionService, Collection<String> excludedSuggestions) {
        // update listview items on focus and on textchange
        Runnable suggestionUpdateHandler = () -> {
            if (!inputField.isFocused()) return;

            // get suggestions
            List<String> suggestions = suggestionService.getSuggestions(inputField.getText());
            Boolean containsCurrentInput = suggestions.remove(inputField.getText());
            suggestions.removeAll(excludedSuggestions);
            this.suggestionList.getItems().setAll(suggestions);

            // select first suggestion if input is not a suggestions. scroll to first element
            if (!containsCurrentInput) this.suggestionList.getSelectionModel().selectFirst();
            this.suggestionList.scrollTo(0);

        };
        inputField.focusedProperty().addListener((observable, oldValue, newValue) -> suggestionUpdateHandler.run());
        inputField.textProperty().addListener((observable, oldValue, newValue) -> suggestionUpdateHandler.run());
        inputField.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> suggestionUpdateHandler.run());
    }

    /**
     * Initialize the handler which is used to adjust the size of the popup content, when the count of displayed
     * suggestions is changed. If zero suggestions are displayed, the content of the popup is set as invisible.
     * <p>
     * The handler is called each time the count of displayed suggestions is changed.
     */
    private void initializeHeightHandler() {
        Runnable heightHandler = () -> {
            if (this.suggestionList.getItems().isEmpty()) {
                this.rootNode.setVisible(false);
            } else {
                // use 23 as cell height + 1px padding at bottom and top
                this.suggestionList.setPrefHeight(this.suggestionList.getItems().size() * 23 + 2);
                this.rootNode.autosize();
                this.rootNode.setVisible(true);
            }
        };
        this.suggestionList.getItems().addListener((ListChangeListener<String>) c -> heightHandler.run());
        heightHandler.run();
    }

    /**
     * Initialize the handler which is used to handle key events. The initialized handler sets the text of a suggestion
     * to the given input field, when a suggestion is selected by enter or clicked by mouse. Also, the displayed
     * selections are cleared on ESC press. Key events which are not handled are passed to the key pressed handler
     * of the given input field. This enables to handle events which would be absorbed by the popup (for example TAB).
     *
     * @param inputField input field to which a selected suggestion is set to and to which key events are forwarded
     */
    private void initializeKeyPressedHandler(TextField inputField) {
        this.suggestionList.setOnKeyPressed(e -> {
            if (e.getCode().equals(KeyCode.ENTER) && this.suggestionList.getSelectionModel().getSelectedItem() != null) {
                // set suggestion to textfield
                inputField.setText(this.suggestionList.getSelectionModel().getSelectedItem());
                inputField.end();
            } else if (e.getCode().equals(KeyCode.ESCAPE)) {
                // hide suggestions on escape
                this.suggestionList.getItems().clear();
            } else {
                // forward event to input field
                if (inputField.getOnKeyPressed() != null) inputField.getOnKeyPressed().handle(e);
            }
        });
        this.suggestionList.setOnMouseClicked(event -> this.suggestionList.getOnKeyPressed().handle(ENTER_PRESSED_EVENT));
    }
}
