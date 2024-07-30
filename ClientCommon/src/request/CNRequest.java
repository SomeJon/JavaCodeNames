package request;

import com.google.gson.Gson;
import constant.attribute.AttributeNames;
import constant.client.HttpCode;
import constant.client.ResponseType;
import dto.type.in.response.ResponseJoin;
import dto.type.out.server.DtoResponse;
import dto.type.out.server.DtoServerStatus;
import okhttp3.*;

import java.io.IOException;

import static prints.Prints.parseGamesStatus;
import static ui.input.InputHandling.errorPrint;

public class CNRequest {
    public static Request getRequestStats(String i_Url, String i_GetTypes) {
        String url = HttpUrl
                .parse(i_Url)
                .newBuilder()
                .addQueryParameter(AttributeNames.WANTED_STATUS, i_GetTypes)
                .build().toString();

        return new Request.Builder()
                .url(url)
                .get()
                .build();
    }

    public static Request putRequestJoin(String i_Url, Integer GameId, Integer TeamId, Integer RoleChoice) {
        ResponseJoin join = new ResponseJoin(GameId, TeamId, RoleChoice);
        String json = new Gson().toJson(join);

        RequestBody body = RequestBody.create(json, MediaType.parse("application/json"));

        return new Request.Builder()
                .url(i_Url)
                .put(body)
                .build();
    }

    public static void printStats(String i_Url, String i_GetTypes,
                                  OkHttpClient i_Client, boolean printCurrentPlayers){
        Request request = getRequestStats(i_Url, i_GetTypes);

        Call call = i_Client.newCall(request);

        try(Response response = call.execute()){
            if (response.code() == HttpCode.OK) {
                DtoResponse<DtoServerStatus> dtoResponse =
                        new Gson().fromJson(response.body().charStream(), ResponseType.DTO_RESPONSE_STATUS);
                String toPrint = parseGamesStatus(dtoResponse.getResult(), printCurrentPlayers);
                System.out.print(toPrint);
            } else if (response.code() == HttpCode.NOT_FOUND || response.code() == HttpCode.BAD_REQUEST) {
                DtoResponse<DtoServerStatus> dtoResponse =
                        new Gson().fromJson(response.body().charStream(), ResponseType.DTO_RESPONSE_STATUS);
                errorPrint(dtoResponse.getErrorMessage());
            } else
                errorPrint("An unexpected error occurred");
        } catch (IOException e) {
            errorPrint("IOException occurred: " + e.getMessage());
        }
    }
}
