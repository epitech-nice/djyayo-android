package eu.epitech.djyayo.jsonhelper;

import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

public class JSONArray extends JSONValue<ArrayList<JSONValue>> {

    @Override
    public JSONArray parseData(JsonReader reader) throws IOException {
        JsonToken next;
        value = new ArrayList<JSONValue>();

        reader.beginArray();
        while (reader.hasNext() && (next = reader.peek()) != JsonToken.END_ARRAY) {
            JSONValue object = JSONTypeHandler.getInstance().instantiateType(next);
            if (object != null) {
                Log.i("JSON", "Adding value to array");
                value.add(object.parseData(reader));
            }
        }
        reader.endArray();

        return this;
    }

}
