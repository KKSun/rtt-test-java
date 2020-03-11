import java.io.IOException; 
import java.net.DatagramPacket; 
import java.net.DatagramSocket; 
import java.net.InetAddress; 

/**
 * This class implements java socket client
 * @author pankaj
 *
 */
public class Client {

    public static void main(String[] args) throws IOException, InterruptedException{
        DatagramSocket ds = new DatagramSocket(3444); 
        InetAddress ip = InetAddress.getByName("127.0.0.1");
  
        byte buf[] = new byte[65535]; 
        byte[] recieves = new byte[65535];
        DatagramPacket DpRecieve = null;
        //ds.setSoTimeout(1000);
  
        // loop while user not enters "bye" 
        Integer i = 0;
        while (true) 
        {
            long startTime = System.nanoTime();
            String stString = Long.toString(startTime);
            // convert the String input into the byte array. 
            buf = i.toString(i).getBytes(); 
  
            // Step 2 : Create the datagramPacket for sending 
            // the data. 
            DatagramPacket DpSend = 
                  new DatagramPacket(buf, buf.length, ip, 2333); 
  
            // Step 3 : invoke the send call to actually send 
            // the data. 
            ds.send(DpSend);
            DpRecieve = new DatagramPacket(recieves, recieves.length);
            ds.receive(DpRecieve);
            long endTime = System.nanoTime();
            long rtt = endTime - startTime;
            System.out.println(rtt);
            Thread.sleep(500);
            i++;
        }
    }
}
