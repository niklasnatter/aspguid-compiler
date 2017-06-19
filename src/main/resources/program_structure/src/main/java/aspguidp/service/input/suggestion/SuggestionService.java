package aspguidp.service.input.suggestion;

import java.util.List;

/**
 * Interface for a suggestion service.
 * <p>
 * A suggestion service is responsible to generate autocomplete suggestions and prompt texts based on a given user
 * input string.
 * <p>
 * Suggestion service instances are used in the autocomplete popup component
 * ({@link aspguidp.controller.component.AutocompletePopupController}), to gather suggestions which are
 * displayed in autocomplete popups. Additionally, entity input components
 * ({@link aspguidp.controller.input.element.EntityInputController}) use instances of suggestion services
 * to generate the prompt text which is set to the background field of the input field of the component.
 */
public interface SuggestionService {
    /**
     * @param input user input for which the suggestions are generated
     * @return list of suggestions for the given user input string
     */
    List<String> getSuggestions(String input);

    /**
     * @param input user input string for which the prompt text is generated
     * @return prompt text for the given user input string
     */
    String getPromptText(String input);
}
