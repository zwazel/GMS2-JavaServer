package Classes;

import GlobalStuff.NetworkCommands;
import GlobalStuff.states.playerStates.MainStates;
import GlobalStuff.states.playerStates.SubStates;
import util.Direction;
import util.NetworkUtils.timer.ShootTimer;
import util.Position;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

import static util.NetworkUtils.GetUtils.*;

public class Client implements Runnable {
    private SocketChannel channel;
    private boolean connected = true;
    private Server server;
    private int myId;
    private String username = null;
    private Position position;
    private Position positionBefore;
    private int speed;
    private int sprintSpeed;
    private int shootingCooldown;
    private Thread shootTimerThread;
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
    private MainStates mainState = MainStates.IDLE;
    private SubStates subState = SubStates.NOTHING;

    public Client(int id, Position position, Server server, SocketChannel channel) {
        this.myId = id;
        this.position = position;
        this.positionBefore = position;
        this.server = server;
        this.channel = channel;

        this.direction = new Direction((byte) 0, (byte) 0);
        this.health = 100;
        this.speed = 5;
        this.sprintSpeed = 8;
        this.shootingCooldown = 2000;
        this.acceleration = 0.2f;
        this.normalDeceleration = 0.2f;
        this.skidDeceleration = 0.3f;
    }

    @Override
    public void run() {
        int shootTimeCounter = 0;
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

                        case update_client_serverSide:
                            this.setDirection(getDirectionFromBuffer(bufferWithActualStuff));

                            Position tempPos = getPositionFromBuffer(bufferWithActualStuff);
                            if (this.direction.isZero() || !this.position.isSamePosition(tempPos)) {
                                this.setPositionBefore(this.position);
                            }
                            this.setPosition(tempPos);

                            this.setRotation(bufferWithActualStuff.getFloat());

                            this.setMainState(MainStates.getValues()[bufferWithActualStuff.get()]);
                            this.setSubState(SubStates.getValues()[bufferWithActualStuff.get()]);

                            if (subState == SubStates.SHOOTING) {
                                if (this.shootTimerThread == null) {
                                    System.out.println("shootTimeCounter = " + shootTimeCounter++);
                                    ShootTimer shootTimer = new ShootTimer(this);
                                    shootTimerThread = new Thread(shootTimer);
                                    shootTimerThread.start();
                                }
                            }

                            break;
                        default:
                            break loopThroughBuffer;
                    }
                }

                increaseReceivedPackages(2);

//                System.out.println(getInfos());
            } catch (IOException ex) {
                ex.printStackTrace();
                System.out.println(channel.socket().getInetAddress().toString() + " (" + this.username + ") has disconnected.");
                connected = false;
                if (shootTimerThread != null) {
                    shootTimerThread.interrupt();
                }
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

    public void increaseSentPackages() {
        increaseSentPackages(1);
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

    public MainStates getMainState() {
        return mainState;
    }

    public void setMainState(MainStates mainState) {
        this.mainState = mainState;
    }

    public SubStates getSubState() {
        return subState;
    }

    public void setSubState(SubStates subState) {
        this.subState = subState;
    }

    public int getSprintSpeed() {
        return sprintSpeed;
    }

    public void setSprintSpeed(int sprintSpeed) {
        this.sprintSpeed = sprintSpeed;
    }

    public int getShootingCooldown() {
        return shootingCooldown;
    }

    public void setShootingCooldown(int shootingCooldown) {
        this.shootingCooldown = shootingCooldown;
    }

    public Thread getShootTimerThread() {
        return shootTimerThread;
    }

    public void setShootTimerThread(Thread shootTimerThread) {
        this.shootTimerThread = shootTimerThread;
    }
}