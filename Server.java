package Assignment3;

import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;

public class Server {

    private static DatagramSocket socket;
    private static DatagramPacket bidPacket, packet;
    private static InetAddress group;
    private static byte[] num = new byte[8];
    private static double greatest = 0;
    private static InetAddress buyerAddress = null;
    private static int buyerPort = 0;

    public static void main(String[] args) {
        //Open a new socket
        try {
            socket = new DatagramSocket(2711);
            socket.setSoTimeout(100);
            group = InetAddress.getByName("234.123.12.12");
            System.out.println(group);
        } catch (SocketException | UnknownHostException e) {
            e.printStackTrace();
        }

        try {
            for(;;) {
                boolean restart = false;
                boolean wait = true;
                long bidStart = System.currentTimeMillis();
                int j = 0;
                for (; ; ) {
                    bidPacket = new DatagramPacket(num, num.length);
                    for (int i = 0; i < 1; i++, j++) {
                        if(wait) {
                            packet = new DatagramPacket(ByteBuffer.allocate(8).putDouble(greatest).array(), 8,
                                    group, 22711);
                            socket.send(packet);
                            String buyer = "0.0.0.0:0";
                            packet = new DatagramPacket(ByteBuffer.allocate(4).putInt(buyer.length()).array(), 4,
                                    group, 22711);
                            socket.send(packet);
                            packet = new DatagramPacket(buyer.getBytes(), buyer.length(), group, 22711);
                            socket.send(packet);
                            packet = new DatagramPacket(ByteBuffer.allocate(4).putInt(60).array(),
                                    4, group, 22711);
                            socket.send(packet);
                            packet = new DatagramPacket(ByteBuffer.allocate(4).putInt(1).array(), 4, group,
                                    22711);
                            socket.send(packet);
                            wait = false;
                        }
                        if(((System.currentTimeMillis() - bidStart) / 1000) >= 60) {
                            buyerAddress = null;
                            buyerPort = 0;
                            greatest = 0;
                            restart = true;
                            break;
                        }
                        try {
                            socket.receive(bidPacket);
                        } catch (SocketTimeoutException e) {
                            i--;
                            if (j > 10) {
                                packet = new DatagramPacket(ByteBuffer.allocate(8).putDouble(greatest).array(),
                                        8, group, 22711);
                                socket.send(packet);
                                if (buyerAddress != null) {
                                    String buyer = buyerAddress + ":" + buyerPort;
                                    buyer = buyer.substring(1);
                                    packet = new DatagramPacket(ByteBuffer.allocate(4).putInt(buyer.length()).array(),
                                            4, group, 22711);
                                    socket.send(packet);
                                    packet = new DatagramPacket(buyer.getBytes(), buyer.length(), group, 22711);
                                    socket.send(packet);
                                    int timeLeft = 60 - (int) ((System.currentTimeMillis() - bidStart) / 1000);
                                    packet = new DatagramPacket(ByteBuffer.allocate(4).putInt(timeLeft).array(),
                                            4, group, 22711);
                                    socket.send(packet);
                                    packet = new DatagramPacket(ByteBuffer.allocate(4).putInt(0).array(), 4,
                                            group, 22711);
                                    socket.send(packet);
                                } else {
                                    String buyer = "0.0.0.0:0";
                                    packet = new DatagramPacket(ByteBuffer.allocate(4).putInt(buyer.length()).array(),
                                            4, group, 22711);
                                    socket.send(packet);
                                    packet = new DatagramPacket(buyer.getBytes(), buyer.length(), group, 22711);
                                    socket.send(packet);
                                    int timeLeft = 60 - (int) ((System.currentTimeMillis() - bidStart) / 1000);
                                    packet = new DatagramPacket(ByteBuffer.allocate(4).putInt(timeLeft).array(),
                                            4, group, 22711);
                                    socket.send(packet);
                                    packet = new DatagramPacket(ByteBuffer.allocate(4).putInt(0).array(), 4,
                                            group, 22711);
                                    socket.send(packet);
                                }
                                j = 0;
                            }
                        }
                    }
                    if(restart) {
                        break;
                    }
                    double b = ByteBuffer.wrap(bidPacket.getData()).getDouble();
                    if (b > greatest) {
                        buyerAddress = bidPacket.getAddress();
                        buyerPort = bidPacket.getPort();
                        greatest = b;
                        packet = new DatagramPacket(ByteBuffer.allocate(8).putDouble(greatest).array(), 8, group,
                                22711);
                        socket.send(packet);
                        String buyer = buyerAddress + ":" + buyerPort;
                        buyer = buyer.substring(1);
                        packet = new DatagramPacket(ByteBuffer.allocate(4).putInt(buyer.length()).array(), 4,
                                group, 22711);
                        socket.send(packet);
                        packet = new DatagramPacket(buyer.getBytes(), buyer.length(), group, 22711);
                        socket.send(packet);
                        int timeLeft = 60 - (int) ((System.currentTimeMillis() - bidStart) / 1000);
                        packet = new DatagramPacket(ByteBuffer.allocate(4).putInt(timeLeft).array(),
                                4, group, 22711);
                        socket.send(packet);
                        packet = new DatagramPacket(ByteBuffer.allocate(4).putInt(0).array(), 4, group,
                                22711);
                        socket.send(packet);
                        System.out.println("Greatest bid = " + greatest + "\t Buyer = " +
                                (buyerAddress + ":" + buyerPort).substring(1));
                    }
                }
            }
        } catch (IOException e) {}
    }
}
