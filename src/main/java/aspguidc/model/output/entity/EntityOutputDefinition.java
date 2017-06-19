package aspguidc.model.output.entity;

import aspguidc.exception.parsing.DefinitionFormatException;
import aspguidc.exception.parsing.InvalidIdentifierFormatException;
import aspguidc.exception.parsing.InvalidJsonObjectException;
import aspguidc.exception.parsing.MissingRepresentationTemplatePlaceholderException;
import aspguidc.helper.JsonHelper;
import aspguidc.model.output.OutputElementDefinition;
import aspguidc.model.string.ConditionStatementDefinition;
import aspguidc.model.string.RepresentationTemplateDefinition;
import com.google.gson.JsonElement;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Model for an output element of the type entity output of a graphical user interface definition.
 */
public class EntityOutputDefinition implements OutputElementDefinition {
    private static final String objectName = "@entity_output";

    private final String identifier;
    private String title;
    private String description;
    private ConditionStatementDefinition outputCondition;
    private RepresentationTemplateDefinition guiRepresentation;
    private RepresentationTemplateDefinition atomRepresentation;
    private final Map<String, OutputAttributeDefinition> attributeElements = new LinkedHashMap<>();

    private EntityOutputDefinition(String identifier) throws InvalidIdentifierFormatException {
        JsonHelper.assertIdentifierIsValid(objectName, identifier);
        this.identifier = identifier;
    }

    /**
     * Create a new entity output model instance with the given identifier from the given json element.
     * The model is created by extracting each property of the given json element and setting it to the
     * model.
     *
     * @param identifier identifier of the entity output element
     * @param j          valid entity output json element according to the gui definition language specification
     * @return model instance
     * @throws DefinitionFormatException if the given json element is not valid according to the gui definition
     *                                   language specification
     */
    public static EntityOutputDefinition fromJsonElement(String identifier, JsonElement j) throws DefinitionFormatException {
        Logger.getGlobal().info("[parsing] read entity output element '" + identifier + "'");

        if (!j.isJsonObject()) throw new InvalidJsonObjectException(objectName);

        EntityOutputDefinition entityOutput = new EntityOutputDefinition(identifier);
        for (Map.Entry<String, JsonElement> entry : j.getAsJsonObject().entrySet()) {
            entityOutput.setValueByJsonProperty(entry.getKey(), entry.getValue());
        }

        entityOutput.assertRepresentationTemplateIntegrity();

        return entityOutput;
    }

    /**
     * Set a specific value of the model instance by the given json property key and json property value.
     * The value which is set is selected based on the given json property key.
     * <p>
     * This method is the central point when creating a new model from a json element, as it is called for each
     * property of the json element.
     * <p>
     * If the given json property key is not defined by the gui definition language specification, a warning is
     * logged.
     *
     * @param propertyKey   key of the json property which is set to the model
     * @param propertyValue value of the json property which is set to the model
     * @throws DefinitionFormatException if the given property key is malformed or the given property value is
     *                                   not valid according to the gui definition language specification.
     */
    private void setValueByJsonProperty(String propertyKey, JsonElement propertyValue) throws DefinitionFormatException {
        if ("title".equals(propertyKey)) {
            JsonHelper.assertPropertyIsString(objectName, propertyKey, propertyValue);
            this.title = propertyValue.getAsJsonPrimitive().getAsString();

        } else if ("description".equals(propertyKey)) {
            JsonHelper.assertPropertyIsString(objectName, propertyKey, propertyValue);
            this.description = propertyValue.getAsJsonPrimitive().getAsString();

        } else if ("output_condition".equals(propertyKey)) {
            JsonHelper.assertPropertyIsString(objectName, propertyKey, propertyValue);
            this.outputCondition = ConditionStatementDefinition.fromString(propertyValue.getAsJsonPrimitive().getAsString());

        } else if ("gui_representation".equals(propertyKey)) {
            JsonHelper.assertPropertyIsString(objectName, propertyKey, propertyValue);
            this.guiRepresentation = RepresentationTemplateDefinition.fromGuiRepresentationString(propertyValue.getAsJsonPrimitive().getAsString());

        } else if ("atom_representation".equals(propertyKey)) {
            JsonHelper.assertPropertyIsString(objectName, propertyKey, propertyValue);
            this.atomRepresentation = RepresentationTemplateDefinition.fromAtomRepresentationString(propertyValue.getAsJsonPrimitive().getAsString());

        } else if (propertyKey.startsWith("@output_attribute:")) {
            String[] keyParts = propertyKey.split(":", 2);
            this.attributeElements.put(keyParts[1], OutputAttributeDefinition.fromJsonElement(keyParts[1], propertyValue));

        } else {
            Logger.getGlobal().warning(String.format("unsupported property '%s' in '%s' object", propertyKey, objectName));
        }
    }

    /**
     * Throw a {@link MissingRepresentationTemplatePlaceholderException} if a representation template of the entity
     * output model contains a placeholder identifier which is not defined.
     *
     * @throws MissingRepresentationTemplatePlaceholderException
     */
    private void assertRepresentationTemplateIntegrity() throws MissingRepresentationTemplatePlaceholderException {
        for (RepresentationTemplateDefinition t : Arrays.asList(this.getGuiRepresentation(), this.getAtomRepresentation())) {
            for (String placeholderId : t.getPlaceholderIds()) {
                if (!this.attributeElements.containsKey(placeholderId)) {
                    throw new MissingRepresentationTemplatePlaceholderException(placeholderId, t.getTemplateString());
                }
            }
        }
    }

    public String getIdentifier() {
        return this.identifier;
    }

    public String getTitle() {
        return this.title;
    }

    public String getDescription() {
        return this.description;
    }

    public ConditionStatementDefinition getOutputCondition() {
        if (this.outputCondition == null) return ConditionStatementDefinition.emptyDefinition();
        return this.outputCondition;
    }

    public RepresentationTemplateDefinition getGuiRepresentation() {
        if (this.guiRepresentation == null) {
            return RepresentationTemplateDefinition.defaultTemplate(this.identifier, this.attributeElements.keySet());
        }
        return this.guiRepresentation;
    }

    public RepresentationTemplateDefinition getAtomRepresentation() {
        if (this.atomRepresentation == null) {
            return RepresentationTemplateDefinition.defaultTemplate(this.identifier, this.attributeElements.keySet());
        }
        return this.atomRepresentation;
    }

    public Map<String, OutputAttributeDefinition> getAttributeElements() {
        return this.attributeElements;
    }
}
