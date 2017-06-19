package aspguidc.model.input.entity;

import aspguidc.exception.parsing.DefinitionFormatException;
import aspguidc.exception.parsing.InvalidIdentifierFormatException;
import aspguidc.exception.parsing.InvalidJsonObjectException;
import aspguidc.exception.parsing.MissingRepresentationTemplatePlaceholderException;
import aspguidc.helper.JsonHelper;
import aspguidc.model.input.InputElementDefinition;
import aspguidc.model.string.RepresentationTemplateDefinition;
import com.google.gson.JsonElement;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Model for an input element of the type entity input of a graphical user interface definition.
 */
public class EntityInputDefinition implements InputElementDefinition {
    private static final String objectName = "@entity_input";

    private final String identifier;
    private String title;
    private String description;
    private Integer inputCountMin;
    private Integer inputCountMax;
    private RepresentationTemplateDefinition guiRepresentation;
    private RepresentationTemplateDefinition atomRepresentation;
    private final Map<String, InputAttributeDefinition> attributeElements = new LinkedHashMap<>();

    private EntityInputDefinition(String identifier) throws InvalidIdentifierFormatException {
        JsonHelper.assertIdentifierIsValid(objectName, identifier);
        this.identifier = identifier;
    }

    /**
     * Create a new entity input model instance with the given identifier from the given json element.
     * The model is created by extracting each property of the given json element and setting it to the
     * model.
     *
     * @param identifier identifier of the entity input element
     * @param j          valid entity input json element according to the gui definition language specification
     * @return model instance
     * @throws DefinitionFormatException if the given json element is not valid according to the gui definition
     *                                   language specification
     */
    public static EntityInputDefinition fromJsonElement(String identifier, JsonElement j) throws DefinitionFormatException {
        Logger.getGlobal().info("[parsing] read entity input element '" + identifier + "'");

        if (!j.isJsonObject()) throw new InvalidJsonObjectException(objectName);

        EntityInputDefinition entityInput = new EntityInputDefinition(identifier);
        for (Map.Entry<String, JsonElement> entry : j.getAsJsonObject().entrySet()) {
            entityInput.setValueByJsonProperty(entry.getKey(), entry.getValue());
        }

        entityInput.assertRepresentationTemplateIntegrity();

        return entityInput;
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

        } else if ("input_count_min".equals(propertyKey)) {
            JsonHelper.assertPropertyIsInteger(objectName, propertyKey, propertyValue);
            this.inputCountMin = propertyValue.getAsJsonPrimitive().getAsInt();

        } else if ("input_count_max".equals(propertyKey)) {
            JsonHelper.assertPropertyIsInteger(objectName, propertyKey, propertyValue);
            this.inputCountMax = propertyValue.getAsJsonPrimitive().getAsInt();

        } else if ("gui_representation".equals(propertyKey)) {
            JsonHelper.assertPropertyIsString(objectName, propertyKey, propertyValue);
            this.guiRepresentation = RepresentationTemplateDefinition.fromGuiRepresentationString(propertyValue.getAsJsonPrimitive().getAsString());

        } else if ("atom_representation".equals(propertyKey)) {
            JsonHelper.assertPropertyIsString(objectName, propertyKey, propertyValue);
            this.atomRepresentation = RepresentationTemplateDefinition.fromAtomRepresentationString(propertyValue.getAsJsonPrimitive().getAsString());

        } else if (propertyKey.startsWith("@input_attribute:")) {
            String[] keyParts = propertyKey.split(":", 2);
            this.attributeElements.put(keyParts[1], InputAttributeDefinition.fromJsonElement(keyParts[1], propertyValue));

        } else {
            Logger.getGlobal().warning(String.format("unsupported property '%s' in '%s' object", propertyKey, objectName));
        }
    }

    /**
     * Throw a {@link MissingRepresentationTemplatePlaceholderException} if a representation template of the entity
     * input model contains a placeholder identifier which is not defined.
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

    public Integer getInputCountMin() {
        if (this.inputCountMin == null) return 0;
        return this.inputCountMin;
    }

    public Integer getInputCountMax() {
        if (this.inputCountMax == null) return Integer.MAX_VALUE;
        return this.inputCountMax;
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

    public Map<String, InputAttributeDefinition> getAttributeElements() {
        return this.attributeElements;
    }
}
