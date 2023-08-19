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
            Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/chat","postgres","123");
            Statement statement = connection.createStatement();
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);

            out.println("connected");

            System.out.println("User is running!");

            System.out.println(in.readLine());


            authLoop: while (true) {
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
                    }
                    case "log" -> {
                        while (!line.equals("|back")) {
                            String[] authData = line.split("\\|");
                            sql = String.format("""
                                    SELECT * from users
                                    WHERE name = '%s' and password = '%s'
                                    """,authData[0],authData[1]);
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

                connection.close();
                statement.close();
            }

            while(true)
            {
                String message = in.readLine();
                System.out.println(message);
                for(var i : Server.users)
                    i.out.println(message);
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
}