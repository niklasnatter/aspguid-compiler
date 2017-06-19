package aspguidp.service.data.model.impl;

import aspguidp.service.data.model.Entity;

import java.util.Map;

/**
 * Basic implementation of the {@link Entity} interface which allows the creation of an entity by a raw attribute map,
 * a raw display representation and a raw atom representation.
 * <p>
 * Instances of this class are usually created by an {@link aspguidp.service.data.model.EntityFactory} instance.
 */
public class SimpleEntity implements Entity {
    private final Map<String, String> attributeMap;
    private final String displayRepresentation;
    private final String atomRepresentation;

    /**
     * Create a new entity instance with the given attribute map, display representation and atom representation.
     *
     * @param attributeMap          attribute map of the created entity
     * @param displayRepresentation display representation of the created entity
     * @param atomRepresentation    atom representation of the created entity
     */
    public SimpleEntity(Map<String, String> attributeMap, String displayRepresentation, String atomRepresentation) {
        this.attributeMap = attributeMap;
        this.displayRepresentation = displayRepresentation;
        this.atomRepresentation = atomRepresentation;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, String> getAttributeMap() {
        return this.attributeMap;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDisplayRepresentation() {
        return this.displayRepresentation;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getAtomRepresentation() {
        return this.atomRepresentation;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return this.getDisplayRepresentation();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Entity)) return false;
        if (obj == this) return true;

        Entity o = (Entity) obj;
        if (!o.getAtomRepresentation().equals(this.getAtomRepresentation())) return false;
        if (!o.getDisplayRepresentation().equals(this.getDisplayRepresentation())) return false;
        if (!o.getAttributeMap().equals(this.getAttributeMap())) return false;

        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return this.attributeMap.hashCode() + this.atomRepresentation.hashCode() + this.displayRepresentation.hashCode();
    }
}
