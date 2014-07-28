package eu.epitech.djyayo.system;

public class SysUtils {

    public static <T> T safeCast(Object obj, Class<T> type) {
        if (obj != null && type.isInstance(obj))
            return (type.cast(obj));
        return null;
    }

}
