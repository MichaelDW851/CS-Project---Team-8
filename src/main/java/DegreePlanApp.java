
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class DegreePlanApp {
    private Student student;
    private JFrame frame;
    private JComboBox<String> trackComboBox;
    // private JList<String> prerequisitesList;
    private JCheckBox fastTrackCheckBox;
    private JCheckBox thesisMastersCheckBox;

    private JPanel prerequisitesPanel;
    private String selectedTranscriptFile;

    private DegreePlanApp window;

    public static void main(String[] args) {
        DegreePlanApp degreePlanApp = new DegreePlanApp();
        degreePlanApp.initialize();

//
    }


    public DegreePlanApp() {

    }

    public DegreePlanApp(Student student) {
        this.student = student;
        options();
    }

    //On startup
    public void initialize(){
        window = new DegreePlanApp();
        Startup start = new Startup();
        // get filepath from startup
        try {
            selectedTranscriptFile = start.getFilePath(); // Store the selected file in the instance variable
        } catch (InterruptedException e) {
            // Handle the InterruptedException if necessary
            e.printStackTrace();
        }

        //OpenOptions();
        options();


    }

    private void options() {

        frame = new JFrame("Degree Plan Application");
        frame.setLocationRelativeTo(null); // Center the frame on the screen
        frame.setVisible(true);
        frame.setBounds(100, 100, 800, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout(0, 0));

        JPanel panel = new JPanel();
        frame.getContentPane().add(panel, BorderLayout.CENTER);
        panel.setLayout(new GridBagLayout()); // use GridBagLayout to customize component positions

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(5, 5, 5, 5); // add some padding between components

        // Track (Specialization) selection
        c.gridx = 0;
        c.gridy = 0;
        panel.add(new JLabel("Track:"), c);

        c.gridx = 1;
        c.gridy = 0;
        trackComboBox = new JComboBox<>(new String[]{
                " ",
                "Cyber Security",
                "Data Science",
                "Intelligent Systems",
                "Interactive Computing",
                "Networks and Telecommunications",
                "Systems",
                "Traditional Computer Science"
        });
        panel.add(trackComboBox, c);

        // Leveling courses/pre-requisites selection
        c.gridx = 0;
        c.gridy = 1;
        panel.add(new JLabel("Leveling Courses/Pre-requisites:"), c);

        c.gridx = 1;
        c.gridy = 1;
        String[] prerequisitesArr = new String[]{
                "CS 3341 Probability & Statistics in CS and SE",
                "CS 5303 Computer Science I",
                "CS 5330 Computer Science II",
                "CS 5333 Discrete Structures",
                "CS 5343 Algorithm Analysis & Data Structures",
                "CS 5348 Operating System Concepts",
                "CS 5349 Automata Theory",
                "CS 5354 Software Engineering",
                "CS 5390 Computer Networks"
        };

        prerequisitesPanel = new JPanel(new GridLayout(0, 1)); // Remove JPanel from the beginning of this line
        for (String prerequisite : prerequisitesArr) {
            JCheckBox checkBox = new JCheckBox(prerequisite);
            checkBox.setActionCommand(prerequisite);
            prerequisitesPanel.add(checkBox);
        }

        JScrollPane prerequisitesScrollPane = new JScrollPane(prerequisitesPanel);
        panel.add(prerequisitesScrollPane, c);

        // Fast Track to Masters selection
        c.gridx = 0;
        c.gridy = 2;
        panel.add(new JLabel("Fast Track to Masters:"), c);

        c.gridx = 1;
        c.gridy = 2;
        fastTrackCheckBox = new JCheckBox();
        panel.add(fastTrackCheckBox, c);

        // Thesis Masters selection
        c.gridx = 0;
        c.gridy = 3;
        panel.add(new JLabel("Thesis Masters:"), c);

        c.gridx = 1;
        c.gridy = 3;
        thesisMastersCheckBox = new JCheckBox();
        panel.add(thesisMastersCheckBox, c);

        // Submit button
        c.gridx = 1;
        c.gridy = 4;
        c.anchor = GridBagConstraints.LINE_END; // align the button to the right side of the window
        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(new SubmitButtonListener());
        panel.add(submitButton, c);

//get student info
        Student student;
        List<String> prerequisites = new ArrayList<>();
        try {
            student = PdfReader.processTranscript(selectedTranscriptFile, trackComboBox.getSelectedItem().toString(),
                    prerequisites, fastTrackCheckBox.isSelected(), thesisMastersCheckBox.isSelected());

            c.gridx = 4;
            c.gridy = 6;
            panel.add(new JLabel("Student name: " + student.name),c);

            c.gridx = 4;
            c.gridy = 8;
            panel.add(new JLabel("Student id: " + student.getStudentID()),c);

            this.student = student;
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        //add student name and id so user knows which student whose transcript they're doing audit for


        frame.pack();
    }

    private void createOutputDOCX(Student student, String track, List<String> prerequisites, boolean isFastTrack, boolean isThesisMasters) {

        try (XWPFDocument document = new XWPFDocument()) {
// Title
            XWPFParagraph titleParagraph = document.createParagraph();
            titleParagraph.setAlignment(ParagraphAlignment.CENTER);
            XWPFRun titleRun = titleParagraph.createRun();
            titleRun.setBold(true);
            titleRun.setFontSize(16);
            titleRun.setText("Audit Report");
            titleRun.addBreak();

// Name and ID
            XWPFParagraph nameIdParagraph = document.createParagraph();
            XWPFRun nameIdRun = nameIdParagraph.createRun();
            nameIdRun.setFontSize(12);
            nameIdRun.setText("Name: " + student.getName());
            nameIdRun.addTab();
            nameIdRun.addTab();
            nameIdRun.addTab();
            nameIdRun.addTab();
            nameIdRun.addTab();
            nameIdRun.addTab();
            nameIdRun.setText("ID: " + student.getStudentID());


// Plan and Major
            XWPFParagraph planMajorParagraph = document.createParagraph();
            XWPFRun planMajorRun = planMajorParagraph.createRun();
            planMajorRun.setFontSize(12);
            planMajorRun.setText("Plan: Master");
            planMajorRun.addTab();
            planMajorRun.addTab();
            planMajorRun.addTab();
            planMajorRun.addTab();
            planMajorRun.addTab();
            planMajorRun.addTab();
            planMajorRun.addTab();
            planMajorRun.setText("Major: " + student.getMajor());


            XWPFParagraph trackParagraph = document.createParagraph();
            //   trackParagraph.setAlignment(ParagraphAlignment.RIGHT);
            XWPFRun trackRun = trackParagraph.createRun();
            trackRun.setFontSize(12);
            trackRun.addTab();
            trackRun.addTab();
            trackRun.addTab();
            trackRun.addTab();
            trackRun.addTab();
            trackRun.addTab();
            trackRun.addTab();
            trackRun.addTab();
            trackRun.setText("Track: " + track);
            trackRun.addBreak();

            // Core GPA
            XWPFParagraph coreGPAParagraph = document.createParagraph();
            XWPFRun coreGPARun = coreGPAParagraph.createRun();
            coreGPARun.setFontSize(12);
            coreGPARun.setText("Core GPA: ");
            coreGPARun.setText(String.format("%.3f", student.calculateCoreGPA()));



            // Elective GPA
            XWPFParagraph electiveGPAParagraph = document.createParagraph();
            XWPFRun electiveGPARun = electiveGPAParagraph.createRun();
            electiveGPARun.setFontSize(12);
            electiveGPARun.setText("Elective GPA: ");
            electiveGPARun.setText(String.format("%.3f", student.calculateElectiveGPA()));



            // Combined GPA
            XWPFParagraph combinedGPAParagraph = document.createParagraph();
            XWPFRun combinedGPARun = combinedGPAParagraph.createRun();
            combinedGPARun.setFontSize(12);
            combinedGPARun.setText("Combined GPA: ");
            combinedGPARun.setText(String.format("%.3f", student.calculateOverallGPA()));

            // Core Courses
            XWPFParagraph coreCoursesParagraph = document.createParagraph();
            XWPFRun coreCoursesRun = coreCoursesParagraph.createRun();
            coreCoursesRun.setFontSize(12);
            coreCoursesRun.setText("Core Courses: ");
            List<Course> coreCourses = student.getCoreCourses();
            for (int i = 0; i < coreCourses.size(); i++) {
                Course course = coreCourses.get(i);
                //(?<=\\D) positive lookbehind assertion, matches a position in the string immediately preceeded by a non-digit
                //(?=\\d) mathches a position in the string immediately followed by a digit
                String[] courseCodeParts = course.getCourseCode().split("(?<=\\D)(?=\\d)");
                coreCoursesRun.setText(courseCodeParts[0] + " " + courseCodeParts[1]);
                if (i < coreCourses.size() - 1) {
                    coreCoursesRun.setText(", "); // add a coma between courses
                }
            }
            // coreCoursesRun.addBreak();

            // Elective Courses, same thing as core courses
            XWPFParagraph electiveCoursesParagraph = document.createParagraph();
            XWPFRun electiveCoursesRun = electiveCoursesParagraph.createRun();
            electiveCoursesRun.setFontSize(12);
            electiveCoursesRun.setText("Elective Courses: ");
            List<Course> electiveCourses = student.getElectiveCourses();
            for (int i = 0; i < electiveCourses.size(); i++) {
                Course course = electiveCourses.get(i);
                String[] courseCodeParts = course.getCourseCode().split("(?<=\\D)(?=\\d)");
                electiveCoursesRun.setText(courseCodeParts[0] + " " + courseCodeParts[1]);
                if (i < electiveCourses.size() - 1) {
                    electiveCoursesRun.setText(", ");
                }
            }
            electiveCoursesRun.addBreak();

//
 //Leveling Courses and Pre-requisites
            XWPFParagraph levelingCoursesParagraph = document.createParagraph();
            XWPFRun levelingCoursesRun = levelingCoursesParagraph.createRun();
            levelingCoursesRun.setFontSize(12);
            levelingCoursesRun.setText("Leveling Courses and Pre-requisites from Admissions Letter:");
            levelingCoursesRun.addBreak();
            //Create a hashset to store unique courses, and add pre-reqs and leveling courses to it
            HashSet<String> uniqueCourses = new HashSet<>();
            for (String courseCode : prerequisites) {
                if (!courseCode.startsWith("CS")) {
                    courseCode = "CS" + courseCode; //sometimes they didnt have a CS for somereason so we added it
                }
                uniqueCourses.add(courseCode);
            }
            for (Course course : student.getLevelingCourses()) {
                String courseCode = course.getCourseCode();
                if (!courseCode.startsWith("CS")) {
                    courseCode = "CS" + courseCode;
                }
                uniqueCourses.add(courseCode);
            }
            //Loop through unique courses and display them in the paragraph
            for (String courseCode : uniqueCourses) {
                levelingCoursesRun = levelingCoursesParagraph.createRun();
                levelingCoursesRun.setFontSize(12);
                levelingCoursesRun.setText("- " + courseCode);
                levelingCoursesRun.addBreak();
            }

// Outstanding Requirements
            XWPFParagraph outstandingRequirementsParagraph = document.createParagraph();
            XWPFRun outstandingRequirementsRun = outstandingRequirementsParagraph.createRun();
            outstandingRequirementsRun.setFontSize(12);
            outstandingRequirementsRun.setText("Outstanding Requirements:");

            //Split the remaining courses message into two parts at the colon
            //(?<=) mathces a postion immediately preceded by a colon.
            String[] remainingMessageParts = student.getRemainingCoursesMessage().split("(?<=:)");
            //create a new pargraph for displaying the first part
            XWPFParagraph remainingCoursesParagraph = document.createParagraph();
            //create a new run to display first part of remaining courses
            XWPFRun remainingCoursesRun = remainingCoursesParagraph.createRun();
            remainingCoursesRun.setText(remainingMessageParts[0]);
            remainingCoursesRun.setFontSize(12);
            remainingCoursesRun.setBold(false);
            //create new paragraph for displaying the first part of remaining courses message
            XWPFParagraph remainingCoursesListParagraph = document.createParagraph();
            //create new run for for displaying second part
            XWPFRun remainingCoursesListRun = remainingCoursesListParagraph.createRun();
            //if there is a second part to remaining courses, display it and add line break
            if (remainingMessageParts.length > 1) {
                remainingCoursesListRun.setText(remainingMessageParts[1]);
                remainingCoursesListRun.addBreak();
            }
            remainingCoursesListRun.setFontSize(12);
            remainingCoursesListRun.setBold(false);
            remainingCoursesListRun.addBreak();


            double desiredOverallGPA = 3.0;
            //Get remaining overall GPA message
            String overallGPAMessage = student.getRemainingOverallGPAMessage(desiredOverallGPA);
            //split at  the colon
            String[] overallGPAMessageParts = overallGPAMessage.split("(?<=:)");
            XWPFParagraph overallGPAParagraph = document.createParagraph();
            XWPFRun overallGPARun = overallGPAParagraph.createRun();
            overallGPARun.setText(overallGPAMessageParts[0]);
            overallGPARun.setFontSize(12);
            overallGPARun.setBold(false);


            XWPFParagraph overallGPAListParagraph = document.createParagraph();
            XWPFRun overallGPAListRun = overallGPAListParagraph.createRun();

            //If there is a second part to remaining overall GPA message, display it
            //otherwise, empty string
            if (overallGPAMessageParts.length > 1) {
                overallGPAListRun.setText(overallGPAMessageParts[1]);
            } else {
                overallGPAListRun.setText(""); // Set an empty string or any other default text
            }

            overallGPAListRun.setFontSize(12);
            overallGPAListRun.setBold(false);
            overallGPAListRun.addBreak();

            //Save the document
            JFileChooser fileChooser = new JFileChooser();
            //create a filter for the save dialog to only show .docx files
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Word Document (*.docx)", "docx");
            fileChooser.setFileFilter(filter);
            //show the save dialog
            int userSelection = fileChooser.showSaveDialog(frame);
            //if user selected a file and clicked save
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                //save selected file
                File fileToSave = fileChooser.getSelectedFile();
                //if file doesnt end in docx, add it
                if (!fileToSave.getAbsolutePath().endsWith(".docx")) {
                    fileToSave = new File(fileToSave.getAbsolutePath() + ".docx");
                }
                //write document to selected file
                document.write(new FileOutputStream(fileToSave));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    // In the DegreePlanApp class, call the processTranscript method in the SubmitButtonListener
    private class SubmitButtonListener implements ActionListener {


        @Override
        public void actionPerformed(ActionEvent e) {
            String track = (String) trackComboBox.getSelectedItem();

            List<String> prerequisites = new ArrayList<>();
            for (Component component : prerequisitesPanel.getComponents()) {
                if (component instanceof JCheckBox) {
                    JCheckBox checkBox = (JCheckBox) component;
                    if (checkBox.isSelected()) {
                        String courseCode = checkBox.getActionCommand();
                        prerequisites.add(courseCode);
                    }
                }
            }

            boolean isFastTrack = fastTrackCheckBox.isSelected();
            boolean isThesisMasters = thesisMastersCheckBox.isSelected();
            if (isFastTrack) {
                student.setFastTrack();
            }

            if (isThesisMasters) {
                student.setThesisMasters();
            }
            // Get the student info
            Student student;
            try {
                student = PdfReader.processTranscript(selectedTranscriptFile, trackComboBox.getSelectedItem().toString(),
                        prerequisites, fastTrackCheckBox.isSelected(), thesisMastersCheckBox.isSelected());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            student.categorizeCoursesByTrack(track,prerequisites);
            GenerateDegreePlan degreePlan = new GenerateDegreePlan(student);

            // Generate the Word document
            int dialogResult = JOptionPane.showConfirmDialog(null, "Save Audit Report?", "Confirmation", JOptionPane.YES_NO_OPTION);
            boolean generateWordDocument = (dialogResult == JOptionPane.YES_OPTION);

            if (generateWordDocument) {
                createOutputDOCX(student, track, prerequisites, isFastTrack, isThesisMasters);
            }

            frame.setVisible(false);
        }
    }


}





