package aspguidp.controller.input.element;

import javafx.scene.Node;

/**
 * Base controller class for an input component. This class implements basic functionality which is used by all input
 * components of the application.
 */
public abstract class InputElementController {
    /**
     * Initialize a handler which sets the css class 'imitate-focus' to the given background field, when the given
     * input field is focused. Additionally, this method sets the background field as not focus traversable.
     * <p>
     * This method is used to initialize validated input fields which are used in the input components. These
     * validated input fields are constructed by stacking up a input field on a background field. The styling of the
     * input field is set as transparent by css, so that the user only sees the background field.
     * When the content of the input field is changed, the background field is used for validation styling.
     * <p>
     * This enables a more flexible way of validation styling, as the field which is changed by the user is not
     * changed. For example, this enables to adjust the prompt text of the background field while the user is typing
     * in the input field.
     *
     * @param inputField      text field which is used for user input
     * @param backgroundField background field to which the css class is set if the given input field is focused
     */
    protected void initializeFocusPropagationHandler(Node inputField, Node backgroundField) {
        backgroundField.setFocusTraversable(false);
        inputField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) backgroundField.getStyleClass().add("imitate-focus");
            else backgroundField.getStyleClass().remove("imitate-focus");
        });
    }
}
