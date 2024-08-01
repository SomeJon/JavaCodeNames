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
import java.util.List;

import static prints.Prints.parseGamesStatus;
import static ui.input.InputHandling.errorPrint;

public class CNRequest {
    public static <T> Request requestWithObject(String url, T object, String method) {
        Gson gson = new Gson();
        String json = gson.toJson(object);

        MediaType JSON = MediaType.get("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(json, JSON);

        Request.Builder builder = new Request.Builder()
                .url(url)
                .method(method.toUpperCase(), body);  // Use the method specified (POST or PUT)

        return builder.build();
    }

    public static Request getRequestCheckGame(String i_Url, Integer GameId) {
        String url = HttpUrl
                .parse(i_Url)
                .newBuilder()
                .addQueryParameter(AttributeNames.WANTED_GAME, GameId.toString())
                .build().toString();

        return new Request.Builder()
                .url(url)
                .get()
                .build();
    }

    public static Request getRequestQueryParameter(String baseUrl, String queryKey, String queryValue) {
        HttpUrl.Builder urlBuilder = HttpUrl
                .parse(baseUrl)
                .newBuilder();

        if (queryKey != null && queryValue != null) {
            urlBuilder.addQueryParameter(queryKey, queryValue);
        }

        String url = urlBuilder.build().toString();

        return new Request.Builder()
                .url(url)
                .get()
                .build();
    }

    public static Request getRequestQueryParametersList(String baseUrl, List<String> queryKeys, List<String> queryValues) {
        HttpUrl.Builder urlBuilder = HttpUrl
                .parse(baseUrl)
                .newBuilder();

        if (queryKeys != null && queryValues != null) {
            if (queryKeys.size() != queryValues.size()) {
                throw new IllegalArgumentException("Query keys and values lists must have the same size");
            }

            for (int i = 0; i < queryKeys.size(); i++) {
                urlBuilder.addQueryParameter(queryKeys.get(i), queryValues.get(i));
            }
        }

        String url = urlBuilder.build().toString();

        return new Request.Builder()
                .url(url)
                .get()
                .build();
    }

    public static Request getRequest(String baseUrl) {
        return getRequestQueryParametersList(baseUrl, null, null);
    }

    public static void printStats(String i_Url, String i_GetTypes,
                                  OkHttpClient i_Client, boolean printCurrentPlayers){
        Request request = getRequestQueryParameter(i_Url, AttributeNames.WANTED_STATUS, i_GetTypes);

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
