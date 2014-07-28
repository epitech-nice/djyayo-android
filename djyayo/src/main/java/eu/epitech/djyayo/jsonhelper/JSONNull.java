package eu.epitech.djyayo.jsonhelper;

import android.util.JsonReader;

import java.io.IOException;

public class JSONNull extends JSONValue<Void> {

    @Override
    public JSONValue<Void> parseData(JsonReader reader) throws IOException {
        reader.nextNull();
        return this;
    }

}
