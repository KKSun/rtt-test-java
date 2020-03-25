import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class ClientMT {

    static Long[] startTimeMap = new Long[1000];
    static Long[] rttMap = new Long[1000];
    static byte buf[] = new byte[100];
    static byte[] recieve = new byte[100];

    public static void main(String[] args) throws Exception {
        DatagramSocket recvsocket = new DatagramSocket(3444);
        DatagramSocket sendsocket = new DatagramSocket(3445);
        SocketHandler(recvsocket, sendsocket);
    }

    static void fillBuf(byte[] buf, int x) {
        byte[] xByte = Integer.toString(x).getBytes();
        for (int i = 0; i < xByte.length; i++) {
            buf[i] = xByte[i];
        }
    }

    static void SocketHandler(DatagramSocket recvsocket, DatagramSocket sendsocket) throws InterruptedException {
        SenderThread senderThread = new SenderThread(sendsocket);
        ReceiverThread receiverThread = new ReceiverThread(recvsocket);

        Thread sender = new Thread(senderThread);
        Thread reciever = new Thread(receiverThread);

        sender.start();
        reciever.start();

        sender.join();
        reciever.join();
    }

    static class SenderThread implements Runnable {
        DatagramSocket socket;

        public SenderThread(DatagramSocket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            for (int i = 0; i < 1000; i++) {
                fillBuf(buf, i);
                long startTime = System.nanoTime();
                startTimeMap[i] = startTime;
                InetAddress ip;
                try {
                    ip = InetAddress.getByName("172.31.41.54");
                    DatagramPacket DpSend = new DatagramPacket(buf, buf.length, ip, 2333);
                    socket.send(DpSend);
                    //System.out.println("message sent");
                    Thread.sleep(300);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static class ReceiverThread implements Runnable{
        DatagramSocket socket;

        public ReceiverThread(DatagramSocket socket){
            this.socket = socket;
        }

        @Override
        public void run(){
            while(true){
                DatagramPacket DpRecieve = new DatagramPacket(recieve, recieve.length);
                try {
                    socket.receive(DpRecieve);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String seq = new String(recieve);
                int seqInt = Integer.parseInt(seq.trim());
                long endTime = System.nanoTime();
                long rtt = endTime - startTimeMap[seqInt];
                rttMap[seqInt] = rtt;
                System.out.println(seqInt + "     " + rtt);
            }
        }
    }
}
