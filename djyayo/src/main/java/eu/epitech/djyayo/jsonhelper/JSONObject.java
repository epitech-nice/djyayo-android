package eu.epitech.djyayo.jsonhelper;

import android.util.JsonReader;
import android.util.JsonToken;

import java.io.IOException;
import java.util.HashMap;

public class JSONObject extends JSONValue<HashMap<String, JSONValue>> {

    @Override
    public JSONObject parseData(JsonReader reader) throws IOException {
        JsonToken next;
        value = new HashMap<String, JSONValue>();

        reader.beginObject();
        while (reader.hasNext() && (next = reader.peek()) != JsonToken.END_OBJECT) {
            String name = reader.nextName();

            next = reader.peek();
            JSONValue object = JSONTypeHandler.getInstance().instantiateType(next);
            if (object != null)
                value.put(name, object.parseData(reader));
        }
        reader.endObject();

        return this;
    }

}
