package eu.epitech.djyayo.jsonhelper;

import android.util.JsonReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

public class JSONHelper {

    private JSONValue entry;

    public JSONHelper() {
        this(null);
    }

    public JSONHelper(JSONValue entry) {
        this.entry = entry;
    }

    /**
     * Used for recursively caching the tokens of a JSON stream.
     * @param is The JSON stream to cache the tokens from
     * @return true if the caching was successful, false otherwise
     */
    public boolean cacheStream(InputStream is) {
        JsonReader reader = new JsonReader(new InputStreamReader(is));

        try {
            entry = JSONTypeHandler.getInstance().instantiateType(reader.peek());
            if (entry != null) {
                entry.parseData(reader);
                reader.close();
                return true; // :)
            }
            reader.close();
            return false;
        } catch (IOException e) {
            return false;
        }
    }

    public JSONValue get(Object... chain) {
        JSONValue value = entry;

        for(Object cur : chain) {
            // Safe check
            if (value == null)
                return null;

            // Let's get serious
            Object obj = value.getValue();
            if (cur instanceof String && obj instanceof HashMap) {
                HashMap curMap = (HashMap) obj;
                obj = curMap.get(cur);
            } else if (cur instanceof Integer && obj instanceof ArrayList) {
                ArrayList curList = (ArrayList) obj;
                obj = curList.get((Integer)cur);
            } else {
                return null;
            }

            // Convert object to value
            if (obj instanceof JSONValue) {
                value = (JSONValue) obj;
            } else {
                return null;
            }
        }
        return value;
    }

    /**
     * Returns a value from a chain. This function is type safe.<br />
     * A chain is a sequence of int and String values, representing the keys of a JSONArray or a
     * JSONObject, respectively. If the sequence is invalid, i.e. it contains a Object that isn't
     * a String or a int, this function will return null. If the sequence is broken, i.e. you try
     * to access a non-containing or a non-existing JSONValue, this function will return null too.
     * @param chain An array for the chain sequence
     * @return The Object at the end of the chain, null if the chain is invalid or broken
     */
    public Object getValue(Object... chain) {
        return get(chain).getValue();
    }

    public JSONValue getEntry() {
        return entry;
    }

}
