package util;

import Classes.Client;
import GlobalStuff.NetworkCommands;

import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.List;

import static util.NetworkUtils.PutUtils.*;

public class ClientUtils {
    public static void setDirection(Client c, List<Client> clients) throws IOException {
        int clientSize = clients.size();
        if (clientSize > 1) {
            SocketChannel channel;
            DataOutputStream dOut;

            for (Client cc : clients) {
                if (cc.getMyId() == c.getMyId()) {
                    continue;
                }

                channel = cc.getChannel();
                dOut = new DataOutputStream(channel.socket().getOutputStream());
                dOut.write(NetworkCommands.send_move_direction.ordinal());
                dOut.writeInt(c.getMyId());
                putDirectionInStream(dOut,c.getDirection());
                putPositionInStream(dOut, c.getPosition());
            }
        }
    }

    public static void sendAllClientsToClient(Client c, List<Client> clients) throws IOException {
        int clientSize = clients.size();

        if (clientSize > 1) {
            int clientSizeToSend = clientSize - 1;
            SocketChannel channel = c.getChannel();
            DataOutputStream dOut = new DataOutputStream(channel.socket().getOutputStream());
            dOut.write(NetworkCommands.send_all_clients.ordinal());
            dOut.writeInt(clientSizeToSend);

            for (Client cc : clients) {
                if (cc.getMyId() == c.getMyId()) {
                    continue;
                }
                putClientInStream(dOut, cc);
            }
        }
    }

    public static void clientDisconnected(Client c, List<Client> clients) throws IOException {
        SocketChannel channel;
        DataOutputStream dOut;
        for (Client cc : clients) {
            channel = cc.getChannel();
            if (cc.getMyId() == c.getMyId()) {
                continue;
            }

            dOut = new DataOutputStream(channel.socket().getOutputStream());
            dOut.write(NetworkCommands.client_disconnect.ordinal());
            dOut.writeInt(c.getMyId());
            dOut.flush();
        }
    }

    public static void newClientConnected(Client c, List<Client> clients) throws IOException {
        updateUsername(c, clients);
    }

    public static void updateUsername(Client client, List<Client> clients) throws IOException {
        SocketChannel channel;
        DataOutputStream dOut;
        for (Client c : clients) {
            channel = c.getChannel();
            if (c.getMyId() == client.getMyId()) {
                continue;
            }

            dOut = new DataOutputStream(channel.socket().getOutputStream());
            dOut.write(NetworkCommands.client_connect.ordinal());
            putClientInStream(dOut, client);
            dOut.flush();
        }
    }
}
