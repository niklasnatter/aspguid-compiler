package aspguidp.service.core.atom.impl;

import aspguidp.service.core.atom.Atom;
import aspguidp.service.core.atom.AtomConsumer;
import aspguidp.service.core.atom.AtomHub;
import aspguidp.service.core.atom.AtomSupplier;

import java.util.*;

/**
 * Basic implementation of the {@link AtomHub} interface which uses array lists to store registered {@link AtomSupplier}
 * instances and registered {@link AtomConsumer} instances.
 */
public class SimpleAtomHub implements AtomHub {
    private final List<AtomSupplier> atomSuppliers = new ArrayList<>();
    private final List<AtomConsumer> atomConsumers = new ArrayList<>();

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerAtomSupplier(AtomSupplier atomSupplier) {
        if (!this.atomSuppliers.contains(atomSupplier)) this.atomSuppliers.add(atomSupplier);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerAtomConsumer(AtomConsumer atomConsumer) {
        if (!this.atomConsumers.contains(atomConsumer)) this.atomConsumers.add(atomConsumer);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<Atom> getAtomsFromSuppliers() {
        Set<Atom> distinctAtoms = new HashSet<>();
        for (AtomSupplier as : this.atomSuppliers) distinctAtoms.addAll(as.getAtoms());
        return distinctAtoms;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setAtomsToConsumers(Collection<Atom> atoms) {
        Set<Atom> distinctAtoms = new HashSet<>(atoms);
        for (AtomConsumer ac : this.atomConsumers) ac.setAtoms(distinctAtoms);
    }
}
