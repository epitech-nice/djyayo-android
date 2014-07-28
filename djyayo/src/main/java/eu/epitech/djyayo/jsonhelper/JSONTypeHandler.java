package eu.epitech.djyayo.jsonhelper;

import android.util.JsonToken;
import android.util.Log;

import java.util.HashMap;

public class JSONTypeHandler {

    private static JSONTypeHandler instance;

    private HashMap<JsonToken, Class<? extends JSONValue>> types;

    private JSONTypeHandler() {
        types = new HashMap<JsonToken, Class<? extends JSONValue>>();

        // Register RFC 4627 JSON types
        registerType(JsonToken.BEGIN_OBJECT, JSONObject.class);
        registerType(JsonToken.BEGIN_ARRAY, JSONArray.class);
        registerType(JsonToken.STRING, JSONString.class);
        registerType(JsonToken.NUMBER, JSONNumber.class);
        registerType(JsonToken.BOOLEAN, JSONBoolean.class);
        registerType(JsonToken.NULL, JSONNull.class);
    }

    public JSONValue instantiateType(JsonToken type) {
        JSONValue object;

        // Instantiate class using reflection, return null if it fails
        try {
            Class<? extends JSONValue> clazz = JSONTypeHandler.getInstance().getTypeClass(type);
            if (clazz == null) {
                Log.w("DJYayo", "Unsupported JSON token");
                object = null;
            } else {
                object = clazz.newInstance();
            }
        } catch (InstantiationException e) {
            return null;
        } catch (IllegalAccessException e) {
            return null;
        }

        return object;
    }

    public void registerType(JsonToken type, Class<? extends JSONValue> clazz) {
        types.put(type, clazz);
    }

    public Class<? extends JSONValue> getTypeClass(JsonToken type) {
        return types.get(type);
    }

    public static JSONTypeHandler getInstance() {
        if (instance == null)
            instance = new JSONTypeHandler();
        return instance;
    }

}
