package aspguidp.controller.output;

import aspguidp.gui.window.WindowSizeManager;
import aspguidp.helper.AtomHelper;
import aspguidp.service.core.CoreServicePool;
import aspguidp.service.core.asp.AnswerSetManager;
import aspguidp.service.core.atom.Atom;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Controller class for the output area of the application. The output area of the application contains all output
 * components of the application ({@link aspguidp.controller.output.element.OutputElementController}). Additionally the
 * output area provides control elements to change the current displayed answer set, show a text representation of the
 * current answer set and close the output area.
 * <p>
 * The output area of the application is only visible, if the {@link AnswerSetManager} instance of the application
 * signals, that the execution of the logic program lead to a set of answer sets.
 * <p>
 * This controller is the link between the answer set manager (which is filled by the logic program solver) and the
 * data of the output components of the application (which is accessed over the output atom hub).
 */
public class ProgramOutputController {
    @FXML
    private VBox rootNode;
    @FXML
    private Label currentSetLabel;
    @FXML
    private Button nextSetButton;
    @FXML
    private Button previousSetButton;
    @FXML
    private Button resetButton;
    @FXML
    private ToggleButton terminalButton;
    @FXML
    private TextArea terminalArea;

    private Integer currentAnswerSetIndex;

    /**
     * Initialization method which is called when the respective .fxml file is loaded.
     * <p>
     * This method binds the visibility of the program output area to the respective property of the answer set manager
     * and sets up a handler which updates the application window size on visibility change.
     * <p>
     * Also, an atom supplier is registered to the output atom hub, which supplies the atoms of the currently displayed
     * answer set. This is needed in order that the output components can access the atoms of the currently displayed
     * answer set.
     * <p>
     * Additionally, this method sets up the buttons for changing the current answer set, displaying a text
     * representation of the current answer set and closing the output area of the program.
     * <p>
     * Finally, this method also binds the preferred height of the output area to the current height.
     * This binding ensures, that the window manager does not shrink the output area height, when adjusting
     * the window size.
     */
    @FXML
    private void initialize() {
        AnswerSetManager answerSetManager = CoreServicePool.getInstance().getAnswerSetManager();

        // show output area only if answer set property of answer set manager is not null
        // adjust height of window on visibility change
        this.rootNode.managedProperty().bind(this.rootNode.visibleProperty());
        this.rootNode.visibleProperty().bind(answerSetManager.answerSetsProperty().isNotNull());
        this.rootNode.visibleProperty().addListener((ob, o, n) -> WindowSizeManager.adaptWindowSize());

        // register atom provider to provide current answer set atoms
        // set current answer set to first answer set if answer set property changes
        this.registerAnswerSetAtomSupplier();
        answerSetManager.answerSetsProperty().addListener((ob, o, n) -> this.trySetCurrentAnswerSet(0));

        // initialize buttons
        this.previousSetButton.setGraphic(new Label("\uf060"));
        this.previousSetButton.setOnKeyPressed(e -> { if (e.getCode().equals(KeyCode.ENTER)) this.previousSetButton.fire(); });
        this.previousSetButton.setOnAction(event -> this.trySetCurrentAnswerSet(this.currentAnswerSetIndex - 1));

        this.nextSetButton.setGraphic(new Label("\uf061"));
        this.nextSetButton.setOnKeyPressed(e -> { if (e.getCode().equals(KeyCode.ENTER)) this.nextSetButton.fire(); });
        this.nextSetButton.setOnAction(event -> this.trySetCurrentAnswerSet(this.currentAnswerSetIndex + 1));

        this.resetButton.setGraphic(new Label("\uf00d"));
        this.resetButton.setOnKeyPressed(e -> { if (e.getCode().equals(KeyCode.ENTER)) this.resetButton.fire(); });
        this.resetButton.setOnAction(event -> answerSetManager.resetProperties());

        // initialize terminal toggle button and terminal area
        this.terminalButton.setGraphic(new Label("\uf121"));
        this.terminalButton.setOnKeyPressed(e -> { if (e.getCode().equals(KeyCode.ENTER)) this.terminalButton.fire(); });
        this.initializeTerminalTextArea();

        // bind pref height to current height to keep height on adaptwindowsize of windowsizemanager
        this.rootNode.heightProperty().addListener((ob, o, n) -> this.rootNode.setPrefHeight(n.doubleValue()));
    }

