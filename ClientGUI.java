package Assignment3;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;

public class ClientGUI {
    private Receiver receiver;
    private DatagramSocket socket;
    private final int SERVER_PORT = 2711;
    private final String HOST_NAME = "129.3.20.26";
    private InetAddress address;
    private JPanel panel1;
    private JTextField bidAmount;
    private JButton bidButton;
    private JLabel errorMessage;
    JLabel maxBid;
    JLabel buyerAddress;
    JLabel timeLeft;
    boolean active = true;

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 1, new Insets(5, 5, 5, 5), -1, -1));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(3, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(500, 120), new Dimension(500, 120), new Dimension(500, 120), 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(1, 5, new Insets(0, 0, 0, 0), -1, -1));
        panel2.add(panel3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Current Maximum Bid:");
        panel3.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        maxBid = new JLabel();
        maxBid.setText("$0.00");
        panel3.add(maxBid, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Current Buyer Address:");
        panel3.add(label2, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buyerAddress = new JLabel();
        buyerAddress.setText("0.0.0.0:0");
        panel3.add(buyerAddress, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel3.add(spacer1, new GridConstraints(0, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(2, 3, new Insets(0, 0, 0, 0), -1, -1));
        panel2.add(panel4, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Bid Amount: $");
        panel4.add(label3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        bidAmount = new JTextField();
        panel4.add(bidAmount, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        bidButton = new JButton();
        bidButton.setText("Bid");
        panel4.add(bidButton, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        errorMessage = new JLabel();
        errorMessage.setText("*");
        panel4.add(errorMessage, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        panel2.add(panel5, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("Time Left in Auction:");
        panel5.add(label4, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        timeLeft = new JLabel();
        timeLeft.setText("0 seconds");
        panel5.add(timeLeft, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        panel5.add(spacer2, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel1;
    }

    private class Listener implements ActionListener, KeyListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (active) {
                errorMessage.setVisible(false);
                bid();
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

    public ClientGUI() {
        Listener listener = new Listener();
        try {
            socket = new DatagramSocket(12711);
            address = InetAddress.getByName(HOST_NAME);
            receiver = new Receiver(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
        JFrame frame = new JFrame("ClientGUI");
        frame.setContentPane(panel1);
        frame.setResizable(false);
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent evt) {
                System.out.println("Killing receiver");
                receiver.safeKill();
            }
        });
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        errorMessage.setForeground(Color.RED);
        errorMessage.setFont(new Font(UIManager.getDefaults().getFont("Label.font").getFontName(), Font.PLAIN,
                8));
        errorMessage.setVisible(false);
        bidButton.addActionListener(listener);
        bidAmount.addActionListener(listener);
        frame.setVisible(true);
    }

    private void bid() {
        double bid;
        String b = bidAmount.getText();
        String[] parts = b.split("\\.");
        if (parts.length > 1) {
            if (parts[1].length() > 2) {
                errorMessage.setText("Amount can only have up to two decimal places");
                errorMessage.setVisible(true);
                return;
            }
        }
        try {
            bid = Double.parseDouble(b);
        } catch (NumberFormatException e) {
            errorMessage.setText("Invalid amount");
            errorMessage.setVisible(true);
            return;
        }
        DatagramPacket packet = new DatagramPacket(ByteBuffer.allocate(8).putDouble(bid).array(), 8, address,
                SERVER_PORT);
        try {
            socket.send(packet);
        } catch (IOException e) {
        }
    }
}