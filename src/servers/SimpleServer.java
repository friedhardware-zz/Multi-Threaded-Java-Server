package servers;
//josh schumacher

public class SimpleServer {
    public static void main(String args[]){
    	
    	
    //Just comment out the lines to change what type of server enviro.
    	
    	
    //SingleThreadedServer server = new SingleThreadedServer(9000);
    //new Thread(server).start();
    
    MultiThreaded server = new MultiThreaded(9000);
    new Thread(server).start();

    try {
        Thread.sleep(10);
    } catch (InterruptedException e) {
        e.printStackTrace();  
    }
    }

}
