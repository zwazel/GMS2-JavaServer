import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.SocketChannel;

public class Client implements Runnable {

    private SocketChannel channel;
    private boolean connected = true;
    private Server server;

    ByteBuffer wBuffer = ByteBuffer.allocate(1024);

    public Client(Server server, SocketChannel channel) {
        this.server = server;
        this.channel = channel;
    }

    @Override
    public void run() {
        System.out.println("run, connected = " + connected);
        while (connected) {
            System.out.println("test");

            try {
                Thread.sleep(5);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }

            int bytesRead = 0;
            ByteBuffer buffer;
            try {
                buffer = ByteBuffer.allocate(1024);
                channel.read(buffer);   // fill buffer from the input stream

                buffer.order(ByteOrder.LITTLE_ENDIAN);

                buffer.position(0);
                final int mid = buffer.get();
                System.out.println("mid = " + mid);
                switch (mid) {
                    case 0:
                        int time = buffer.getInt();
                        System.out.println("time = " + time);

                        wBuffer.clear();
                        wBuffer.position(0);
                        wBuffer.put((byte) 0);
                        wBuffer.putInt(time);
                        channel.write(wBuffer);
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