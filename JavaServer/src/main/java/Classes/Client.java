package Classes;

import GlobalStuff.NetworkCommands;
import util.Direction;
import util.NetworkUtils.PutUtils;
import util.Position;

import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.SocketChannel;

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
    private int ping;

    public Client(int id, Position position, Server server, SocketChannel channel) {
        this.myId = id;
        this.position = position;
        this.server = server;
        this.channel = channel;

        this.health = 100;
        this.speed = 3;

        try {
            initClientThroughNetwork();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Client(int id, int speed, int health, Position position, Server server, SocketChannel channel) {
        this.myId = id;
        this.position = position;
        this.server = server;
        this.channel = channel;
        this.speed = speed;
        this.health = health;

        try {
            initClientThroughNetwork();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initClientThroughNetwork() throws IOException {
        DataOutputStream dOut = new DataOutputStream(channel.socket().getOutputStream());
        dOut.write(NetworkCommands.send_client_its_id.ordinal());
        PutUtils.putClientInStream(dOut, this, false);

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

                Sender sender = new Sender(bufferWithActualStuff, this);
                Thread t = new Thread(sender);
                t.start();

                increaseReceivedPackages(2);

                System.out.println(getInfos());
            } catch (IOException ex) {
                ex.printStackTrace();
                System.out.println(channel.socket().getInetAddress().toString() + " (" + this.username + ") has disconnected.");
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

    public String getInfos() {
        return (this.getMyId() + "{" + this.getUsername() +
                "},ping{" + this.getPing() +
                "},sent/received{" + this.getSentPackages() + "," + this.getReceivedPackages() +
                "}," + this.direction +
                "," + this.position);
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
}