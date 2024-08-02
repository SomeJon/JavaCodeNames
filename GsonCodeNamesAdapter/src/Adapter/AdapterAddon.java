package Adapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dto.Dto;
import dto.type.out.board.card.DtoGroupCard;
import dto.type.out.board.card.DtoGroupNeutral;
import dto.type.out.board.card.DtoGroupTeam;
import message.Message;
import message.SystemMessage;
import message.UserMessage;

public class AdapterAddon {
    public static Gson getGson() {
        RuntimeTypeAdapterFactory<DtoGroupCard> dtoGroupCardAdapterFactory = RuntimeTypeAdapterFactory
                .of(DtoGroupCard.class, "type")
                .registerSubtype(DtoGroupTeam.class, "DtoGroupTeam")
                .registerSubtype(DtoGroupNeutral.class, "DtoGroupNeutral");

        RuntimeTypeAdapterFactory<Message> messageAdapterFactory = RuntimeTypeAdapterFactory
                .of(Message.class, "type")
                .registerSubtype(UserMessage.class, "UserMessage")
                .registerSubtype(SystemMessage.class, "SystemMessage");

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder
                .registerTypeAdapterFactory(dtoGroupCardAdapterFactory)
                .registerTypeAdapterFactory(messageAdapterFactory)
                .registerTypeAdapter(Dto.class, new DtoTypeAdapter());

        return gsonBuilder.create();
    }
}
