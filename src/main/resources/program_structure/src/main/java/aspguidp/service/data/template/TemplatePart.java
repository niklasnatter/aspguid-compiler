package aspguidp.service.data.template;

/**
 * Interface for a template parts.
 * <p>
 * Template parts are the atomic parts of an representation template. In other words, a representation template
 * consists of a sequence of template parts. Currently, there are two types of template parts:
 * {@link aspguidp.service.data.template.impl.FixedTemplatePart} and {@link aspguidp.service.data.template.impl.PlaceholderTemplatePart}.
 * An example for a representation template string is 'edge(::from,::to)'.
 * <p>
 * Representation templates (and therefore representation parts) are fundamental data structures which are used in
 * various parts of the application. For example, the conversion between atom/display representations and entities is
 * implemented based on template parts ({@link aspguidp.service.data.model.impl.TemplateEntityFactory}).
 * Also, the implementation of the validation of user input and the generation of suggestions uses template parts
 * ({@link aspguidp.service.input.validation.ValidationService}, {@link aspguidp.service.input.suggestion.SuggestionService}).
 */
public interface TemplatePart {
    /**
     * @return string which is used to display the placeholder part if needed
     */
    String getDisplayValue();

    /**
     * @return pattern string which matches a value for the template part
     */
    String getPattern();

    /**
     * @return true, if the template part is a placeholder template part
     */
    boolean isPlaceholderPart();

    /**
     * @return identifier of the placeholder of the template part. null, if the template part is not a placeholder
     * template part
     */
    String getPlaceholderIdentifier();

    /**
     * @return value source for the values of the placeholder template part. null, if the template part is not a
     * placeholder template part
     */
    String getPlaceholderValueSource();
}
