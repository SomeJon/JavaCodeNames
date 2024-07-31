package Adapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dto.Dto;
import dto.type.out.board.card.*;

public class AdapterAddon {
    public static Gson getGson() {
        RuntimeTypeAdapterFactory<DtoGroupCard> dtoGroupCardAdapterFactory = RuntimeTypeAdapterFactory
                .of(DtoGroupCard.class, "type")
                .registerSubtype(DtoGroupTeam.class, "DtoGroupTeam")
                .registerSubtype(DtoGroupNeutral.class, "DtoGroupNeutral");

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder
                .registerTypeAdapterFactory(dtoGroupCardAdapterFactory)
                .registerTypeAdapter(Dto.class, new DtoTypeAdapter());
        Gson gson = gsonBuilder.create();
        return gson;
    }
}
