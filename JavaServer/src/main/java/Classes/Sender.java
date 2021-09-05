package Classes;

import GlobalStuff.NetworkCommands;
import util.NetworkUtils.sender.InitClientForEveryone;
import util.NetworkUtils.sender.SendPing;

import java.nio.ByteBuffer;
import java.util.List;

import static util.NetworkUtils.GetUtils.*;

public record Sender(ByteBuffer buffer, Client client) implements Runnable {
    @Override
    public void run() {
        List<Client> clients = client.getServer().getClients();

        loopThroughBuffer:
        while (buffer.hasRemaining()) {
            final byte commandByte = buffer.get();

            final NetworkCommands command = NetworkCommands.getValues()[commandByte];
            System.out.println(client.getUsername() + ", command = " + command);

            SendPing sP;
            Thread t;
            switch (command) {
                case test:
                    break;

                case send_ping:
                    sP = new SendPing(client.getServer(), client, buffer.getInt(), false);
                    sP.run();
//                    t = new Thread(sP);
//                    t.start();
                    break;
                case send_ping_other:
                    client.setPing(buffer.getInt());
                    sP = new SendPing(client.getServer(), client, client.getPing(), true);
                    sP.run();
//                    t = new Thread(sP);
//                    t.start();
                    break;
                case receive_username:
                    int stringLength = buffer.getInt();
                    String username = getStringFromBuffer(buffer, stringLength);

                    boolean newConnection = (client.getUsername() == null);
                    client.setUsername(username);

                    if (newConnection) {
                        if (clients.size() > 1) {
                            InitClientForEveryone initClientForEveryone = new InitClientForEveryone(client);
                            initClientForEveryone.run();
//                            t = new Thread(initClientForEveryone);
//                            t.start();
                        }
                    } else {
//                            ClientUtils.updateUsername(client, clients);
                    }
                    break;

                case get_move_direction:
                        client.setDirection(getDirectionFromBuffer(buffer));

                        client.setPosition(getPositionFromBuffer(buffer));

//                        ClientUtils.setDirection(client, clients);
                    break;
                default:
                    break loopThroughBuffer;
            }
        }
    }
}
