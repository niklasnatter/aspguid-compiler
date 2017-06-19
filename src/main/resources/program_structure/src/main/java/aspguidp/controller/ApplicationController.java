package aspguidp.controller;

import aspguidp.gui.window.WindowSizeManager;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

/**
 * Controller class for the root application node, which holds all displayed interface elements.
 */
public class ApplicationController {
    @FXML
    private VBox rootNode;

    /**
     * Initialization method which is called when the respective .fxml file is loaded.
     * <p>
     * This method registers the root node which contains all displayed elements to the window manager of the
     * application and binds the preferred width of the root node to the current width.
     * <p>
     * The width binding ensures, that the window manager does not shrink the window width when adjusting the window
     * size.
     */
    @FXML
    private void initialize() {
        WindowSizeManager.setRootNode(this.rootNode);

        // bind pref width to current width to keep width of application on adaptwindowsize of windowsizemanager
        this.rootNode.prefWidthProperty().bind(this.rootNode.widthProperty());
    }
}
