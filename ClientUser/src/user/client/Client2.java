package user.client;

import console.ChoiceNotifier;
import console.MenuItem;
import constant.attribute.AttributeNames;
import dto.type.in.response.common.IntResponse;
import dto.type.in.response.common.StringResponse;
import dto.type.out.server.Choice.DtoServerGameChoice;
import dto.type.out.server.Choice.DtoServerTeamChoice;
import dto.type.out.server.Choice.DtoSubServerChoice;
import dto.type.out.server.DtoServerTeam;
import request.CNRequest;
import ui.input.InputHandling;
import user.client.action.Action;
import user.client.data.GameData;
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
import java.util.Optional;

import static prints.Prints.parseGamesChoice;
import static request.CNRequest.*;
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
                String str = ((StringResponse) Data.CurrentResponse).getStr();
                System.out.println("--Logged in as " + str + "--\n");
                Data.getMain().getStartMenu().setMenuName("User Client - " + str);
                Data.buildMenu2(this);
            } else if (response.code() == HttpCode.CONFLICT || response.code() == HttpCode.BAD_REQUEST) {
                DtoResponse<Map<String, Boolean>> dtoResponse =
                    new Gson().fromJson(response.body().charStream(), ResponseType.STRING_BOOLEAN);
                errorPrint(dtoResponse.getErrorMessage());
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
                case REFRESH: //todo: (Low priority) make it so you can refresh + keep choices that fits
                    Data.buildMenu20(this);
                case SHOW_PENDING_GAMES:
                    showPendingGames();
                    break;
                case CLEAN_CHOICE:
                    if(Data.getMain().getCurrentMenu().getMenuName().equalsIgnoreCase("Join Game"))
                        Data.buildMenu20(this);
                    break;
                case ENTER_GAME:
                    joinGame();
                    break;
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
                    getGameId();
                    break;
                case GET_TEAM_ID:
                    getTeamId();
                    break;
                case GET_ROLE:
                    getRole();
                    break;
            }
            Data.CurrentInput = null;
        }
    }

    private void waitForGame(){
        boolean check = didGameStart();

        while(!check){
            updateData();
            check = Data.CurrentSubChoice.isActive();
            sleepForSomeTime(1000);
        }

        Data.buildMenu3(this);
        //todo: start game update thread with some infos
        //todo: start chat update thread
    }

    private boolean didGameStart(){
        boolean ret = false;

        if(Data.NewUpdate) {
            Request request = getRequestCheckGame(ClientConst.SERVER_CONTEXT + LinkConst.CHECK_GAME,
                    Data.GameData.getGameId());

            Call call = Data.HTTP_CLIENT.newCall(request);

            try (Response response = call.execute()) {
                if (response.code() == HttpCode.CREATED) {
                    ret = true;
                } else
                    errorPrint("Unexpected server update error occurred!");
            } catch (IOException e) {
                errorPrint("IOException occurred: " + e.getMessage());
            }
        }

        return ret;
    }

    private void updateData(){
        Request request = getRequestCheckGame(ClientConst.SERVER_CONTEXT + LinkConst.UPDATE_GAME_STATUS,
                Data.GameData.getGameId());

        Call call = Data.HTTP_CLIENT.newCall(request);

        try(Response response = call.execute()) {
            if (response.code() == HttpCode.OK) {
                Data.CurrentSubChoice = new Gson().fromJson(response.body().charStream(),
                        DtoSubServerChoice.class);
                printUpdate();
                Data.NewUpdate = true;
            } else if (response.code() == HttpCode.NO_CONTENT) {
                Data.NewUpdate = false;
            } else
                errorPrint("Unexpected server update error occurred!");
        } catch (IOException e){
            errorPrint("IOException occurred: " + e.getMessage());
        }
    }

    private void printUpdate(){
        StringBuilder toPrint = new StringBuilder();

        for(DtoServerTeamChoice teamChoice : Data.CurrentSubChoice.getTeamChoices()){
            DtoServerTeam team = teamChoice.getTeamInfo();
            toPrint.append(team.getTeam().getName())
                    .append(" - Roles state(Connected/Needed): ")
                    .append(parseRolesShort(team)).append("\n");
        }

        System.out.println(toPrint);
    }

    private void joinGame(){
        Request request = putRequestJoin(ClientConst.SERVER_CONTEXT + LinkConst.JOIN_GAME,
                Data.GameData.getGameId(), Data.GameData.getTeamId(), Data.GameData.getRole().GetChoice());

        Call call = Data.HTTP_CLIENT.newCall(request);

        try(Response response = call.execute()){
            String str = null;
            if(response.code() == HttpCode.OK) {
                str = "Joined game " + Data.GameData.getGameName() +
                        "!\nPlease wait for other users to join...";
                System.out.println(str);
                waitForGame();
            }
            else if (response.code() == HttpCode.GONE || response.code() == HttpCode.BAD_REQUEST
                    || response.code() == HttpCode.UNAUTHORIZED) {
                assert response.body() != null;
                str = response.body().string();
                System.out.println(str);
            }
        } catch (IOException e) {
            errorPrint("IOException occurred: " + e.getMessage());
        }
    }

    private void getRole(){
        IntResponse resp = new IntResponse();
        printRoleShortInfo();
        Data.activateCurrentInput(resp);

        int roleChoice = ((IntResponse) Data.CurrentResponse).getInt();
        Data.CurrentResponse = null;

        DtoServerTeam team = Data.CurrentTeamChoice.getTeamInfo();

        boolean available;
        switch (roleChoice) {
            case 0:
                available = team.getNumOfDefiners() != team.getConnectedDefiners();
                if (available) {
                    Data.GameData.setRole(GameData.roleChoice.IDENTIFIER);
                }
                break;
            case 1:
                available = team.getConnectedGuessers() != team.getNumOfGuessers();
                if (available) {
                    Data.GameData.setRole(GameData.roleChoice.GUESSER);
                }
                break;
            default:
                available = false;
                break;
        }

        if(available){
            String newText = "Switch Role - Current Choice: " + Data.GameData.getRole().toString();
            Data.buildMenu23(this, newText);
        } else{
            errorPrint("Chosen role is full!");
        }
    }

    private void getTeamId(){
        IntResponse resp = new IntResponse();
        printTeamShortInfo();
        Data.activateCurrentInput(resp);

        int teamId = ((IntResponse) Data.CurrentResponse).getInt();
        Data.CurrentResponse = null;

        Optional<DtoServerTeamChoice> team = Data.CurrentSubChoice.getTeamChoices()
                .stream()
                .filter(T -> T.getTeamId() == teamId)
                .findFirst();

        if(team.isPresent()){
            if(!checkTeamFull(team.get().getTeamInfo())) {
                Data.CurrentTeamChoice = team.get();
                Data.GameData.setTeamId(Data.CurrentTeamChoice.getTeamId());
                Data.GameData.setTeamName(Data.CurrentTeamChoice.getTeamInfo().getTeam().getName());
                String newText = "Switch Team - Current Choice: " + Data.GameData.getTeamName();
                Data.buildMenu22(this, newText);
            } else{
                errorPrint("No available roles to pick in chosen team!");
            }
        } else{
            errorPrint("No available team match the entered id!");
        }
    }

    private void getGameId(){
        IntResponse resp = new IntResponse();
        printGameShortInfo();
        Data.activateCurrentInput(resp);

        int gameId = ((IntResponse) Data.CurrentResponse).getInt();
        Data.CurrentResponse = null;

        Optional<DtoSubServerChoice> game = Data.CurrentChoices
                .getSubServerChoices()
                .stream()
                .filter(T -> T.getId() == gameId)
                .findAny();

        if(game.isPresent()){
            Data.CurrentSubChoice = game.get();
            Data.GameData.setGameId(Data.CurrentSubChoice.getId());
            Data.GameData.setGameName(Data.CurrentSubChoice.getGameName());
            String newText = "Switch Game - Current Choice: " + Data.CurrentSubChoice.getGameName();
            Data.buildMenu21(this, newText);
        } else{
            errorPrint("No available game match the entered id!");
        }
    }

    private void showPendingGames(){
        printGameChoices(ClientConst.SERVER_CONTEXT + LinkConst.GET_CHOICE_STATUSES,
                AttributeNames.PENDING, Data.HTTP_CLIENT, false);
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

    public void printGameChoices(String i_Url, String i_GetTypes,
                                        OkHttpClient i_Client, boolean adminRequest){
        Request request = getRequestStats(i_Url, i_GetTypes);

        Call call = i_Client.newCall(request);

        try(Response response = call.execute()){
            if (response.code() == HttpCode.OK) {
                DtoResponse<DtoServerGameChoice> dtoResponse =
                        new Gson().fromJson(response.body().charStream(), ResponseType.DTO_RESPONSE_CHOICE);
                Data.CurrentChoices = dtoResponse.getResult();
                String toPrint = parseGamesChoice(Data.CurrentChoices);
                System.out.print(toPrint);
            } else if(response.code() == HttpCode.NO_CONTENT) {
                String toPrint = parseGamesChoice(Data.CurrentChoices);
                System.out.print(toPrint);
            } else if (response.code() == HttpCode.NOT_FOUND || response.code() == HttpCode.BAD_REQUEST) {
                DtoResponse<DtoServerGameChoice> dtoResponse =
                        new Gson().fromJson(response.body().charStream(), ResponseType.DTO_RESPONSE_STATUS);
                errorPrint(dtoResponse.getErrorMessage());
                Data.getMain().cancelMenuChange();
            } else {
                errorPrint("An unexpected error occurred");
                Data.getMain().cancelMenuChange();
            }
        } catch (IOException e) {
            errorPrint("IOException occurred: " + e.getMessage());
            Data.getMain().cancelMenuChange();
        }
    }

    private void printGameShortInfo(){
        DtoServerGameChoice info = Data.CurrentChoices;
        StringBuilder toPrint = new StringBuilder();

        toPrint.append("Available games: ");
        for(DtoSubServerChoice subServerChoice : info.getSubServerChoices()){
            toPrint.append("(")
                    .append(subServerChoice.getId())
                    .append(": ")
                    .append(subServerChoice.getGameName())
                    .append(")");
        }

        System.out.println(toPrint);
    }

    private void printTeamShortInfo(){
        DtoSubServerChoice info = Data.CurrentSubChoice;
        StringBuilder toPrint = new StringBuilder();

        toPrint.append("Available teams: ");
        for(DtoServerTeamChoice team : info.getTeamChoices()) {
            if (team.getTeamInfo().getConnectedGuessers() != team.getTeamInfo().getNumOfGuessers() ||
                    team.getTeamInfo().getConnectedDefiners() != team.getTeamInfo().getNumOfDefiners()) {
                toPrint.append("(")
                        .append(team.getTeamId())
                        .append(": ")
                        .append(team.getTeamInfo().getTeam().getName())
                        .append(")");
            }
        }

        System.out.println(toPrint);
    }

    private void printRoleShortInfo() {
        DtoServerTeam info = Data.CurrentTeamChoice.getTeamInfo();
        StringBuilder toPrint = new StringBuilder();

        toPrint.append("Roles state(Connected/Needed): ")
                .append(parseRolesShort(info));


        System.out.println(toPrint);
    }

    private StringBuilder parseRolesShort(DtoServerTeam info){
        StringBuilder ret = new StringBuilder();
        return ret.append("(0:Definers: (")
                .append(info.getConnectedDefiners())
                .append("/")
                .append(info.getNumOfDefiners())
                .append(")) ")
                .append("(1:Guessers: (")
                .append(info.getConnectedGuessers())
                .append("/")
                .append(info.getNumOfGuessers())
                .append(")");
    }

    private boolean checkTeamFull(DtoServerTeam i_ToCheck){
        return i_ToCheck.getNumOfDefiners() == i_ToCheck.getConnectedDefiners() &&
                i_ToCheck.getNumOfGuessers() == i_ToCheck.getConnectedGuessers();
    }

    private static void sleepForSomeTime(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException ignored) {

        }
    }
}
