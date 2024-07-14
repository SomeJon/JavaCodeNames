package console;

import java.io.Serializable;
import java.util.InputMismatchException;
import java.util.Scanner;

public class MainMenu implements ChoiceNotifier, Serializable{
    private final Menu StartMenu;
    private Menu CurrentMenu;
    private boolean Running = false;
    private boolean Closing = false;


    public Menu getStartMenu() {
        return StartMenu;
    }

    public boolean isClosing() {
        return Closing;
    }

    public MainMenu(String i_MainMenuName)
    {
        final boolean MAIN_MENU = true;

        StartMenu = new Menu(i_MainMenuName, MAIN_MENU, this);
        CurrentMenu = StartMenu;
    }

    public void play()
    {
        Running = true;
        Closing = false;

        while(Running) {
            CurrentMenu.printMenu(20);
            int userIntInput = receivedUserInput(0, CurrentMenu.getMenuItems().size());
            if (userIntInput != 0) {
                CurrentMenu.getMenuItems().get(userIntInput - 1).MenuItemChosen();
            } else {
                previousMenu();
            }
        }
    }

    public void pauseRunning(){
        Running = false;
    }

    public void previousMenu(){
        if(CurrentMenu.getPreviousMenu() != null)
            CurrentMenu = CurrentMenu.getPreviousMenu();
        else{
            Running = false;
            Closing = true;
        }
    }

    public int receivedUserInput(int min, int max){
            Scanner scanner = new Scanner(System.in);
            boolean continueLoop;
            int userInput = 0;

            do {
                try {
                    userInput = scanner.nextInt();
                    if (userInput >= min && userInput <= max) {
                        continueLoop = false;
                    }
                    else{
                        continueLoop = true;
                        System.out.println("!!!!Input out of range! range for input: " +
                                        min + "-" + max + "!!!!!");
                        System.out.print("Enter your choice: ");
                    }
                } catch (InputMismatchException i_ExceptionOccurred) {
                    continueLoop = true;
                    scanner.nextLine();
                    System.out.println("!!!!Incorrect input type! Please enter an int!!!!!");
                    System.out.print("Enter your choice: ");
                }
            } while(continueLoop);

            return userInput;
    }

    public void Notify(Object sender) {
        CurrentMenu = (Menu)((MenuItem)sender).getItemValue();
    }
}
