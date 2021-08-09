import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(6666);
        Socket client = serverSocket.accept();
        BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        PrintWriter out = new PrintWriter(client.getOutputStream(), true);
        out.println("hello gamemaker studio: "); //the clients receive this message
        while (true) {
            System.out.println("in while loop");//the server console prints this message
            String string = in.readLine();//keeps stuck on this
            //after client disconnect, all messages the client has sent are displayed in the console
            System.out.println("reading string:" + string);
            if (string == null) {
                break;
            }

            out.println("we have received this answer: " + string);
            System.out.println("stopped");
        }
    }
}