package admin.client;

import Adapter.AdapterAddon;
import admin.client.action.Action;
import admin.client.data.ClientData;
import admin.client.data.LinkConst;
import com.google.gson.Gson;
import console.ChoiceNotifier;
import console.Menu;
import console.MenuItem;
import constant.attribute.AttributeNames;
import constant.client.ClientConst;
import constant.client.HttpCode;
import constant.client.ResponseType;
import constant.response.Responses;
import dto.type.in.response.common.IntResponse;
import dto.type.in.response.load.LoadFilesResponse;
import dto.type.out.server.Choice.DtoServerGameChoice;
import dto.type.out.server.DtoResponse;
import okhttp3.*;
import request.CNRequest;
import ui.input.InputHandling;

import java.io.IOException;
import java.net.ConnectException;
import java.util.Map;

import static prints.Prints.parseGamesChoiceAdmin;
import static request.CNRequest.getRequestStats;
import static ui.input.InputHandling.errorPrint;


public class Client implements ChoiceNotifier {
    private final ClientData Data;
    private final static Gson gson = AdapterAddon.getGson();

    public Client(ClientData Data) {
        this.Data = Data;
    }

    private void StartClient() throws IOException {
        Request request = new Request.Builder()
                .url(ClientConst.SERVER_CONTEXT + LinkConst.LOGIN)
                .post(RequestBody.create(MediaType.parse("text/plain"), ""))
                .build();
        Call call = Data.HTTP_CLIENT.newCall(request);

        try(Response response = call.execute()) {
            if (response.code() == HttpCode.CREATED) {
                DtoResponse<Map<String, Boolean>> dtoResponse =
                    new Gson().fromJson(response.body().charStream(), ResponseType.STRING_BOOLEAN);
                Data.setLoggedIn(true);
                System.out.println("--Logged in as an admin--\n");
                Data.buildMenu1Or2(!dtoResponse.getResult().get(Responses.NO_SUB_SERVERS), this);
            } else if (response.code() == HttpCode.CONFLICT) {
                DtoResponse<Map<String, Boolean>> dtoResponse =
                    new Gson().fromJson(response.body().charStream(), ResponseType.STRING_BOOLEAN);
                errorPrint("--" + dtoResponse.getErrorMessage() + "--\n");
            } else errorPrint("An unexpected error occurred");
        } catch (ConnectException e) {
            errorPrint("Server could not be found");
        }

    }

    private void logOut() throws IOException {
        Request request = new Request.Builder()
                .url(ClientConst.SERVER_CONTEXT + ClientConst.LOGOUT)
                .delete()
                .build();

        Call call = Data.HTTP_CLIENT.newCall(request);
        try(Response response = call.execute()){
            if (response.code() == HttpCode.OK) {
                System.out.println("Logged out...");
            } if (response.code() == HttpCode.NOT_FOUND) {
                errorPrint("Unexpected error, user was not found");
            }
        } catch(ConnectException e){
            errorPrint("Server could not be found");
        }
    }

    public void RunClient() throws IOException {
        StartClient();
        if(Data.isLoggedIn()) {
            runNext();


            logOut();
        }
    }

    @Override
    public void Notify(Object sender) {
        Data.setCurrentAction(getCurrentAction(sender));
        Data.setCurrentInput(getInputHandling(sender));

        if(Data.getCurrentAction() != null) {
            switch (Data.getCurrentAction()) {
                case UPLOAD:
                    Data.setCurrentAction(null);
                    upload();
                    break;
                case SHOW_GAMES:
                    Data.setCurrentAction(null);
                    showGames();
                    break;
                case REFRESH:
                    updateActiveGames();
                    if(Data.getCurrentChoices() != null) {
                        String toPrint = parseGamesChoiceAdmin(Data.getCurrentChoices());
                    }
                    break;
                case FETCH:
                    fetchStatus();
                    break;
            }
        }
        Data.setCurrentAction(null);

        if(Data.getCurrentInput() != null) {
            dto.type.in.response.Response resp = null;
            switch (Data.getCurrentInput()) {
                case FILE_PATH:
                    resp = new LoadFilesResponse();
                    Data.activateCurrentInput(resp);
                    loadFiles();
                    break;
                case GET_GAME_ID:
                    resp = new IntResponse();
                    Data.activateCurrentInput(resp);
                    enterGameView();
                    break;
            }
        }
    }

    private void fetchStatus() {
        //todo
    }

    private void showGames(){
        CNRequest.printStats(
                ClientConst.SERVER_CONTEXT + LinkConst.GET_STATUSES,
                        AttributeNames.ALL, Data.HTTP_CLIENT, false);
    }

