import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

class EditableTextFieldUI extends JFrame {

    private JPanel panel;
    private ArrayList<JTextField> textFields;

    public void addTextField(String text) {
        textFields.add(new JTextField(text,20));
    }
    public EditableTextFieldUI() {
        super("Editable Text Field UI");

        // create the panel to hold the text fields
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        textFields = new ArrayList<>();
        // create the text fields
        int length = 5;
        for (int i = 0; i < length; i++) {
            addTextField("Text Field " + i);
            textFields.get(i).setEditable(true); // allow editing
            textFields.get(i).setAlignmentX(Component.LEFT_ALIGNMENT);

            // add a mouse listener to detect clicks
            textFields.get(i).addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    JTextField source = (JTextField)e.getSource();

                    // if right-click, show a popup menu with delete option
                    if (SwingUtilities.isRightMouseButton(e)) {
                        JPopupMenu popup = new JPopupMenu();
                        JMenuItem deleteItem = new JMenuItem("Delete");
                        deleteItem.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                panel.remove(source); // remove from panel
                                revalidate(); // re-layout panel
                                repaint(); // repaint UI
                            }
                        });
                        popup.add(deleteItem);
                        popup.show(source, e.getX(), e.getY());
                    }
                }
            });

            // add the text field to the panel
            panel.add(textFields.get(i));
        }

        // add the panel to the frame
        getContentPane().add(panel, BorderLayout.CENTER);

        // set window size and show the UI
        setSize(300, 200);
        setVisible(true);
    }


}
