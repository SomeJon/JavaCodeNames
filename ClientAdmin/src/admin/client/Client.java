package admin.client;

import admin.client.action.Action;
import admin.client.data.ClientData;
import admin.client.data.LinkConst;
import com.google.gson.Gson;
import console.ChoiceNotifier;
import console.MenuItem;
import constant.client.ClientConst;
import constant.client.HttpCode;
import constant.client.ResponseType;
import constant.response.Responses;
import dto.type.out.server.DtoResponse;
import okhttp3.*;
import ui.input.InputHandling;

import java.io.IOException;
import java.net.ConnectException;
import java.util.Map;


public class Client implements ChoiceNotifier {
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
            if (response.code() == HttpCode.CREATED) {
                Data.LoggedIn = true;
                System.out.println("--Logged in as an admin--\n");
                Data.buildMenu1Or2(!dtoResponse.getResult().get(Responses.NO_SUB_SERVERS), this);
            } else if (response.code() == HttpCode.CONFLICT) {
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
            if (response.code() == HttpCode.OK) {
                System.out.println("Logged out...");
            } if (response.code() == HttpCode.NOT_FOUND) {
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

    @Override
    public void Notify(Object sender) {
        Data.CurrentAction = getCurrentAction(sender);
        Data.CurrentInput = getInputHandling(sender);
    }

    private Action getCurrentAction(Object sender) {
        Action ret = null;

        if (((MenuItem) sender).getItemValue() instanceof Action) {
            ret = (Action) ((MenuItem) sender).getItemValue();
        }

        return ret;
    }

    private InputHandling getInputHandling(Object sender) {
        InputHandling ret = null;

        if (((MenuItem) sender).getItemValue() instanceof InputHandling) {
            ret = (InputHandling) ((MenuItem) sender).getItemValue();
        }

        return ret;
    }
}
