package servers;
//josh schumacher

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.Socket;

/**

 */
public class WorkerRunnable implements Runnable{

    protected Socket clientSocket = null;
    protected String serverText   = null;

    public WorkerRunnable(Socket clientSocket, String serverText) {
        this.clientSocket = clientSocket;
        this.serverText   = serverText;
    }

    public void run() {
    	
    	BufferedReader reader = null;
    	File file=null;
    	String fileName = null;
    	String temp = null;
    	InputStream  input  = null;
    	try {
			input=clientSocket.getInputStream();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	DataOutputStream outToClient = null;
    	try {
			outToClient=new DataOutputStream(clientSocket.getOutputStream());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
        final String directoryLinuxMac ="/Users/jschumacher/Documents/workspace/SimpleServer/www";
        
        reader = new BufferedReader(new InputStreamReader(input));
        int lineno=0;
        try {
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
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
        } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        try {
			outToClient.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        try {
			input.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        System.out.println("Request processed");
    }
}