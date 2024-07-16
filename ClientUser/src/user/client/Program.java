package user.client;

import user.client.data.ClientData;

import java.io.IOException;

public class Program {
    public static void main(String[] args) throws IOException {
        Client client = new Client(new ClientData());

        client.RunClient();
    }
}
