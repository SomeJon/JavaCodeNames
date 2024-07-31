package admin.client.data;

import admin.client.Client;
import admin.client.action.Action;
import console.MainMenu;
import console.Menu;
import console.NotifyList;
import cookiejar.copied.SimpleCookieManager;
import dto.type.in.response.Response;
import dto.type.out.server.Choice.DtoServerGameChoice;
import okhttp3.OkHttpClient;
import ui.input.InputHandling;

import java.io.File;

public class ClientData {
    private final MainMenu Main;
    public final OkHttpClient HTTP_CLIENT = new OkHttpClient.Builder()
                .cookieJar(new SimpleCookieManager())
                .build();
    private boolean Menu2 = false;
    private boolean LoggedIn = false;
    private Action CurrentAction = null;
    private InputHandling CurrentInput = null;
    private Response CurrentResponse = null;
    private int CurrentGameId;
    private File WaitingTxt = null;
    private File WaitingXml = null;
    private DtoServerGameChoice CurrentChoices = null;


    public DtoServerGameChoice getCurrentChoices() {
        return CurrentChoices;
    }

    public void setCurrentChoices(DtoServerGameChoice i_CurrentChoices) {
        CurrentChoices = i_CurrentChoices;
    }

    public boolean isMenu2() {
        return Menu2;
    }

    public ClientData() {
        Main = new MainMenu("Admin Client");
    }

    public MainMenu getMain() {
        return Main;
    }

    public boolean isLoggedIn() {
        return LoggedIn;
    }

    public Action getCurrentAction() {
        return CurrentAction;
    }

    public InputHandling getCurrentInput() {
        return CurrentInput;
    }

    public File getWaitingTxt() {
        return WaitingTxt;
    }

    public Response getCurrentResponse() {
        return CurrentResponse;
    }

    public void setLoggedIn(boolean i_LoggedIn) {
        LoggedIn = i_LoggedIn;
    }

    public void setCurrentAction(Action i_CurrentAction) {
        CurrentAction = i_CurrentAction;
    }

    public void setCurrentInput(InputHandling i_CurrentInput) {
        CurrentInput = i_CurrentInput;
    }

    public void activateCurrentInput(Response i_CurrentResponse) {
        CurrentResponse = i_CurrentResponse;
        CurrentInput.getInput(CurrentResponse);
        CurrentInput = null;
    }

    public void setCurrentResponse(Response i_CurrentResponse) {
        CurrentResponse = i_CurrentResponse;
    }

    public void setWaitingTxt(File i_WaitingTxt) {
        WaitingTxt = i_WaitingTxt;
    }

    public File getWaitingXml() {
        return WaitingXml;
    }

    public void setWaitingXml(File i_WaitingXml) {
        WaitingXml = i_WaitingXml;
    }

    public void buildMenu1Or2(boolean i_OpenGame, Client client){
        Menu Load = Main.getStartMenu().createSubMenu("Load Game Files");
        Load.createMenuOption("Chose a file path to a .xml file", InputHandling.FILE_PATH, client);
        //Load.createMenuOption("Chose a file path to a .txt file", InputHandling.FILE_PATH, client);
        Load.createMenuOption("Upload to server", Action.UPLOAD, client);
        if(i_OpenGame){
            addFirstOptions(client);
        }
    }

    public void addFirstOptions(Client i_Client) {
        if (!Menu2) {
            Main.getStartMenu().createMenuOption("Showcase All Game States", Action.SHOW_GAMES, i_Client);
            NotifyList notifiers = new NotifyList();
            notifiers.addNotifyBefore(i_Client, Action.REFRESH);
            Menu subMenu1 = Main.getStartMenu().createSubMenuWithActions("Watch a game as a spectator", notifiers);
            subMenu1.createMenuOption("Refresh games info", Action.REFRESH, i_Client);
            subMenu1.createMenuOption("Enter game id", InputHandling.GET_GAME_ID, i_Client);
            Menu2 = true;
        }
    }

    public void buildMenu3(Client client) {
        Menu Load = Main.getStartMenu().createSubMenu("Active Game Viewer");
        Load.createMenuOption("Fetch game status", Action.FETCH, client);
        Main.setCurrentMenu(Load);
        Main.getStartMenu().getMenuItems().remove(2);
    }
}
