import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.PdfPTable;
//import com.itextpdf.text.Font;



import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.Font;
import java.awt.event.*;
import java.io.FileOutputStream;
import java.util.ArrayList;
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

    Map<String, Integer> coursePositionMappings;
    Map<String,String> couseNameMappings;

    private Student student;
    private String studentName;
    private String studentID;
    private String semesterAdmitted;
    private boolean fastTrack = false;
    private boolean thesis = false;

    private String anticipatedGrad;
    private int rowCounter = 0;

    private int defaultRowHeight = 16;

    public void setCourseNames() {

    }

    //Sets up the blank degree plan for each respective track
    public void setupByTrack(String track) {
        //Going to use a map to determine where each course listing is
        //First int is the row
        String[] coreCoursesCodes;
        String[] levelingCoursesCodes;
        String[] trackElectivesCodes;

        String[] coreCoursesNames;
        String[] levelingCoursesNames;
        String[] trackElectivesNames;

        coursePositionMappings = new HashMap<>();
        couseNameMappings = new HashMap<>();

        ArrayList<String> approvedElectives = new ArrayList<>();
        ArrayList<String> additionalElectives = new ArrayList<>();

        switch (track) {

            case "Data Science":
                coreCoursesCodes = new String[]{"CS6313", "CS6350", "CS6363", "CS6375"}; // Add core courses for this track
                coreCoursesNames = new String[]{"Statistical Methods for Data Sciences","Big Data Management and Analytics","Design and Analysis of Computer Algorithms","Machine Learning"};
                setupCores(coreCoursesCodes,coreCoursesNames);

                trackElectivesCodes =new String[]{"CS6301", "CS6320", "CS6327", "CS6347", "CS6360"};
                trackElectivesNames = new String[]{"Social Network Analytics","Natural Language Processing","Video Analytics","Statistics for Machine Learning","Database Design"};
                setupTrackElectives(trackElectivesCodes,trackElectivesNames);

                setupApprovedElectives();
                setupAdditionalElectives();

                levelingCoursesCodes =new String[]{"CS5303", "CS5330", "CS5333", "CS5343", "CS5348", "CS3341"};
                levelingCoursesNames = new String[]{"Computer Science I","Computer Science II","Discrete Structures","Algorithm Analysis & Data Structures","Operating System Concepts","Probability & Statistics in CS"};
                setupLeveling(levelingCoursesCodes,levelingCoursesNames);
                break;
            // Add cases for other tracks and their respective core and elective courses
            case "Systems":

                coreCoursesCodes = new String[]{"CS6304", "CS6363", "CS6378", "CS6396"};
                coreCoursesNames = new String[]{"Computer Architecture","Design and Analysis of Computer Algorithms","Advanced Operating Systems","Real-Time Systems"};
                setupCores(coreCoursesCodes, coreCoursesNames);

                trackElectivesCodes = new String[]{"CS6349","CS6376","CS6380","CS6397","CS6399"};
                trackElectivesNames = new String[]{"Network Security","Parallel Processing","Parallel Processing","Synthesis and Opt. of High-Perf. Systems","Parallel Architectures and Systems"};
                setupTrackElectives(trackElectivesCodes, trackElectivesNames);

                setupApprovedElectives();
                setupAdditionalElectives();

                levelingCoursesCodes = new String[]{"CS5303","CS5330","CS5333","CS5343","CS5348","CS5390"};
                levelingCoursesNames = new String[]{"Computer Science I","Computer Science II","Discrete Structures","Algorithm Analysis & Data Structures","Operating System Concepts","Computer Networks **"};
                setupLeveling(levelingCoursesCodes, levelingCoursesNames);
                break;
            case "Interactive Computing":
                coreCoursesCodes = new String[]{"CS6326", "CS6363"};
                coreCoursesNames = new String[]{"Human Computer Interaction","Design and Analysis of Computer Algorithms"};

                setupCores(coreCoursesCodes, coreCoursesNames);

                trackElectivesCodes = new String[]{"CS6323", "CS6328", "CS6331", "CS6334", "CS6366"};
                trackElectivesNames = new String[]{"Computer Animation and Gaming","Modeling and Simulation","Multimedia Systems","Virtual Reality","Computer Graphics"};
                setupTrackElectives(trackElectivesCodes, trackElectivesNames);

                setupApprovedElectives();
                setupAdditionalElectives();

                levelingCoursesCodes = new String[]{"CS5303", "CS5330", "CS5333", "CS5343", "CS5348"};
                levelingCoursesNames = new String[]{"Computer Science I","Computer Science II","Discrete Structures","Algorithm Analysis & Data Structures","Operating System Concepts"};
                setupLeveling(levelingCoursesCodes, levelingCoursesNames);
                break;
            case "Cyber Security":
                coreCoursesCodes = new String[]{"CS6324", "CS6363", "CS6378"};
                coreCoursesNames = new String[]{"Information Security","Design and Analysis of Computer Algorithms","Advanced Operating Systems"};

                setupCores(coreCoursesCodes, coreCoursesNames);

                trackElectivesCodes = new String[]{"CS6332", "CS6348", "CS6349", "CS6377"};
                trackElectivesNames = new String[]{"System Security & Malicious Code Analysis","Data and Applications Security","Network Security","Introduction To Cryptography"};
                setupTrackElectives(trackElectivesCodes, trackElectivesNames);

                setupApprovedElectives();
                setupAdditionalElectives();

                levelingCoursesCodes =new String[]{"CS5303", "CS5330", "CS5333", "CS5343", "CS5348", "CS5390"};
                levelingCoursesNames = new String[]{"Computer Science I","Computer Science II","Discrete Structures","Algorithm Analysis & Data Structures","Operating System Concepts","Computer Networks"};
                setupLeveling(levelingCoursesCodes, levelingCoursesNames);
                break;
            case "Intelligent Systems":
                coreCoursesCodes = new String[]{"CS6320", "CS6363", "CS6364", "CS6375"};
                coreCoursesNames = new String[]{"Natural Language Processing","Design and Analysis of Computer Algorithms","Artificial Intelligence","Machine Learning"};
                setupCores(coreCoursesCodes, coreCoursesNames);

                trackElectivesCodes = new String[]{"CS6360", "CS6378"};
                trackElectivesNames = new String[]{"Database Design","Advanced Operating Systems"};
                setupTrackElectives(trackElectivesCodes, trackElectivesNames);

                setupApprovedElectives();
                setupAdditionalElectives();

                levelingCoursesCodes =new String[]{"CS5303", "CS5330", "CS5343", "CS5333", "CS5348"};
                levelingCoursesNames = new String[]{"Computer Science I","Computer Science II","Discrete Structures","Algorithm Analysis & Data Structures","Operating System Concepts"};
                setupLeveling(levelingCoursesCodes, levelingCoursesNames);
                break;

            case "Networks and Telecommunications":
                coreCoursesCodes = new String[]{"CS6352", "CS6363", "CS6378", "CS6385", "CS6390"};
                coreCoursesNames = new String[]{"Perf. of Computer Systems and Networks","Design and Analysis of Computer Algorithms","Advanced Operating Systems","Algorithmic Aspects of Telecomm. Networks","Advanced Computer Networks"};
                setupCores(coreCoursesCodes, coreCoursesNames);

                setupApprovedElectives();
                setupAdditionalElectives();

                levelingCoursesCodes =new String[]{"CS5303", "CS5330", "CS5333", "CS5343", "CS5348", "CS5390", "CS3341"};
                levelingCoursesNames = new String[]{"Computer Science I","Computer Science II","Discrete Structures","Algorithm Analysis & Data Structures","Operating System Concepts","Computer Networks",""};
                setupLeveling(levelingCoursesCodes, levelingCoursesNames);
                break;
            case "Traditional Computer Science":
                coreCoursesCodes = new String[]{"CS6363", "CS6378", "CS6390"};
                coreCoursesNames = new String[]{"Design and Analysis of Computer Algorithms","Advanced Operating Systems","Advanced Computer Networks"};
                setupCores(coreCoursesCodes, coreCoursesNames);

                trackElectivesCodes = new String[]{"CS6353", "CS6360", "CS6371"};
                trackElectivesNames = new String[]{"Compiler Construction","Database Design","Advanced Programming Languages"};
                setupTrackElectives(trackElectivesCodes, trackElectivesNames);

                setupApprovedElectives();
                setupAdditionalElectives();

                levelingCoursesCodes =new String[]{"CS5303", "CS5330", "CS5333", "CS5343", "CS5348", "CS5349", "CS5390"};
                levelingCoursesNames = new String[]{"Computer Science I","Computer Science II","Discrete Structures","Algorithm Analysis & Data Structures","Operating System Concepts","Automata Theory","Computer Networks"};
                setupLeveling(levelingCoursesCodes, levelingCoursesNames);

        }


    }





    private void setupCores(String[] coreCoursesCodes, String[] coresNames) {
        Object[] row = new Object[]{"","",""};
        model.addRow(row);
        model.setValueAt("Core Courses",rowCounter,0);
        model.setValueAt("(15 Credit Hours)",rowCounter,1);
        model.setValueAt("3.19 Grade Point Average Required",rowCounter,2);
        rowCounter++;
        for (int i = 0;i < coresNames.length;i++) {
            coursePositionMappings.put(coreCoursesCodes[i],rowCounter);

            row = new Object[]{"","","","",""};
            model.addRow(row);
            model.setValueAt(coresNames[i],rowCounter,0);
            model.setValueAt(coreCoursesCodes[i],rowCounter,1);
            rowCounter++;
        }
    }

    public void addCores(ArrayList<Course> cores) {
        for (Course course: cores) {
            addCourse(course);
        }
    }







    private void setupTrackElectives(String[] trackElectivesCodes, String[] trackElectives) {
        Object[] row = new Object[]{""};
        model.addRow(row);
        model.setValueAt("of The Following Required",rowCounter,0);
        rowCounter++;
        for (int i = 0;i < trackElectives.length;i++) {
            coursePositionMappings.put(trackElectivesCodes[i],rowCounter);
            row = new Object[]{"","","","",""};
            model.addRow(row);
            model.setValueAt(trackElectives[i],rowCounter,0);
            model.setValueAt(trackElectivesCodes[i],rowCounter,1);
            rowCounter++;
        }
    }


    public void addTrackElectives(ArrayList<Course> trackElectives) {

        for (Course course: trackElectives) {
            addCourse(course);
        }

    }

    private void setupApprovedElectives() {
        Object[] row = new Object[]{ "", "", ""};

        model.addRow(row);
        model.setValueAt("FIVE APPROVED 6000 LEVEL ELECTIVES",rowCounter,0);
        model.setValueAt("(15* Credit Hours)",rowCounter,1);
        model.setValueAt("3.0 Grade Point Average",rowCounter,2);
        rowCounter++;
        approvedElectivesRow = rowCounter;
        for(int i = 0;i < 5;i++) {


            row = new Object[]{"","","","",""};
            model.addRow(row);
            rowCounter++;
        }


    }

    //columns:
    //0 = course name
    //1 = course code
    //2 = semester
    //3 = transfer
    //4 = grade
    public void addApprovedElectives(ArrayList<Course> approvedElectives) {

        for (int i = 0;i < approvedElectives.size();i++) {

            Course currentCourse = approvedElectives.get(i);

            if(Integer.parseInt(currentCourse.getCourseCode().substring(2,3)) >= 6) {
                model.setValueAt(currentCourse.getCourseName(),approvedElectivesRow+i,0);
                model.setValueAt(currentCourse.getCourseCode(),approvedElectivesRow+i,1);
                model.setValueAt(currentCourse.getSemester(),approvedElectivesRow+i,2);
                model.setValueAt(currentCourse.getGrade(),approvedElectivesRow+i,4);
            }
            else {
                addAdditionalElective(currentCourse);
            }

        }
    }


    private void setupAdditionalElectives() {
        Object[] row = new Object[]{""};
        model.addRow(row);
        model.setValueAt("Additional Electives (3 Credit Hours Minimum)",rowCounter,0);
        rowCounter++;
        additionalElectivesRow = rowCounter;
        for (int i = 0;i < 3;i++) {
            row = new Object[]{"","","","",""};
            model.addRow(row);
            rowCounter++;
        }
    }


    public void addAdditionalElective(Course elective) {
//        Object[] row = new Object[]{""};
//        model.addRow(row);
//        model.setValueAt("Additional Electives (3 Credit Hours Minimum)",rowCounter,0);
//        rowCounter++;
        model.setValueAt(elective.getCourseName(),additionalElectivesRow,0);
        model.setValueAt(elective.getCourseCode(),additionalElectivesRow,1);
        model.setValueAt(elective.getSemester(),additionalElectivesRow,2);
        model.setValueAt(elective.getGrade(),additionalElectivesRow,4);
        additionalElectivesRow++;

    }

    private void setupLeveling(String[] levelingCoursesCodes, String[] levelingCourses) {
        Object[] row = new Object[]{"", "", "", "", "", ""};
        model.addRow(row);
        model.setValueAt("Admission Prerequisites",rowCounter,0);
        model.setValueAt("Course",rowCounter,1);
        model.setValueAt("UTD Semester",rowCounter,2);
        model.setValueAt("Waiver",rowCounter,3);
        model.setValueAt("Grade",rowCounter,4);
        rowCounter++;
        for (int i=0;i < levelingCourses.length;i++) {
            coursePositionMappings.put(levelingCoursesCodes[i],rowCounter);

            row = new Object[]{"","","","",""};
            model.addRow(row);
            model.setValueAt(levelingCourses[i],rowCounter,0);
            model.setValueAt(levelingCoursesCodes[i],rowCounter,1);
            rowCounter++;
        }
    }



    public void addCourse(Course course) {

//      Columns:
//      0 = course name
//      1 = course code
//      2 = semester
//      4 = grade

        String courseCode = course.getCourseCode();

        if(coursePositionMappings.containsKey(courseCode)) {
            model.setValueAt(course.getSemester(),coursePositionMappings.get(courseCode),2);
            model.setValueAt(course.getGrade(),coursePositionMappings.get(courseCode),4);
        }


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




    public SpreadsheetUI(String studentName,String studentID,String semesterAdmitted,boolean isFastTrack, boolean isThesis,String track) {

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

        JLabel trackLabel = new JLabel(track,SwingConstants.CENTER);
        trackLabel.setFont(new Font("Arial", Font.BOLD,24));

        JPanel titlePanel = new JPanel(new GridLayout(4, 1));
        titlePanel.add(titleLabel1);
        titlePanel.add(titleLabel2);
        titlePanel.add(titleLabel3);
        titlePanel.add(trackLabel);
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








        this.studentName =studentName;
        this.studentID = studentID;
        this.semesterAdmitted = semesterAdmitted;
        this.fastTrack = isFastTrack;
        this.thesis = isThesis;
        setupByTrack(track);
        addFields(studentName,studentID,semesterAdmitted,fastTrack,thesis);


        //adding separate fields for name, fast track, thesis, etc



        //Buttons

//        JPanel buttonPanel = new JPanel(new BorderLayout());
//        JButton addButton = new JButton("Add Row");
//        JButton saveAsPdfButton = new JButton("Save as PDF");
//        buttonPanel.add(addButton,BorderLayout.NORTH);
//        buttonPanel.add(saveAsPdfButton, BorderLayout.NORTH);
//
//        //  buttonPanel.add(closeButton, BorderLayout.NORTH);
//        add(buttonPanel, BorderLayout.EAST);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));


        JButton addButton = new JButton("Add Row");
        JButton saveAsPdfButton = new JButton("Save as PDF");

        buttonPanel.add(saveAsPdfButton);
        buttonPanel.add(addButton);
        add(buttonPanel, BorderLayout.EAST);



        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // create a dialog or frame to get input from the user
                // for example:
                Object[] row = {"", "", "", "", ""};
                model.addRow(row);

            }
        });


        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    int row = table.rowAtPoint(e.getPoint());
                    if (row >= 0 && table.getSelectedRow() == row) {
                        JPopupMenu popupMenu = new JPopupMenu();

                        JMenuItem deleteMenuItem = new JMenuItem("Delete Row");
                        deleteMenuItem.addActionListener((event) -> {
                            model.removeRow(row);
                        });

                        JMenuItem headerMenuItem = new JMenuItem("Set as Header");
                        headerMenuItem.addActionListener((event) -> {
                            Object[] headerRow = {"","","","","oefkosfeos"};
//                            for (int i = 0; i < model.getColumnCount(); i++) {
//                                headerRow[i] = model.getValueAt(row, i);
//                            }
                            model.removeRow(row);
                            model.insertRow(row, headerRow);
                            table.setRowSelectionInterval(row, row);



                        });

                        popupMenu.add(deleteMenuItem);
                        popupMenu.add(headerMenuItem);
                        popupMenu.show(table, e.getX(), e.getY());
                    }
                }
            }
        });


        saveAsPdfButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showSaveDialog(SpreadsheetUI.this);
            if (result == JFileChooser.APPROVE_OPTION) {
                try {
                    for (int rows = 0; rows < table.getRowCount(); rows++) {
                        for (int cols = 0; cols < table.getColumnCount(); cols++) {
                            Object cellValue = table.getValueAt(rows, cols);
                            if (cellValue == null) {
                                // assign default value or handle null value
                                table.setValueAt("",rows,cols);
                            }
                        }
                    }
                    Document document = new Document();
                    PdfWriter.getInstance(document, new FileOutputStream(fileChooser.getSelectedFile() + ".pdf"));
                    document.open();
                    PdfPTable pdfTable = new PdfPTable(table.getColumnCount());
                    pdfTable.setWidthPercentage(100);

                    Paragraph paragraph = new Paragraph("Degree Plan");
                    paragraph.add("\n");
                    paragraph.add("University of Texas at Dallas");
                    paragraph.add("\nMaster of Computer Science");
                    paragraph.add("\n" + track );
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

    private class HeaderEditor extends DefaultCellEditor {
        public HeaderEditor() {
            super(new JTextField());
            setClickCountToStart(1);
        }

        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            JTextField editor = (JTextField) super.getTableCellEditorComponent(table, value, isSelected, row, column);
            editor.setFont(editor.getFont().deriveFont(Font.BOLD));
            editor.setBorder(BorderFactory.createLineBorder(Color.black));
            return editor;
        }
    }
}


