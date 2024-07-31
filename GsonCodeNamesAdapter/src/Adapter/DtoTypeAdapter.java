package Adapter;

import com.google.gson.*;
import dto.Dto;
import dto.type.out.board.card.DtoGroupCard;
import dto.type.out.board.card.DtoGroupNeutral;
import dto.type.out.board.card.DtoGroupTeam;
import dto.type.out.server.game.*;

import java.lang.reflect.Type;

public class DtoTypeAdapter implements JsonSerializer<Dto>, JsonDeserializer<Dto> {
    @Override
    public JsonElement serialize(Dto src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", src.getClass().getSimpleName());
        jsonObject.add("data", context.serialize(src));
        return jsonObject;
    }

    @Override
    public Dto deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String type = jsonObject.get("type").getAsString();
        JsonElement data = jsonObject.get("data");

        try {
            switch (type) {
                case "DtoBoardUpdate":
                    return context.deserialize(data, DtoBoardUpdate.class);
                case "DtoGameUpdate":
                    return context.deserialize(data, DtoGameUpdate.class);
                case "DtoSingleTurnUpdate":
                    return context.deserialize(data, DtoSingleTurnUpdate.class);
                case "DtoEndResult":
                    return context.deserialize(data, DtoEndResult.class);
                case "DtoGroupTeam":
                    return context.deserialize(data, DtoGroupTeam.class);
                case "DtoGroupNeutral":
                    return context.deserialize(data, DtoGroupNeutral.class);
                default:
                    throw new JsonParseException("Unknown element type: " + type);
            }
        } catch (Exception e) {
            throw new JsonParseException(e);
        }
    }
}
