package client.data;

import client.action.Action;
import client.action.ActionData;
import client.action.ActionInput;
import console.MainMenu;
import console.Menu;
import cookiejar.copied.SimpleCookieManager;
import okhttp3.OkHttpClient;
import ui.input.InputHandling;

public class ClientData {
    private final MainMenu Main;
    public final ActionInput INPUT = new ActionInput();
    public final OkHttpClient HTTP_CLIENT = new OkHttpClient.Builder()
                .cookieJar(new SimpleCookieManager())
                .build();
    public final ActionData ACTION_DATA = new ActionData();
    public boolean Menu2 = false;
    public boolean LoggedIn = false;

    public ClientData() {
        Main = new MainMenu("Admin Client");
    }

    public MainMenu getMain() {
        return Main;
    }

    public void buildMenu1Or2(boolean i_OpenGame){
        Menu Load = Main.getStartMenu().createSubMenu("Load Game Files");
        Load.createMenuOption("Chose an .xml path to a file", InputHandling.FILE_PATH_XML, INPUT);
        Load.createMenuOption("Chose a .txt path to a file", InputHandling.FILE_PATH_TXT, INPUT);
        if(i_OpenGame){
            addFirstOptions();
        }
    }

    public void addFirstOptions() {
        if (!Menu2) {
            Main.getStartMenu().createMenuOption("Showcase All Game States", Action.SHOW_GAMES, ACTION_DATA);
            Menu toAdd = Main.getStartMenu().createSubMenu("Active Game Viewer");
            toAdd.createMenuOption("Chose game", InputHandling.GET_GAME_ID, INPUT);
            Menu2 = true;
        }
    }


}
