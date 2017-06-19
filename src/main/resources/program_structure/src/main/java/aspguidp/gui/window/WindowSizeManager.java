package aspguidp.gui.window;

import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Manager class which provides methods to adjust the program window size to the current displayed content.
 * <p>
 * The application dynamically adds and removes components from the displayed content (for example; when the logic
 * program is executed, the application adds the logic program output component
 * ({@link aspguidp.controller.output.ProgramOutputController}) to the displayed content to present the results of the
 * logic program).
 * <p>
 * The methods of this class are used to adjust the size of the program window, after a component was added or removed
 * from the displayed content of the window.
 */
public class WindowSizeManager {
    private static Stage primaryStage;
    private static VBox rootNode;

    /**
     * Set the primary stage of the application which represents the program window, of which the size is adjusted.
     *
     * @param primaryStage program window, of which the size is adjusted.
     */
    public static void setPrimaryStage(Stage primaryStage) {
        WindowSizeManager.primaryStage = primaryStage;
    }

    /**
     * Set the root node of the program window, which is used to calculate a suitable size for the program window.
     *
     * @param rootNode node which is used to calculate a suitable size for the program window.
     */
    public static void setRootNode(VBox rootNode) {
        WindowSizeManager.rootNode = rootNode;
    }

    /**
     * Set the min size of the application window to the min size of the window content, to prevent that parts of the
     * window content are hidden because the window size is too small.
     */
    public static void updateMinWindowSize() {
        if (primaryStage == null || rootNode == null) return;

        // adapt min height
        Double nodeStageHeightDifference = primaryStage.getHeight() - rootNode.getHeight();
        Double minWindowHeight = rootNode.minHeight(rootNode.getWidth()) + nodeStageHeightDifference;
        primaryStage.setMinHeight(minWindowHeight);

        // adapt min width
        Double nodeStageWidthDifference = primaryStage.getWidth() - rootNode.getWidth();
        Double minWindowWidth = rootNode.minWidth(rootNode.getHeight()) + nodeStageWidthDifference;
        primaryStage.setMinWidth(minWindowWidth);
    }

    /**
     * Adapt the size of the application window to match the preferred size of the window content.
     * Additionally adapt the min size of the application window, so that the user cannot resize the window to a
     * too small size.
     */
    public static void adaptWindowSize() {
        if (primaryStage == null || rootNode == null) return;

        updateMinWindowSize();
        primaryStage.sizeToScene();
    }
}
