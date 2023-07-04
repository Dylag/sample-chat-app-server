package Chat.Server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;


public class User extends Thread {
    Socket socket;
    public PrintWriter out;
    User(Socket _socket)
    {
        socket = _socket;
    }
    public void run()
    {
        try
        {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())),true);

            System.out.println("User started!");
            String testCheck = in.readLine();
            System.out.println(testCheck);

            out.println("Hello from server!");
            while(true)
            {                                
                String message = in.readLine();

                for(int i = 0 ;i < Server.users.size();i++)
                    Server.users.get(i).out.println(message);
                System.out.println(message);
            }

            
        }
        catch(Exception ex)
        {
            System.out.println(ex);
        }
    }
}