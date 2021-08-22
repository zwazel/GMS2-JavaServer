import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ServerNew {
    private List<ClientNew> clients;
    private ServerSocket ss = null;
    private boolean running;

    public ServerNew(int port) {
        this.clients = new ArrayList<>();

        System.out.print("Trying to Listen on Port : " + port + "...");
        try {
            ss = new ServerSocket(port); // Create a new server socket

            System.out.println("Success!");

            // Get IP (Not needed, but used for the user to easily copy and send to others)
            System.out.println("Getting IP adress...");
            System.out.println("My IP adress: " + getPublicIp());

            running = true;

            // Accept incoming connections
            System.out.println("wating for conenctions...");
            while (running) {
                try {
                    Socket s = ss.accept(); // Wait for a request from a client

                    // Tell the client what ID he has
                    if (s != null) {
//                        DataOutputStream dOut = new DataOutputStream(s.getOutputStream());
//                        dOut.writeInt(idCounter); // increase id then write id
//                        dOut.flush(); // Send off the data

                        // get the username from the client that just connected
//                        DataInputStream dIn = new DataInputStream(s.getInputStream()); // Create new input stream
//                        String clientUsername = dIn.readUTF(); // Read text and save it

                        System.out.println("New Connection " + s.getInetAddress().toString());
                        ClientNew c = new ClientNew(this, s);
                        Thread t = new Thread(c);
                        t.start();
                        clients.add(c);
                    }
                } catch (IOException e) { // catch error
                    System.out.println("Can't accept socket connection! VERY BAD");
                }
            }
        } catch (IOException e) { // Catch error if we can't create a server socket
            // Failure
            System.err.println("Failed!");
            running = false;
            ss = null;
        }
    }

    public void removeClient(ClientNew client) {
        clients.remove(client);
    }

    private String getPublicIp() {
        String result = null;
        try {
            // Set the url from where we'll be getting the data from
            URL url = new URL("http://ipv4bot.whatismyipaddress.com/"); // Nice and fast website for getting your ipv4

            //Retrieving the contents of the specified page
            try {
                Scanner sc = new Scanner(url.openStream());

                //Instantiating the StringBuffer class to hold the result
                StringBuilder sb = new StringBuilder();
                while (sc.hasNext()) {
                    sb.append(sc.next());
                }
                //Retrieving the String from the String Buffer object
                result = sb.toString();
                result = result.replaceAll("<[^>]*>", ""); // Remove the html tags with some fancy black magic
                // Catch error if we can't create an open stream to the provided url
            } catch (IOException e) {
                System.out.println("Can't create openStream Scanner! VERY BAD");
            }
            // Catch error if we can't connect to the website
        } catch (MalformedURLException e) {
            System.out.println("Can't connect to URL! VERY BAD");
        }

        // return the ip, or null if we can't find an IP
        return result;
    }

    public static void main(String[] args) {
        new ServerNew(21337);
    }
}
