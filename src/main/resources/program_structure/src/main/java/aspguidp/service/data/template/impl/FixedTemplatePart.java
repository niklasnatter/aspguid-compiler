package aspguidp.service.data.template.impl;

import aspguidp.service.data.template.TemplatePart;

import java.util.regex.Pattern;

/**
 * Implementation of the {@link TemplatePart} interface which represents a fixed template part.
 * <p>
 * A fixed template part matches a fixed value. Therefore, the fixed template parts of representation template
 * define the fixed contents of the respective representation.
 * <p>
 * For example, the representation template 'edge(::from,::to)' contains the fixed parts ['edge(', ',', ')'].
 */
public class FixedTemplatePart implements TemplatePart {
    private final String content;

    /**
     * Create a new template part instance with the given fixed content
     *
     * @param content fixed content of the created template part
     */
    public FixedTemplatePart(String content) {
        this.content = content;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDisplayValue() {
        return this.content;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPattern() {
        return Pattern.quote(this.content);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isPlaceholderPart() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPlaceholderIdentifier() {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPlaceholderValueSource() {
        throw new UnsupportedOperationException();
    }
}
