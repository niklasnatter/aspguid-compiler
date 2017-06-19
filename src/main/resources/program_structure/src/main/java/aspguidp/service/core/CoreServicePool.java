package aspguidp.service.core;

import aspguidp.service.core.asp.AnswerSetManager;
import aspguidp.service.core.asp.AspSolverService;
import aspguidp.service.core.asp.SolverMessageManager;
import aspguidp.service.core.asp.impl.DlvAnswerSetManager;
import aspguidp.service.core.asp.impl.DlvAspSolverService;
import aspguidp.service.core.asp.impl.DlvSolverMessageManager;
import aspguidp.service.core.atom.AtomHub;
import aspguidp.service.core.atom.impl.SimpleAtomHub;

/**
 * Singleton core service pool class.
 * <p>
 * The core service pool provide methods to access services and managers of the application core, which are handling
 * the management of the logic program atoms and the execution of the logic program solver.
 * <p>
 * This class enables the initialisation of these services and managers in a single place. Other parts of the
 * application can access the services and managers of the core of the application in a simple way through this
 * class.
 */
public class CoreServicePool {
    private static CoreServicePool instance;
    private AtomHub inputAtomHubInstance;
    private AtomHub outputAtomHubInstance;
    private AnswerSetManager answerSetManagerInstance;
    private SolverMessageManager solverMessageManagerInstance;
    private AspSolverService aspSolverServiceInstance;

    private CoreServicePool() {
    }

    /**
     * @return singleton service pool instance of this class
     */
    public static CoreServicePool getInstance() {
        if (instance == null) {
            instance = new CoreServicePool();
        }
        return instance;
    }

    /**
     * Method to access the input atom hub of the application, which manages the atoms which are passed to the logic
     * program on logic program execution.
     * If the input atom hub is not initialized yet, it is initializes before it is returned.
     *
     * @return input atom hub instance of the application
     */
    public AtomHub getInputAtomHub() {
        if (this.inputAtomHubInstance == null) {
            this.inputAtomHubInstance = new SimpleAtomHub();
        }
        return this.inputAtomHubInstance;
    }

    /**
     * Method to access the output atom hub of the application, which manages the atoms of the currently displayed
     * answer set.
     * If the output hub is not initialized yet, it is initialized before it is returned.
     *
     * @return output atom hub instance of the application
     */
    public AtomHub getOutputAtomHub() {
        if (this.outputAtomHubInstance == null) {
            this.outputAtomHubInstance = new SimpleAtomHub();
        }
        return this.outputAtomHubInstance;
    }

    /**
     * Method to access the answer set manager of the application, which manages the resulting answer sets of the
     * execution of the logic program.
     * If the answer set manager is not initialized yet, it is initialized before it is returned.
     *
     * @return answer set manager instance of the application
     */
    public AnswerSetManager getAnswerSetManager() {
        if (this.answerSetManagerInstance == null) {
            this.answerSetManagerInstance = new DlvAnswerSetManager();
        }
        return this.answerSetManagerInstance;
    }

    /**
     * Method to access the solver message manager of the application, which manages messages of the logic program
     * solver which is executed on the execution of the logic program.
     * If the solver message manager is not initialized yet, it is initialized before it is returned.
     *
     * @return solver message manager instance of the application
     */
    public SolverMessageManager getSolverMessageManager() {
        if (this.solverMessageManagerInstance == null) {
            this.solverMessageManagerInstance = new DlvSolverMessageManager();
        }
        return this.solverMessageManagerInstance;
    }

    /**
     * Method to access the asp solver service of the application, which is responsible for execution the logic
     * program.
     * If the asp solver service is not initialized yet, it is initialized before it is returned.
     *
     * @return asp solver service instance of the application
     */
    public AspSolverService getAspSolverService() {
        if (this.aspSolverServiceInstance == null) {
            this.aspSolverServiceInstance = new DlvAspSolverService("/asp/logic-program.dl");
        }
        return this.aspSolverServiceInstance;
    }
}
