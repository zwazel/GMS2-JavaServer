package Classes;

import GlobalStuff.NetworkCommands;

import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.List;

public class SendPing implements Runnable {
    private final Server server;
    private final Client client;
    private SocketChannel channel;
    private final int time;
    private final boolean toEveryone;

    public SendPing(Server server, Client client, int time, boolean toEveryone) {
        this.server = server;
        this.client = client;
        this.channel = client.getChannel();
        this.time = time;
        this.toEveryone = toEveryone;
    }

    @Override
    public void run() {
        DataOutputStream dOut;
        try {
            if(toEveryone) {
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

    public Server getServer() {
        return server;
    }

    public Client getClient() {
        return client;
    }

    public int getTime() {
        return time;
    }
}
