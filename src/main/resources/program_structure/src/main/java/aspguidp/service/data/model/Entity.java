package aspguidp.service.data.model;

import java.util.Map;

/**
 * Interface for an entity.
 * <p>
 * Entities are basic data structures which are used in the input and output components of the the application
 * ({@link aspguidp.controller.input.element.InputElementController}, {@link aspguidp.controller.output.element.OutputElementController}).
 * The input and output components of the application use the entity type, to represent the data which is inputted
 * by the user or outputted to the user. For example, an entity output component displays a list of entities
 * in the list view of the component.
 * <p>
 * An entity has an display representation, which defines how it is displayed in the user interface and an
 * atom representation, which defines how it is represented in the logic program code. Additionally, an entity
 * contains an attribute map, which holds the values of the attributes of the entity.
 * <p>
 * For example, an edge entity which is used in an input component of the application could contain the following data:
 * atom representation: 'edge(alpha,beta)'
 * display representation: 'alpha -&gt; beta'
 * attribute map: {'from':'alpha', 'to':'beta'}
 */
public interface Entity {
    /**
     * @return attribute map which contains attribute names and values of the entity
     */
    Map<String, String> getAttributeMap();

    /**
     * @return string which defines how the entity is displayed in the user interface
     */
    String getDisplayRepresentation();

    /**
     * @return string which defines how the entity is represented in the logic program code
     */
    String getAtomRepresentation();

    /**
     * {@inheritDoc}
     */
    @Override
    boolean equals(Object obj);

    /**
     * {@inheritDoc}
     */
    @Override
    int hashCode();
}
