package user.client;

import console.ChoiceNotifier;
import console.MenuItem;
import constant.attribute.AttributeNames;
import dto.type.in.response.StringResponse;
import request.CNRequest;
import ui.input.InputHandling;
import user.client.action.Action;
import user.client.data.LinkConst;
import com.google.gson.Gson;
import constant.client.ClientConst;
import constant.client.HttpCode;
import constant.client.ResponseType;
import dto.type.out.server.DtoResponse;
import okhttp3.*;
import user.client.data.ClientData;

import java.io.IOException;
import java.net.ConnectException;
import java.util.Map;

import static ui.input.InputHandling.errorPrint;

public class Client2 implements ChoiceNotifier {
    private final ClientData Data;

    public Client2(ClientData Data) {
        this.Data = Data;
    }

    private void login(){
        String url = HttpUrl.parse(ClientConst.SERVER_CONTEXT +
                        LinkConst.LOGIN).newBuilder().addQueryParameter(AttributeNames.USERNAME,
                ((StringResponse)Data.CurrentResponse).getStr()).build().toString();
        Request request = new Request.Builder()
                .url(url)
                .put(RequestBody.create(MediaType.parse("text/plain"), ""))
                .build();
        Call call = Data.HTTP_CLIENT.newCall(request);

        try(Response response = call.execute()){
            if (response.code() == HttpCode.CREATED) {
                Data.LoggedIn = true;
                System.out.println("--Logged in as " + ((StringResponse) Data.CurrentResponse).getStr() + "--\n");
                Data.buildMenu2(this);
            } else if (response.code() == HttpCode.CONFLICT || response.code() == HttpCode.BAD_REQUEST) {
                DtoResponse<Map<String, Boolean>> dtoResponse =
                    new Gson().fromJson(response.body().charStream(), ResponseType.STRING_BOOLEAN);
                System.out.println("--" + dtoResponse.getErrorMessage() + "--\n");
            } else errorPrint("An unexpected server error occurred");
        } catch (ConnectException e) {
            errorPrint("Server could not be found");
        } catch (IOException e) {
            errorPrint("Unexpected IOException occurred");
        }
        finally {
            Data.CurrentInput = null;
            Data.CurrentResponse = null;
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
        Data.buildMenu1(this);
        Data.getMain().play();

        if(Data.LoggedIn)
            logOut();
    }

    @Override
    public void Notify(Object sender) {
        Data.CurrentAction = getCurrentAction(sender);
        Data.CurrentInput = getInputHandling(sender);

        if(Data.CurrentAction != null){
            switch (Data.CurrentAction) {
                case SHOW_GAMES:
                    CNRequest.printStats(ClientConst.SERVER_CONTEXT + LinkConst.GET_STATUSES,
                        AttributeNames.ALL, Data.HTTP_CLIENT, true);
                    break;
                case WATCH_GAMES:
                    //todo
            }
            Data.CurrentAction = null;
        }

        if(Data.CurrentInput != null){
            dto.type.in.response.Response resp = null;
            switch (Data.CurrentInput) {
                case GET_NAME:
                    resp = new StringResponse();
                    Data.activateCurrentInput(resp);
                    login();
                    break;
                case GET_GAME_ID:
                    //todo
            }
            Data.CurrentInput = null;
        }
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
