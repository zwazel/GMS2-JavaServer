import java.io.IOException;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private List<Client> clients;
    private ServerSocketChannel socket;
    private boolean running;

    public Server(int port) {
        this.clients = new ArrayList<>();
        this.running = false;

        System.out.print("Trying to Listen on Port : " + port + "...");
        try {
            // Success
            ServerSocketChannel channel = ServerSocketChannel.open();
            channel.socket().bind(new java.net.InetSocketAddress(port));
            System.out.println("Success!");
            channel.configureBlocking(false);
            socket = channel;
            running = true;
        } catch (IOException e) {
            // Failure
            System.err.println("Failed!");
            socket = null;
            running = false;
        }

        // Server loop
        while (running) {
            try {
                // Check for new connections
                SocketChannel newChannel = socket.accept();

                // If a connection is found, create a Client and add it to
                // the client list
                if (newChannel != null) {
                    System.out.println("New Connection " + newChannel.socket().getInetAddress().toString());
                    Client c = new Client(clients.size(), this, newChannel);
                    Thread t = new Thread(c);
                    t.start();

                    for (Client cc : clients) {
                        cc.newClientConnected(c);
                    }

                    clients.add(c);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public void updateUsername(Client client) {
        for (Client c : clients) {
            if (c.getMyId() == client.getMyId()) {
                continue;
            }

            try {
                c.updateUsername(client);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void removeClient(Client client) {
        clients.remove(client);
    }

    public static void main(String... args) {
        new Server(21337);
    }

}