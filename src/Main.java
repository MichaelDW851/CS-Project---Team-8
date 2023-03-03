import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Create a JFrame object, which represents a window
        JFrame frame = new JFrame("My Swing App");

        // Set the size of the window
        frame.setSize(400, 300);

        // Set the default close operation (exit when the user clicks the close button)
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create a JLabel object, which represents a text label
        JLabel label = new JLabel("Hello, world!");

        // Add the label to the content pane of the window
        frame.getContentPane().add(label);

        // Display the window
        frame.setVisible(true);
    }
}