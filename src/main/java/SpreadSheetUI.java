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
    private JPanel topPanel;

    int additionalElectivesRow;

    int approvedElectivesRow;

    Map<String, Integer> courseMappings;

    private String studentName;
    private String studentID;
    private String semesterAdmitted;
    private boolean fastTrack = false;
    private boolean thesis = false;

    private String anticipatedGrad;
    private int rowCounter = 0;

    private int defaultRowHeight = 16;

    public void setupByTrack(String track) {
        //Going to use a map to determine where each course listing is
        //First int is the row
        String[] coreCoursesCodes;
        String[] levelingCoursesCodes;
        String[] trackElectivesCodes;

        courseMappings = new HashMap<>();
        ArrayList<String> approvedElectives = new ArrayList<>();
        ArrayList<String> additionalElectives = new ArrayList<>();
        System.out.println("JIOGW$REIJOW$TIJOIKOJW#");
        switch (track) {

            case "Data Science":
                coreCoursesCodes = new String[]{"CS6313", "CS6350", "CS6363", "CS6375", "CS6360"}; // Add core courses for this track
                setupCores(coreCoursesCodes);

                trackElectivesCodes =new String[]{"CS6301", "CS6320", "CS6327", "CS6347", "CS6360"};
                setupTrackElectives(trackElectivesCodes);

                setupApprovedElectives();
                setupAdditionalElectives();

                levelingCoursesCodes =new String[]{"CS3341", "CS5333", "CS5343", "CS5303", "CS5348", "CS5330"};
                setupLeveling(levelingCoursesCodes);
                break;
            // Add cases for other tracks and their respective core and elective courses
            case "Systems":

                coreCoursesCodes = new String[]{"CS6304", "CS6363", "CS6378", "CS6396"};
                setupCores(coreCoursesCodes);

                trackElectivesCodes = new String[]{"CS6349","CS6376","CS6380","CS6397","CS6399"};
                setupTrackElectives(trackElectivesCodes);

                setupApprovedElectives();
                setupAdditionalElectives();

                levelingCoursesCodes = new String[]{"CS5303","CS5330","CS5333","CS5343","CS5348","CS5390"};
                setupLeveling(levelingCoursesCodes);
                break;
            case "Interactive Computing":
                coreCoursesCodes = new String[]{"CS6326", "CS6363"};
                setupCores(coreCoursesCodes);

                trackElectivesCodes = new String[]{"CS6323", "CS6328", "CS6331", "CS6334", "CS6366"};
                setupTrackElectives(trackElectivesCodes);

                setupApprovedElectives();
                setupAdditionalElectives();

                levelingCoursesCodes = new String[]{"CS5303", "CS5330", "CS5333", "CS5343", "CS5348"};
                setupLeveling(levelingCoursesCodes);
                break;
            case "Cyber Security":
                coreCoursesCodes = new String[]{"CS6324", "CS6363", "CS6378"};
                setupCores(coreCoursesCodes);

                trackElectivesCodes = new String[]{"CS6332", "CS6348", "CS6349", "CS6377"};
                setupTrackElectives(trackElectivesCodes);

                setupApprovedElectives();
                setupAdditionalElectives();

                levelingCoursesCodes =new String[]{"CS5303", "CS5330", "CS5333", "CS5343", "CS5348", "CS5390"};
                setupLeveling(levelingCoursesCodes);
                break;
            case "Intelligent Systems":
                coreCoursesCodes = new String[]{"CS6320", "CS6363", "CS6364", "CS6375"};
                setupCores(coreCoursesCodes);

                trackElectivesCodes = new String[]{"CS6360", "CS6378"};
                setupTrackElectives(trackElectivesCodes);

                setupApprovedElectives();
                setupAdditionalElectives();

                levelingCoursesCodes =new String[]{"CS5303", "CS5330", "CS5343", "CS5333", "CS5348"};
                setupLeveling(levelingCoursesCodes);
                break;

            case "Networks and Telecommunications":
                coreCoursesCodes = new String[]{"CS6352", "CS6363", "CS6378", "CS6385", "CS6390"};
                setupCores(coreCoursesCodes);

                setupApprovedElectives();
                setupAdditionalElectives();

                levelingCoursesCodes =new String[]{"CS5303", "CS5330", "CS5343", "CS5348", "CS5390", "CS3341", "CS5333"};
                setupLeveling(levelingCoursesCodes);
                break;
            case "Traditional Computer Science":
                coreCoursesCodes = new String[]{"CS6363", "CS6378", "CS6390"};
                setupCores(coreCoursesCodes);

                trackElectivesCodes = new String[]{"CS6353", "CS6360", "CS6371"};
                setupTrackElectives(trackElectivesCodes);

                setupApprovedElectives();
                setupAdditionalElectives();

                levelingCoursesCodes =new String[]{"CS5303", "CS5330", "CS5333", "CS5343", "CS5348", "CS5349", "CS5390"};
                setupLeveling(levelingCoursesCodes);
        }


    }



    public SpreadsheetUI(String studentName, String studentID, String semesterAdmitted,boolean isFastTrack, boolean isThesis,String track) {
        this();
      //  setupByTrack(track);
        addFields(studentName,studentID,semesterAdmitted,isFastTrack,isThesis);

    }


    private void setupCores(String[] cores) {
        Object[] row = new Object[]{"","",""};
        model.addRow(row);
        model.setValueAt("Core Courses",rowCounter,0);
        model.setValueAt("(15 Credit Hours)",rowCounter,1);
        model.setValueAt("3.19 Grade Point Average Required",rowCounter,2);
        rowCounter++;
        for (String course: cores) {
            courseMappings.put(course,rowCounter);

            row = new Object[]{"","","","",""};
            model.addRow(row);
            model.setValueAt(course,rowCounter,1);
            rowCounter++;
        }
    }









    private void setupTrackElectives(String[] trackElectives) {
        Object[] row = new Object[]{""};
        model.addRow(row);
        model.setValueAt("of The Following Required",rowCounter,0);
        rowCounter++;
        for (String course: trackElectives) {
            courseMappings.put(course,rowCounter);

            row = new Object[]{"","","","",""};
            model.addRow(row);
            model.setValueAt(course,rowCounter,1);
            rowCounter++;
        }
    }


    public void addTrackElectives(ArrayList<Course> trackElectives) {
        Object[] row = new Object[]{""};
        model.addRow(row);
        model.setValueAt("of The Following Required",rowCounter,0);
        rowCounter++;
        for (Course course: trackElectives) {

        }

    }

    private void setupApprovedElectives() {
        Object[] row = new Object[]{ "", "", ""};

        model.addRow(row);
        model.setValueAt("FIVE APPROVED 6000 LEVEL ELECTIVES",rowCounter,0);
        model.setValueAt("(15* Credit Hours)",rowCounter,1);
        model.setValueAt("3.0 Grade Point Average",rowCounter,2);
        rowCounter++;
        for(int i = 0;i < 5;i++) {


            row = new Object[]{"","","","",""};
            model.addRow(row);
            rowCounter++;
        }


    }


    public void addApprovedElectives(ArrayList<Course> approvedElectives) {
//        Object[] row = new Object[]{ "", "", ""};
//
//        model.addRow(row);
//        model.setValueAt("FIVE APPROVED 6000 LEVEL ELECTIVES",rowCounter,0);
//        model.setValueAt("(15* Credit Hours)",rowCounter,1);
//        model.setValueAt("3.0 Grade Point Average",rowCounter,2);
//        rowCounter++;
        for (Course course: approvedElectives) {
            addCourse(course);
        }
    }


    private void setupAdditionalElectives() {
        Object[] row = new Object[]{""};
        model.addRow(row);
        model.setValueAt("Additional Electives (3 Credit Hours Minimum)",rowCounter,0);
        rowCounter++;
        for (int i = 0;i < 3;i++) {
            row = new Object[]{"","","","",""};
            model.addRow(row);
            rowCounter++;
        }
    }


    public void addAdditionalElectives(ArrayList<Course> additionalElectives) {
//        Object[] row = new Object[]{""};
//        model.addRow(row);
//        model.setValueAt("Additional Electives (3 Credit Hours Minimum)",rowCounter,0);
//        rowCounter++;
        for (Course course: additionalElectives) {
            addCourse(course);
        }
    }

    private void setupLeveling(String[] levelingCourses) {
        Object[] row = new Object[]{"", "", "", "", "", ""};
        model.addRow(row);
        model.setValueAt("Admission Prerequisites",rowCounter,0);
        model.setValueAt("Course",rowCounter,1);
        model.setValueAt("UTD Semester",rowCounter,2);
        model.setValueAt("Waiver",rowCounter,3);
        model.setValueAt("Grade",rowCounter,4);
        rowCounter++;
        for (String course: levelingCourses) {
            courseMappings.put(course,rowCounter);

            row = new Object[]{"","","","",""};
            model.addRow(row);
            model.setValueAt(course,rowCounter,1);
            rowCounter++;
        }
    }



    public void addCourse(Course course) {


//      Columns:
//      0 = course name
//      1 = semester
//      3 = grade

//        if(courseMappings.containsKey(course.getCourseCode())) {
//            model.setValueAt(course.courseName, courseMappings.get(course.getCourseCode()), 0);
//            model.setValueAt(course.getCourseCode(),courseMappings.get(course.getCourseCode()),1);
//            model.setValueAt(course.getSemester(), courseMappings.get(course.getCourseCode()), 2);
//            //     model.setValueAt("Course Transfer", rowCounter, 3);
//            model.setValueAt(course.getGrade(), courseMappings.get(course.getCourseCode()), 4);
//            rowCounter++;
//        }

        Object[] row = new Object[]{"", "", "", "", "", ""};
        model.addRow(row);
        model.setValueAt(course.getCourseName(),rowCounter,0);
        model.setValueAt(course.getCourseCode(),rowCounter,1);
        model.setValueAt(course.getSemester(), rowCounter, 2);
        //     model.setValueAt("Course Transfer", rowCounter, 3);
        model.setValueAt(course.getGrade(), rowCounter, 4);
        rowCounter++;

    }
    public void addCourse(String courseName,String courseCode,String semester, String grade) {


//      Columns:
//      0 = course name
//      1 = semester
//      3 = grade

        Object[] row = new Object[]{"", "", "", "", "", ""};
        model.addRow(row);
        model.setValueAt(courseName, rowCounter, 0);
        model.setValueAt(courseCode,rowCounter,1);
        model.setValueAt(semester, rowCounter, 2);
        //  model.setValueAt("Course Transfer", rowCounter, 2);
        model.setValueAt(grade, rowCounter, 4);
        rowCounter++;

    }

    public void addSpanningRow(String value) {
        Object[] rowData = new Object[model.getColumnCount()];
        for (int i = 0; i < rowData.length; i++) {
            if (i == 0) {
                // First column should span all columns
                rowData[i] = value;
            } else {
                rowData[i] = "";
            }
        }
        model.addRow(rowData);
        int lastRow = model.getRowCount() - 1;
        int lastCol = model.getColumnCount() - 1;
        table.setRowHeight(lastRow,16 );
        table.getCellRenderer(lastRow, 0).getTableCellRendererComponent(table, value, false, false, lastRow, 0);
        for (int i = 1; i < rowData.length; i++) {
            table.setValueAt("", lastRow, i);
        }
        table.setValueAt(value, lastRow, 0);
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



     //   JButton closeButton = new JButton("Finish Editing and Save");





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

//    private class HeaderEditor extends DefaultCellEditor {
//        public HeaderEditor() {
//            super(new JTextField());
//            setClickCountToStart(1);
//        }
//
//        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
//            JTextField editor = (JTextField) super.getTableCellEditorComponent(table, value, isSelected, row, column);
//            editor.setFont(editor.getFont().deriveFont(Font.BOLD));
//            editor.setBorder(BorderFactory.createLineBorder(Color.black));
//            return editor;
//        }
//    }
}


