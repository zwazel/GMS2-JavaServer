package Classes;

import GlobalStuff.NetworkCommands;
import util.NetworkUtils.PutUtils;

import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

public record Sender(Server server) implements Runnable {
    @Override
    public void run() {
        while (server.isRunning()) {
            try {
                Thread.sleep(100);
                List<Client> clients = server.getClients();

                for (Client clientReceiver : clients) {
                    if (clientReceiver.isReady()) {
                        ArrayList<Client> clientsReady = new ArrayList<>();
                        for (Client c : clients) {
                            if (c.isReady() && (clientReceiver.getMyId() != c.getMyId())) clientsReady.add(c);
                        }

                        if (clientsReady.size() > 0) {
                            SocketChannel channel = clientReceiver.getChannel();
                            DataOutputStream dOut = new DataOutputStream(channel.socket().getOutputStream());
                            dOut.write(NetworkCommands.update_clients.ordinal());
                            dOut.writeInt(clientsReady.size());

                            for (Client clientToSend : clientsReady) {
                                PutUtils.putClientInStream(dOut, clientToSend, false);
                            }

                            dOut.flush();
                        }
                    }
                }
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
        }
    }
}
