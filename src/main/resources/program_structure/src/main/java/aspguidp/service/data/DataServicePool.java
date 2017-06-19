package aspguidp.service.data;

import aspguidp.service.data.manager.EntityDataManager;
import aspguidp.service.data.manager.ValueDataManager;
import aspguidp.service.data.model.EntityFactory;
import aspguidp.service.data.template.TemplatePart;

import java.util.List;

/**
 * Base data service pool class.
 * <p>
 * A data service pool provides methods to access data related services, managers and other data related information
 * like the display representation template and the atom representation template of a specific entity or value.
 * <p>
 * A singleton class extending this base class is generated for each input entity, output entity, input value and
 * output value of the application by the aspguid compiler.
 * This enables the initialization of the data related managers, services and other data related information for a
 * entity/value in a single place. Other parts of the program can access data related objects of an entity/value in a
 * simple way through this generated singleton class.
 */
public abstract class DataServicePool {
    /**
     * Method to access the display representation template of the entity/value of the service pool instance.
     *
     * @return display representation template of the entity/value of the service pool instance.
     */
    public abstract List<TemplatePart> getDisplayTemplateParts();

    /**
     * Method to access the atom representation template of the entity/value of the service pool instance.
     *
     * @return atom representation template of the entity/value of the service pool instance.
     */
    public abstract List<TemplatePart> getAtomTemplateParts();

    /**
     * Method to access the entity data manager instance for the entity/value of the service pool instance.
     * If the entity data manager is not initialized yet, it is initialized before it is returned.
     * <p>
     * If the entity/value of the service pool is an input entity/value, the entity data manager is registered to the
     * input atom hub as atom consumer and atom supplier.
     * If the entity/value of the service pool is an output entity/value, the entity data manager is registered to the
     * output atom hub as atom consumer.
     *
     * @return entity data manager instance for the entity/value of the service pool instance.
     */
    public abstract EntityDataManager getEntityDataManager();

    /**
     * Method to access the value data manager instance for the entity/value of the service pool instance.
     * If the value data manager is not initialized yet, it is initialized before it is returned.
     * <p>
     * If the entity/value of the service pool is an input entity/value, the value data manager is registered to the
     * input atom hub as atom consumer and atom supplier.
     * If the entity/value of the service pool is an output entity/value, the value data manager is registered to the
     * output atom hub as atom consumer.
     *
     * @return value data manager instance for the entity/value of the service pool instance.
     */
    public abstract ValueDataManager getValueDataManager();

    /**
     * Method to access the entity factory for the entity/value of the service pool instance.
     * If the entity factory is not initialized yet, it is initialized before it is returned.
     *
     * @return entity factory for the entity/value of the service pool instance.
     */
    public abstract EntityFactory getEntityFactory();

    /**
     * Model class which represents an attribute of an entity.
     * Properties of this class are used for the initialization of the data related managers and services which are
     * accessible through a data service pool instance.
     */
    protected static class AttributeDefinition {
        private final String identifier;
        private final String name;
        private final String valueSource;

        /**
         * Create a new attribute definition with the given identifier, name and value source.
         *
         * @param identifier  identifier of the attribute
         * @param name        name of the attribute
         * @param valueSource value source for the values of the attribute
         */
        public AttributeDefinition(String identifier, String name, String valueSource) {
            this.identifier = identifier;
            this.name = name;
            this.valueSource = valueSource;
        }

        public String getIdentifier() {
            return this.identifier;
        }

        public String getName() {
            return this.name;
        }

        public String getValueSource() {
            return this.valueSource;
        }
    }
}
