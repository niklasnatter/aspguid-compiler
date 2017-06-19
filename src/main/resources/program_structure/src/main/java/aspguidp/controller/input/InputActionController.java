package aspguidp.controller.input;

import aspguidp.Main;
import aspguidp.gui.status.DataStatusManager;
import aspguidp.gui.status.ExecutionStatusManager;
import aspguidp.helper.AtomHelper;
import aspguidp.service.core.CoreServicePool;
import aspguidp.service.core.asp.AnswerSetManager;
import aspguidp.service.core.asp.AspSolverService;
import aspguidp.service.core.asp.SolverMessageManager;
import aspguidp.service.core.atom.Atom;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Controller class of the input action area of the application. The input action area contains buttons which are used
 * to trigger various actions on or with the data of the input area of the program.
 * <p>
 * Currently, the input action area contains a save button, a load button and a run button.
 * The save button saves the current input data of the input area to a problem instance file. The load button sets the
 * data of the input area of the program by a problem instance file. The run button executes the logic program
 * with the data of the input area of the application.
 * <p>
 * This class is the link between the data of the input components
 * ({@link aspguidp.controller.input.element.InputElementController})(which is accessed over the input atom hub) and
 * the logic program solver, as it queries the input data from the input atom hub and passes them to the logic
 * program solver.
 */
public class InputActionController {
    private static final FileChooser.ExtensionFilter problemInstanceFilter = new FileChooser.ExtensionFilter(
            "Problem Instance File (*.asp)", "*.asp", "*.ASP"
    );

    @FXML
    private Button loadButton;
    @FXML
    private Button saveButton;
    @FXML
    private Button runButton;

    /**
     * Initialization method which is called when the respective .fxml file is loaded.
     * <p>
     * This method sets the click handlers for the buttons of the input action area.
     * Additionally, the clickability of the save button is bound to the application data status and the clickability
     * of the run button is bound to the application execution status.
     */
    @FXML
    private void initialize() {
        this.loadButton.setOnKeyPressed(e -> { if (e.getCode().equals(KeyCode.ENTER)) this.loadButton.fire(); });
        this.loadButton.setOnAction(event -> {
            try {
                this.loadProblemInstance(); }
            catch (IOException e) { e.printStackTrace(); }
        });

        // enable save button when at least one element contains data which can be saved
        this.saveButton.disableProperty().bind(DataStatusManager.anyElementContainsDataProperty().not());
        this.saveButton.setOnKeyPressed(e -> { if (e.getCode().equals(KeyCode.ENTER)) this.saveButton.fire(); });
        this.saveButton.setOnAction(event -> {
            try {
                this.saveProblemInstance(); }
            catch (IOException e) { e.printStackTrace(); }
        });

        // enable run button when all elements are ready for execution
        this.runButton.disableProperty().bind(ExecutionStatusManager.allElementsReadyProperty().not());
        this.runButton.setOnKeyPressed(e -> { if (e.getCode().equals(KeyCode.ENTER)) this.runButton.fire(); });
        this.runButton.setOnAction(event -> {
            Collection<Atom> inputAtoms = CoreServicePool.getInstance().getInputAtomHub().getAtomsFromSuppliers();
            AspSolverService solverService = CoreServicePool.getInstance().getAspSolverService();
            SolverMessageManager messageManager = CoreServicePool.getInstance().getSolverMessageManager();
            AnswerSetManager answerSetManager = CoreServicePool.getInstance().getAnswerSetManager();

            try { solverService.executeLogicProgram(inputAtoms, messageManager, answerSetManager); }
            catch (IOException e) { e.printStackTrace(); }
        });
    }

    /**
     * Method which is called on click of the load button.
     * <p>
     * This method opens a new file chooser window in the directory in where the application is stored.
     * When the user selects a problem instance in the file chooser, the atoms are extracted from the content
     * of the selected file and are set to the consumers of the input atom hub.
     * The consumers of the input atom hub are usually the input components of the program.
     *
     * @throws IOException
     */
    private void loadProblemInstance() throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialFileName("problem-instance");
        fileChooser.setTitle("load problem instance");
        fileChooser.getExtensionFilters().setAll(problemInstanceFilter);
        this.trySetInitialDirToProgramDir(fileChooser);

        File loadFile = fileChooser.showOpenDialog(this.saveButton.getScene().getWindow());
        if (loadFile != null) {
            List<String> lines = Files.readAllLines(loadFile.toPath(), Charset.defaultCharset());
            List<String> atomStrings = AtomHelper.extractAtomStringsFromProgramCode(String.join(" ", lines));
            List<Atom> atoms = AtomHelper.atomsFromStringRepresentations(atomStrings);
            CoreServicePool.getInstance().getInputAtomHub().setAtomsToConsumers(atoms);
        }
    }

    /**
     * Method which is called on click of the load button.
     * <p>
     * This method opens a new file chooser in the directory in where the application is stored.
     * When the user provides a filename in the file chooser, all atoms of registered atom providers of the input atom
     * hub are saved into a file with the provided filename.
     *
     * @throws IOException
     */
    private void saveProblemInstance() throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialFileName("problem-instance");
        fileChooser.setTitle("save problem instance");
        fileChooser.getExtensionFilters().setAll(problemInstanceFilter);
        this.trySetInitialDirToProgramDir(fileChooser);

        File saveFile = fileChooser.showSaveDialog(this.saveButton.getScene().getWindow());
        if (saveFile != null) {
            // add extension if not provided
            String fileName = saveFile.getName();
            if (!fileName.toLowerCase().endsWith(".asp")) {
                saveFile = new File(saveFile.getAbsolutePath() + ".asp");
            }

            Collection<Atom> atoms = CoreServicePool.getInstance().getInputAtomHub().getAtomsFromSuppliers();
            Map<String, List<Atom>> predicateAtomMap = AtomHelper.groupAtomsByPredicateName(atoms);
            // use treemap to sort predicate map by key
            List<String> lines = AtomHelper.getProgramCodeLines(new TreeMap<>(predicateAtomMap).values());
            Files.write(saveFile.toPath(), lines, Charset.defaultCharset());
        }
    }

    /**
     * Try to set the initial directory of the given file chooser to the directory, where the executable
     * of the running program is stored.
     *
     * @param fileChooser file chooser instance of which the initial directory is set
     */
    private void trySetInitialDirToProgramDir(FileChooser fileChooser) {
        try {
            File file = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
            if (!file.isDirectory()) file = file.getParentFile();
            fileChooser.setInitialDirectory(file);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
