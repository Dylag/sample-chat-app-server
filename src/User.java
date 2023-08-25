import java.io.*;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;


public class User extends Thread {
    Socket socket;
    public PrintWriter out;
    User(Socket _socket) {
        socket = _socket;
    }

    public void run() {

        try {
            //initializing closeable stuff
            //i know that i should delete my url, user and password, but im too lazy to do it

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);

            out.println("connected");

            System.out.println("User is running!");

            System.out.println(in.readLine());

            systemLoop:while(true){
                authLoop: while (true) {

                    Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/chat","postgres","123");
                    Statement statement = connection.createStatement();

                    String option = in.readLine();
                    System.out.println("option: " + option);
                    String line;
                    String sql;
                    line = in.readLine();

                    switch (option) {

                        case "reg" -> {
                            while (!line.equals("|back")) {
                                String[] authData = line.split("\\|");

                                sql = String.format("""
                                        SELECT * FROM users
                                        WHERE name = '%s'""", authData[0]);

                                if (!statement.executeQuery(sql).next()) {
                                    sql = String.format("""
                                        INSERT INTO users(name,password)
                                        VALUES ('%s', '%s');
                                            """,authData[0], authData[1]);
                                    statement.executeUpdate(sql);
                                    out.println("yes");
                                    break authLoop;
                                } else
                                    out.println("no");

                                line = in.readLine();
                            }

                            System.out.println("quiting reg");
                        }

                        case "log" -> {
                            while (!line.equals("|back")) {
                                String[] authData = line.split("\\|");
                                sql = String.format("""
                                        SELECT * from users
                                        WHERE name = '%s' and password = '%s'
                                        """,authData[0],authData[1]);

                                System.out.println(sql);
                                if(statement.executeQuery(sql).next()){
                                    out.println("yes");
                                    break authLoop;
                                } else
                                    out.println("no");

                                line = in.readLine();
                            }
                        }

                        default -> {
                            break authLoop;
                        }
                    }

                    System.out.println("closing stuff");
                    //closing closeable stuff
                    connection.close();
                    statement.close();
                }
                String message = in.readLine();
                while(!message.equals("|back"))
                {
                    System.out.println(message);
                    for(var i : Server.users)
                        i.out.println(message);
                    message = in.readLine();
                }
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
}