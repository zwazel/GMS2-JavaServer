package Classes;

import util.NetworkUtils.sender.InitClient;
import util.Position;

import java.io.IOException;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private List<Client> clients;
    private ServerSocketChannel socket;
    private boolean running;
    private Position startPosition;
    private int idCounter = 0;
    private long sentPackages = 0;
    private long receivedPackages = 0;

    public Server(int port) {
        this.clients = new ArrayList<>();
        this.running = false;
        this.startPosition = new Position(100, 100);

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

        while (running) {
            try {
                // Check for new connections
                SocketChannel newChannel = socket.accept();

                // If a connection is found, create a Client and add it to
                // the client list
                if (newChannel != null) {
                    System.out.println("New Connection " + newChannel.socket().getInetAddress().toString());
                    Client c = new Client(idCounter++, startPosition, this, newChannel);
                    if (this.clients.size() > 0) {
                        c.addNewClients(clients);
                    }
                    Thread t = new Thread(c);
                    t.start();

                    InitClient initClient = new InitClient(c);
                    t = new Thread(initClient);
                    t.start();

                    clients.add(c);
                    System.out.println("NEW CLIENT! clients.size() = " + clients.size());
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public void printClientInfos() {
        System.out.println("currently connected clients: " + this.clients.size());
        for (Client c : this.clients) {
            System.out.println(c.getInfos());
        }
    }

    public void addNewClient(Client newClient) {
        for (Client client : clients) {
            if (client.getMyId() == newClient.getMyId()) {
                continue;
            }

            client.addNewClient(newClient);
        }
    }

    public void removeClient(Client client) {
        for (Client clientToInform : clients) {
            if (client.getMyId() == clientToInform.getMyId()) {
                continue;
            }

            clientToInform.addClientDisconnected(client);
        }
        clients.remove(client);
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

    public int getIdCounter() {
        return idCounter;
    }

    public void setIdCounter(int idCounter) {
        this.idCounter = idCounter;
    }

    public Position getStartPosition() {
        return startPosition;
    }

    public void setStartPosition(Position startPosition) {
        this.startPosition = startPosition;
    }

    public long getSentPackages() {
        return sentPackages;
    }

    public void setSentPackages(long sentPackages) {
        this.sentPackages = sentPackages;
    }

    public void increaseSentPackages(long amount) {
        this.sentPackages += amount;
    }

    public long getReceivedPackages() {
        return receivedPackages;
    }

    public void setReceivedPackages(long receivedPackages) {
        this.receivedPackages = receivedPackages;
    }

    public void increaseReceivedPackages(long amount) {
        this.receivedPackages += amount;
    }

    public static void main(String... args) {
        new Server(21337);
    }
}