package user.client.data;

import console.*;
import dto.type.in.response.Response;
import dto.type.out.server.choice.DtoServerGameChoice;
import dto.type.out.server.choice.DtoServerTeamChoice;
import dto.type.out.server.choice.DtoSubServerChoice;
import ui.input.InputHandling;

import cookiejar.copied.SimpleCookieManager;
import okhttp3.OkHttpClient;
import user.client.action.Action;
import user.client.action.ChatSetting;

import java.util.List;

public class ClientData {
    private final MainMenu Main;
    private MenuItem PlayTurn;
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
    public boolean NewUpdate = false;
    public boolean GameStarted = false;

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
        notifiers.addNotifyBefore(i_Client, Action.CLEAN_CHOICE);
        notifiers.addNotifyBefore(i_Client, Action.SHOW_PENDING_GAMES);
        Menu subMenu1 = Main.getStartMenu().createSubMenuWithActions("Join Game", notifiers);
        subMenu1.createMenuOption("Refresh games info", Action.REFRESH, i_Client);
        subMenu1.createMenuOption("Enter game id", InputHandling.GET_GAME_ID, i_Client);
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

    public void buildMenu3(ChoiceNotifier i_Client){
        Menu main = Main.getStartMenu();

        main.getMenuItems().remove(1);

        Menu newGameMenu = main.createSubMenu("Joined Game Menu - " + GameData.getGameName());
        Main.setCurrentMenu(newGameMenu);
        newGameMenu.createMenuOption("Fetch Game Status", Action.GAME_SHOW, i_Client);
        newGameMenu.createMenuOption("Play turn {Waiting for game to start}", Action.PLAY_TURN, i_Client);
        PlayTurn = newGameMenu.getMenuItems().get(1);

        Menu chatMenu = newGameMenu.createSubMenu("Chat");
        chatMenu.createMenuOption("Enter Chat", Action.CHAT_OPEN, i_Client); //todo: might change it to a chat object
        Menu chatSettings = chatMenu.createSubMenu("Settings");
        chatSettings.createMenuOption("Show game messages - ON" +
                "\n-Show messages created by the server to log actions",
                ChatSetting.CHAT_SERVER, i_Client); //todo:same
        Menu entrySetting = chatSettings.createSubMenu("Mode setting - Current: {All/Partly:10/None}" +
                "\n-All: Shows all chat messages on each entry into the chat" +
                "\n-Partly: Shows only the top 10 messages" +
                "\n-None: Does not show any previous messages, only new ones");
        if(GameData.getRole() == user.client.data.GameData.roleChoice.IDENTIFIER){
            chatSettings.createMenuOption("Show Identifiers Messages - On",
                    ChatSetting.CHAT_IDENTIFIER, i_Client); //todo: same
            chatSettings.createMenuOption("Show Guessers Messages - On",
                    ChatSetting.CHAT_GUESSER, i_Client); //todo: same
        }

        entrySetting.createMenuOption("All - ON", ChatSetting.CHAT_ALL, i_Client);
        entrySetting.createMenuOption("Partly - OFF", ChatSetting.CHAT_PARTLY, i_Client);
        entrySetting.createMenuOption("None - OFF", ChatSetting.CHAT_NONE, i_Client);
    }

    public void updateTurnChoice(String i_PlayingTeam){
        PlayTurn.setItemText("Play turn {" + i_PlayingTeam + "}");
    }

    public void rebuildMenu2(ChoiceNotifier i_Client){
        Main.getStartMenu().getMenuItems().remove(1);
        NotifyList notifiers = new NotifyList();
        notifiers.addNotifyBefore(i_Client, Action.CLEAN_CHOICE);
        notifiers.addNotifyBefore(i_Client, Action.SHOW_PENDING_GAMES);
        Menu subMenu1 = Main.getStartMenu().createSubMenuWithActions("Join Game", notifiers);
        subMenu1.createMenuOption("Refresh games info", Action.REFRESH, i_Client);
        subMenu1.createMenuOption("Enter game id", InputHandling.GET_GAME_ID, i_Client);
        Main.previousMenu();
    }
}
