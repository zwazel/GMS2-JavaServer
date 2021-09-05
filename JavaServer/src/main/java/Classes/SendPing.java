package Classes;

import GlobalStuff.NetworkCommands;

import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.channels.SocketChannel;

public class SendPing implements Runnable {
    private final Server server;
    private final Client client;
    private final SocketChannel channel;
    private final int time;

    public SendPing(Server server, Client client, int time) {
        this.server = server;
        this.client = client;
        this.channel = client.getChannel();
        this.time = time;
    }

    @Override
    public void run() {
        DataOutputStream dOut;
        try {
            dOut = new DataOutputStream(channel.socket().getOutputStream());
            dOut.write(NetworkCommands.send_ping.ordinal());
            dOut.writeInt(time);
            dOut.flush();
            client.increaseSentPackages(1);
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
