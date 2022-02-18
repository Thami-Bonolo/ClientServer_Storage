
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import static java.lang.System.in;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author bonolo
 */
public class Client {

    /**
     * @param args the command line arguments
     */
    // initialize socket and input output streams 
    private final Socket socket            = null; 
    private final BufferedReader  input   = null; 
    private final DataOutputStream out     = null; 
    private final BufferedReader reader =null;
    private DataInputStream serverIn;
    private InputStreamReader streamReader;
    public static void main(String[] args) throws IOException {

           String hostname = "Unknown";
        try
        {
            DataInputStream dis; 
            DataOutputStream dos;
               try (Scanner scn = new Scanner(System.in)) {
                   hostname = InetAddress.getLocalHost().getHostName();
                   Socket s = new Socket(hostname, 5555);
                   dis = new DataInputStream(s.getInputStream());
                   dos = new DataOutputStream(s.getOutputStream());
                   String []receivedArg=null;
                   while (true)
                   {
                       String received = dis.readUTF();
                       
                       receivedArg= received.split("@&#");
                       System.out.println(receivedArg[0]);
                       //takes in user input
                       String tosend = scn.nextLine();
                       System.out.println("57");

                       //checks if file needs to be download if the user input is a path based on @&#getLocalFile()

                       dos.writeUTF(tosend);
                       System.out.println("62");

                       if((receivedArg!=null)&&(receivedArg.length>1)){
                       System.out.println("66");
                       //System.out.println(receivedArg[1]);
                        if (((receivedArg[1].compareToIgnoreCase("getLocalFile()"))==0)){
                           
                       System.out.println("69");

                            int count;

                            try{
                              System.out.println("valid");
                              //dos.writeUTF("valid");
                            InputStream inf = new FileInputStream(tosend);
                             byte[] buffer = new byte[8192]; // or 4096, or more
                            System.out.println("uploading...");
                             while ((count = inf.read(buffer)) > 0)
                             {
                               dos.write(buffer, 0, count);
                             }inf.close();
                            System.out.println("Upload Successful!\n\nThank you for storing with Storage.io!\n\n");
                            }catch(FileNotFoundException ex){
                                System.out.println("File invalid");
                                //dos.writeUTF("INVALID");
                                break;
                            }break;

                        }else if((receivedArg[1].compareToIgnoreCase("saveLocalFile()"))==0){
                            System.out.println("91");

                            String fileOut ="/mnt/c/Users/Nkateko/Desktop/"+tosend; //change pathname
                            int count;
                            File newfile = new File(fileOut); 
                            newfile.createNewFile();
                            FileOutputStream fos = new FileOutputStream(fileOut);
                             byte[] buffer = new byte[8192]; // or 4096, or more
                            while ((count = in.read(buffer)) > 0)
                            {
                              fos.write(buffer, 0, count);
                             System.out.println("in loop");
                            }
                            System.out.println("Out of loop");

                        }
                       }
                      
                       
                       // If client sends exit,close this connection
                       // and then break from the while loop
                       if(tosend.equals("Exit"))
                       {
                           System.out.println("Closing this connection : " + s);
                           s.close();
                           System.out.println("Connection closed");
                           break;
                       }
                       
                       received = dis.readUTF();
                       
                       receivedArg= received.split("@&#");
                       System.out.println(receivedArg[0]);
                   }
                   
                   // closing resources
               } 
            dis.close(); 
            dos.close(); 
        }catch(Exception e){ 
            e.printStackTrace();
        } 
    }
    
    
}
