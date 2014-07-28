package eu.epitech.djyayo.jsonhelper;

import android.util.JsonReader;

import java.io.IOException;

public abstract class JSONValue<T> {

    protected T value;

    public JSONValue() {
        this(null);
    }

    // Not very useful for reading, but JSON writing could be implementer later...
    public JSONValue(T value) {
        this.value = value;
    }

    public abstract JSONValue<T> parseData(JsonReader reader) throws IOException;

    public T getValue() {
        return value;
    }

}
