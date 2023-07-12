package Chat.Server;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;

public class User extends Thread {
    Socket socket;
    public PrintWriter out;

    User(Socket _socket) {
        socket = _socket;
    }

    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);

            System.out.println("User started!");

            out.println("Hello from server!");
            System.out.println(in.readLine());

            //getting db
            Map<String, String> users = new HashMap<>();
            String line;
            try (BufferedReader dbReader = new BufferedReader(new FileReader("users.txt"))) {


                System.out.println("dbReader and map created");

                line = dbReader.readLine();
                while (line != null) {
                    String[] buf = line.split("\\|");
                    users.put(buf[0], buf[1]);

                    System.out.println("new user added: " + buf[0] + " " + buf[1]);
                    line = dbReader.readLine();
                }
            } catch (Exception ex) {
                System.out.println("loading db:  " + ex);
            }

            System.out.println("Got db with size: " + users.size());
           //end of getting db

            authLoop: while (true) {
                String option = in.readLine();
                System.out.println("option: " + option);
                switch (option) {
                    case "reg":
                        while (true) {
                            line = in.readLine();
                            if (line.equals("|back"))
                                break;

                            if (!users.containsKey(line)) {
                                out.println("yes");
                                String password = in.readLine();

                                try (PrintWriter pw = new PrintWriter(new FileWriter("users.txt",true))) {
                                    pw.println(line + "|" + password);
                                } catch (Exception ex) {
                                    System.out.println(ex);
                                }

                                break authLoop;
                            }

                            else
                                out.println("no");
                        }

                        break;

                    case "log":

                        while (true){

                            line = in.readLine();

                            if(line.equals("|back"))
                                break;

                            if (users.containsKey(line)) {
                                out.println("continue");
                                String password = in.readLine();

                                if(users.get(line).equals(password)) {
                                    out.println("yes");
                                    break authLoop;
                                }
                                else
                                    out.println("Icncorrect password");

                            } else
                                out.println("Unknown username");
                        }

                        break;

                    default:
                        break authLoop;
                }
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
}