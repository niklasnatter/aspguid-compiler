package aspguidp.controller.output;

import aspguidp.gui.window.WindowSizeManager;
import aspguidp.service.core.CoreServicePool;
import javafx.fxml.FXML;
import javafx.scene.Node;

/**
 * Controller class of the empty result message component of the application. The empty result message component
 * contains a message, that the execution of the logic program did not lead to any answer sets.
 * <p>
 * The component is only shown, if the {@link aspguidp.service.core.asp.AnswerSetManager} instance of the application
 * signals, that the execution of the logic program did not lead to any answer sets.
 */
public class EmptyResultMessageController {
    @FXML
    private Node rootNode;

    /**
     * Initialization method which is called when the respective .fxml file is loaded.
     * <p>
     * This method binds the visibility of this component to the respective property of the answer set manager.
     * Additionally, a handler which updates the application window size on visibility change of the component is
     * initialized.
     */
    @FXML
    private void initialize() {
        // show node only if no answer set property of answer set manager is set
        // adjust height of window on visibility change
        this.rootNode.managedProperty().bind(this.rootNode.visibleProperty());
        this.rootNode.visibleProperty().bind(CoreServicePool.getInstance().getAnswerSetManager().noValidAnswerSetProperty());
        this.rootNode.visibleProperty().addListener((observable, o, n) -> WindowSizeManager.adaptWindowSize());
    }
}
