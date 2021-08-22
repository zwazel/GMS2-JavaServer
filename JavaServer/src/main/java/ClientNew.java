import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientNew implements Runnable {
    private Socket s;
    private boolean connected = true;
    private ServerNew server;

    public ClientNew(ServerNew server, Socket s) {
        this.s = s;
        this.server = server;
    }

    @Override
    public void run() {
        System.out.println("run, connected = " + connected);
        while (connected) {
            System.out.println("test");
            DataInputStream dIn = null;
            try {
                dIn = new DataInputStream(s.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
                disconnect();
            }
            if (dIn != null) {
                try {
                    int cmd = dIn.read();

                    System.out.println("cmd = " + cmd);
                    switch (cmd) {
                        case 0:
                            int time = dIn.readInt();
                            System.out.println("time = " + time);

                            break;
                        default:
                            // ...
                            break;
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                    disconnect();
                }
            }
        }
    }

    public void disconnect() {
        System.out.println(s.getInetAddress().toString() + " has disconnected.");
        connected = false;
        server.removeClient(this);
        try {
            s.close();
        } catch (IOException ex1) {
            ex1.printStackTrace();
        }
    }
}
