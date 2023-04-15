package main.java;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.io.IOException;

import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import java.io.File;
import java.util.stream.Collectors;

public class DegreePlanApp {
    private JFrame frame;
    private JComboBox<String> trackComboBox;
    // private JList<String> prerequisitesList;
    private JCheckBox fastTrackCheckBox;
    private JCheckBox thesisMastersCheckBox;

    private JPanel prerequisitesPanel;
    private File selectedTranscriptFile;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                DegreePlanApp window = new DegreePlanApp();
                window.frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public DegreePlanApp() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame("Degree Plan Application");
        frame.setBounds(100, 100, 800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout(0, 0));

        JPanel panel = new JPanel();
        frame.getContentPane().add(panel, BorderLayout.CENTER);
        panel.setLayout(new GridLayout(5, 2));

        // Add a new panel for the transcript file selection
        JPanel transcriptPanel = new JPanel(new GridLayout(1, 2));
        panel.add(new JLabel("Transcript File:"));
        JButton transcriptButton = new JButton("Select File");
        transcriptButton.addActionListener(new TranscriptButtonListener());
        transcriptPanel.add(transcriptButton);
        panel.add(transcriptPanel);

        // Track (Specialization) selection
        panel.add(new JLabel(""));
        panel.add(new JLabel("Track:"));
        trackComboBox = new JComboBox<>(new String[]{
                "Cyber Security",
                "Data Science",
                "Intelligent Systems",
                "Interactive Computing",
                "Networks and Telecommunications",
                "Systems",
                "Traditional Computer Science"
        });
        panel.add(trackComboBox);

        // Leveling courses/pre-requisites selection
        panel.add(new JLabel(""));
        panel.add(new JLabel("Leveling Courses/Pre-requisites:"));
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
        panel.add(prerequisitesScrollPane);

        // Fast Track to Masters selection
        panel.add(new JLabel(""));
        panel.add(new JLabel("Fast Track to Masters:"));
        fastTrackCheckBox = new JCheckBox();
        panel.add(fastTrackCheckBox);

        // Thesis Masters selection
        panel.add(new JLabel(""));
        panel.add(new JLabel("Thesis Masters:"));
        thesisMastersCheckBox = new JCheckBox();
        panel.add(thesisMastersCheckBox);

        // Submit button
        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(new SubmitButtonListener());
        frame.getContentPane().add(submitButton, BorderLayout.SOUTH);
    }

    private void createOutputDOCX(Student student, String track, List<String> prerequisites, boolean isFastTrack, boolean isThesisMasters) {
        try (XWPFDocument document = new XWPFDocument()) {
            // Add student info
            XWPFParagraph paragraph = document.createParagraph();
            XWPFRun run = paragraph.createRun();
            run.setBold(true);
            run.setFontSize(14);
            run.setText("Student Info:");
            run.addBreak();

            run = paragraph.createRun();
            run.setFontSize(12);
            run.setText("Name: " + student.getName());
            run.addBreak();
            run.setText("ID: " + student.getStudentID());
            run.addBreak();
            run.setText("Track: " + track);
            run.addBreak();
            run.setText("Plan: Master");

            // Print core GPA
            paragraph = document.createParagraph();
            run = paragraph.createRun();
            run.setBold(true);
            run.setFontSize(14);
            run.setText("Core GPA:");
            run.addBreak();

            run = paragraph.createRun();
            run.setFontSize(12);
            run.setText(Double.toString(student.calculateCoreGPA()));
            run.addBreak();

            // Print elective GPA
            paragraph = document.createParagraph();
            run = paragraph.createRun();
            run.setBold(true);
            run.setFontSize(14);
            run.setText("Elective GPA:");
            run.addBreak();

            run = paragraph.createRun();
            run.setFontSize(12);
            run.setText(Double.toString(student.calculateElectiveGPA()));
            run.addBreak();

            // Add core courses
            paragraph = document.createParagraph();
            run = paragraph.createRun();
            run.setBold(true);
            run.setFontSize(14);
            run.setText("Core Courses:");
            run.addBreak();

            run = paragraph.createRun();
            run.setFontSize(12);
            for (Course course : student.getCoreCourses()) {
                String[] courseCodeParts = course.getCourseCode().split("(?<=\\D)(?=\\d)");
                run.setText(courseCodeParts[0] + " " + courseCodeParts[1]);
                run.addBreak();
            }

            // Add elective courses
            paragraph = document.createParagraph();
            run = paragraph.createRun();
            run.setBold(true);
            run.setFontSize(14);
            run.setText("Elective Courses:");
            run.addBreak();

            run = paragraph.createRun();
            run.setFontSize(12);
            for (Course course : student.getElectiveCourses()) {
                String[] courseCodeParts = course.getCourseCode().split("(?<=\\D)(?=\\d)");
                run.setText(courseCodeParts[0] + " " + courseCodeParts[1]);
                run.addBreak();
            }

            paragraph = document.createParagraph();
            run = paragraph.createRun();
            run.setBold(true);
            run.setFontSize(14);
            run.setText("Pre-requisites and Leveling Courses:");
            run.addBreak();

            //  HashSet<String> uniqueCourses = new HashSet<>(prerequisites);
            // student.getLevelingCourses().stream().map(Course::getCourseCode).forEach(uniqueCourses::add);
            //  HashSet<String> uniqueCourses = new HashSet<>();
            HashSet<String> uniqueCourses = new HashSet<>();
            prerequisites.stream().map(courseCode -> courseCode.startsWith("CS ") ? courseCode.substring(3) : courseCode).map(courseCode -> "CS " + courseCode).forEach(uniqueCourses::add);
            student.getLevelingCourses().stream().map(Course::getCourseCode).map(courseCode -> courseCode.startsWith("CS ") ? courseCode.substring(3) : courseCode).map(courseCode -> "CS " + courseCode).forEach(uniqueCourses::add);

            for (String courseCode : uniqueCourses) {
                run = paragraph.createRun();
                run.setFontSize(12);
                run.setText("- " + courseCode);
                run.addBreak();
            }
            // Save the document
            JFileChooser fileChooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Word Document (*.docx)", "docx");
            fileChooser.setFileFilter(filter);

            int userSelection = fileChooser.showSaveDialog(frame);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToSave = fileChooser.getSelectedFile();
                if (!fileToSave.getAbsolutePath().endsWith(".docx")) {
                    fileToSave = new File(fileToSave.getAbsolutePath() + ".docx");
                }
                document.write(new FileOutputStream(fileToSave));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }}

    private class TranscriptButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(frame);
            if (result == JFileChooser.APPROVE_OPTION) {
                selectedTranscriptFile = fileChooser.getSelectedFile(); // Store the selected file in the instance variable
            }
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

            // Check if a transcript file has been selected before proceeding
            if (selectedTranscriptFile == null) {
                JOptionPane.showMessageDialog(frame, "Please select a transcript file first.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Get the student info
            Student student;
            try {
                student = PdfReader.processTranscript(selectedTranscriptFile.getAbsolutePath(), trackComboBox.getSelectedItem().toString(),
                        prerequisites, fastTrackCheckBox.isSelected(), thesisMastersCheckBox.isSelected());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            student.categorizeCoursesByTrack(track,prerequisites);

            // Generate the Word document
            createOutputDOCX(student, track, prerequisites, isFastTrack, isThesisMasters);
        }
    }
}






