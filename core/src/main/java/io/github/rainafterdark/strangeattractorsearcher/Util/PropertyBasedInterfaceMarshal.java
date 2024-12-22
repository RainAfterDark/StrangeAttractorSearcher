package io.github.rainafterdark.strangeattractorsearcher.Util;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

// Thank you so much https://stackoverflow.com/questions/3629596/deserializing-an-abstract-class-in-gson
public class PropertyBasedInterfaceMarshal implements
    JsonSerializer<Object>, JsonDeserializer<Object> {

    private static final String CLASS_META_KEY = "@type";
    private final Map<String, String> classMap = new HashMap<>();

    public PropertyBasedInterfaceMarshal(Class<?>... classes) {
        for (Class<?> clazz : classes) {
            classMap.put(clazz.getSimpleName(), clazz.getCanonicalName());
        }
    }

    @Override
    public Object deserialize(JsonElement jsonElement, Type type,
            JsonDeserializationContext jsonDeserializationContext)
            throws JsonParseException {
        JsonObject jsonObj = jsonElement.getAsJsonObject();
        String simpleClassName = jsonObj.get(CLASS_META_KEY).getAsString();
        String className = classMap.get(simpleClassName);
        if (className == null) {
            throw new JsonParseException("Unknown class: " + simpleClassName);
        }
        try {
            Class<?> clz = Class.forName(className);
            return jsonDeserializationContext.deserialize(jsonElement, clz);
        } catch (ClassNotFoundException e) {
            throw new JsonParseException(e);
        }
    }

    @Override
    public JsonElement serialize(Object object, Type type,
            JsonSerializationContext jsonSerializationContext) {
        JsonElement jsonEle = jsonSerializationContext.serialize(object, object.getClass());
        jsonEle.getAsJsonObject().addProperty(CLASS_META_KEY,
                object.getClass().getSimpleName());
        return jsonEle;
    }
}
