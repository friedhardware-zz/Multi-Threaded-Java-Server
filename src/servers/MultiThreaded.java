package servers;
//josh schumacher

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MultiThreaded implements Runnable{

    protected int          serverPort   = 8080;
    protected ServerSocket serverSocket = null;
    protected boolean      isStopped    = false;
    protected Thread       runningThread= null;

    public MultiThreaded(int port){
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
            new Thread(
                    new WorkerRunnable(
                    clientSocket, "Multithreaded Server")
                    ).start();
            
        }
    }

  

    private void openServerSocket() {

        try {
            this.serverSocket = new ServerSocket(this.serverPort);
        } catch (IOException e) {
            throw new RuntimeException("Cannot open port 8080", e);
        }
    }
}
