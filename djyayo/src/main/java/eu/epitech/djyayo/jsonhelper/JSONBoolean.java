package eu.epitech.djyayo.jsonhelper;

import android.util.JsonReader;

import java.io.IOException;

public class JSONBoolean extends JSONValue<Boolean> {

    @Override
    public JSONValue<Boolean> parseData(JsonReader reader) throws IOException {
        value = reader.nextBoolean();
        return this;
    }

}
