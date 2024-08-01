package ui.input;

import dto.type.in.response.*;
import dto.type.in.response.common.IntResponse;
import dto.type.in.response.common.StringResponse;
import dto.type.in.response.ingame.GuesserResponse;
import dto.type.in.response.ingame.IdentificationResponse;
import dto.type.in.response.load.LoadFileResponse;
import dto.type.in.response.load.LoadFilesResponse;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.nio.file.Paths;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
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
    FILE_PATH {
        @Override
        public void getInput(Response o_Response) {
            Scanner scanner = new Scanner(System.in);

            System.out.print("Enter a full xml File path: ");
            String xmlPath = scanner.nextLine();
            File xmlFile = new File(xmlPath);

            if (!xmlFile.isFile()) {
                errorPrint("A File was not found at the given path!");
            } else if (!xmlPath.endsWith(".xml")) {
                errorPrint("File path does not lead to an xml File!");
            } else {
                try {
                    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                    Document doc = dBuilder.parse(xmlFile);

                    doc.getDocumentElement().normalize();

                    NodeList nList = doc.getElementsByTagName("ECN-Dictionary-File");
                    if (nList.getLength() > 0) {
                        Node nNode = nList.item(0);
                        if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element element = (Element) nNode;
                            String dictionaryFile = element.getTextContent();
                            String txtPath = Paths.get(xmlPath)
                                    .getParent()
                                    .resolve(dictionaryFile)
                                    .toString();

                            File txtFile = new File(txtPath);

                            if (!txtFile.isFile()) {
                                errorPrint("The Dictionary file was not found!" +
                                        " Please make sure it's in the same directory as the xml file");
                            } else if (!txtPath.endsWith(".txt")) {
                                errorPrint("Dictionary file has to be a txt file!");
                            } else {
                                o_Response.loadResponse(new LoadFilesResponse(xmlFile, txtFile));
                            }
                        }
                    } else {
                        errorPrint("No Dictionary File name was found in the xml.");
                    }
                } catch (Exception e) {
                    errorPrint("An error occurred while trying to read the Dictionary-File from the xml xmlFile!");
                }
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
                    errorPrint("Non Number entered! Please try again");
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
                    errorPrint("None Number entered! Please try again");
                }
            } while (continueLoop);

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
                System.out.print("Please chose a game id: ");
                try {
                    Int = scanner.nextInt();
                    continueLoop = false;
                } catch (InputMismatchException e) {
                    continueLoop = true;
                    scanner.nextLine();
                    errorPrint("None Number entered! Please try again");
                }
            } while (continueLoop);

            o_Response.loadResponse(new IntResponse(Int));
        }
    },
    GET_TEAM_ID{
        @Override
        public void getInput(Response o_Response) {
            Scanner scanner = new Scanner(System.in);
            boolean continueLoop;
            int Int = 0;

            do {
                System.out.print("Please chose a team id: ");
                try {
                    Int = scanner.nextInt();
                    continueLoop = false;
                } catch (InputMismatchException e) {
                    continueLoop = true;
                    scanner.nextLine();
                    errorPrint("None Number entered! Please try again");
                }
            } while (continueLoop);

            o_Response.loadResponse(new IntResponse(Int));
        }
    },
    GET_ROLE{
        @Override
        public void getInput(Response o_Response) {
            Scanner scanner = new Scanner(System.in);
            boolean continueLoop;
            int Int = 0;

            do {
                System.out.print("Please enter role id: "); //todo handle errors
                try {
                    Int = scanner.nextInt();
                    if(Int == 0 || Int == 1) {
                        continueLoop = false;
                    }
                    else {
                        continueLoop = true;
                        errorPrint("Please only enter 0 or 1! Please try again");
                    }
                } catch (InputMismatchException e) {
                    continueLoop = true;
                    scanner.nextLine();
                    errorPrint("None Number entered! Please try again");
                }
            } while (continueLoop);

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
    public static void errorPrint(String errorMessage){
        String toPrint = "\n!!!An error occurred!!!\n" + errorMessage + "\n!!!!!!\n";
        System.out.print(toPrint);
    }
}
