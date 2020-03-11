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

    public static void main(final String[] args) throws IOException, InterruptedException{
        DatagramSocket socket = new DatagramSocket(3444); 
        InetAddress ip = InetAddress.getByName("127.0.0.1");
        Long[] timeMap = new Long[1000];
        byte buf[] = new byte[100]; 
        byte[] recieve = new byte[100];
        DatagramPacket DpRecieve = null;
        
        for(int i = 0; i < 1000; i++){
            fillBuf(buf, i);
            long startTime = System.nanoTime();
            timeMap[i] = startTime;
            DatagramPacket DpSend = new DatagramPacket(buf, buf.length, ip, 2333); 
            socket.send(DpSend);
            DpRecieve = new DatagramPacket(recieve, recieve.length);
            socket.receive(DpRecieve);
            String seq = new String(recieve);
            int seqInt = Integer.parseInt(seq.trim());
            long endTime = System.nanoTime();
            long rtt = endTime - timeMap[seqInt];
            System.out.println(rtt + " = " + endTime + " - " + startTime);
            
            Thread.sleep(500);
        }
    }

    static void fillBuf(byte[] buf, int x){
        byte[] xByte = Integer.toString(x).getBytes();
        for(int i = 0; i < xByte.length; i++){
            buf[i] = xByte[i];
        }
    }
}
