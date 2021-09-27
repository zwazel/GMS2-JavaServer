package Classes;

import Classes.Network.NetworkTracker;
import util.NetworkUtils.sender.InitClient;
import util.Position;

import java.io.IOException;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private List<Client> clientsUnready;
    private List<Client> clientsReady;
    private ServerSocketChannel socket;
    private boolean running;
    private Position startPosition;
    private int idCounter = 0;
    private long sentPackages = 0;
    private long receivedPackages = 0;
    private NetworkTracker networkTracker;

    public Server(int port) {
        this.clientsUnready = new ArrayList<>();
        this.clientsReady = new ArrayList<>();
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

        networkTracker = new NetworkTracker(this);
        Thread t = new Thread(networkTracker);
        t.start();

        while (running) {
            try {
                // Check for new connections
                SocketChannel newChannel = socket.accept();

                // If a connection is found, create a Client and add it to
                // the client list
                if (newChannel != null) {
                    System.out.println("New Connection " + newChannel.socket().getInetAddress().toString());
                    Client c = new Client(idCounter++, startPosition, this, newChannel);
                    if (this.clientsUnready.size() > 0) {
                        c.addNewClients(clientsUnready);
                    }
                    t = new Thread(c);
                    t.start();

                    InitClient initClient = new InitClient(c);
                    t = new Thread(initClient);
                    t.start();

                    clientsUnready.add(c);
                    System.out.println("NEW CLIENT! clients.size() = " + clientsUnready.size());
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void printClientInfos() {
        System.out.println("currently connected clients: " + this.clientsUnready.size());
        for (Client c : this.clientsUnready) {
            System.out.println(c.getInfos());
        }
    }

    public void addNewClient(Client newClient) {
        this.clientsReady.add(newClient);
        this.clientsUnready.remove(newClient);
        for (Client client : clientsReady) {
            if (client.getMyId() == newClient.getMyId()) {
                continue;
            }

            client.addNewClient(newClient);
        }
    }

    public void removeClient(Client client) {
        for (Client clientToInform : clientsUnready) {
            if (client.getMyId() == clientToInform.getMyId()) {
                continue;
            }

            clientToInform.addClientDisconnected(client);
        }
        clientsReady.remove(client);
        clientsUnready.remove(client);
    }

    public List<Client> getClientsUnready() {
        return clientsUnready;
    }

    public void setClientsUnready(List<Client> clientsUnready) {
        this.clientsUnready = clientsUnready;
    }

    public List<Client> getClientsReady() {
        return clientsReady;
    }

    public void setClientsReady(List<Client> clientsReady) {
        this.clientsReady = clientsReady;
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