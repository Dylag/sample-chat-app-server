package Chat.Server;

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
    static int PORT = 0;

    public static ArrayList<User> users = new ArrayList<User>();
    



    public static void main(String[] args) throws IOException
    {
        System.out.println(InetAddress.getLocalHost());
        PORT = 6040;
        System.out.println("Connecting...");


        // try(ServerSocket serverSocket = new ServerSocket(PORT))
        // {
        //     try(Socket socket = serverSocket.accept())
        //     {
        //         BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        //         PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())),false);

        //         String message = in.readLine();
        //         System.out.println(message);
        //     }
        // }

        Thread connectionScanner = new Thread(new ConnectionScanner(PORT));
        connectionScanner.start();
    }
}

