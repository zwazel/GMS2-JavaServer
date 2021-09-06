package util.NetworkUtils.sender;

import Classes.Client;
import GlobalStuff.NetworkCommands;
import util.NetworkUtils.PutUtils;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static util.NetworkUtils.PutUtils.putClientInStream;

public record InitClient(Client newClient) implements Runnable {
    @Override
    public void run() {
        // Send information for the client itself to the new client
        DataOutputStream dOut;
        try {
            dOut = new DataOutputStream(newClient.getChannel().socket().getOutputStream());
            dOut.write(NetworkCommands.send_client_its_id.ordinal());
            putClientInStream(dOut, newClient, true);

            List<Client> clients = newClient.getNewClients();
            System.out.println("clients.size() = " + clients.size());
            if (clients.size() > 0) {
                dOut.write(NetworkCommands.send_all_clients.ordinal());
                dOut.writeInt(clients.size());
                for (Client client : clients) {
                    System.out.println("client = " + client.getUsername() + "," + client.getMyId());
                    PutUtils.putClientInStream(dOut, client, true);
                }

                newClient.setNewClients(new ArrayList<>());
            }

            dOut.write(NetworkCommands.end_of_packet.ordinal());
            dOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
