package aspguidp.gui.component;

import aspguidp.Main;
import aspguidp.controller.component.AutocompletePopupController;
import aspguidp.service.input.suggestion.SuggestionService;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Popup;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

/**
 * Assistant class which provides static methods to set up a autocomplete popup.
 * Autocomplete popups are used to display suggestion in a popup below input fields in the input components
 * of the application ({@link aspguidp.controller.input.element.InputElementController}).
 * <p>
 * This class provides methods to load the popup content from the .fxml file into a node, initialize the popup content
 * controller ({@link AutocompletePopupController}) and setup a handler to manage the popup visibility and position.
 */
public class AutocompletePopupAssistant {
    /**
     * Initialize an autocomplete popup for the given input field. The displayed suggestions in the popup are gathered
     * from the given suggestion service.
     *
     * @param inputField        input field for which content of the autocomplete popup is initialized
     * @param suggestionService service which is used to gather suggestions for the current input field content
     */
    public static void initializeAutocompletePopup(TextField inputField, SuggestionService suggestionService) {
        initializeAutocompletePopup(inputField, suggestionService, Collections.emptyList());
    }

    /**
     * Initialize an autocomplete popup for the given input field. The displayed suggestions in the popup are gathered
     * from the given suggestion service. Suggestions in the given list of excluded suggestions are not displayed
     * in the popup.
     *
     * @param inputField          input field for which content of the autocomplete popup is initialized
     * @param suggestionService   service which is used to gather suggestions for the current input field content
     * @param excludedSuggestions list of suggestions which should not be displayed in the popup window
     */
    public static void initializeAutocompletePopup(TextField inputField, SuggestionService suggestionService, List<String> excludedSuggestions) {
        try {
            // initialize popup content
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/fxml/component/autocomplete_popup.fxml"));
            Node popupContent = loader.load();
            AutocompletePopupController popupController = loader.getController();
            popupController.initialize(inputField, suggestionService, excludedSuggestions);

            // initialize popup
            Popup popup = new Popup();
            popup.setHideOnEscape(false);
            popup.getContent().add(popupContent);

            // handle popup visibility
            initializePopupVisibilityHandler(inputField, popup);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Initialize the popup visibility handling for the given input field and the given autocomplete popup.
     * <p>
     * The popup is displayed below the given input field if the given input field gets focused and is hidden when the
     * given input field looses focus or the position of the given input field on the screen changes.
     * <p>
     * Additionally, the popup visibility is updated when the text content of the given input field changes or
     * the given input field gets clicked by mouse. This ensures, that the popup visibility stays consistent when
     * the popup is hidden because the program window is moved on the screen.
     *
     * @param inputField        input field which is used to setup the handler
     * @param autocompletePopup autocomplete popup which is hidden and shown by the handler.
     */
    private static void initializePopupVisibilityHandler(TextField inputField, Popup autocompletePopup) {
        Consumer<Boolean> popupVisibilityHandler = newVisibility -> {
            if (newVisibility && inputField.isFocused() && !autocompletePopup.isShowing()) {
                // we need to subtract width and size of drop shadow to get the right position
                Bounds b = inputField.localToScreen(inputField.getBoundsInLocal());
                autocompletePopup.show(inputField, b.getMinX() - 12, b.getMaxY() - 12 + 8);
            } else if (!newVisibility && autocompletePopup.isShowing()) {
                autocompletePopup.hide();
            }
        };

        // show popup on focus, mouse click and text change
        inputField.focusedProperty().addListener((observable, o, n) -> popupVisibilityHandler.accept(n));
        inputField.textProperty().addListener(((observable, o, n) -> popupVisibilityHandler.accept(true)));
        inputField.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> popupVisibilityHandler.accept(true));

        // hide popup if input field size changes or the window is moved or resized
        inputField.heightProperty().addListener((observable, o, n) -> popupVisibilityHandler.accept(false));
        inputField.widthProperty().addListener((observable, o, n) -> popupVisibilityHandler.accept(false));
        inputField.sceneProperty().addListener((sceneProperty, oldScene, newScene) -> {
            if (newScene == null) return;
            newScene.windowProperty().addListener((windowProperty, oldWindow, newWindow) -> {
                if (newWindow == null) return;
                newWindow.xProperty().addListener((observable, o, n) -> popupVisibilityHandler.accept(false));
                newWindow.yProperty().addListener((observable, o, n) -> popupVisibilityHandler.accept(false));
                newWindow.heightProperty().addListener((observable, o, n) -> popupVisibilityHandler.accept(false));
                newWindow.widthProperty().addListener((observable, o, n) -> popupVisibilityHandler.accept(false));
            });
        });
    }
}
