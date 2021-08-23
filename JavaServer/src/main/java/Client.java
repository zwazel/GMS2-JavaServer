import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
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

            try {
                Thread.sleep(5);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }

            ByteBuffer buffer;
            ByteBuffer wBuffer;
            try {
                final int bufferSize = 1024;
                buffer = ByteBuffer.allocate(bufferSize);
                wBuffer = ByteBuffer.allocate(bufferSize);
                channel.read(buffer);   // fill buffer from the input stream

                buffer.order(ByteOrder.LITTLE_ENDIAN);

                buffer.position(0);
                final byte mid = buffer.get();
                System.out.println("mid = " + mid);

                int time;
                switch (mid) {
                    case 0:
                        time = buffer.getInt();
                        System.out.println("time = " + time);

                        wBuffer.position(0);
                        wBuffer.putInt(1);
                        System.out.println("buffer position = " + wBuffer.position());
                        wBuffer.putInt(time);
                        wBuffer.order(ByteOrder.LITTLE_ENDIAN);
                        channel.write(wBuffer);
                        break;
                    case 1:
                        time = buffer.getInt();
                        System.out.println("time = " + time);

                        DataOutputStream dOut = new DataOutputStream(channel.socket().getOutputStream());
                        dOut.write(2);
                        dOut.writeInt(time);
                        System.out.println("size = " + dOut.size());
                        dOut.flush();


                        DataInputStream dIn = new DataInputStream(channel.socket().getInputStream());
                        dIn.readInt();
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