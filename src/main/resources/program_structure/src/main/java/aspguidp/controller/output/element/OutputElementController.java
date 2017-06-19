package aspguidp.controller.output.element;

import aspguidp.gui.window.WindowSizeManager;
import aspguidp.service.output.condition.ConditionStatementService;
import aspguidp.service.output.condition.impl.AtomConditionStatementService;
import javafx.scene.Node;

/**
 * Base controller class for an output component. This class implements basic functionality which is used by all output
 * components of the application.
 */
public abstract class OutputElementController {
    /**
     * Initialize a handler which sets the visibility of the given root node based on if the given condition atom
     * is contained in the current displayed answer set. This method is used by the output components of the
     * application, to hide/show the output component based on the display condition of the output component.
     * <p>
     * This method creates a new condition service with the given atom and binds the visibility of the root node
     * to the respective property of the condition service. Also, the method sets up a handler which updates the
     * application window size on visibility change.
     *
     * @param conditionAtom string representation of an atom
     * @param rootNode      javafx node which is hidden if the given atom is not contained in the currently displayed answer
     *                      set
     */
    protected void initializeConditionStatementHandler(String conditionAtom, Node rootNode) {
        ConditionStatementService conditionService = new AtomConditionStatementService(conditionAtom);

        // show element only if condition is fulfilled
        // adjust height of window on visibility change
        rootNode.managedProperty().bind(rootNode.visibleProperty());
        rootNode.visibleProperty().bind(conditionService.fulfilledProperty());
        rootNode.visibleProperty().addListener((observable, o, n) -> WindowSizeManager.adaptWindowSize());
    }
}
