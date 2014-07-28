package eu.epitech.djyayo.jsonhelper;

import android.util.JsonReader;

import java.io.IOException;

public class JSONString extends JSONValue<String> {

    @Override
    public JSONValue<String> parseData(JsonReader reader) throws IOException {
        value = reader.nextString();
        return this;
    }

}
