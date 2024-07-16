package codenames;

import com.google.gson.Gson;

public class Utils {
    public static String toJson(Object toWrite) {
        return new Gson().toJson(toWrite);
    }
}
