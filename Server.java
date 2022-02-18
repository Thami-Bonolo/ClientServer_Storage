
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author bonolo
 */
public class Server {
    private static DataOutputStream out;
    private static DataInputStream in;
    private static ServerSocket serverSocket;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int port = 5555; //port number for connection
        String query="upload";
        try {
            //try to accept connection from a client
            serverSocket = new ServerSocket(port);
            while(true){ // to keep on trying to connect
                //Creates a connection between a server and a client
                
                Socket clientSocket = serverSocket.accept();
                in = new DataInputStream(clientSocket.getInputStream());
                out = new DataOutputStream(clientSocket.getOutputStream());
                //outputStream.write("Connected to server\n".getBytes());
                ServerThread thread = new ServerThread(clientSocket, in,out);
                thread.start();
            }
        } catch (IOException ex) {
            //exception handling
            ex.printStackTrace();
        }           try {
            serverSocket.close();
        } catch (IOException ex) {
                        ex.printStackTrace();

        }

    }
    
}
