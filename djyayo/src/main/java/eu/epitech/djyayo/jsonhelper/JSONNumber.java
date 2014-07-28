package eu.epitech.djyayo.jsonhelper;

import android.util.JsonReader;

import java.io.IOException;

public class JSONNumber extends JSONValue<Double> {

    @Override
    public JSONValue<Double> parseData(JsonReader reader) throws IOException {
        value = reader.nextDouble();
        return this;
    }

}
