import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;

public class HService implements Runnable {
    private Socket s;
    private DataInputStream in;
    private DataOutputStream out;
    private Hotel h;
    private String username;

    public HService(Socket socket, Hotel hotel) {
        s = socket;
        h = hotel;
    }

    @Override
    public void run() {
        try {
            try {
                in = new DataInputStream(s.getInputStream());
                out = new DataOutputStream(s.getOutputStream());
                doService();
            } finally {
                s.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void doService() throws IOException {

        if (in.readUTF().equalsIgnoreCase("user")) {
            System.out.println("Received command USER");
            username = in.readUTF();
            System.out.println("username is " + username);
            out.writeUTF("Hello " + username + "\n\n");
            out.flush();
        }
        //put the check here to see if command is not USER or wahtever
        while (true) {
            System.out.println("Waiting on command from client");
            String command = in.readUTF();
            executeCommand(command);
        }
    }

    private void executeCommand(String command) throws IOException {
        //based on command received , do these
        switch (command) {
            case ("RESERVE"):
                System.out.println("Received command RESERVE from" + username);
                //get the check-in date
                System.out.println("Getting the check-in day from client " + username);
                out.writeUTF("What date would you like to check in?");
                out.flush();
                int checkIn = in.readInt();

                //get the check-out date
                System.out.println("Getting the check-out day from client " + username);
                out.writeUTF("What date would you like to check out?");
                out.flush();
                int checkOut = in.readInt();

                //try to book
                System.out.println("Attempting to book RESERVATION for " + username + " from " + checkIn + " to " + checkOut);

                if (h.requestReservation(username, checkIn, checkOut)) {
                    System.out.println("RESERVATION success");
                    out.writeUTF("Reservation made: " + username + " from " + checkIn + " through " + checkOut);
                } else {
                    System.out.println("RESERVATION failed");
                    out.writeUTF("Reservation unsuccessful: " + username + " from " + checkIn + " through " + checkOut);
                }

                out.flush();
                break;
            case ("CANCEL"):
                System.out.println("Received command CANCEL from " + username);
                System.out.println("Attempting to CANCEL reservation for " + username);

                if (h.cancelReservation(username)) {
                    System.out.println("CANCEL success");
                    out.writeUTF("Reservations successfully cancelled for " + username);
                } else {
                    System.out.println("CANCEL failed");
                    out.writeUTF("Reservations not cancelled for " + username + ", no current reservation.");
                }

                out.flush();
                break;
            case ("AVAIL"):
                System.out.println("Received command AVAIL from " + username);
                System.out.println("Printing AVAILABILITY of hotel using hotel's toString()");
                out.writeUTF(h.toString());
                out.flush();
                break;
            case ("QUIT"):
                System.out.println("Received command QUIT from" + username);
                System.out.println("Closing connection for " + username);
                out.writeUTF("Closing connection.");
                out.flush();
                s.close();
                break;
            default:
                System.out.println("Closing connection for " + username);
                out.writeUTF("Invalid command: Closing connection.");
                out.flush();
                s.close();
        }
    }
}

