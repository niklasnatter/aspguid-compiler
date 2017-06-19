package aspguidp.service.data.manager.impl;

import aspguidp.exception.InvalidEntityRepresentationException;
import aspguidp.helper.AtomHelper;
import aspguidp.service.core.atom.Atom;
import aspguidp.service.data.manager.EntityDataManager;
import aspguidp.service.data.model.Entity;
import aspguidp.service.data.model.EntityFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Basic implementation of the {@link EntityDataManager} interface which uses an {@link EntityFactory} instance to create
 * {@link Entity} instances from given user input strings and string representations of atoms.
 */
public class SimpleEntityDataManager implements EntityDataManager {
    private final EntityFactory entityFactory;
    private final ObservableList<Entity> entities = FXCollections.observableArrayList();

    /**
     * Crete a new manager instance with the given entity factory.
     *
     * @param entityFactory entity factory which is used to create entities from user input strings and string
     *                      representations of atoms
     */
    public SimpleEntityDataManager(EntityFactory entityFactory) {
        this.entityFactory = entityFactory;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean addByUserInput(String displayRepresentation) {
        try {
            Entity e = this.entityFactory.fromDisplayRepresentation(displayRepresentation);
            if (!this.entities.contains(e)) this.entities.add(e);
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
        this.entities.clear();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ObservableList<Entity> getEntities() {
        return this.entities;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<Atom> getAtoms() {
        List<String> atomStrings = this.entities.stream().map(Entity::getAtomRepresentation).collect(Collectors.toList());
        return AtomHelper.atomsFromStringRepresentations(atomStrings);
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

        this.entities.setAll(matchingEntities);
    }
}
