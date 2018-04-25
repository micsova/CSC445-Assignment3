package Assignment3;

import java.io.IOException;
import java.io.PrintStream;
import java.net.*;
import java.nio.ByteBuffer;
import java.text.DecimalFormat;

public class Receiver extends Thread {

    private MulticastSocket socket;
    private InetAddress group;
    private byte[] num = new byte[8];
    private byte[] length = new byte[4];
    private byte[] buyer;
    private byte[] time = new byte[4];
    private byte[] waitInt = new byte[4];
    private ClientGUI gui;
    private PrintStream log;
    private Thread receiverThread;
    private DecimalFormat df = new DecimalFormat("#0.00");

    public Receiver(ClientGUI g) {
        gui = g;
        log = System.out;
        receiverThread = new Thread(this);
        receiverThread.setDaemon(true);
        receiverThread.start();
    }

    public Receiver(ClientGUI g, PrintStream ps) {
        gui = g;
        log = ps;
        receiverThread = new Thread(this);
        receiverThread.setDaemon(true);
        receiverThread.start();
    }

    public void run() {
        System.setProperty("java.net.preferIPv4Stack", "true");
        try {
            socket = new MulticastSocket(22711);
            socket.setSoTimeout(100);
            group = InetAddress.getByName("234.123.12.12");
            socket.joinGroup(group);
            log.println("Joined group");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (;;) {
            try {
                DatagramPacket packet = new DatagramPacket(num, num.length);
                for (int i = 0; i < 1; i++) {
                    try {
                        socket.receive(packet);
                    } catch (SocketTimeoutException e) {
                        i--;
                    }
                }
                gui.maxBid.setText("$" + df.format(ByteBuffer.wrap(num).getDouble()));
                packet = new DatagramPacket(length, length.length);
                socket.receive(packet);
                buyer = new byte[ByteBuffer.wrap(length).getInt()];
                packet = new DatagramPacket(buyer, buyer.length);
                socket.receive(packet);
                gui.buyerAddress.setText(new String(buyer));
                packet = new DatagramPacket(time, time.length);
                socket.receive(packet);
                gui.timeLeft.setText(ByteBuffer.wrap(time).getInt() + " seconds");
                packet = new DatagramPacket(waitInt, waitInt.length);
                socket.receive(packet);
                if(ByteBuffer.wrap(waitInt).getInt() == 1) {
                    gui.active = false;
                    long timeDeactive = System.currentTimeMillis();
                    for(;;) {
                        if((System.currentTimeMillis() - timeDeactive) >= 1500) {
                            break;
                        }
                    }
                    gui.active = true;
                }
            } catch (IOException e) {}
        }
    }

    public void safeKill() {
        try {
            socket.leaveGroup(group);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
