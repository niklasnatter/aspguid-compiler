package aspguidp.service.input.validation;

/**
 * Interface for a validation service.
 * <p>
 * A validation service is responsible to determine the validation status of a given user input string.
 * <p>
 * Validation service instances are used to validate the data which is inputted by the user into the input components
 * of the application ({@link aspguidp.controller.input.element.InputElementController}).
 */
public interface ValidationService {
    /**
     * @param input user input which is validated
     * @return validation status of the given string
     */
    ValidationStatus validate(String input);

    /**
     * Enum which represents a validation status. Each validation status is assigned to a css class, which can be used
     * in the interface of the application.
     */
    enum ValidationStatus {
        INVALID("validation-invalid"),
        INCOMPLETE("validation-incomplete"),
        VALID("validation-valid");

        private final String cssClass;

        ValidationStatus(String cssClass) {
            this.cssClass = cssClass;
        }

        public String getCssClass() {
            return this.cssClass;
        }
    }
}
