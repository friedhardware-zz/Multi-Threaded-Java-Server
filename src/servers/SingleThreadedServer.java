package servers;

//josh schumacher
import java.net.ServerSocket;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class SingleThreadedServer implements Runnable{

    protected int          serverPort   = 8080;
    protected ServerSocket serverSocket = null;
    protected boolean      isStopped    = false;
    protected Thread       runningThread= null;

    public SingleThreadedServer(int port){
        this.serverPort = port;
    }

    public void run(){
        synchronized(this){
            this.runningThread = Thread.currentThread();
        }
        openServerSocket();
        
        while(true){
            Socket clientSocket = null;
            try {
                clientSocket = this.serverSocket.accept();
            } catch (IOException e) {
            	System.out.println("Server Stopped.") ;  
                throw new RuntimeException(
                    "Error accepting client connection", e);
            }
            try {
                processClientRequest(clientSocket);
            } catch (IOException e) {
                //log exception and go on to next request.
            }
        }
    }

    private void processClientRequest(Socket clientSocket)
    throws IOException {
    	BufferedReader reader = null;
    	File file=null;
    	String fileName = null;
    	String temp = null;
        InputStream  input  = clientSocket.getInputStream();
        DataOutputStream outToClient = new DataOutputStream(clientSocket.getOutputStream());
        final String directoryLinuxMac ="/Users/jschumacher/Documents/workspace/SimpleServer/www";
        
        
        reader = new BufferedReader(new InputStreamReader(input));
        int lineno=0;
        for (String line; (line = reader.readLine()) != null;) {
            if (line.isEmpty()) break; // Stop when headers are completed. We're not interested in all the HTML.
            if(lineno==0){
            	temp=line.substring(4, line.indexOf("HTTP")-1);
            	if(temp.equals("/")){
            		file = new File(directoryLinuxMac+"/index.html");
            		fileName="index.html";
            	}else{
            		file = new File(directoryLinuxMac+temp);
            		fileName= temp.substring(1);
            		if(!file.exists()) {
            			file = new File(directoryLinuxMac+"/Error.html");
            			fileName="Error.html";
            			}
            		}
            }
            System.out.println(line);
            lineno++;
        }
        System.out.println("HTTP Response");
        int numOfBytes=2000;
        FileInputStream inFile= null;
        try{
        	numOfBytes= (int) file.length();
        	inFile = new FileInputStream(file);
        	byte[] fileInBytes = new byte[numOfBytes];
            inFile.read(fileInBytes);
            outToClient.writeBytes("HTTP/1.1 200 Document Follows\r\n");
            System.out.println("HTTP/1.1 200 Document Follows");
            if (fileName.endsWith(".jpg")){
            	outToClient.writeBytes("Content-Type: image/jpeg\r\n");
            	System.out.println("Content-Type: image/jpeg");}
            if (fileName.endsWith(".gif")){
            	outToClient.writeBytes("Content-Type: image/gif\r\n");
            	System.out.println("Content-Type: image/gif");}
            if (fileName.endsWith(".html")){
            	outToClient.writeBytes("Content-Type: text/html\r\n");
            	System.out.println("Content-Type: text/html");}
            if (fileName.endsWith(".css")){
            	outToClient.writeBytes("Content-Type: text/css\r\n");
            	System.out.println("Content-Type: text/css");}
            if (fileName.endsWith(".js")){
            	outToClient.writeBytes("Content-Type: text/javascript\r\n");
            	System.out.println("Content-Type: text/javascript");}
            outToClient.writeBytes("Content-Length: " + numOfBytes + "\r\n");
            System.out.println("Content-Length: " + numOfBytes);
            outToClient.writeBytes("\r\n");
            outToClient.write(fileInBytes, 0, numOfBytes);
            inFile.close();
            
        }catch(NullPointerException err){
        }
        
        outToClient.close();
        input.close();
        System.out.println("Request processed");
    }



    private void openServerSocket() {

        try {
            this.serverSocket = new ServerSocket(this.serverPort);
        } catch (IOException e) {
            throw new RuntimeException("Cannot open port 8080", e);
        }
    }
}
