package aspguidp.service.output.condition;

import javafx.beans.property.ReadOnlyBooleanProperty;

/**
 * Interface for a condition statement service.
 * <p>
 * A condition statement service is responsible to determine if a specific condition statement is fulfilled.
 * A condition statement usually refers to an atom of the answer set. It is fulfilled, if the referred atom is
 * contained in the currently displayed answer set.
 * A condition statement service provides a observable property, which signals if the condition statement is currently
 * fulfilled or not fulfilled.
 * <p>
 * Condition statement service instances are used to show/hide output components of the application
 * ({@link aspguidp.controller.output.element.OutputElementController}), based on the condition atom of the output
 * component.
 */
public interface ConditionStatementService {
    /**
     * @return observable property, which signals if the condition statement of the service is fulfilled.
     */
    ReadOnlyBooleanProperty fulfilledProperty();
}
