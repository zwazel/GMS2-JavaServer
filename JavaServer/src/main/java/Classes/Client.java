package Classes;

import GlobalStuff.NetworkCommands;
import util.Direction;
import util.Position;

import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

import static util.NetworkUtils.GetUtils.*;
import static util.NetworkUtils.PutUtils.putClientInStream;

public class Client implements Runnable {
    private SocketChannel channel;
    private boolean connected = true;
    private Server server;
    private int myId;
    private String username;
    private Position position;
    private int speed;
    private int health;
    private Direction direction = new Direction();
    private long sentPackages = 0;
    private long receivedPackages = 0;
    private int pingTime = -1;
    private int ping = -1;
    private boolean ready = false;
    private ArrayList<Client> newClients = new ArrayList<>();
    private ArrayList<Client> clientsDisconnected = new ArrayList<>();

    public Client(int id, Position position, Server server, SocketChannel channel) {
        this.myId = id;
        this.position = position;
        this.direction = new Direction((byte) 0, (byte) 0);
        this.server = server;
        this.channel = channel;

        this.health = 100;
        this.speed = 3;
    }

    public Client(int id, int speed, int health, Position position, Server server, SocketChannel channel) {
        this.myId = id;
        this.position = position;
        this.server = server;
        this.channel = channel;
        this.speed = speed;
        this.health = health;
    }

    private void initClientThroughNetwork() throws IOException {
        DataOutputStream dOut = new DataOutputStream(channel.socket().getOutputStream());
        dOut.write(NetworkCommands.send_client_its_id.ordinal());
        putClientInStream(dOut, this, false);

        dOut.flush();
    }

    @Override
    public void run() {
        while (connected) {
            try {
                final int bufferSize = 64;
                ByteBuffer buffer = ByteBuffer.allocate(bufferSize);
                channel.read(buffer);   // fill buffer from the input stream
                buffer.order(ByteOrder.LITTLE_ENDIAN);
                buffer.position(0);

                final int receivedBufferSize = buffer.getInt();
                System.out.println("receivedBufferSize = " + receivedBufferSize);

                ByteBuffer bufferWithActualStuff = ByteBuffer.allocate(bufferSize);
                channel.read(bufferWithActualStuff);   // fill buffer from the input stream
                bufferWithActualStuff.order(ByteOrder.LITTLE_ENDIAN);
                bufferWithActualStuff.position(0);

                List<Client> clients = this.server.getClients();

                loopThroughBuffer:
                while (bufferWithActualStuff.hasRemaining()) {
                    final byte commandByte = bufferWithActualStuff.get();

                    final NetworkCommands command = NetworkCommands.getValues()[commandByte];
                    System.out.println(this.getUsername() + ", command in = " + command);

                    switch (command) {
                        case test:
                            break;

                        case send_ping:
                            this.pingTime = bufferWithActualStuff.getInt();
                            break;
                        case send_ping_other:
                            this.ping = bufferWithActualStuff.getInt();
                            break;
                        case receive_username:
                            int stringLength = bufferWithActualStuff.getInt();
                            String username = getStringFromBuffer(bufferWithActualStuff, stringLength);

                            boolean newConnection = (this.getUsername() == null);
                            this.setUsername(username);

                            if (newConnection) {
                                this.ready = true;
                                server.addNewClient(this);
                            } else {
                                // TODO: 05.09.2021: Update username
                            }
                            break;

                        case get_move_direction:
                            this.setDirection(getDirectionFromBuffer(bufferWithActualStuff));

                            this.setPosition(getPositionFromBuffer(bufferWithActualStuff));
                            break;
                        default:
                            break loopThroughBuffer;
                    }
                }

                increaseReceivedPackages(2);

                if (this.ready) {
                    sendOutData();

                    increaseSentPackages(1);
                }

                System.out.println(getInfos());
            } catch (IOException ex) {
                ex.printStackTrace();
                System.out.println(channel.socket().getInetAddress().toString() + " (" + this.username + ") has disconnected.");
                connected = false;
                try {
                    channel.close();
                } catch (IOException ex1) {
                    ex1.printStackTrace();
                } finally {
                    server.removeClient(this);
                }
            }
        }
    }

