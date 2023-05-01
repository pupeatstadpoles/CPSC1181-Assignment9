import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class HotelServer {


    public static void main(String[] args) throws IOException {
        Hotel hotel = new Hotel();
        ServerSocket server = new ServerSocket(8989);
        System.out.println("Waiting for client connection...");


        try {
            while (true) {
                Socket s = server.accept();
                System.out.println("\n\nClient connected.");
                HService service = new HService(s, hotel);

                Thread t = new Thread(service);
                t.start();
            }
        }
        finally {
            server.close();
        }
    }
}
