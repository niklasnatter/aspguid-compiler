package aspguidc.helper;

import aspguidc.exception.parsing.InvalidIdentifierFormatException;
import aspguidc.exception.parsing.InvalidPropertyTypeException;
import com.google.gson.JsonElement;

import java.util.regex.Pattern;

/**
 * Helper class which provides static methods regarding to the handling of json elements.
 */
public class JsonHelper {
    /**
     * Throw a {@link InvalidPropertyTypeException} if the given property value is not of the type string.
     *
     * @param objectName    name of the parent object of the property which is passed to the exception
     * @param propertyName  name of the property which is passed to the exception
     * @param propertyValue value which is checked
     * @throws InvalidPropertyTypeException
     */
    public static void assertPropertyIsString(String objectName, String propertyName, JsonElement propertyValue) throws InvalidPropertyTypeException {
        if (!propertyValue.isJsonPrimitive() || !propertyValue.getAsJsonPrimitive().isString()) {
            throw new InvalidPropertyTypeException(objectName, propertyName, "string");
        }
    }

    /**
     * Throw a {@link InvalidPropertyTypeException} if the given property value is not of the type integer.
     *
     * @param objectName    name of the parent object of the property which is passed to the exception
     * @param propertyName  name of the property which is passed to the exception
     * @param propertyValue value which is checked
     * @throws InvalidPropertyTypeException
     */
    public static void assertPropertyIsInteger(String objectName, String propertyName, JsonElement propertyValue) throws InvalidPropertyTypeException {
        if (!propertyValue.isJsonPrimitive() || !propertyValue.getAsJsonPrimitive().isNumber()) {
            throw new InvalidPropertyTypeException(objectName, propertyName, "integer");
        }
    }


    /**
     * Throw a {@link InvalidIdentifierFormatException} if the given identifier is not a valid identifier.
     *
     * @param objectName name of the object assigned to the identifier which is passed to the exception
     * @param identifier identifier which is checked
     * @throws InvalidIdentifierFormatException
     */
    public static void assertIdentifierIsValid(String objectName, String identifier) throws InvalidIdentifierFormatException {
        if (!Pattern.matches("^[a-z][a-zA-Z0-9_]+$", identifier)) {
            throw new InvalidIdentifierFormatException(objectName, identifier);
        }
    }
}
