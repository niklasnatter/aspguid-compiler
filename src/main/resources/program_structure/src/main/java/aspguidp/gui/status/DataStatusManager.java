package aspguidp.gui.status;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Manager class for the data status of the application.
 * The data status of the application has two possible values. The value true means, that input components of the
 * application ({@link aspguidp.controller.input.element.InputElementController}) do hold input data. The value false
 * means, that no data was inputted into the input components of the application.
 * <p>
 * This status is used to update the clickability of the problem instance save button of the application
 * ({@link aspguidp.controller.input.InputActionController}). The button is clickable, if the data status is true, which
 * means that the input component of the application are holding data.
 */
public class DataStatusManager {
    private static final Collection<ObservableValue<Boolean>> elementContainsDataValues = new ArrayList<>();
    private static final BooleanProperty anyElementContainsDataProperty = new SimpleBooleanProperty(false);

    /**
     * Register a new component data status. This method is called by every input component, to register its
     * own data status. The application data status is set to true, if at least one registered component data
     * status is true.
     *
     * @param containsDataStatusValue data status of the component
     */
    public static void registerElementContainsDataStatus(ObservableValue<Boolean> containsDataStatusValue) {
        elementContainsDataValues.add(containsDataStatusValue);
        containsDataStatusValue.addListener((observable, oldValue, newValue) -> updateAnyElementContainsDataProperty());
        updateAnyElementContainsDataProperty();
    }

    /**
     * @return observable property which holds the data status of the application.
     */
    public static ReadOnlyBooleanProperty anyElementContainsDataProperty() {
        return anyElementContainsDataProperty;
    }

    /**
     * Update the application data status. The application data status is set to true, if at least one registered
     * component data status is true.
     */
    private static void updateAnyElementContainsDataProperty() {
        anyElementContainsDataProperty.set(elementContainsDataValues.stream().anyMatch(ObservableValue::getValue));
    }
}
