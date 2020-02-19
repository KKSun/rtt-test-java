import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * This class implements java socket client
 * @author pankaj
 *
 */
public class Client {

    public static void main(String[] args) throws UnknownHostException, IOException, ClassNotFoundException, InterruptedException{
        //get the localhost IP address, if server is running on some other IP, you need to use that
        InetAddress host = InetAddress.getLocalHost();
        Socket socket = null;
        ObjectOutputStream oos = null;
        ObjectInputStream ois = null;
        while(true){
            //establish socket connection to server
            socket = new Socket(host.getHostName(), 5555);
            //write to socket using ObjectOutputStream
            oos = new ObjectOutputStream(socket.getOutputStream());
            // System.out.println("Sending request to Socket Server");
            long startTime = System.nanoTime();
            oos.writeObject("");
            //read the server response message
            ois = new ObjectInputStream(socket.getInputStream());
            long reachTime = (long) ois.readObject();
            long endTime = System.nanoTime();
            long rtt = endTime - startTime;
            // System.out.println("t1 = " + (reachTime - startTime));
            // System.out.println("t2 = " + (endTime - reachTime));
            // System.out.println("rtt = " + rtt);
            System.out.println((reachTime - startTime) + " " + (endTime - reachTime) + " " + rtt);
            //close resources
            ois.close();
            oos.close();
            Thread.sleep(100);
        }
    }
}
