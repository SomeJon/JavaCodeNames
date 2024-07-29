package codenames;

import com.google.gson.Gson;
import constant.attribute.AttributeNames;
import data.server.controllers.ServerManager;
import dto.type.out.server.Choice.DtoSubServerChoice;
import dto.type.out.server.HasActive;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static Adapter.AdapterAddon.getGson;

public class Utils {
    private final static Gson gson = getGson();

    public static String toJson(Object toWrite) {
        return gson.toJson(toWrite);
    }

    public static <T extends HasActive> Stream<T> filterByState(Stream<T> i_Stream, String requestedState) {
        return i_Stream.filter(status -> {
                    switch (requestedState) {
                        case AttributeNames.ACTIVE:
                            return status.isActive();
                        case AttributeNames.PENDING:
                            return !status.isActive();
                        default:
                            return true;
                    }
                });
    }

    public static List<DtoSubServerChoice> getFilteredChoices(ServerManager manager, String requestedState) {
        List<DtoSubServerChoice> subServerChoices = manager.getServerGameChoices().getSubServerChoices();
        return Utils
                    .filterByState(subServerChoices.stream(), requestedState)
                    .collect(Collectors.toList());
    }
}
