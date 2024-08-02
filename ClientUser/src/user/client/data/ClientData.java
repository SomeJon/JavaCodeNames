package user.client.data;

import console.*;
import data.ChatData;
import dto.type.in.response.Response;
import dto.type.out.server.Choice.DtoServerGameChoice;
import dto.type.out.server.Choice.DtoServerTeamChoice;
import dto.type.out.server.Choice.DtoSubServerChoice;
import message.format.SystemMessageConsoleFormat;
import message.format.UserInfoMessageConsoleFormat;
import message.format.UserMessageConsoleFormat;
import okhttp3.OkHttp;
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
    public GameData GameData = new GameData();
    public Response CurrentResponse = null;
    public Action CurrentAction = null;
    public InputHandling CurrentInput = null;
    public DtoServerGameChoice CurrentChoices = null;
    public DtoSubServerChoice CurrentSubChoice = null;
    public DtoServerTeamChoice CurrentTeamChoice = null;
    public boolean NewUpdate = false;
    public boolean GameStarted = false;
    public UserChat Chat;

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
        notifiers.addNotifyAfter(i_Client, Action.CLEAN_CHOICE);
        notifiers.addNotifyBefore(i_Client, Action.SHOW_PENDING_GAMES);
        Menu subMenu1 = Main.getStartMenu().createSubMenuWithActionsOnEnter("Join Game", notifiers);
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

        switch(GameData.getRole()){
            case GUESSER:
                Chat = new UserChat(new UserMessageConsoleFormat(), new SystemMessageConsoleFormat(), HTTP_CLIENT);
                break;
            default:
                Chat = new UserChat(new UserInfoMessageConsoleFormat(), new SystemMessageConsoleFormat(), HTTP_CLIENT);
                break;
        }

        NotifyList notifiers = new NotifyList();
        notifiers.addNotifyAfter(i_Client, Action.GAME_SHOW);
        Menu newGameMenu = main.createSubMenuWithActionsOnEnter("Joined Game Menu - "
                + GameData.getGameName() + " - " + GameData.getTeamName() + " - "
                + GameData.getRole().toString(), notifiers);
        Main.setCurrentMenu(newGameMenu);
        newGameMenu.createMenuOption("Fetch Game Status", Action.GAME_SHOW, i_Client);
        newGameMenu.createMenuOption("Play turn {Waiting for game to start}", Action.PLAY_TURN, i_Client);
        PlayTurn = newGameMenu.getMenuItems().get(1);

        Menu chatMenu = newGameMenu.createSubMenu("Chat");
        chatMenu.createMenuOption("Enter Chat", ChatSetting.CHAT_OPEN, Chat);
        Menu chatSettings = chatMenu.createSubMenu("Settings");
        chatSettings.createMenuOption("Show game messages - ON" +
                "\n-Show messages created by the server to log actions",
                ChatSetting.CHAT_SERVER, Chat);

        if(GameData.getRole() == user.client.data.GameData.roleChoice.IDENTIFIER){
            chatSettings.createMenuOption("Show Identifiers Messages - On",
                    ChatSetting.CHAT_IDENTIFIER, Chat);
            chatSettings.createMenuOption("Show Guessers Messages - On",
                    ChatSetting.CHAT_GUESSER, Chat);
            Chat.printIdentifier = true;
        }

        Chat.setMenuToChange(chatSettings);
    }

    public void updateTurnChoice(String i_PlayingTeam){
        PlayTurn.setItemText("Play turn {" + i_PlayingTeam + "}");
    }

    public void updateTurnChoice(){
        PlayTurn.setItemText("Play turn {" + GameData.getCurrentTurn().getPlayingTeam().getName() +
                ":" + GameData.getCurrentTurn().getTurnRole().toString() + "}");
    }

    public void rebuildMenu2(ChoiceNotifier i_Client){
        Main.getStartMenu().getMenuItems().remove(1);
        NotifyList notifiers = new NotifyList();
        notifiers.addNotifyAfter(i_Client, Action.CLEAN_CHOICE);
        notifiers.addNotifyBefore(i_Client, Action.SHOW_PENDING_GAMES);
        Menu subMenu1 = Main.getStartMenu().createSubMenuWithActionsOnEnter("Join Game", notifiers);
        subMenu1.createMenuOption("Refresh games info", Action.REFRESH, i_Client);
        subMenu1.createMenuOption("Enter game id", InputHandling.GET_GAME_ID, i_Client);
        Main.previousMenu();
    }
}
