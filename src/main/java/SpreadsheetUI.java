import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.PdfPTable;
//import com.itextpdf.text.Font;



import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

class SpreadsheetUI extends JFrame {
    private JTable table;
    private DefaultTableModel model;
    private JScrollPane scrollPane;
    private JLabel titleLabel;

    private String studentName;
    private String studentID;
    private String semesterAdmitted;
    private boolean fastTrack = false;
    private boolean thesis = false;

    private String anticipatedGrad;
    private int rowCounter = 0;

    public SpreadsheetUI(String studentName, String studentID, String semesterAdmitted,boolean isFastTrack, boolean isThesis,String track) {
        this();
        //  setupByTrack(track);
        addFields(studentName,studentID,semesterAdmitted,isFastTrack,isThesis);

    }

    public void addCourse(Course course) {

        Object[] row = new Object[]{"", "", "", "", "", ""};
        model.addRow(row);
        model.setValueAt(course.getCourseName(),rowCounter,0);
        model.setValueAt(course.getCourseCode(),rowCounter,1);
        model.setValueAt(course.getSemester(), rowCounter, 2);
        //     model.setValueAt("Course Transfer", rowCounter, 3);
        model.setValueAt(course.getGrade(), rowCounter, 4);
        rowCounter++;

    }




    public SpreadsheetUI() {

        // Add a button to clear the selection


        super("Degree Plan Editor");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());


        JLabel titleLabel1 = new JLabel("Degree Plan", SwingConstants.CENTER);
        titleLabel1.setFont(new Font("Arial", Font.BOLD, 24));
        JLabel titleLabel2 = new JLabel("University of Texas at Dallas", SwingConstants.CENTER);
        titleLabel2.setFont(new Font("Arial", Font.BOLD, 24));
        JLabel titleLabel3 = new JLabel("Master of Computer Science",SwingConstants.CENTER);
        titleLabel3.setFont(new Font("Arial", Font.BOLD,24));

        JPanel titlePanel = new JPanel(new GridLayout(3, 1));
        titlePanel.add(titleLabel1);
        titlePanel.add(titleLabel2);
        titlePanel.add(titleLabel3);

        add(titlePanel, BorderLayout.NORTH);


        // Set up the table
        model = new DefaultTableModel();
        table = new JTable(model);
        table.setRowHeight(20);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Add columns
        String[] columnHeaders = new String[]{ "Course Title", "Course Number" ,"UTD Semester", "Transfer", "Grade"};
        model.setColumnIdentifiers(columnHeaders);



        // Set up header renderer and editor
        table.getTableHeader().setDefaultRenderer(new HeaderRenderer());
        table.setDefaultEditor(Object.class, new DefaultCellEditor(new JTextField()));


