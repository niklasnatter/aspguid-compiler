package aspguidp.service.data.model;

import aspguidp.exception.InvalidEntityRepresentationException;

/**
 * Interface for an entity factory.
 * <p>
 * Entity factories are used to create {@link Entity} instances from raw strings.
 * For example, this functionality is used, to create a respective entity from the user input in an input component
 * of the application ({@link aspguidp.controller.input.element.InputElementController}).
 */
public interface EntityFactory {
    /**
     * Create a new entity from the given display representation.
     *
     * @param displayRepresentation display representation to create the entity from
     * @return entity extracted from the given display representation
     * @throws InvalidEntityRepresentationException if the given display representation is not valid
     */
    Entity fromDisplayRepresentation(String displayRepresentation) throws InvalidEntityRepresentationException;

    /**
     * Create a new entity from the given atom representation.
     *
     * @param atomRepresentation atom representation to create the entity from
     * @return entity extracted from the given atom representation
     * @throws InvalidEntityRepresentationException if the given atom representation is not valid
     */
    Entity fromAtomRepresentation(String atomRepresentation) throws InvalidEntityRepresentationException;
}
