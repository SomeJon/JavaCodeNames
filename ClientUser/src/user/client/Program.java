package user.client;

import user.client.data.ClientData;

import java.io.IOException;

public class Program {
    public static void main(String[] args) throws IOException {
        Client2 client = new Client2(new ClientData());

        client.RunClient();
    }
}
