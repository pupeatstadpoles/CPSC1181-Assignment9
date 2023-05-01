import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class HotelClient {
    private static String user = null;

    public static void main(String[] args) {
        Socket socket = null;
        DataOutputStream toServer = null;
        DataInputStream fromServer = null;
        String serverHost = "localhost";


        try {
            socket = new Socket(serverHost, 8989);
            fromServer = new DataInputStream(socket.getInputStream());
            toServer = new DataOutputStream(socket.getOutputStream());
            menu(fromServer, toServer, socket);
        } catch (UnknownHostException e) {
            System.out.println("the IP address of the host could not be found...cannot go on, bye");
        } catch (IOException e) {
            System.err.println("Cannot connect to the IP");
            System.err.println(" \"" + serverHost + "\"");
        } catch (SecurityException e) {
            System.err.println("a security manager exists");
            System.err.println("its checkConnect doesn't allow the connection.... bye");
        }
    }

    private static void menu(DataInputStream fromServer, DataOutputStream toServer, Socket socket) {
        Scanner scan = new Scanner(System.in);
        System.out.println("Hi, please type in your name: ");
        user = scan.nextLine();
        try {
            toServer.writeUTF("USER");
            toServer.writeUTF(user);
            toServer.flush();
            System.out.println(fromServer.readUTF());
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        while (user != null) {
            try {
                toServer.flush();
                System.out.println();
                System.out.println("Please choose from the following options:");
                System.out.println("RESERVE  |   CANCEL   |    AVAILABLE   |  QUIT");
                System.out.println();

                //next command is retrieved from user
                String command = scan.next();
                command = command.toUpperCase();

                //based on the command sent, do these...
                switch (command) {
                    case ("RESERVE"):
                        //send it to the server
                        toServer.writeUTF(command);
                        toServer.flush();
                        System.out.println(fromServer.readUTF());
                        int checkIn = scan.nextInt();
                        toServer.writeInt(checkIn);
                        toServer.flush();


                        //get server response then give checkout date
                        System.out.println(fromServer.readUTF());
                        int checkOut = scan.nextInt();
                        toServer.writeInt(checkOut);
                        toServer.flush();

                        //service will try to book reservation using those details here, just need to print the results
                        System.out.println(fromServer.readUTF());
                        toServer.flush();
                        break;
                    case ("CANCEL"):
                        //send it to the server
                        toServer.writeUTF(command);
                        toServer.flush();

                        //server will try to check using the username, print the results here
                        String cancellationResults = fromServer.readUTF();
                        System.out.println(cancellationResults);
                        toServer.flush();
                        break;
                    case ("AVAILABLE"):
                        //modify first -- server expects AVAIL instead of AVAILABLE
                        command = "AVAIL";
                        toServer.writeUTF(command);
                        toServer.flush();

                        //server will send us the hotel.toString() so just print that out
                        String hotelAvailability = fromServer.readUTF();
                        System.out.println(hotelAvailability);
                        break;
                    case ("QUIT"):
                        //send it to the server
                        toServer.writeUTF(command);
                        toServer.flush();
                        //server will then close connection here
                        System.out.println("Connection closed. Bye!");
                        socket.close();
                        return;
                    case (""):
                        break;
                    default:
                        //send it to the server
                        toServer.writeUTF(command);
                        toServer.flush();
                        System.out.println(fromServer.readUTF());
                        socket.close();
                        return;
                    //something
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
