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
    NetworkCommands command;
    Client client;
    ByteBuffer buffer;

    public Sender(NetworkCommands command, Client client, ByteBuffer buffer) {
        this.command = command;
        this.client = client;
        this.buffer = buffer;
    }

    @Override
    public void run() {
        DataOutputStream dOut;
        SocketChannel channel = client.getChannel();
        List<Client> clients = client.getServer().getClients();
        switch (command) {
            case test:
                break;

            case send_ping:
                int time = buffer.getInt();

                try {
                    dOut = new DataOutputStream(channel.socket().getOutputStream());
                    dOut.write(command.ordinal());
                    dOut.writeInt(time);
                    dOut.flush();
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
                break;
        }
    }
}