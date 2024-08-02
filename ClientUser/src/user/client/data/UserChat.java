package user.client.data;

import Adapter.AdapterAddon;
import com.google.gson.Gson;
import console.ChoiceNotifier;
import console.Menu;
import console.MenuItem;
import constant.client.ClientConst;
import constant.client.HttpCode;
import data.ChatData;
import dto.type.out.server.chat.DtoServerChat;
import message.Message;
import message.SystemMessage;
import message.UserMessage;
import message.format.interfaces.Format;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import request.CNRequest;
import ui.input.InputHandling;
import user.client.action.ChatSetting;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static ui.input.InputHandling.errorPrint;

public class UserChat extends ChatData implements ChoiceNotifier {
    public Format UserFormatPrint;
    public Format SystemFormatUpdate;
    private boolean printGuesser = true;
    public boolean printIdentifier = false;
    private boolean printSystem = true;
    private Menu menuToChange;
    private volatile boolean running = true;
    private volatile boolean userTyping = false;
    private final OkHttpClient client;
    private final Gson gson = AdapterAddon.getGson();;
    private List<Message> WaitingList = new ArrayList<>();

    public UserChat(Format userFormatPrint, Format systemFormatUpdate, OkHttpClient client) {
        UserFormatPrint = userFormatPrint;
        SystemFormatUpdate = systemFormatUpdate;
        this.client = client;
    }

    public void setMenuToChange(Menu i_menuToChange) {
        menuToChange = i_menuToChange;
    }

    public void printMessages() {
        lock.readLock().lock();
        try {
            List<Message> toPrint = new ArrayList<>(Messages);
            StringBuilder strs = new StringBuilder();

            for (Message m : toPrint) {
                if (m instanceof UserMessage) {
                    switch (((UserMessage) m).getRole()) {
                        case Guesser:
                            if (printGuesser) {
                                strs.append(m.getMessage(UserFormatPrint)).append("\n");
                            }
                            break;
                        case Definer:
                            if (printIdentifier) {
                                strs.append(m.getMessage(SystemFormatUpdate)).append("\n");
                            }
                            break;
                    }
                } else {
                    if (printSystem) {
                        strs.append(m.getMessage(UserFormatPrint)).append("\n");
                    }
                }
            }
            System.out.println(strs.toString());
        } finally {
            lock.readLock().unlock();
        }
    }

