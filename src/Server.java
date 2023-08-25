import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class Server {
    static final int PORT = 6040;

    public static ArrayList<User> users = new ArrayList<User>();

    public static void main(String[] args) throws IOException {
        System.out.println(InetAddress.getLocalHost());
        System.out.println("Connecting...\n" + InetAddress.getLocalHost() + "   " + PORT);
        Thread connectionScanner = new Thread(new ConnectionScanner(PORT));
        connectionScanner.start();
    }
}
