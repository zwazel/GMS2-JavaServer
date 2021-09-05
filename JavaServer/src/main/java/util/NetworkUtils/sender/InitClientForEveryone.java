package util.NetworkUtils.sender;

import Classes.Client;
import GlobalStuff.NetworkCommands;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

import static util.NetworkUtils.PutUtils.putClientInStream;

public record InitClientForEveryone(Client newClient) implements Runnable {
    @Override
    public void run() {
        List<Client> clients = newClient.getServer().getClients();
        DataOutputStream dOut;

        try {
            // Send every already connected client to the new client
            dOut = new DataOutputStream(newClient.getChannel().socket().getOutputStream());
            dOut.write(NetworkCommands.send_all_clients.ordinal());
            for (Client client : clients) {
                if (client.getMyId() == newClient().getMyId()) {
                    continue;
                }
                putClientInStream(dOut, client, true);
            }
            dOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // send new client to everyone
        for (Client client : clients) {
            if (client.getMyId() == newClient().getMyId()) {
                continue;
            }
            try {
                dOut = new DataOutputStream(client.getChannel().socket().getOutputStream());
                dOut.write(NetworkCommands.client_connect.ordinal());

                dOut.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
