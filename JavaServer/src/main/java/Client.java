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

            ByteBuffer buffer;
            try {
                final int bufferSize = 1024;
                buffer = ByteBuffer.allocate(bufferSize);
                channel.read(buffer);   // fill buffer from the input stream

                buffer.order(ByteOrder.LITTLE_ENDIAN);

                buffer.position(0);
                final byte mid = buffer.get();
                System.out.println("mid = " + mid);

                DataOutputStream dOut;
                int time;
                switch (mid) {
                    case 1: // TODO: just testing change to enum
                        time = buffer.getInt();
                        System.out.println("time = " + time);

                        dOut = new DataOutputStream(channel.socket().getOutputStream());
                        dOut.write(1); // TODO: just testing, change to enum
                        dOut.writeInt(time);
                        System.out.println("size = " + dOut.size());
                        dOut.flush();
                        break;
                    case 2: // TODO: just testing change to enum
                        int stringLength = buffer.getInt();
                        System.out.println("stringLength = " + stringLength);
                        String username = getStringFromBuffer(buffer, stringLength);

                        System.out.println("username = " + username);

                        dOut = new DataOutputStream(channel.socket().getOutputStream());
                        dOut.write(2); // TODO: just testing, change to enum

                        putStringInStream(dOut, "gotcha");

                        System.out.println("size = " + dOut.size());
                        dOut.flush();
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

    public void putStringInStream(DataOutputStream dOut, String s) throws IOException {
        char[] chars = s.toCharArray();
        dOut.writeInt(chars.length);
        System.out.println("string length = " + chars.length);
        for(char c : chars) {
            System.out.println("c = " + c);
            int i = c;
            System.out.println("i = " + i);

            dOut.write(i);
        }
    }

    public String getStringFromBuffer(ByteBuffer buffer, int stringLength) {
        StringBuilder s = new StringBuilder("");
        for (int i = 0; i < stringLength; i++) {
            int ii = buffer.get();
            char c = (char) ii;
            s.append(c);
        }
        return s.toString();
    }
}