package aspguidp.controller.input.element;

import aspguidp.gui.component.AutocompletePopupAssistant;
import aspguidp.gui.status.DataStatusManager;
import aspguidp.gui.status.ExecutionStatusManager;
import aspguidp.service.data.DataServicePool;
import aspguidp.service.data.manager.ValueDataManager;
import aspguidp.service.input.suggestion.SuggestionService;
import aspguidp.service.input.suggestion.impl.TemplateSuggestionService;
import aspguidp.service.input.validation.ValidationService;
import aspguidp.service.input.validation.impl.TemplateValidationService;
import com.sun.javafx.scene.control.skin.BehaviorSkinBase;
import javafx.application.Platform;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

import java.util.function.Consumer;

/**
 * Base controller class for an value input component. This class implements functionality which is used by all value
 * input components of the application.
 */
public abstract class ValueInputController extends InputElementController {
    /**
     * Initialize the given stacked field (which consists of an input field and a background field) with the given
     * data service pool.
     * <p>
     * This method initializes the validation of the stacked field, sets up a handler which updates the value of the
     * field, when the value data manager of the given service pool changes its value and initializes an autocomplete
     * popup for the given stacked field.
     * <p>
     * Additionally, the execution status and the data status of the field is registered to the execution/data status
     * manager of the application and a focus propagation handler is initialized for the stacked field.
     *
     * @param servicePool     service pool which holds the data services which are used to initialize the stacked field
     * @param inputField      input field of the stacked field which is initialized
     * @param backgroundField background field of the stacked field which is initialized
     */
    protected void initializeValueField(DataServicePool servicePool, TextField inputField, TextField backgroundField) {
        this.initializeFieldValidation(servicePool, inputField, backgroundField);

        // update value if changed in manager. this happens when loading a problem instance from a file
        ValueDataManager dataManager = servicePool.getValueDataManager();
        dataManager.getValue().addListener((observable, oldValue, newValue) -> {
            if (inputField.isFocused()) return;
            if (newValue == null) inputField.clear();
            else inputField.setText(newValue.getDisplayRepresentation());
        });

        // initialize autocomplete popup for input field
        ValidationService validationService = new TemplateValidationService(servicePool.getDisplayTemplateParts());
        SuggestionService suggestionService = new TemplateSuggestionService(servicePool.getDisplayTemplateParts(), validationService);
        AutocompletePopupAssistant.initializeAutocompletePopup(inputField, suggestionService);

        // traverse focus on tab key event
        inputField.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.TAB) && inputField.getSkin() instanceof BehaviorSkinBase) {
                // enable traversing to next element when autocomplete popup is opened
                // https://stackoverflow.com/questions/29984025/focus-on-next-item-in-container
                ((BehaviorSkinBase) inputField.getSkin()).getBehavior().traverseNext();
            }
        });

        // register input element status
        ExecutionStatusManager.registerElementReadyStatus(dataManager.getValue().isNotNull());
        DataStatusManager.registerElementContainsDataStatus(dataManager.getValue().isNotNull());

        // propagate focus to background field
        this.initializeFocusPropagationHandler(inputField, backgroundField);
    }

    /**
     * Initialize the validation of the given stacked field (which consists of an input field and a background field)
     * with the given data service pool.
     * <p>
     * This method creates a new validation service and suggestions service for the display template parts of the given
     * service pool and sets up a handler which manages the validation and the setting of data to the value data manager
     * of the service pool.
     * <p>
     * The initialized handler sets the respective validation css class and stores the input data to the value
     * data manager on text change of the given stacked field. Additionally, this method sets the prompt text of the
     * given background field to the prompt text which is gathered from this suggestion service on text change of the
     * input field.
     *
     * @param servicePool     service pool which holds the data services which are used to initialize validation of the
     *                        stacked field
     * @param inputField      input field of the stacked field of which the validation which is initialized
     * @param backgroundField background field of the stacked field of which the validation which is initialized
     */
    private void initializeFieldValidation(DataServicePool servicePool, TextField inputField, TextField backgroundField) {
        ValueDataManager dataManager = servicePool.getValueDataManager();
        ValidationService validationService = new TemplateValidationService(servicePool.getDisplayTemplateParts());
        SuggestionService suggestionService = new TemplateSuggestionService(servicePool.getDisplayTemplateParts(), validationService);

        Consumer<String> textChangeHandler = newValue -> {
            backgroundField.setPromptText(suggestionService.getPromptText(newValue));
            ValidationService.ValidationStatus validationStatus = validationService.validate(newValue);
            backgroundField.getStyleClass().removeAll("validation-invalid", "validation-incomplete", "validation-valid");
            backgroundField.getStyleClass().add(validationStatus.getCssClass());

            if (validationStatus.equals(ValidationService.ValidationStatus.VALID)) {
                dataManager.setByUserInput(newValue);
            } else {
                dataManager.clear();
            }
        };
        inputField.textProperty().addListener((observable, oldValue, newValue) -> textChangeHandler.accept(newValue));

        // validate current input after gui is initialized
        Platform.runLater(() -> textChangeHandler.accept(inputField.getText()));
    }
}
