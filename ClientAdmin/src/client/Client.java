package client;

import client.data.LinkConst;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import constant.client.ClientConst;
import constant.client.ResponseType;
import constant.response.Responses;
import dto.type.out.server.DtoResponse;
import okhttp3.*;
import client.data.ClientData;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.ConnectException;
import java.util.Map;


public class Client {
    private final ClientData Data;

    public Client(ClientData Data) {
        this.Data = Data;
    }

    private void StartClient() throws IOException {
        Request request = new Request.Builder()
                .url(ClientConst.SERVER_CONTEXT + LinkConst.LOGIN)
                .post(RequestBody.create(MediaType.parse("text/plain"), ""))
                .build();
        Call call = Data.HTTP_CLIENT.newCall(request);

        try {
            Response response = call.execute();

            DtoResponse<Map<String, Boolean>> dtoResponse =
                    new Gson().fromJson(response.body().charStream(), ResponseType.STRING_BOOLEAN);
            if (response.code() == 201) {
                Data.LoggedIn = true;
                System.out.println("--Logged in as an admin--\n");
                Data.buildMenu1Or2(!dtoResponse.getResult().get(Responses.NO_SUB_SERVERS));
            } else if (response.code() == 409) {
                System.out.println("--" + dtoResponse.getErrorMessage() + "--\n");
            } else System.out.println("An unexpected error occurred");
        } catch (ConnectException e) {
            System.out.println("Server could not be found");
        }

    }

    private void logOut() throws IOException {
        Request request = new Request.Builder()
                .url(ClientConst.SERVER_CONTEXT + ClientConst.LOGOUT)
                .delete()
                .build();

        Call call = Data.HTTP_CLIENT.newCall(request);
        try {
            Response response = call.execute();
            if (response.code() == 200) {
                System.out.println("Logged out...");
            } if (response.code() == 404) {
                System.out.println("Unexpected error, user was not found");
            }
        } catch(ConnectException e){
            System.out.println("Server could not be found");
        }
    }

    public void RunClient() throws IOException {
        StartClient();
        if(Data.LoggedIn) {
            Data.getMain().play();


            logOut();
        }
    }
}
