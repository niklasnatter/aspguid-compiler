package aspguidp.controller.output;

import aspguidp.gui.window.WindowSizeManager;
import aspguidp.service.core.CoreServicePool;
import aspguidp.service.core.asp.SolverMessageManager;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextArea;

/**
 * Controller class of the solver message component of the application. The solver message component contains a text
 * area which displays the non answer set output of the logic program solver. Usually, the non answer set output
 * of an logic program solver is an error message or a parsing error.
 * <p>
 * The component is only shown, if the {@link SolverMessageManager} instance of the application signals, that the
 * execution of the logic program did lead to non answer set output by the logic program solver.
 */
public class SolverMessageController {
    @FXML
    private Node rootNode;
    @FXML
    private TextArea solverMessageArea;

    /**
     * Initialization method which is called when the respective .fxml file is loaded.
     * <p>
     * This method binds the visibility of this component to the respective property of the solver message manager.
     * Also, a handler which updates the application window size on visibility change of the component is
     * initialized. Additionally, a handler which updates the height of the message text area is set up.
     */
    @FXML
    private void initialize() {
        SolverMessageManager solverMessageManager = CoreServicePool.getInstance().getSolverMessageManager();

        // bind text of textarea to property text, adjust textarea height on text change
        this.solverMessageArea.textProperty().bind(solverMessageManager.solverMessageProperty());
        this.solverMessageArea.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) return;
            this.solverMessageArea.setPrefRowCount(newValue.split("\\R").length);
        });

        // show node only if solver message of solver message manager is not null
        // adjust height of window on visibility change
        this.rootNode.managedProperty().bind(this.rootNode.visibleProperty());
        this.rootNode.visibleProperty().bind(solverMessageManager.solverMessageProperty().isNotNull());
        this.rootNode.visibleProperty().addListener((observable, o, n) -> WindowSizeManager.adaptWindowSize());
    }
}
