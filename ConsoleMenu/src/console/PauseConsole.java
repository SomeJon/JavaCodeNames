package console;

import java.util.Scanner;

public class PauseConsole {
    public static void pause(){
        Scanner scanner = new Scanner(System.in);

        System.out.print("Game is paused, press Enter to continue...");
        scanner.nextLine();

    }
}
