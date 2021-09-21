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
    private String username = null;
    private Position position;
    private Position positionBefore;
    private int speed;
    private int health;
    private Direction direction;
    private long sentPackages = 0;
    private long receivedPackages = 0;
    private int pingTime = -1;
    private int ping = -1;
    private boolean ready = false;
    private ArrayList<Client> newClients = new ArrayList<>();
    private ArrayList<Client> clientsDisconnected = new ArrayList<>();
    private float rotation;
    private float horizontalVelocity = 0;
    private float verticalVelocity = 0;
    private float acceleration;
    private float normalDeceleration;
    private float skidDeceleration;

    public Client(int id, Position position, Server server, SocketChannel channel) {
        this.myId = id;
        this.position = position;
        this.positionBefore = position;
        this.server = server;
        this.channel = channel;

        this.direction = new Direction((byte) 0, (byte) 0);
        this.health = 100;
        this.speed = 5;
        this.acceleration = 0.2f;
        this.normalDeceleration = 0.2f;
        this.skidDeceleration = 0.3f;
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
//                System.out.println("receivedBufferSize = " + receivedBufferSize);

                ByteBuffer bufferWithActualStuff = ByteBuffer.allocate(bufferSize);
                channel.read(bufferWithActualStuff);   // fill buffer from the input stream
                bufferWithActualStuff.order(ByteOrder.LITTLE_ENDIAN);
                bufferWithActualStuff.position(0);

                loopThroughBuffer:
                while (bufferWithActualStuff.hasRemaining()) {
                    final byte commandByte = bufferWithActualStuff.get();

                    final NetworkCommands command = NetworkCommands.getValues()[commandByte];
//                    System.out.println(this.getUsername() + ", command in = " + command);

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

                            System.out.println("username = " + username + ", id = " + myId);

                            if (newConnection) {
                                this.ready = true;
                                server.addNewClient(this);
                            } else {
                                // TODO: 05.09.2021: Update username
                            }
                            break;

                        case get_move_direction:
                            this.setDirection(getDirectionFromBuffer(bufferWithActualStuff));

                            Position tempPos = getPositionFromBuffer(bufferWithActualStuff);
                            if (this.direction.isZero() || !this.position.isSamePosition(tempPos)) {
                                this.setPositionBefore(this.position);
                            }
                            this.setPosition(tempPos);

                            this.setRotation(bufferWithActualStuff.getFloat());
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

//                System.out.println(getInfos());
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

        if (this.server.getClients().size() > 1) {
            ArrayList<Client> clientsToUpdate = new ArrayList<>();
            for (Client client : this.server.getClients()) {
                if (!client.isReady() || this.myId == client.getMyId() || newClients.contains(client) || clientsDisconnected.contains(client)) {
                    continue;
                }

                clientsToUpdate.add(client);
            }

            if (clientsToUpdate.size() > 0) {
                dOut.write(NetworkCommands.update_clients.ordinal());
                dOut.writeInt(clientsToUpdate.size());
//                System.out.println("clientsToUpdate.size() = " + clientsToUpdate.size());
                for (Client client : clientsToUpdate) {
//                    System.out.println("put client " + client.username + " as update in " + this.username);
                    putClientInStream(dOut, client, false);
                }
            }
        }

        if (this.newClients.size() > 0) {
//            System.out.println("newClients.size() = " + newClients.size() + " in " + username);
//            System.out.println("newClients = " + newClients + " in " + username);
            ArrayList<Client> clientsReady = new ArrayList<>();
            for (Client client : newClients) {
                if (client.isReady()) {
//                    System.out.println("current new client: " + client.username + " in " + username);
                    clientsReady.add(client);
                }
            }
//            System.out.println("clientsReady.size() = " + clientsReady.size() + " in " + username);
            if (clientsReady.size() > 0) {
                ArrayList<Client> clientsToRemove = new ArrayList<>();
                dOut.write(NetworkCommands.client_connect.ordinal());
                dOut.writeInt(clientsReady.size());
//                System.out.println("clientsReady.size() = " + clientsReady.size());
                for (Client client : clientsReady) {
//                    System.out.println("put client " + client.username + " as new client in " + this.username);
                    putClientInStream(dOut, client, true);
                    clientsToRemove.add(client);
                }

                for (Client client : clientsToRemove) {
                    newClients.remove(client);
                }
            }
        }

        if (this.clientsDisconnected.size() > 0) {
            dOut.write(NetworkCommands.client_disconnect.ordinal());
            dOut.writeInt(clientsDisconnected.size());
            for (Client client : clientsDisconnected) {
                dOut.writeInt(client.getMyId());
            }

            clientsDisconnected.clear();
        }

        dOut.write(NetworkCommands.end_of_packet.ordinal());
        dOut.flush();
    }

    @Override
    public String toString() {
        return "Client{" +
                "connected=" + connected +
                ", myId=" + myId +
                ", username='" + username + '\'' +
                ", position=" + position +
                ", direction=" + direction +
                ", rotation=" + rotation +
                ", speed=" + speed +
                ", health=" + health +
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
            if (client.getMyId() == this.myId || this.newClients.contains(client) || !client.isReady()) {
                continue;
            }

//            System.out.println("adding client " + client.username + "(" + client.getMyId() + ") as new client to " + this);
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

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public float getHorizontalVelocity() {
        return horizontalVelocity;
    }

    public void setHorizontalVelocity(float horizontalVelocity) {
        this.horizontalVelocity = horizontalVelocity;
    }

    public float getVerticalVelocity() {
        return verticalVelocity;
    }

    public void setVerticalVelocity(float verticalVelocity) {
        this.verticalVelocity = verticalVelocity;
    }

    public float getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(float acceleration) {
        this.acceleration = acceleration;
    }

    public float getNormalDeceleration() {
        return normalDeceleration;
    }

    public void setNormalDeceleration(float normalDeceleration) {
        this.normalDeceleration = normalDeceleration;
    }

    public float getSkidDeceleration() {
        return skidDeceleration;
    }

    public void setSkidDeceleration(float skidDeceleration) {
        this.skidDeceleration = skidDeceleration;
    }

    public Position getPositionBefore() {
        return positionBefore;
    }

    public void setPositionBefore(Position positionBefore) {
        this.positionBefore = positionBefore;
    }
}