package main.java;
//test commit
//test commit again
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DegreePlanApp {
    private JFrame frame;
    private JComboBox<String> trackComboBox;
    // private JList<String> prerequisitesList;
    private JCheckBox fastTrackCheckBox;
    private JCheckBox thesisMastersCheckBox;

    private JPanel prerequisitesPanel;

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
        panel.setLayout(new GridLayout(4, 2));

        // Track (Specialization) selection
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
        panel.add(new JLabel("Fast Track to Masters:"));
        fastTrackCheckBox = new JCheckBox();
        panel.add(fastTrackCheckBox);

        // Thesis Masters selection
        panel.add(new JLabel("Thesis Masters:"));
        thesisMastersCheckBox = new JCheckBox();
        panel.add(thesisMastersCheckBox);

        // Submit button
        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(new SubmitButtonListener());
        frame.getContentPane().add(submitButton, BorderLayout.SOUTH);
    }

    private void createOutputPDF(Student student, String track, List<String> prerequisites, boolean isFastTrack, boolean isThesisMasters) {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
                contentStream.newLineAtOffset(50, 750);
                contentStream.showText("User Inputs:");
                contentStream.endText();

                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA, 12);
                contentStream.newLineAtOffset(50, 730);
                contentStream.showText("Track: " + track);
                contentStream.newLineAtOffset(0, -20);
                contentStream.showText("Prerequisites:");
                contentStream.newLineAtOffset(0, -20);

                for (String prerequisite : prerequisites) {
                    contentStream.showText("- " + prerequisite);
                    contentStream.newLineAtOffset(0, -20);
                }

                contentStream.showText("Fast Track to Masters: " + (isFastTrack ? "Yes" : "No"));
                contentStream.newLineAtOffset(0, -20);
                contentStream.showText("Thesis Masters: " + (isThesisMasters ? "Yes" : "No"));
                contentStream.endText();

                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
                contentStream.newLineAtOffset(50, 400);
                contentStream.showText("Student Info:");
                contentStream.endText();

                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA, 12);
                contentStream.newLineAtOffset(50, 380);
                contentStream.showText("Name: " + student.getName());
                contentStream.newLineAtOffset(0, -20);
                contentStream.showText("ID: " + student.getStudentID());
                contentStream.newLineAtOffset(0, -20);
                contentStream.showText("CoursesYUP:");
                contentStream.newLineAtOffset(0, -20);

                for (Course course : student.getCourses()) {
                    contentStream.showText("- " + course.getCourseCode() + " " + course.getCourseName() + " (" + course.getGrade() + ")");
                    contentStream.newLineAtOffset(0, -20);
                }

                contentStream.endText();
            }

            document.save(new File("output.pdf"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
   private void docx(Student student, String track, List<String> prerequisites, boolean isFastTrack, boolean isThesisMasters) throws IOException {
       XWPFDocument docx = new XWPFDocument();
       XWPFParagraph title = docx.createParagraph();
       XWPFRun run = title.createRun();
       run.setText("Degree Plan for " + student.getName());

       XWPFParagraph userInputTitle = docx.createParagraph();
       XWPFRun userInputRun = userInputTitle.createRun();
       userInputRun.setBold(true);
       userInputRun.setText("User Inputs:");
       userInputRun.addBreak();
       userInputRun.setText("Track: " + track);
       userInputRun.addBreak();
       userInputRun.setText("Prerequisites:");
       userInputRun.addBreak();
       for (String prerequisite : prerequisites) {
           userInputRun.setText("- " + prerequisite);
           userInputRun.addBreak();
       }
       userInputRun.setText("Fast Track to Masters: " + (isFastTrack ? "Yes" : "No"));
       userInputRun.addBreak();
       userInputRun.setText("Thesis Masters: " + (isThesisMasters ? "Yes" : "No"));
       userInputRun.addBreak();
       userInputRun.addBreak();

       XWPFParagraph studentInfoTitle = docx.createParagraph();
       XWPFRun studentInfoRun = studentInfoTitle.createRun();
       studentInfoRun.setBold(true);
       studentInfoRun.setText("Student Info:");
       studentInfoRun.addBreak();
       studentInfoRun.setText("Name: " + student.getName());
       studentInfoRun.addBreak();
       studentInfoRun.setText("ID: " + student.getStudentID());
       studentInfoRun.addBreak();
       studentInfoRun.setText("Courses:");
       studentInfoRun.addBreak();
       for (Course course : student.getCourses()) {
           studentInfoRun.setText("- " + course.getCourseCode() + " " + course.getCourseName() + " (" + course.getGrade() + ")");
           studentInfoRun.addBreak();
       }

       FileOutputStream out = new FileOutputStream("output.docx");
       docx.write(out);
       out.close();
       docx.close();
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
                        prerequisites.add(checkBox.getActionCommand());
                    }
                }
            }
            boolean isFastTrack = fastTrackCheckBox.isSelected();
            boolean isThesisMasters = thesisMastersCheckBox.isSelected();

            // Call the processTranscript method with the user inputs
            try {
                System.out.println("Before processing transcript");
                Student student = PdfReader.processTranscript("C:/Users/adria/Downloads/Krab, Krusty-SSR_TSRPT-1.pdf", track, prerequisites, isFastTrack, isThesisMasters);
                System.out.println("After processing transcript");
                // Call the createOutputPDF method with the user inputs and the student object
                System.out.println("Before creating output PDF");
                createOutputPDF(student, track, prerequisites, isFastTrack, isThesisMasters);
                System.out.println("After creating output PDF");
                File outputFile = new File("output.pdf");
                if (outputFile.exists()) {
                    System.out.println("PDF file created: " + outputFile.getAbsolutePath());
                } else {
                    System.out.println("PDF file not created.");
                }

                System.out.println("Before creating output docx");
                String outputFileName = "output.docx";
                docx(student, track, prerequisites, isFastTrack, isThesisMasters);
                System.out.println("After creating output docx");
                File outputFileDocX= new File(outputFileName);
                if (outputFileDocX.exists()) {
                    System.out.println("DOCX file created: " + outputFileDocX.getAbsolutePath());
                } else {
                    System.out.println("DOCX file not created.");
                }

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}







