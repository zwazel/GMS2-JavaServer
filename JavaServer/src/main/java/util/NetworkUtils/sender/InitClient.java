package util.NetworkUtils.sender;

import Classes.Client;
import GlobalStuff.NetworkCommands;

import java.io.DataOutputStream;
import java.io.IOException;

import static util.NetworkUtils.PutUtils.putClientInStream;

public record InitClient(Client newClient) implements Runnable{

    @Override
    public void run() {
        // Send information for the client itself to the new client
        DataOutputStream dOut;
        try {
            dOut = new DataOutputStream(newClient.getChannel().socket().getOutputStream());
            dOut.write(NetworkCommands.send_client_its_id.ordinal());
            putClientInStream(dOut, newClient, true);
            dOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
