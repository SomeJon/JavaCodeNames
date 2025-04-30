package Adapter;

import com.google.gson.*;
import message.Message;
import message.SystemMessage;
import message.UserMessage;

import java.lang.reflect.Type;

public class MessageTypeAdapter implements JsonSerializer<Message>, JsonDeserializer<Message> {
    @Override
    public JsonElement serialize(Message src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", src.getClass().getSimpleName());
        jsonObject.add("data", context.serialize(src));
        return jsonObject;
    }

    @Override
    public Message deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String type = jsonObject.get("type").getAsString();
        JsonElement data = jsonObject.get("data");

        try {
            switch (type) {
                case "UserMessage":
                    return context.deserialize(data, UserMessage.class);
                case "SystemMessage":
                    return context.deserialize(data, SystemMessage.class);
                default:
                    throw new JsonParseException("Unknown element type: " + type);
            }
        } catch (Exception e) {
            throw new JsonParseException(e);
        }
    }
}
