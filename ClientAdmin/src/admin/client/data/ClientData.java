package admin.client.data;

import admin.client.Client;
import admin.client.action.Action;
import console.MainMenu;
import console.Menu;
import cookiejar.copied.SimpleCookieManager;
import okhttp3.OkHttpClient;
import ui.input.InputHandling;

public class ClientData {
    private final MainMenu Main;
    public final OkHttpClient HTTP_CLIENT = new OkHttpClient.Builder()
                .cookieJar(new SimpleCookieManager())
                .build();
    public boolean Menu2 = false;
    public boolean LoggedIn = false;
    public Action CurrentAction = null;
    public InputHandling CurrentInput = null;

    public ClientData() {
        Main = new MainMenu("Admin Client");
    }

    public MainMenu getMain() {
        return Main;
    }

    public void buildMenu1Or2(boolean i_OpenGame, Client client){
        Menu Load = Main.getStartMenu().createSubMenu("Load Game Files");
        Load.createMenuOption("Chose an .xml path to a file", InputHandling.FILE_PATH_XML, client);
        Load.createMenuOption("Chose a .txt path to a file", InputHandling.FILE_PATH_TXT, client);
        Load.createMenuOption("Upload to server", Action.UPLOAD, client);
        if(i_OpenGame){
            addFirstOptions(client);
        }
    }

    public void addFirstOptions(Client i_Client) {
        if (!Menu2) {
            Main.getStartMenu().createMenuOption("Showcase All Game States", Action.SHOW_GAMES, i_Client);
            Menu toAdd = Main.getStartMenu().createSubMenu("Active Game Viewer");
            toAdd.createMenuOption("Chose game", InputHandling.GET_GAME_ID, i_Client);
            Menu2 = true;
        }
    }


}