        table.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    table.clearSelection();
                }
            }
        });

        // Set up the bottom panel with editable text fields for selected cells
        JPanel bottomPanel = new JPanel(new GridLayout(1, 0));
        add(bottomPanel, BorderLayout.SOUTH);
        table.getSelectionModel().addListSelectionListener(e -> {
            int[] selectedRows = table.getSelectedRows();
            int[] selectedCols = table.getSelectedColumns();
            bottomPanel.removeAll();
            if (selectedRows.length == 1 && selectedCols.length == 1) {
                JTextField field = new JTextField((String) table.getValueAt(selectedRows[0], selectedCols[0]));
                field.addActionListener(e2 -> {
                    table.setValueAt(field.getText(), selectedRows[0], selectedCols[0]);
                });
                bottomPanel.add(field);
            } else {
                bottomPanel.revalidate();
                bottomPanel.repaint();
            }
        });
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);



        //adding separate fields for name, fast track, thesis, etc.
        addFields();


        JPanel buttonPanel = new JPanel(new BorderLayout());
        JButton saveAsPdfButton = new JButton("Save as PDF");
        buttonPanel.add(saveAsPdfButton, BorderLayout.CENTER);

        //  buttonPanel.add(closeButton, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.EAST);

        saveAsPdfButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showSaveDialog(SpreadsheetUI.this);
            if (result == JFileChooser.APPROVE_OPTION) {
                try {
                    Document document = new Document();
                    PdfWriter.getInstance(document, new FileOutputStream(fileChooser.getSelectedFile() + ".pdf"));
                    document.open();
                    PdfPTable pdfTable = new PdfPTable(table.getColumnCount());
                    pdfTable.setWidthPercentage(100);

                    Paragraph paragraph = new Paragraph("Degree Plan");
                    paragraph.add("\n");
                    paragraph.add("University of Texas at Dallas");
                    paragraph.add("\nMaster of Computer Science");
                    paragraph.setAlignment(Element.ALIGN_CENTER);
                    document.add(paragraph);

                    paragraph.clear();

                    paragraph.setAlignment(Element.ALIGN_LEFT);
                    paragraph.add("\n\nStudent Name: " + studentName);
                    paragraph.add("\nStudent ID: " + studentID);
                    paragraph.add("\nSemester Admitted: " + semesterAdmitted);
                    paragraph.add("\nAnticipated Graduation: " + anticipatedGrad);

                    paragraph.add("\n\nIs Fast Track: ");
                    if (fastTrack) {
                        paragraph.add("Yes");
                    }
                    else {
                        paragraph.add("No");

                    }
                    paragraph.add("\nIs Thesis Masters: ");
                    if (thesis) {
                        paragraph.add("Yes");
                    }
                    else {
                        paragraph.add("No");

                    }

                    //    paragraph.setAlignment(Element.ALIGN_TOP);
                    document.add(paragraph);

                    document.add(Chunk.NEWLINE);



                    for (int i = 0; i < table.getColumnCount(); i++) {
                        pdfTable.addCell(new Phrase(table.getColumnName(i)));
                    }
                    for (int i = 0; i < table.getRowCount(); i++) {
                        for (int j = 0; j < table.getColumnCount(); j++) {
                            pdfTable.addCell(new Phrase(table.getValueAt(i, j).toString()));
                        }
                    }


                    document.add(pdfTable);
                    document.close();
                    JOptionPane.showMessageDialog(SpreadsheetUI.this, "File saved successfully!");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(SpreadsheetUI.this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });


        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    void addFields() {
        addFields("","","",false,false);
    }

    void addFields(String studentName, String studentID, String semesterAdmitted, boolean isFastTrack, boolean isThesis) {
        //   System.out.println("ADDING FIELDS");


        this.studentID = studentID;
        this.studentName = studentName;
        this.semesterAdmitted = semesterAdmitted;
        this.fastTrack = isFastTrack;
        this.thesis = isThesis;
        JPanel bottomPanelTwo = new JPanel(new GridBagLayout());
        add(bottomPanelTwo, BorderLayout.SOUTH);
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 5, 5, 5);
        c.anchor = GridBagConstraints.WEST;


        // student name field
        JLabel nameLabel = new JLabel("Student Name: ");
        JTextField nameField = new JTextField(10);
        c.gridx = 0;
        c.gridy = 0;
        bottomPanelTwo.add(nameLabel, c);
        c.gridx = 1;
        c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        bottomPanelTwo.add(nameField, c);
        c.weightx = 0;
        //      c.fill = GridBagConstraints.NONE;
        nameField.setText(studentName);

        // student id field
        JLabel IDLabel = new JLabel("Student ID:");
        JTextField IDField = new JTextField(10);
        c.gridx = 0;
        c.gridy = 1;
        bottomPanelTwo.add(IDLabel, c);
        c.gridx = 1;
        c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        bottomPanelTwo.add(IDField, c);
        c.weightx = 0;
        c.fill = GridBagConstraints.NONE;
        IDField.setText(studentID);


        //semester admitted field
        JLabel SemesterAdmittedLabel = new JLabel("Semester Admitted to Program:");
        JTextField SemesterAdmittedField = new JTextField(10);
        c.gridx = 0;
        c.gridy = 2;
        bottomPanelTwo.add(SemesterAdmittedLabel, c);
        c.gridx = 1;
        c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        bottomPanelTwo.add(SemesterAdmittedField, c);
        c.weightx = 0;
        c.fill = GridBagConstraints.NONE;
        SemesterAdmittedField.setText(semesterAdmitted);


        //anticipated graduation field
        JLabel graduationLabel = new JLabel("Anticipated Graduation:");
        JTextField graduationField = new JTextField(10);
        c.gridx = 0;
        c.gridy = 3;
        bottomPanelTwo.add(graduationLabel, c);
        c.gridx = 1;
        c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        bottomPanelTwo.add(graduationField, c);
        c.weightx = 0;
        c.fill = GridBagConstraints.NONE;



        // Add checkbox
        JCheckBox fastTrackCheckBox = new JCheckBox("Fast Track");
        c.gridx = 0;
        c.gridy = 4;
        bottomPanelTwo.add(fastTrackCheckBox, c);
        if (isFastTrack) {
            //   System.out.println("FAST TRACK");
            fastTrackCheckBox.setSelected(true);

        }

        JCheckBox thesisCheckBox = new JCheckBox("Thesis");
        c.gridx = 1;
        bottomPanelTwo.add(thesisCheckBox, c);
        if(isThesis) {
            //       System.out.println("THESIS");
            thesisCheckBox.setSelected(true);
        }

    }

    private class HeaderRenderer extends DefaultTableCellRenderer {
        public HeaderRenderer() {
            setHorizontalAlignment(JLabel.CENTER);
            setOpaque(true);
            setBackground(Color.lightGray);
            setFont(getFont().deriveFont(Font.BOLD));
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText(value.toString());
            return this;
        }
    }

}

