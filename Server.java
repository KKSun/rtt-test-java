import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Server {

    public static void main(String[] args) throws IOException {
        DatagramSocket socket = new DatagramSocket(2333);
        byte[] recieve = new byte[100];
        DatagramPacket DpRecieve = null;
        byte buf[] = new byte[100];
        DatagramPacket Dpsend = null;
        InetAddress ip = InetAddress.getByName("10.9.9.2");
        System.out.println("server start...");
        while(true){
            DpRecieve = new DatagramPacket(recieve, recieve.length);
            socket.receive(DpRecieve);
            String seq = new String(recieve);
            fillBuf(buf, seq);
            Dpsend = new DatagramPacket(buf, buf.length, ip, 3444);
            socket.send(Dpsend);
            System.out.println("respond sent, seq: " + seq + " | buf length: " + buf.length + ".");
            recieve = new byte[100];
            buf = new byte[100];            
        }
    }

    static void fillBuf(byte[] buf, String seq){
        for(int i = 0; i < 100; i++){
            buf[i] = seq.getBytes()[i];
        }
    }
}