    public void printMessages(List<Message> messages) {
        lock.readLock().lock();
        try {
            List<Message> toPrint = new ArrayList<>(messages);
            StringBuilder strs = new StringBuilder();

            for (Message m : toPrint) {
                if (m instanceof UserMessage) {
                    switch (((UserMessage) m).getRole()) {
                        case Guesser:
                            if (printGuesser) {
                                strs.append(m.getMessage(UserFormatPrint)).append("\n");
                            }
                            break;
                        case Definer:
                            if (printIdentifier) {
                                strs.append(m.getMessage(SystemFormatUpdate)).append("\n");
                            }
                            break;
                    }
                } else {
                    if (printSystem) {
                        strs.append(m.getMessage(UserFormatPrint)).append("\n");
                    }
                }
            }
            System.out.println(strs.toString());
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public void Notify(Object sender) {
        ChatSetting ret = null;

        if (((MenuItem) sender).getItemValue() instanceof InputHandling) {
            ret = (ChatSetting) ((MenuItem) sender).getItemValue();
        }

        if (ret == null) {
            System.out.println("Big unexpected problem");
        } else {
            switch (ret) {
                case CHAT_SERVER:
                    togglePrintSystem();
                    break;
                case CHAT_GUESSER:
                    togglePrintGuesser();
                    break;
                case CHAT_IDENTIFIER:
                    togglePrintIdentifier();
                    break;
                case CHAT_OPEN:
                    openChat();
                    break;
            }
        }
    }

    private void togglePrintSystem() {
        if (printSystem) {
            menuToChange.getMenuItems().get(0).setItemText("Show game messages - OFF");
        } else {
            menuToChange.getMenuItems().get(0).setItemText("Show game messages - ON");
        }
        printSystem = !printSystem;
    }

    private void togglePrintGuesser() {
        if (printGuesser) {
            menuToChange.getMenuItems().get(2).setItemText("Show Guessers Messages - OFF");
        } else {
            menuToChange.getMenuItems().get(2).setItemText("Show Guessers Messages - ON");
        }
        printGuesser = !printGuesser;
    }

    private void togglePrintIdentifier() {
        if (printIdentifier) {
            menuToChange.getMenuItems().get(1).setItemText("Show Identifiers Messages - OFF");
        } else {
            menuToChange.getMenuItems().get(1).setItemText("Show Identifiers Messages - ON");
        }
        printIdentifier = !printIdentifier;
    }

    private void openChat() {
        running = true;
        printMessages();

        Thread updateThread = new Thread(new Runnable() {
            @Override
            public void run() {
                fetchUpdates();
            }
        });
        updateThread.start();

        handleUserInput();

        running = false;
        try {
            updateThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void handleUserInput() {
        try (InputStreamReader reader = new InputStreamReader(System.in);
             Scanner scanner = new Scanner(System.in)) {
            while (running) {
                int firstChar = reader.read();
                userTyping = true;
                String input = (char) firstChar + scanner.nextLine();
                userTyping = false;
                synchronized (this) {
                    if ("exit".equalsIgnoreCase(input.trim())) {
                        running = false;
                    } else {
                       sendMessage(input);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendMessage(String message) {
        Request request = CNRequest.requestWithObject(
                ClientConst.SERVER_CONTEXT + LinkConst.CHAT_END_POINT, message, "POST");


        Call call = client.newCall(request);

        try(Response response = call.execute()){
            if(response.code() == HttpCode.OK){
                System.out.println("Message uploaded successfully");
            } else if(response.code() == HttpCode.UNAUTHORIZED
                    || response.code() == HttpCode.BAD_REQUEST
                    || response.code() == HttpCode.NOT_FOUND){
                String str = response.body().string();
                if (!str.isEmpty()) {
                    errorPrint(str);
                } else {
                    errorPrint("Internal Server Error");
                }
            } else{
                errorPrint("Unexpected update error");
            }
        } catch (IOException e) {
            System.out.println("Chat update failed");
        }
    }

    private void getMessage() {
        Request request = CNRequest.getRequest(
                ClientConst.SERVER_CONTEXT + LinkConst.CHAT_END_POINT);

        Call call = client.newCall(request);
        try(Response response = call.execute()){
            if(response.code() == HttpCode.OK){
                System.out.println("Message uploaded successfully");
                DtoServerChat ret = gson.fromJson(response.body().charStream(), DtoServerChat.class);
                WaitingList.addAll(ret.getReceivedMessages());
                Messages.addAll(ret.getReceivedMessages());
            } else if(response.code() == HttpCode.UNAUTHORIZED
                    || response.code() == HttpCode.NOT_FOUND){
                String str = response.body().string();
                if (!str.isEmpty()) {
                    errorPrint(str);
                } else {
                    errorPrint("Internal Server Error");
                }
            } else{
                errorPrint("Unexpected update error");
            }
        } catch (IOException e) {
            System.out.println("Chat update failed");
        }
    }

    private void fetchUpdates() {
        while (running) {
            try {
                Thread.sleep(3000); // Simulate delay
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }

            if (!running) {
                break;
            }

            lock.writeLock().lock();
            try {
                getMessage();
            } finally {
                lock.writeLock().unlock();
            }

            if (!userTyping) {
                synchronized (this) {
                    if (running) {
                        printMessages(WaitingList);
                        WaitingList.clear();
                    }
                }
            }
        }
    }
}
