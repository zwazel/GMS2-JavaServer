import java.io.IOException;
import java.nio.ByteBuffer;
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
                // since your buffer in GameMaker is unsigned, let's prevent all that signed to unsigned nonsense by doing a
                // bitmask
                final int mid = buffer.get() & 0x000000FF;
                switch (mid) {
                    case 0:
                        long time = buffer.getLong();
                        wBuffer.clear();
                        wBuffer.position(0);
                        wBuffer.put((byte) 0);
                        wBuffer.putLong(time);
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