package Adapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import message.Message;

public class AdapterAddon {
    public static Gson getGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Message.class, new MessageTypeAdapter());
        Gson gson = gsonBuilder.create();

        return gson;
    }
}