    /**
     * Register an atom supplier to the output atom hub which supplies the atoms of the currently displayed answer set.
     * This is needed in order that the output components can access the atoms of the currently displayed answer set.
     */
    private void registerAnswerSetAtomSupplier() {
        AnswerSetManager answerSetManager = CoreServicePool.getInstance().getAnswerSetManager();
        CoreServicePool.getInstance().getOutputAtomHub().registerAtomSupplier(() -> {
            List<List<Atom>> answerSets = answerSetManager.answerSetsProperty().get();
            if (answerSets == null || this.currentAnswerSetIndex >= answerSets.size()) {
                return Collections.emptyList();
            } else {
                return answerSets.get(this.currentAnswerSetIndex);
            }
        });
    }

    /**
     * Initialize the text area which is used to display a text representation of the currently displayed answer set.
     * The text area visibility is bound to the selected property of the respective button.
     * <p>
     * Also an atom consumer is registered to the output atom hub, which sets text representation of the atoms of the
     * output atom hub to the text area. This is ensures, that the tet area always displays the atoms uf the currently
     * displayed answer set.
     */
    private void initializeTerminalTextArea() {
        // show terminal area when terminal toggle-button is selected
        this.terminalArea.managedProperty().bind(this.terminalArea.visibleProperty());
        this.terminalArea.visibleProperty().bind(this.terminalButton.selectedProperty());

        // register atom consumer which sets atoms to terminal area text
        CoreServicePool.getInstance().getOutputAtomHub().registerAtomConsumer(atoms -> {
            Map<String, List<Atom>> predicateAtomMap = AtomHelper.groupAtomsByPredicateName(atoms);
            // use treemap to sort predicate map by key
            List<String> lines = AtomHelper.getProgramCodeLines(new TreeMap<>(predicateAtomMap).values());
            this.terminalArea.setText(String.join("\n", lines));
        });
    }

    /**
     * Try to set the current displayed answer set to the answer set of the answer set manager with the given index.
     * An answer set is set as displayed by setting the atoms of the answer sets to the consumers of the output
     * atom hub.
     * <p>
     * This method is the link between the answer sets of a executed logic program (which are accessed through the
     * answer set manager of the application) and the data of the output components of the application (which is
     * accessed over the output atom hub).
     * <p>
     * This method is called if the answer set manager of the application signals that the execution of the logic
     * program lead to a set of answer sets with the index 0. Additionally this method is called with the respective
     * index, if the user fires one of the buttons which are used to change the currently displayed answer set.
     *
     * @param answerSetIndex index of the answer set which is set as displayed
     */
    private void trySetCurrentAnswerSet(int answerSetIndex) {
        List<List<Atom>> answerSets = CoreServicePool.getInstance().getAnswerSetManager().answerSetsProperty().get();
        if (answerSets == null || answerSetIndex >= answerSets.size()) return;

        // update gui elements
        this.currentAnswerSetIndex = answerSetIndex;
        this.currentSetLabel.setText(String.format("%d/%d", answerSetIndex + 1, answerSets.size()));
        this.nextSetButton.setDisable(this.currentAnswerSetIndex + 1 >= answerSets.size());
        this.previousSetButton.setDisable(this.currentAnswerSetIndex - 1 < 0);

        // set atoms to atom consumers of output hub
        List<Atom> currentAnswerSet = answerSets.get(this.currentAnswerSetIndex);
        CoreServicePool.getInstance().getOutputAtomHub().setAtomsToConsumers(currentAnswerSet);
    }
}
