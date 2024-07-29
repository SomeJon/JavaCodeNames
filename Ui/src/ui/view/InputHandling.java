package ui.view;

import dto.type.in.response.ingame.GuesserResponse;
import dto.type.in.response.ingame.IdentificationResponse;
import dto.type.in.response.load.LoadXmlResponse;
import dto.type.in.response.Response;
import ui.save.FileLocationResponse;

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
                UiView.errorPrint("A file was not found at the given path!");
            } else if (!path.endsWith(".xml")) {
                UiView.errorPrint("File path does not lead to an xml file!");
            } else {
                o_Response.loadResponse(new LoadXmlResponse(file));
            }
        }
    },
    FILE_PATH_SAVE{
        @Override
        public void getInput(Response o_Response) {
            Scanner scanner = new Scanner(System.in);

            System.out.print("Enter a full file path, include the name of the file: ");
            String path = scanner.nextLine();
            path = path + ".cn";

            o_Response.loadResponse(new FileLocationResponse(path));
        }
    },
    IDENTIFICATION{
        @Override
        public void getInput(Response o_Response) {
            Scanner scanner = new Scanner(System.in);
            boolean continueLoop;

            System.out.println("Please enter an identification word and number of related words after");
            System.out.print("Identification word: ");
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
                    UiView.errorPrint("Non Number entered! please try again");
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
                    UiView.errorPrint("Non Number entered! please try again");
                }
            } while (continueLoop);

            System.out.println();
            o_Response.loadResponse(new GuesserResponse(CardId));
        }
    };

    private final static int EndGuessId = 0;
    public abstract void getInput(Response o_Response);

}
