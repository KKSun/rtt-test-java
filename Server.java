import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;



/**
 * This class implements java socket client
 * @author pankaj
 *
 */
public class Server {

    public static void main(String[] args) throws UnknownHostException, IOException, ClassNotFoundException, InterruptedException{
        DatagramSocket ds = new DatagramSocket(3334);
        byte[] recieve = new byte[65535];
        DatagramPacket DpRecieve = null;
        byte buf[] = null;
        //ds.setSoTimeout(1000);
        InetAddress ip = InetAddress.getByName("172.31.41.54");

        while(true){
            DpRecieve = new DatagramPacket(recieve, recieve.length);
            ds.receive(DpRecieve);
            // System.out.println(new String(recieve));
            String startTime = new String(recieve);
            long stLong = Long.parseLong(startTime.trim());
            System.out.println(stLong);
            long middleTime = System.nanoTime();
            String mtString = Long.toString(middleTime);
            long half_rtt = middleTime - stLong;
            buf = mtString.getBytes();
            DatagramPacket Dpsend = new DatagramPacket(buf, buf.length, ip, 2223);
            ds.send(Dpsend);
            System.out.println("respond sent");
            recieve = new byte[65535];
        }
    }
}
