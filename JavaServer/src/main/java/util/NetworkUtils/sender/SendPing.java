package util.NetworkUtils.sender;

import Classes.Client;
import Classes.Server;
import GlobalStuff.NetworkCommands;

import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.List;

public record SendPing(Server server, Client client, int time, boolean toEveryone) implements Runnable {
    @Override
    public void run() {
        DataOutputStream dOut;
        SocketChannel channel;
        try {
            if (toEveryone) {
                List<Client> clients = server.getClients();
                for (Client cc : clients) {
                    if (client.getMyId() == cc.getMyId()) {
                        continue;
                    }
                    channel = cc.getChannel();

                    dOut = new DataOutputStream(channel.socket().getOutputStream());
                    dOut.write(NetworkCommands.send_ping_other.ordinal());
                    dOut.writeInt(client.getMyId());
                    dOut.writeInt(client.getPing());
                    dOut.flush();
                    cc.increaseSentPackages(1);
                }
            } else {
                channel = client.getChannel();
                dOut = new DataOutputStream(channel.socket().getOutputStream());
                dOut.write(NetworkCommands.send_ping.ordinal());
                dOut.writeInt(time);
                dOut.flush();
                client.increaseSentPackages(1);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
