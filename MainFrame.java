import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Main frame class
public class MainFrame extends JFrame {
    public MainFrame() {
        setTitle("Button Click Example");
        setSize(400, 300);
        setLayout(new GridLayout(2, 5)); // 2 rows, 5 buttons per row
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(Color.WHITE); // Set background color of the main frame

        // Create 10 buttons
        for (int i = 1; i <= 10; i++) {
            JButton button = new RoundedButton("Button " + i);
            int buttonNumber = i; // Needed for the ActionListener
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Open the popup frame when the button is clicked
                    new PopupFrame("You clicked Button " + buttonNumber);
                }
            });
            add(button);
        }
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainFrame();
        });
    }
}

// Popup frame class
class PopupFrame extends JFrame {
    public PopupFrame(String message) {
        setTitle("Popup Frame");
        setSize(200, 150);
        setLayout(new FlowLayout());
        getContentPane().setBackground(Color.WHITE); // Set background color of the popup frame

        JLabel label = new JLabel(message);
        add(label);

        JButton closeButton = new RoundedButton("Close");
        closeButton.addActionListener(e -> {
            dispose(); // Close the popup when the button is clicked
        });
        add(closeButton);

        setVisible(true);
        setLocationRelativeTo(null); // Center the popup frame
    }
}

// Custom rounded button class
class RoundedButton extends JButton {
    public RoundedButton(String text) {
        super(text);
        setFocusPainted(false);
        setBackground(Color.BLUE); // Set button background color
        setForeground(Color.WHITE); // Set text color
        setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30)); // Set padding
        setContentAreaFilled(false); // Prevent button from being filled
        setOpaque(false); // Make it transparent but still take up space
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    @Override
    protected void paintComponent(Graphics g) {
        // Draw a rounded button background
        g.setColor(getBackground());
        g.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20); // Set border radius
        super.paintComponent(g); // Handle painting the label (button text)
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(100, 40); // Specify preferred size for the button
    }
}