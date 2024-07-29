package user.client.data;

import console.*;
import dto.type.in.response.Response;
import dto.type.out.server.Choice.DtoServerGameChoice;
import dto.type.out.server.Choice.DtoServerTeamChoice;
import dto.type.out.server.Choice.DtoSubServerChoice;
import ui.input.InputHandling;

import cookiejar.copied.SimpleCookieManager;
import okhttp3.OkHttpClient;
import user.client.action.Action;

import java.util.List;

public class ClientData {
    private final MainMenu Main;
    public final OkHttpClient HTTP_CLIENT = new OkHttpClient.Builder()
                .cookieJar(new SimpleCookieManager())
                .build();
    public boolean LoggedIn = false;
    public final GameData GameData = new GameData();
    public Response CurrentResponse = null;
    public Action CurrentAction = null;
    public InputHandling CurrentInput = null;
    public DtoServerGameChoice CurrentChoices = null;
    public DtoSubServerChoice CurrentSubChoice = null;
    public DtoServerTeamChoice CurrentTeamChoice = null;

    public ClientData() {
        Main = new MainMenu("User Client");
    }

    public MainMenu getMain() {
        return Main;
    }

    public void buildMenu1(ChoiceNotifier i_Client){
        Main.getStartMenu().createMenuOption("Login", InputHandling.GET_NAME, i_Client);
    }

    public void buildMenu2(ChoiceNotifier i_Client){
        Main.getStartMenu().getMenuItems().remove(0);
        Main.getStartMenu().createMenuOption("Show all games details", Action.SHOW_GAMES, i_Client);
        NotifyList notifiers = new NotifyList();
        notifiers.addNotifyBefore(i_Client, Action.SHOW_PENDING_GAMES);
        Menu subMenu1 = Main.getStartMenu().createSubMenuWithActions("Join Game", notifiers);
        subMenu1.createMenuOption("Refresh games info", Action.REFRESH, i_Client);
        subMenu1.createMenuOption("Enter game id", InputHandling.GET_GAME_ID, i_Client);
        subMenu1.createMenuOption("Enter team id", InputHandling.GET_TEAM_ID, i_Client);
        subMenu1.createMenuOption("Chose a role", InputHandling.GET_ROLE, i_Client);
        subMenu1.createMenuOption("Enter game", Action.ENTER_GAME, i_Client);
    }

    public void buildMenu20(ChoiceNotifier i_Client){
        Menu toChange = Main.getCurrentMenu();
        List<MenuItem> menuItems = toChange.getMenuItems();

        if (menuItems.size() > 4) {
            menuItems.remove(4);
        }
        if (menuItems.size() > 3) {
            menuItems.remove(3);
        }
        if (menuItems.size() > 2) {
            menuItems.remove(2);
        }

        CurrentTeamChoice = null;
        CurrentSubChoice = null;
        GameData.clear();

        menuItems.get(1).setItemText("Enter game id");
    }

    public void buildMenu21(ChoiceNotifier i_Client, String i_NewName){
        Menu toChange = Main.getCurrentMenu();
        List<MenuItem> menuItems = toChange.getMenuItems();

        if (menuItems.size() > 4) {
            menuItems.remove(4);
            GameData.setRole(null);
        }
        if (menuItems.size() > 3) {
            menuItems.remove(3);
            GameData.setTeamName(null);
            GameData.setTeamId(null);
            CurrentTeamChoice = null;
        }
        if (menuItems.size() > 2) {
            menuItems.get(2).setItemText("Enter team id");
        } else{
            toChange.createMenuOption("Enter team id", InputHandling.GET_TEAM_ID, i_Client);
        }

        menuItems.get(1).setItemText(i_NewName);
    }

    public void buildMenu22(ChoiceNotifier i_Client, String i_NewName){
        Menu toChange = Main.getCurrentMenu();
        List<MenuItem> menuItems = toChange.getMenuItems();

        if (menuItems.size() > 4) {
            menuItems.remove(4);
            GameData.setRole(null);
        }
        if (menuItems.size() > 3) {
            menuItems.get(3).setItemText("Chose a role");

        } else{
            toChange.createMenuOption("Chose a role", InputHandling.GET_ROLE, i_Client);
        }

        menuItems.get(2).setItemText(i_NewName);
    }

    public void buildMenu23(ChoiceNotifier i_Client, String i_NewName){
        Menu toChange = Main.getCurrentMenu();
        List<MenuItem> menuItems = toChange.getMenuItems();

        if (!(menuItems.size() > 4)) {
            toChange.createMenuOption("Enter game", Action.ENTER_GAME, i_Client);
        }

        menuItems.get(3).setItemText(i_NewName);
    }

    public void activateCurrentInput(Response i_CurrentResponse) {
        CurrentResponse = i_CurrentResponse;
        CurrentInput.getInput(CurrentResponse);
        CurrentInput = null;
    }
}
