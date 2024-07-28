package user.client.data;

import console.ChoiceNotifier;
import console.Menu;
import dto.type.in.response.Response;
import ui.input.InputHandling;
import user.client.Client2;

import console.MainMenu;
import cookiejar.copied.SimpleCookieManager;
import okhttp3.OkHttpClient;
import user.client.action.Action;

public class ClientData {
    private final MainMenu Main;
    public final OkHttpClient HTTP_CLIENT = new OkHttpClient.Builder()
                .cookieJar(new SimpleCookieManager())
                .build();
    public boolean LoggedIn = false;
    public Response CurrentResponse = null;
    public Action CurrentAction = null;
    public InputHandling CurrentInput = null;

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
        Menu SubMenu1 = Main.getStartMenu().createSubMenu("Join Game");
        SubMenu1.createMenuOption("Choose a game", InputHandling.GET_GAME_ID, i_Client);
    }

    public void activateCurrentInput(Response i_CurrentResponse) {
        CurrentResponse = i_CurrentResponse;
        CurrentInput.getInput(CurrentResponse);
        CurrentInput = null;
    }
}
