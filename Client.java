import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;

public class Client {
    private static final int TIMEOUT = 500; // Wait timeout (milliseconds)
    private static final int ECHOMAX = 100; // Maximum size of echo datagram
    static long[] startTime = new long[1000];
    static long[] rtt = new long[1000];
    static byte[] buf = new byte[100];

    public static void main(String[] args) throws IOException {

        // Create a selector to multiplex client connections.
        Selector selector = Selector.open();

        DatagramChannel channel = DatagramChannel.open();
        channel.configureBlocking(false);
        channel.socket().bind(new InetSocketAddress(3444));
        channel.register(selector, SelectionKey.OP_WRITE, new ClientRecord());
        System.out.println("start sending seq number");
        

        int i = 0;
        while(true) { 
            // Run forever, receiving and echoing datagrams
            // Wait for task or until timeout expires
            if (selector.select(TIMEOUT) == 0) {
                System.out.print(".");
                continue;
            }

            // Get iterator on set of keys with I/O to process
            Iterator<SelectionKey> keyIter = selector.selectedKeys().iterator();
            while (keyIter.hasNext()) {
                SelectionKey key = keyIter.next(); // Key is bit mask

                // Client socket channel has pending data?
                if (key.isReadable()){
                    handleRead(key, i);
                }

                // Client socket channel is available for writing and
                // key is valid (i.e., channel not closed).
                if (key.isValid() && key.isWritable())
                    handleWrite(key, i);

                keyIter.remove();
            }

            i++;
            if(i >= 1000) break;
        }
    }

    public static void handleRead(SelectionKey key, int i) throws IOException {
        DatagramChannel channel = (DatagramChannel) key.channel();
        ClientRecord clntRec = (ClientRecord) key.attachment();
        clntRec.buffer.clear();    // Prepare buffer for receiving
        clntRec.clientAddress = channel.receive(clntRec.buffer);
        Long endTime = System.nanoTime();
        int seqInt = Integer.parseInt(new String(clntRec.buffer.array()));
        rtt[seqInt] = endTime - startTime[seqInt];
        System.out.println("index: " + i + ", rtt; " + rtt[seqInt]);
        if (clntRec.clientAddress != null) {  // Did we receive something?
            // Register write with the selector
            key.interestOps(SelectionKey.OP_WRITE);
        }
    }

    public static void handleWrite(SelectionKey key, int i) throws IOException {
        DatagramChannel channel = (DatagramChannel) key.channel();
        ClientRecord clntRec = (ClientRecord) key.attachment();
        
        long startT = System.nanoTime();
        startTime[i] = startT;
        fillBuf(buf, i);
        ByteBuffer.wrap(buf);
        clntRec.buffer.flip(); // Prepare buffer for sending
        int bytesSent = channel.send(clntRec.buffer, clntRec.clientAddress);
        System.out.println("buffer sent. ");
        if (bytesSent != 0) { // Buffer completely written?
            // No longer interested in writes
            key.interestOps(SelectionKey.OP_READ);
        }
    }

    static class ClientRecord {
        public SocketAddress clientAddress = new InetSocketAddress("172.31.41.54", 2333);
        public ByteBuffer buffer = ByteBuffer.allocate(ECHOMAX);
    }

    static void fillBuf(byte[] buf, int x) {
        byte[] xByte = Integer.toString(x).getBytes();
        for (int i = 0; i < xByte.length; i++) {
            buf[i] = xByte[i];
        }
    }
}