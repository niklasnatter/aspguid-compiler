package aspguidp.gui.status;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Manager class for the execution status of the application.
 * The execution status of the application has two possible values. The value true means, that all input components
 * of the application ({@link aspguidp.controller.input.element.InputElementController}) are ready for execution of the
 * logic program. The value false means, that at leas one input component of the application is not ready for execution
 * of the logic program.
 * <p>
 * An input component is not ready for the execution of the logic program, if the constraints of the component are
 * not fulfilled (for example: if the data of an value input component is not valid).
 * <p>
 * This status is used to update the clickability of the run button of the application
 * ({@link aspguidp.controller.input.InputActionController}). The button is clickable, if the execution status is true,
 * which means that all input components of the application
 * are ready for execution of the logic program.
 */
public class ExecutionStatusManager {
    private static final Collection<ObservableValue<Boolean>> elementReadyValues = new ArrayList<>();
    private static final BooleanProperty allElementsReadyProperty = new SimpleBooleanProperty(false);

    /**
     * Register a new component execution status. This method is called by every input component, to register its own
     * execution status. The application execution status is set to true, if all registered component execution
     * status are true.
     *
     * @param readyToExecuteStatusValue execution status of the component
     */
    public static void registerElementReadyStatus(ObservableValue<Boolean> readyToExecuteStatusValue) {
        elementReadyValues.add(readyToExecuteStatusValue);
        readyToExecuteStatusValue.addListener((observable, oldValue, newValue) -> updateAllElementsReadyProperty());
        updateAllElementsReadyProperty();
    }

    /**
     * @return observable property which holds the execution status of the component
     */
    public static ReadOnlyBooleanProperty allElementsReadyProperty() {
        return allElementsReadyProperty;
    }

    /**
     * Update the application execution status. The application execution status is set tu true, if all registered
     * component execution status are true.
     */
    private static void updateAllElementsReadyProperty() {
        allElementsReadyProperty.set(elementReadyValues.stream().allMatch(ObservableValue::getValue));
    }
}
