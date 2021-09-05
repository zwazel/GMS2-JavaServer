package Classes;

import GlobalStuff.NetworkCommands;
import util.ClientUtils;

import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.List;

import static util.NetworkUtils.GetUtils.*;

public class Sender implements Runnable {
    private final Client client;
    private final ByteBuffer buffer;

    public Sender(ByteBuffer buffer, Client client) {
        this.client = client;
        this.buffer = buffer;
    }

    @Override
    public void run() {
        DataOutputStream dOut;
        SocketChannel channel = client.getChannel();
        List<Client> clients = client.getServer().getClients();

        loopThroughBuffer:
        while (buffer.hasRemaining()) {
            final byte commandByte = buffer.get();

            final NetworkCommands command = NetworkCommands.getValues()[commandByte];
            System.out.println(client.getUsername() + ", command = " + command);

            switch (command) {
                case test:
                    break;

                case send_ping:
                    SendPing sP = new SendPing(client.getServer(), client, buffer.getInt());
                    Thread t = new Thread(sP);
                    t.start();
                    break;
                case send_ping_other:
                    int ping = buffer.getInt();
                    try {
                        client.setPing(ping);
                        ClientUtils.sendPingToEveryone(this.client, clients, ping);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case receive_username:
                    int stringLength = buffer.getInt();
                    String username = getStringFromBuffer(buffer, stringLength);

                    boolean newConnection = (client.getUsername() == null);
                    client.setUsername(username);

                    try {
                        if (newConnection) {
                            ClientUtils.newClientConnected(client, clients);

                            ClientUtils.sendAllClientsToClient(client, clients);
                        } else {
                            ClientUtils.updateUsername(client, clients);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;

                case get_move_direction:
                    try {
                        client.setDirection(getDirectionFromBuffer(buffer));

                        client.setPosition(getPositionFromBuffer(buffer));

                        ClientUtils.setDirection(client, clients);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    break loopThroughBuffer;
            }
        }
    }
}
