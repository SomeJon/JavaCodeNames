package ui.input;

import dto.type.in.response.*;

import java.io.File;
import java.util.InputMismatchException;
import java.util.Scanner;

public enum InputHandling {
    FILE_PATH_XML {
        @Override
        public void getInput(Response o_Response) {
            Scanner scanner = new Scanner(System.in);

            System.out.print("Enter a full xml file path: ");
            String path = scanner.nextLine();
            File file = new File(path);

            if (!file.isFile()) {
                errorPrint("A file was not found at the given path!");
            } else if (!path.endsWith(".xml")) {
                errorPrint("File path does not lead to an xml file!");
            } else {
                o_Response.loadResponse(new LoadFileResponse(file));
            }
        }
    },
    FILE_PATH_TXT{
        @Override
        public void getInput(Response o_Response) {
            Scanner scanner = new Scanner(System.in);

            System.out.print("Enter a full txt file path: ");
            String path = scanner.nextLine();
            File file = new File(path);

            if (!file.isFile()) {
                errorPrint("A file was not found at the given path!");
            } else if (!path.endsWith(".txt")) {
                errorPrint("File path does not lead to a txt file!");
            } else {
                o_Response.loadResponse(new LoadFileResponse(file));
            }
        }
    },
    IDENTIFICATION{
        @Override
        public void getInput(Response o_Response) {
            Scanner scanner = new Scanner(System.in);
            boolean continueLoop;

            String toPrint = "Please enter an identification word and number of related words after\n" +
                    "Identification word: ";
            System.out.print(toPrint);
            String identificationWord = scanner.nextLine();
            int numberOfRelatedWords = 0;

            do {
                System.out.print("Number of related words: ");
                try {
                    numberOfRelatedWords = scanner.nextInt();
                    continueLoop = false;
                } catch (InputMismatchException e) {
                    continueLoop = true;
                    scanner.nextLine();
                    errorPrint("Non Number entered! please try again");
                }
            } while (continueLoop);

            o_Response.loadResponse(new IdentificationResponse(identificationWord, numberOfRelatedWords));
        }
    },
    GUESSER{
        @Override
        public void getInput(Response o_Response) {
            Scanner scanner = new Scanner(System.in);
            boolean continueLoop;
            int CardId = 0;
            
            do {
                System.out.print("Please enter the number of the guessed card, or enter " +
                        EndGuessId + " to end guessing: ");
                try {
                    CardId = scanner.nextInt();
                    continueLoop = false;
                } catch (InputMismatchException e) {
                    continueLoop = true;
                    scanner.nextLine();
                    errorPrint("None Number entered! please try again");
                }
            } while (continueLoop);

            System.out.println();
            o_Response.loadResponse(new GuesserResponse(CardId));
        }
    },
    GET_GAME_ID{
        @Override
        public void getInput(Response o_Response) {
            Scanner scanner = new Scanner(System.in);
            boolean continueLoop;
            int Int = 0;

            do {
                System.out.print("Please chose a game by entering its id: "); //todo handle errors
                try {
                    Int = scanner.nextInt();
                    continueLoop = false;
                } catch (InputMismatchException e) {
                    continueLoop = true;
                    scanner.nextLine();
                    errorPrint("None Number entered! please try again");
                }
            } while (continueLoop);

            System.out.println();
            o_Response.loadResponse(new IntResponse(Int));
        }
    },
    GET_NAME{
        @Override
        public void getInput(Response o_Response) {
            Scanner scanner = new Scanner(System.in);

            String toPrint = "Please enter a username: ";
            System.out.print(toPrint);
            String Name = scanner.nextLine();

            o_Response.loadResponse(new StringResponse(Name));
        }
    };

    private final static int EndGuessId = 0;
    public abstract void getInput(Response o_Response);
    private static void errorPrint(String errorMessage){
        String toPrint = "\n!!!An error occurred!!!\n" + errorMessage + "\n!!!!!!\n";
        System.out.println(toPrint);
    }
}
