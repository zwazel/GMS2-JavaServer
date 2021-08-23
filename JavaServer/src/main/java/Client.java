import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.SocketChannel;

public class Client implements Runnable {

    private SocketChannel channel;
    private boolean connected = true;
    private Server server;

    public Client(Server server, SocketChannel channel) {
        this.server = server;
        this.channel = channel;
    }

    @Override
    public void run() {
        System.out.println("run, connected = " + connected);
        while (connected) {
            System.out.println("test");

            ByteBuffer buffer;
            try {
                final int bufferSize = 1024;
                buffer = ByteBuffer.allocate(bufferSize);
                channel.read(buffer);   // fill buffer from the input stream

                buffer.order(ByteOrder.LITTLE_ENDIAN);

                buffer.position(0);
                final byte mid = buffer.get();
                System.out.println("mid = " + mid);

                int time;
                switch (mid) {
                    case 1: // TODO: just testing change to enum
                        time = buffer.getInt();
                        System.out.println("time = " + time);

                        DataOutputStream dOut = new DataOutputStream(channel.socket().getOutputStream());
                        dOut.write(2); // TODO: just testing, change to enum
                        dOut.writeInt(time);
                        System.out.println("size = " + dOut.size());
                        dOut.flush();
                        break;
                    case 2: // TODO: just testing change to enum
                        BufferedReader in = new BufferedReader(new InputStreamReader(channel.socket().getInputStream()));
                        String username = in.readLine();
                        System.out.println("username = " + username);
                        break;
                    default:
                        // ...
                        break;
                }
            } catch (IOException ex) {
                ex.printStackTrace();
                System.out.println(channel.socket().getInetAddress().toString() + " has disconnected.");
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
}