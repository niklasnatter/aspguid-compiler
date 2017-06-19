package aspguidp.service.data.template.impl;

import aspguidp.helper.PatternHelper;
import aspguidp.service.data.template.TemplatePart;

/**
 * Implementation of the {@link TemplatePart} interface which represents a placeholder template part.
 * <p>
 * A placeholder template part represents a placeholder for concrete values and is assigned to a specific placeholder
 * id, placeholder name and a value source. The value source of the placeholder template part defines, which concrete
 * values can be assigned to the placeholder.
 * <p>
 * For example, the representation template 'edge(::from,::to)' contains the placeholder parts ['::from', '::to'].
 * In a real application, the value source of this placeholder template parts could be set to 'node', to ensure,
 * that a edge can only connect two nodes.
 */
public class PlaceholderTemplatePart implements TemplatePart {
    private final String placeholderIdentifier;
    private final String placeholderName;
    private final String placeholderValueSource;

    /**
     * Create a new template part instance for the given placeholder id, placeholder name and value source.
     *
     * @param placeholderId          id of the placeholder of the created placeholder template part
     * @param placeholderName        name of the placeholder of the created placeholder template part
     * @param placeholderValueSource value source which defines possible values for the created placeholder template
     *                               part
     */
    public PlaceholderTemplatePart(String placeholderId, String placeholderName, String placeholderValueSource) {
        this.placeholderIdentifier = placeholderId;
        this.placeholderName = placeholderName;
        this.placeholderValueSource = placeholderValueSource;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDisplayValue() {
        return this.placeholderName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPattern() {
        return PatternHelper.getValuePattern();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isPlaceholderPart() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPlaceholderIdentifier() {
        return this.placeholderIdentifier;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPlaceholderValueSource() {
        return this.placeholderValueSource;
    }
}
