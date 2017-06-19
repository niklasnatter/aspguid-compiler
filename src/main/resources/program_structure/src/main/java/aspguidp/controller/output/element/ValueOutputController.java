package aspguidp.controller.output.element;

import aspguidp.service.data.DataServicePool;
import aspguidp.service.data.manager.ValueDataManager;
import javafx.scene.control.Label;

/**
 * Base controller class for an value output component. This class implements functionality which is used by all value
 * output components of the application.
 */
public abstract class ValueOutputController extends OutputElementController {
    /**
     * Bind the text of the given label to value of the value data manager of the given data service pool.
     *
     * @param dataServicePool service pool which is used to access the value data manager which holds the value
     * @param outputLabel     label of which the text is bound to the value of the value data manager
     */
    protected void initializeValueLabel(DataServicePool dataServicePool, Label outputLabel) {
        // update label value on manager change
        ValueDataManager dataManager = dataServicePool.getValueDataManager();
        dataManager.getValue().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) outputLabel.setText("");
            else outputLabel.setText(newValue.getDisplayRepresentation());
        });
    }
}
