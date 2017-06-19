package aspguidp.controller.input;

import aspguidp.helper.AtomHelper;
import aspguidp.service.core.CoreServicePool;
import aspguidp.service.core.atom.Atom;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

import java.util.List;

/**
 * Controller class for input area of the application. The input area of the application contains all input components
 * of the application ({@link aspguidp.controller.input.element.InputElementController}).
 */
public class ProgramInputController {
    @FXML
    private VBox rootNode;

    /**
     * Initialization method which is called when the respective .fxml file is loaded.
     * <p>
     * This method extracts the atoms from the program code of the solver manager of the application, and registers
     * an atom supplier to the input atom hub, which supplies the extracted atoms.
     * This is needed in order that a suggestion service instance can query the atoms of the program code for the
     * generation of suggestions.
     * Additionally, the extracted atoms are set to the registered consumers of the input atom hub, to signal, that
     * the program code contains atoms of a predicate which is inputted over a input component.
     * <p>
     * The method also binds the preferred height of the input area to the current height.
     * This binding ensures, that the window manager does not shrink or stretches the input area height, when adjusting
     * the window size.
     */
    @FXML
    private void initialize() {
        // get atoms from program code
        String programContent = CoreServicePool.getInstance().getAspSolverService().getLogicProgramContent();
        List<String> programAtomStrings = AtomHelper.extractAtomStringsFromProgramCode(programContent);
        List<Atom> programAtoms = AtomHelper.atomsFromStringRepresentations(programAtomStrings);

        // register program code as atom source to enable autocompletion from program atoms
        CoreServicePool.getInstance().getInputAtomHub().registerAtomSupplier(() -> programAtoms);

        // set program atoms to input consumers; removal of this atoms in input elements will not have any effect
        // usually, the program code should not contain any input entity atoms
        CoreServicePool.getInstance().getInputAtomHub().setAtomsToConsumers(programAtoms);

        // bind pref height to current height to keep height on adaptwindowsize of windowsizemanager
        this.rootNode.prefHeightProperty().bind(this.rootNode.heightProperty());
    }
}
