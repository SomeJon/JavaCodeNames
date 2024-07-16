package ui.program;

import engine.Engine;
import engine.data.GameData;
import ui.Controller;
import ui.view.UiView;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.net.URL;

public class Program {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Run();
    }

    public static void Run() throws IOException{
        boolean saveOnExit;
        Controller codeNameController;

        UiView gameUi = new UiView();
        Engine gameEngine = new Engine(new GameData());
        codeNameController = new Controller(gameUi, gameEngine);

        codeNameController.Start();
    }
}