    private void sendOutData() throws IOException {
        DataOutputStream dOut = new DataOutputStream(this.channel.socket().getOutputStream());
        dOut.write(NetworkCommands.send_ping.ordinal());
        dOut.writeInt(this.pingTime);

        if (this.newClients.size() > 0) {
            System.out.println("username = " + username);
            System.out.println("newClients = " + newClients.size());
            ArrayList<Client> clientsToRemove = new ArrayList<>();
            dOut.write(NetworkCommands.client_connect.ordinal());
            dOut.writeInt(this.newClients.size());
            for (Client client : newClients) {
                if (client.isReady()) {
                    putClientInStream(dOut, client, true);
                    clientsToRemove.add(client);
                }
            }

            for (Client client : clientsToRemove) {
                newClients.remove(client);
            }
        }

        if (this.server.getClients().size() > 1) {
            ArrayList<Client> clientsToUpdate = new ArrayList<>();
            for (Client client : this.server.getClients()) {
                if (newClients.contains(client) || this.myId == client.getMyId()) {
                    continue;
                }

                clientsToUpdate.add(client);
            }

            if (clientsToUpdate.size() > 0) {
                dOut.write(NetworkCommands.update_clients.ordinal());
                dOut.writeInt(clientsToUpdate.size());
                for (Client client : clientsToUpdate) {
                    putClientInStream(dOut, client, false);
                }
            }
        }

        if(this.clientsDisconnected.size() > 0) {
            dOut.write(NetworkCommands.client_disconnect.ordinal());
            dOut.writeInt(clientsDisconnected.size());
            for(Client client : clientsDisconnected) {
                dOut.writeInt(client.getMyId());
            }
        }

        dOut.write(NetworkCommands.end_of_packet.ordinal());
        dOut.flush();
    }

    @Override
    public String toString() {
        return "Client{" +
                "channel=" + channel +
                ", connected=" + connected +
                ", server=" + server +
                ", myId=" + myId +
                ", username='" + username + '\'' +
                ", position=" + position +
                ", speed=" + speed +
                ", health=" + health +
                ", direction=" + direction +
                ", sentPackages=" + sentPackages +
                ", receivedPackages=" + receivedPackages +
                ", ping=" + ping +
                ", ready=" + ready +
                '}';
    }

    public String getInfos() {
        return (this.getMyId() + "{" + this.getUsername() +
                "},ping{" + this.getPing() +
                "},sent/received{" + this.getSentPackages() + "," + this.getReceivedPackages() +
                "}," + this.direction +
                "," + this.position);
    }

    public int getPingTime() {
        return pingTime;
    }

    public void setPingTime(int pingTime) {
        this.pingTime = pingTime;
    }

    public ArrayList<Client> getNewClients() {
        return newClients;
    }

    public void setNewClients(ArrayList<Client> newClients) {
        this.newClients = newClients;
    }

    public void addNewClient(Client client) {
        if (this.myId != client.getMyId()) {
            this.newClients.add(client);
        }
    }

    public void addNewClients(List<Client> clients) {
        for (Client client : clients) {
            if (!client.isReady() || client.getMyId() == this.myId) {
                continue;
            }

            this.newClients.add(client);
        }
    }

    public ArrayList<Client> getClientsDisconnected() {
        return clientsDisconnected;
    }

    public void setClientsDisconnected(ArrayList<Client> clientsDisconnected) {
        this.clientsDisconnected = clientsDisconnected;
    }

    public void addClientDisconnected(Client client) {
        if (this.myId != client.getMyId()) {
            this.clientsDisconnected.add(client);
        }
    }

    public SocketChannel getChannel() {
        return channel;
    }

    public void setChannel(SocketChannel channel) {
        this.channel = channel;
    }

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public int getMyId() {
        return myId;
    }

    public void setMyId(int myId) {
        this.myId = myId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public long getSentPackages() {
        return sentPackages;
    }

    public void setSentPackages(long sentPackages) {
        this.sentPackages = sentPackages;
    }

    public void increaseSentPackages(long amount) {
        this.sentPackages += amount;
        this.server.increaseSentPackages(amount);
    }

    public long getReceivedPackages() {
        return receivedPackages;
    }

    public void setReceivedPackages(long receivedPackages) {
        this.receivedPackages = receivedPackages;
    }

    public void increaseReceivedPackages(long amount) {
        this.receivedPackages += amount;
        this.server.increaseReceivedPackages(amount);
    }

    public int getPing() {
        return ping;
    }

    public void setPing(int ping) {
        this.ping = ping;
    }

    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }
}