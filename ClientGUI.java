package Assignment3;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;

public class ClientGUI extends Client {
    JFrame window;
    private JPanel panel1;
    JLabel maxBid;
    JLabel buyerAddress;
    JLabel timeLeft;
    private JTextField bidAmount;
    private JButton bidButton;
    private JLabel errorMessage;
    boolean active = true;

    private class Listener implements ActionListener, KeyListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (active) {
                errorMessage.setVisible(false);
                bid(bidAmount.getText());
                bidAmount.setText("");
            }
        }

        @Override
        public void keyTyped(KeyEvent e) {
        }

        @Override
        public void keyPressed(KeyEvent e) {
        }

        @Override
        public void keyReleased(KeyEvent e) {
        }
    }

    private class MyTask implements Runnable {
        public void run() {
            for(;;) {
                byte[] receiveData = new byte[2000];
                try {
                    DatagramSocket clientSocket = new DatagramSocket(port + 1);
                    DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                    clientSocket.receive(receivePacket);
                    bid = ByteBuffer.wrap(receiveData).getDouble();

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
                    System.out.println("Max bid: " + bid);
                    maxBid.setText("$" + bid);
                    clientSocket.close();
                } catch (IOException e) {}
            }
        }
    }

    public ClientGUI() {
        startWindow();
    }

    private void startWindow() {
        window = new JFrame();
        window.setTitle("Startup");
        window.setSize(300, 80);
        window.setLocationRelativeTo(null);
        final JPanel startPanel = new JPanel();

        startPanel.setSize(300, 80);
        // Initialize the components
        final JLabel label = new JLabel("Would you like to start the bidding app?");
        final JButton startButton = new JButton("Yes");

        //format the components

        // Setup the action listeners
        startButton.addActionListener((ActionEvent event) -> {
            startPanel.setVisible(false);
            window.getContentPane().remove(startPanel);
            bidWindow();
        });

        // Add all of the labels to the JFrame
        startPanel.add(label);
        startPanel.add(startButton);
        window.getContentPane().add(startPanel);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void bidWindow() {
        //Panel1
        panel1 = new JPanel();
        GroupLayout layout1 = new GroupLayout(panel1);
        panel1.setLayout(layout1);
        layout1.setAutoCreateGaps(true);
        layout1.setAutoCreateContainerGaps(true);
        //Panel1 -> Panel2
        final JPanel panel2 = new JPanel();
        GroupLayout layout2 = new GroupLayout(panel2);
        panel2.setLayout(layout2);
        layout2.setAutoCreateContainerGaps(true);
        final JLabel label1 = new JLabel("Current Maximum Bid:");
        maxBid = new JLabel("$0.00");
        final JLabel label2 = new JLabel("Current Buyer Address:");
        buyerAddress = new JLabel("0.0.0.0:0");
        layout2.setHorizontalGroup(
                layout2.createSequentialGroup()
                        .addComponent(label1)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(maxBid)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(label2)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buyerAddress)
        );
        layout2.setVerticalGroup(
                layout2.createSequentialGroup()
                        .addGroup(layout2.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(label1)
                                .addComponent(maxBid)
                                .addComponent(label2)
                                .addComponent(buyerAddress))
        );
        //Panel1 -> Panel3
        final JPanel panel3 = new JPanel();
        GroupLayout layout3 = new GroupLayout(panel3);
        panel3.setLayout(layout3);
        layout3.setAutoCreateContainerGaps(true);
        final JLabel label4 = new JLabel("Time Left in Auction:");
        timeLeft = new JLabel("0 seconds");
        layout3.setHorizontalGroup(
                layout3.createSequentialGroup()
                        .addComponent(label4)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(timeLeft)
        );
        layout3.setVerticalGroup(
                layout3.createSequentialGroup()
                        .addGroup(layout3.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(label4)
                                .addComponent(timeLeft))
        );
        //Panel1 -> Panel4
        final JPanel panel4 = new JPanel();
        GroupLayout layout4 = new GroupLayout(panel4);
        panel4.setLayout(layout4);
        layout4.setAutoCreateContainerGaps(true);
        final JLabel label3 = new JLabel("Bid Amount: $");
        bidAmount = new JTextField("", 8);
        errorMessage = new JLabel("*");
        bidButton = new JButton("Bid");
        layout4.setHorizontalGroup(
                layout4.createSequentialGroup()
                        .addComponent(label3)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout4.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(bidAmount)
                                .addComponent(errorMessage))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bidButton)
        );
        layout4.setVerticalGroup(
                layout4.createSequentialGroup()
                        .addGroup(layout4.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(label3)
                                .addComponent(bidAmount)
                                .addComponent(bidButton))
                        .addComponent(errorMessage)
        );
        //Add Panels2-4 to Panel1
        layout1.setHorizontalGroup(
                layout1.createSequentialGroup()
                        .addGroup(layout1.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(panel2)
                                .addComponent(panel3)
                                .addComponent(panel4))
        );
        layout1.setVerticalGroup(
                layout1.createSequentialGroup()
                        .addComponent(panel2)
                        .addComponent(panel3)
                        .addComponent(panel4)
        );
        //Final setup stuff
        Listener listener = new Listener();
        try {
            socket = new DatagramSocket(12711);
            address = InetAddress.getByName(HOST_NAME);
        } catch (IOException e) {
            e.printStackTrace();
        }
        window.setContentPane(panel1);
        window.setResizable(false);
        window.setTitle("Bidding");
        window.pack();
        errorMessage.setForeground(Color.RED);
        errorMessage.setFont(new Font(UIManager.getDefaults().getFont("Label.font").getFontName(), Font.PLAIN,
                8));
        errorMessage.setVisible(false);
        bidButton.addActionListener(listener);
        bidAmount.addActionListener(listener);
        window.setVisible(true);
    }

    public void bid(String input) {
        try {
            if(socket == null) {
                findGoodPort();
                socket = new DatagramSocket(port);
                address = InetAddress.getByName("localhost");
                contactServer();
            }

            Thread t = new Thread(new MyTask());
            t.start();
            double bid;
            String[] parts = input.split("\\.");
            if (parts.length > 1) {
                if (parts[1].length() > 2) {
                    errorMessage.setText("*Amount can only have up to two decimal places");
                    errorMessage.setVisible(true);
                    return;
                }
            }
            try {
                bid = Double.parseDouble(input);
            } catch (NumberFormatException e) {
                errorMessage.setText("*Invalid amount");
                errorMessage.setVisible(true);
                return;
            }

            //long startTime = System.nanoTime();
            System.out.println("Client sent $" + input + " as bid");
            DatagramPacket sendPacket = new DatagramPacket(ByteBuffer.allocate(8).putDouble(bid).array(), 8,
                    address, SERVER_PORT);
            socket.send(sendPacket);
            socket.close();
        } catch (IOException e) {}
    }

    public void contactServer() {
        bid("-1");
    }

    public void main() {
        EventQueue.invokeLater(() -> {
            ClientGUI gui = new ClientGUI();
            gui.window.setVisible(true);
        });
    }
}