    private void loadFiles(){
        LoadFilesResponse resp = (LoadFilesResponse) Data.getCurrentResponse();
        Data.setCurrentResponse(null);
        if(resp.receivedResponse()) {
            Data.getMain().getCurrentMenu().getMenuItems().get(0)
                    .setItemText("Load a different xml path\n" +
                            "   Current Loaded Files:\n" +
                            "       Xml file: " + resp.getXmlFileName() + "\n" +
                            "       Txt file: " + resp.getTxtFileName());
            Data.setWaitingTxt(resp.getTxtFile());
            Data.setWaitingXml(resp.getXmlFile());
            System.out.println("Loaded: \"" + resp.getXmlFileName() + "\" and \"" + resp.getTxtFileName() + "\" into the client successfully");
        }
    }

    private void upload() {
        if (Data.getWaitingTxt() != null && Data.getWaitingXml() != null) {
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart(AttributeNames.XML_FILE, Data.getWaitingXml().getName(),
                            RequestBody.create(Data.getWaitingXml(), MediaType.parse("application/octet-stream")))
                    .addFormDataPart(AttributeNames.TXT_File, Data.getWaitingTxt().getName(),
                            RequestBody.create(Data.getWaitingTxt(), MediaType.parse("application/octet-stream")))
                    .build();

            Request request = new Request.Builder()
                    .url(ClientConst.SERVER_CONTEXT + LinkConst.UPLOAD)
                    .method("POST", requestBody)
                    .build();

            Call call = Data.HTTP_CLIENT.newCall(request);

            try (Response response = call.execute()) {
                if (response.body() != null) {
                    if (response.code() == HttpCode.CREATED) {
                        DtoResponse<Map<String, String>> dtoResponse =
                            new Gson().fromJson(response.body().charStream(), ResponseType.STRING_STRING);
                        System.out.println(dtoResponse.getResult().get(Responses.CREATED));
                        Data.setWaitingTxt(null);
                        Data.setWaitingXml(null);
                        Data.getMain().getCurrentMenu().getMenuItems()
                                .get(0).setItemText("Chose a file path to a .xml file");
                        //Data.getMain().previousMenu();
                        if(!Data.isMenu2())
                            Data.addFirstOptions(this);

                    } else if (response.code() == HttpCode.BAD_REQUEST || response.code() == HttpCode.UNAUTHORIZED) {
                        DtoResponse<Map<String, String>> dtoResponse =
                            new Gson().fromJson(response.body().charStream(), ResponseType.STRING_STRING);
                        errorPrint(dtoResponse.getResult().get(Responses.ERROR_ENCOUNTERED));
                    }
                } else {
                    errorPrint("Unexpected error occurred");
                }
            } catch (IOException e) {
                System.out.println("Upload failed: " + e.getMessage());
            }
        }else errorPrint("Please first enter an xml file path before trying to upload into the server!");

    }

    private void updateActiveGames(){
        updateChoices(ClientConst.SERVER_CONTEXT + LinkConst.GET_CHOICE_STATUSES,
                AttributeNames.ACTIVE, Data.HTTP_CLIENT);
    }

    public void updateChoices(String i_Url, String i_GetTypes,
                              OkHttpClient i_Client){
        Request request = getRequestStats(i_Url, i_GetTypes);

        Call call = i_Client.newCall(request);

        try(Response response = call.execute()){
            if (response.code() == HttpCode.OK) {
                DtoResponse<DtoServerGameChoice> dtoResponse =
                        gson.fromJson(response.body().charStream(), ResponseType.DTO_RESPONSE_CHOICE);
                Data.setCurrentChoices(dtoResponse.getResult());
            } else if (response.code() == HttpCode.NOT_FOUND || response.code() == HttpCode.BAD_REQUEST) {
                DtoResponse<DtoServerGameChoice> dtoResponse =
                        gson.fromJson(response.body().charStream(), ResponseType.DTO_RESPONSE_STATUS);
                errorPrint(dtoResponse.getErrorMessage());
                Data.getMain().setCurrentMenu(Data.getMain().getStartMenu());
                Data.setCurrentChoices(null);
            }
        } catch (IOException e) {
            errorPrint("IOException occurred: " + e.getMessage());
            Data.getMain().cancelMenuChange();
        }
    }

    private void enterGameView(){
        int GameId = ((IntResponse)Data.getCurrentResponse()).getInt();
        Data.setCurrentResponse(null);
        Request request = CNRequest.getRequestCheckGame(
                ClientConst.SERVER_CONTEXT + LinkConst.CONNECT_GAME, GameId);

        Call call = Data.HTTP_CLIENT.newCall(request);

        try(Response response = call.execute()){
            if(response.code() == HttpCode.OK) {
                System.out.println("Entered game: " + GameId); //todo change to name
                Data.buildMenu3(this);
            }
        } catch (IOException e){
            errorPrint("IOException occurred " +
                    "while trying to connect to the server");
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

    private void runNext(){
        Menu NextToRun = Data.getMain().getCurrentMenu();

        Data.getMain().play();

    }
}
