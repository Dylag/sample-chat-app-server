import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class ConnectionScanner implements Runnable{
    private int port = 0;
    ConnectionScanner(int _port)
    {
        port = _port;
    }


    public void run()
    {
        while(true)
        {
            try(ServerSocket serverSocket = new ServerSocket(port))
            {
                try
                {
                    Socket socket = serverSocket.accept();
                    System.out.println("new user connected");
                    Server.users.add(new User(socket));
                    Server.users.get(Server.users.size()-1).start();
                }
                catch (Exception ex){
                    System.out.println("Connection scanner: "+ex);
                }
            }
            catch(Exception ex)
            {
                System.out.println(ex);
            }
        }
    }
}
