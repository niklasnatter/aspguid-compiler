package aspguidp.service.data.manager.impl;

import aspguidp.exception.InvalidEntityRepresentationException;
import aspguidp.helper.AtomHelper;
import aspguidp.service.core.atom.Atom;
import aspguidp.service.data.manager.ValueDataManager;
import aspguidp.service.data.model.Entity;
import aspguidp.service.data.model.EntityFactory;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.util.*;

/**
 * Basic implementation of the {@link ValueDataManager} interface which uses an {@link EntityFactory} instance to create
 * {@link Entity} instances from given user input strings and string representations of atoms.
 */
public class SimpleValueDataManager implements ValueDataManager {
    private final EntityFactory entityFactory;
    private final ObjectProperty<Entity> value = new SimpleObjectProperty<>();

    /**
     * Create a new manager instance with the given entity factory.
     *
     * @param entityFactory entity factory which is used to create entities from user input strings and string
     *                      representations of atoms
     */
    public SimpleValueDataManager(EntityFactory entityFactory) {
        this.entityFactory = entityFactory;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean setByUserInput(String displayRepresentation) {
        try {
            Entity e = this.entityFactory.fromDisplayRepresentation(displayRepresentation);
            this.value.setValue(e);
            return true;
        } catch (InvalidEntityRepresentationException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clear() {
        this.value.set(null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ObjectProperty<Entity> getValue() {
        return this.value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<Atom> getAtoms() {
        if (this.value.get() == null) return Collections.emptyList();
        return AtomHelper.atomsFromStringRepresentations(Collections.singletonList(this.value.get().getAtomRepresentation()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setAtoms(Collection<Atom> atoms) {
        List<Entity> matchingEntities = new ArrayList<>();

        for (Atom a : atoms) {
            // ignoring invalid format exception, because we try to convert every atom from the atom hub
            try { matchingEntities.add(this.entityFactory.fromAtomRepresentation(a.getStringRepresentation())); }
            catch (InvalidEntityRepresentationException e) {}
        }

        if (!matchingEntities.isEmpty()) {
            matchingEntities.sort(Comparator.comparing(Entity::getDisplayRepresentation));
            this.value.set(matchingEntities.get(0));
        } else {
            this.value.set(null);
        }
    }
}
