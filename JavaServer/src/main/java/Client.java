import GlobalStuff.NetworkCommands;

import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.SocketChannel;

import static util.NetworkUtils.getStringFromBuffer;
import static util.NetworkUtils.putStringInStream;

public class Client implements Runnable {

    private SocketChannel channel;
    private boolean connected = true;
    private Server server;
    private int myId;
    private String username;

    public Client(int id, Server server, SocketChannel channel) {
        this.myId = id;
        this.server = server;
        this.channel = channel;

        try {
            initClientThroughNetwork();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Client(int id, String username, Server server, SocketChannel channel) {
        this.myId = id;
        this.server = server;
        this.channel = channel;

        try {
            initClientThroughNetwork();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initClientThroughNetwork() throws IOException {
        DataOutputStream dOut = new DataOutputStream(channel.socket().getOutputStream());
        dOut.write(NetworkCommands.send_client_its_id.ordinal());
        dOut.writeInt(this.myId);
        if (username != null) {
            putStringInStream(dOut, username);
        }
        dOut.flush();
    }

    @Override
    public void run() {
        System.out.println("run, connected = " + connected);
        while (connected) {
            System.out.println("test");

            ByteBuffer buffer;
            try {
                final int bufferSize = 1024;
                buffer = ByteBuffer.allocate(bufferSize);
                channel.read(buffer);   // fill buffer from the input stream

                buffer.order(ByteOrder.LITTLE_ENDIAN);

                buffer.position(0);
                final byte mid = buffer.get();

                DataOutputStream dOut;
                NetworkCommands command = NetworkCommands.getValues()[mid];
                System.out.println("command = " + command);
                switch (command) {
                    case send_ping:
                        int time = buffer.getInt();

                        System.out.println("time = " + time);

                        dOut = new DataOutputStream(channel.socket().getOutputStream());
                        dOut.write(command.ordinal());
                        dOut.writeInt(time);
                        dOut.flush();
                        break;
                    case receive_username:
                        int stringLength = buffer.getInt();
                        String username = getStringFromBuffer(buffer, stringLength);

                        System.out.println("username = " + username);

                        this.username = username;
                        break;
                    default:
                        // ...
                        break;
                }
            } catch (IOException ex) {
                ex.printStackTrace();
                System.out.println(channel.socket().getInetAddress().toString() + " has disconnected.");
                connected = false;
                server.removeClient(this);
                try {
                    channel.close();
                } catch (IOException ex1) {
                    ex1.printStackTrace();
                }
            }
        }
    }
}