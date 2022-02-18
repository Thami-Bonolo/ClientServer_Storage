
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
/**
 *
 * @author bonolo
 */
//For multi-threading
public class ServerThread extends Thread{

    private final Socket clientSocket;
    //private ClientRequest clientRequest;
    private final  DataOutputStream out;
    private final DataInputStream in;
    
    public ServerThread(Socket clientSocket, DataInputStream in, DataOutputStream out) throws IOException{
        this.clientSocket = clientSocket;
        this.out = out;
        this.in = in;
    }
    
    @Override
    public void run(){
        try {
            handleClientSocket();
        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
        }
    }
    
    private void handleClientSocket() throws IOException, InterruptedException{
        //client - server interactions
       
        //OutputStream outputStream = clientSocket.getOutputStream();
        out.writeUTF("Welcome to Storage.io\nWould you like to upload or download? \n");
        //clientRequest = new ClientRequest(clientSocket, query);
        inputStream = clientSocket.getInputStream();
        request = in.readUTF();
        Request();
        clientSocket.close();
    }
    
    
    
    

    private OutputStream os; //for file sharing
    private FileInputStream fileStream;
    byte[] filebytes = new byte[4000]; //Random file size /come back to remove this
    //every time you share a file create a new byte[]. so it cannot be global
    private InputStream inputStream;
    private BufferedReader reader;
    private String request;
    private String username;
    private String password;
    String userDir, userInput;
    private File file;
    private boolean fileExist;
    Scanner input = new Scanner(System.in);
    //DataInputStream in;
    
   
   
    public void Request() throws IOException{
        while(!request.isEmpty()){
                if(request.equalsIgnoreCase("upload")){
                System.out.println(request);

                out.writeUTF("Your query was \""+request+"\". \n");
                upload();
            }
            else if(request.equalsIgnoreCase("download")){
                System.out.println("in request if2" );

                out.writeUTF(("Your query was \""+request+"\". \n"));
                download();

            }
            else if(request.equalsIgnoreCase("list")){
                            System.out.println("in request if3" );

                out.writeUTF(("Your query was \""+request+"\". \n"));
                list("./");
            }
            else{
                            System.out.println("in request if4" );

                out.writeUTF(("Your query \""+request+"\" was not found.\n"));
                out.writeUTF("Please re-enter your query.\n");
                request = in.readUTF();
                Request();

            }
        }
        
    }
    //For uploading  a file
    public void upload() throws IOException{
        String fileDir, database;
        int privacy = 1;
        boolean loop = true;
        
        
        while(loop){ //To make sure the user enters a valid option
            out.writeUTF("File privacy options, choose any option below:\n(1)Public\n(2)Friends\n(3)Only me\n");
            
            String str = in.readUTF();
            privacy = Integer.parseInt(str);
            

            if(privacy >=1 && privacy <=3){
                
                loop = false;
            }
            else{
                 
                out.writeUTF("You have chosen an invalid option. \n");
            }
        }
        if(privacy == 1){ //everyone can upload and download from this.
            boolean loop2 =true;
           while(loop2){
            out.writeUTF("Enter the path of the file you would like to upload:\n");
            out.writeUTF("hint: '/Docs/Foo.txt'@&#getLocalFile()" );
            
            
            String filePath = in.readUTF();
            
            //String validityCheck =in.readUTF();
           
            

               String[] pathArray = filePath.split("/");
               int lenArr = pathArray.length;
               String fileName =pathArray[lenArr-1];
               int count;
               byte[] buffer = new byte[8192]; // or 4096, or more
               String fileOut ="/mnt/c/Users/Nkateko/Desktop/Database/public/" +fileName; //change pathname
               out.writeUTF(" @&#" +fileOut);
               File newfile = new File(fileOut); 
               newfile.createNewFile();
               FileOutputStream fos = new FileOutputStream(fileOut);
               //out.writeUTF("file writing");
               while ((count = in.read(buffer)) > 0)
               {
                 fos.write(buffer, 0, count);
                System.out.println("in loop");
               }
                System.out.println("Out of loop");
               fos.close();
               loop2=false;
               

               
              // out.writeUTF("Thank you for storing with Storage.io!" );
            
            
            }
            

        }
        
        if(privacy == 2){ //only user's friends can download this files.
            String friends = "";
            String[] listOfFriends = new String[25]; //for this case a user cannot have more than 25 friends
            int count = 0;
            while(true){
                out.writeUTF("Enter name of friend and press return(type \"done\" when finished)\n");
                friends = in.readUTF();
                if(!friends.equalsIgnoreCase("done")){
                    listOfFriends[count] = friends;
                }
                else{
                    out.writeUTF("Names of friends captured\n");
                    break;
                }
            }
            //from here save the file to the database as [list, of, friends], nameOfFile.extension
        }
        
        if(privacy == 3){ //Only the user can download this file or see it from list
            out.writeUTF("Enter your name: ");
            username = in.readUTF();
            out.writeUTF("Enter your password: ");
            password = in.readUTF();
            
            //from here save the file to the database as [nameOfUser+password], nameOfFile.extension
        }
    }
    
    public void download() throws IOException{
        out.writeUTF("Enter your name: \n");
        username = in.readUTF();
        out.writeUTF("Enter your password:  \n");
        out.writeUTF("*please note that passwords are  case sensitive*");
        password = in.readUTF();
       // out.writeUTF(in.readUTF());
        //getting a list of available files for user.
        //using the list method
        list(username);
        
        //Asking user to choose from list:
        String fileToLoad;
        out.writeUTF("Enter the name of the file(as it appears on the list) you wish to download:@&#saveLocalFile()");
        fileToLoad = in.readUTF();
        //out.writeUTF("downloading file");
        out.writeUTF(fileToLoad);
        //download file from server to client.
        InputStream inf = new FileInputStream("/mnt/c/Users/Nkateko/Desktop/Database/"+username+"/"+fileToLoad);
        int count;
        byte[] buffer = new byte[8192]; // or 4096, or more
         while ((count = inf.read(buffer)) > 0)
         {
           out.write(buffer, 0, count);
         }inf.close();
        
        
    }
    
    public void list(String usrDir) throws IOException{
         File f = new File("/mnt/c/Users/Nkateko/Desktop/Database/"+usrDir);

        // Populates the array with names of files and directories
        String[] pathnames = f.list();
        String output = "";
        // For each pathname in the pathnames array
        for (String pathname : pathnames) {
            // Print the names of files and directories
            output+=pathname+"\n";
        }
        out.writeUTF(output);
    }

    
}
