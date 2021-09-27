package Classes.Network;

import Classes.Client;
import Classes.Server;
import GlobalStuff.NetworkCommands;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import static util.NetworkUtils.PutUtils.putClientInStream;

public class NetworkTracker implements Runnable {
    private final Server server;
    private ArrayList<Streamable> objectsToTrack = new ArrayList<>();

    public NetworkTracker(Server server) {
        this.server = server;
    }

    @Override
    public void run() {
        while (server.isRunning()) {
            try {
                Thread.sleep(100);

                for (Client c : server.getClientsReady()) {
                    DataOutputStream dOut = new DataOutputStream(c.getChannel().socket().getOutputStream());
                    dOut.write(NetworkCommands.send_ping.ordinal());
                    dOut.writeInt(c.getPingTime());

                    ArrayList<Client> newClients = c.getNewClients();
                    ArrayList<Client> clientsToUpdate = new ArrayList<>();
                    for (Client client : this.server.getClientsReady()) {
                        if (newClients.contains(client) || c.getClientsDisconnected().contains(client)) {
                            continue;
                        }

                        clientsToUpdate.add(client);
                    }

                    if (clientsToUpdate.size() > 0) {
                        dOut.write(NetworkCommands.update_clients_clientSide.ordinal());
                        dOut.writeInt(clientsToUpdate.size());
                        for (Client client : clientsToUpdate) {
                            putClientInStream(dOut, client, false);
                        }
                    }

                    if (newClients.size() > 0) {
                        ArrayList<Client> clientsReady = new ArrayList<>();
                        for (Client client : newClients) {
                            if (client.isReady()) {
                                clientsReady.add(client);
                            }
                        }
                        if (clientsReady.size() > 0) {
                            ArrayList<Client> clientsToRemove = new ArrayList<>();
                            dOut.write(NetworkCommands.client_connect.ordinal());
                            dOut.writeInt(clientsReady.size());
                            for (Client client : clientsReady) {
                                putClientInStream(dOut, client, true);
                                clientsToRemove.add(client);
                            }

                            for (Client client : clientsToRemove) {
                                newClients.remove(client);
                            }
                        }
                    }

                    ArrayList<Client> clientsDisconnected = c.getClientsDisconnected();
                    if (clientsDisconnected.size() > 0) {
                        dOut.write(NetworkCommands.client_disconnect.ordinal());
                        dOut.writeInt(clientsDisconnected.size());
                        for (Client client : clientsDisconnected) {
                            dOut.writeInt(client.getMyId());
                        }

                        clientsDisconnected.clear();
                    }

                    dOut.write(NetworkCommands.end_of_packet.ordinal());
                    dOut.flush();
                    c.increaseSentPackages();
                }
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Server getServer() {
        return server;
    }

    public ArrayList<Streamable> getObjectsToTrack() {
        return objectsToTrack;
    }

    public void setObjectsToTrack(ArrayList<Streamable> objectsToTrack) {
        this.objectsToTrack = objectsToTrack;
    }

    public void addObjectToTrack(Streamable object) {
        this.objectsToTrack.add(object);
    }
}
