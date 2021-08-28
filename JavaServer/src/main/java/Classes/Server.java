package Classes;

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

                    clients.add(c);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public void removeClient(Client client) {
        clients.remove(client);

        // todo tell clients someone has disconnected
    }

    public List<Client> getClients() {
        return clients;
    }

    public void setClients(List<Client> clients) {
        this.clients = clients;
    }

    public ServerSocketChannel getSocket() {
        return socket;
    }

    public void setSocket(ServerSocketChannel socket) {
        this.socket = socket;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public static void main(String... args) {
        new Server(21337);
    }
}