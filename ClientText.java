/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Assignment3;

/**
 * @author Chairman
 */


import java.io.IOException;
import java.util.*;
import java.net.*;
import java.nio.ByteBuffer;

public class ClientText extends Client {

    double newBid;
    String buyerAddress = "0.0.0.0:0";

    private class MyTask implements Runnable {

        public void run() {
            try {
                DatagramSocket clientSocket = new DatagramSocket(port + 1);
                for(;;) {
                    byte[] bidPacket = new byte[8];
                    byte[] addressPacket = new byte[64];
                    byte[] timePacket = new byte[4];
                    DatagramPacket receivePacket = new DatagramPacket(bidPacket, bidPacket.length);
                    for (int i = 0; i < 1; i++) {
                        try {
                            clientSocket.receive(receivePacket);
                        } catch (SocketTimeoutException e) {
                            i--;
                        }
                    }
                    newBid = ByteBuffer.wrap(bidPacket).getDouble();

                    //Uncomment this to add receiving of buyer address and time left in auction
                    //CLIENT TEXT SHOULDN'T DO ANYTHING WITH TIME OTHER THAN RECEIVE TO CLEAR THE PACKET
                    /*
                    receivePacket = new DatagramPacket(addressPacket, addressPacket.length);
                    clientSocket.receive(receivePacket);
                    buyerAddress = new String(addressPacket);
                    buyerAddress = buyerAddress.trim();
                    receivePacket = new DatagramPacket(timePacket, timePacket.length);
                    socket.receive(receivePacket);
                    */
                    if (newBid != bid) {
                        bid = newBid;
                        System.out.print("\rMax bid: $" + bid + "\tBuyer address: " + buyerAddress);
                        System.out.print("\tBid: ");
                    }
                }
            } catch (IOException e) {}
        }
    }

    public void main() {
        Scanner kb = new Scanner(System.in);
        for (; ; ) {
            try {
                if (socket == null) {
                    findGoodPort();
                    socket = new DatagramSocket(port);
                    address = InetAddress.getByName("localhost");
                    contactServer();
                }
                System.out.print("\rMax bid: $" + bid + "\tBuyer address: " + buyerAddress);
                System.out.print("\tBid: $");
                bid(kb.nextLine());
            } catch (IOException e) {}
        }
    }

    public void contactServer() {
        bid("-1");
    }

    public void bid(String input) {
        try {
            Thread t = new Thread(new MyTask());
            t.start();
            double bidAmount;
            String[] parts = input.split("\\.");
            if (parts.length > 1) {
                if (parts[1].length() > 2) {
                    System.out.println("\n***Amount can only have up to two decimal places.***\n");
                    System.out.print("Max bid: $" + bid + "\tBuyer address: " + buyerAddress);
                    System.out.print("\tBid: $");
                    return;
                }
            }
            try {
                bidAmount = Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.println("\n***Invalid amount.***\n");
                System.out.print("Max bid: $" + bid + "\tBuyer address: " + buyerAddress);
                System.out.print("\tBid: $");
                return;
            }

            //long startTime = System.nanoTime();
            DatagramPacket sendPacket = new DatagramPacket(ByteBuffer.allocate(8).putDouble(bidAmount).array(),
                    8, address, SERVER_PORT);
            socket.send(sendPacket);
        } catch (IOException e) {}
    }
}
