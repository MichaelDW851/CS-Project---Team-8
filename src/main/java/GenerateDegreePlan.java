import javax.swing.*;
import java.awt.*;
import java.util.List;
import javax.swing.border.*;
import java.awt.event.*;



public class GenerateDegreePlan {


    Student student;
    public GenerateDegreePlan(Student student) {
        this.student = student;
        generateDegree();
    }

    private void generateDegree() {
        EditableTextFieldUI degreePlan = new EditableTextFieldUI();
    }

//    Student student;
//    JFrame frame = new JFrame("Degree Plan");
//    JPanel panel = new JPanel();
//    public GenerateDegreePlan(Student student) {
//        this.student = student;
//        generateDegree();
//    }
//
//    void generateDegree() {
//        // Set the panel's background color
//        panel.setBackground(new Color(255, 255, 240)); // ivory color
//
//        //Call method to get student box
//        getStudentBox();
//
//        // Call get courses
//        getCourses();
//
//        //Call get prerequisites
//        getPrerequisites();
//
//
//        // Set the frame's look and feel to the system's default
//        try {
//            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//
//        // Create and configure the frame
//        frame.setContentPane(panel);
//        frame.pack();
//        frame.setLocationRelativeTo(null); // Center the frame on the screen
//        frame.setVisible(true);
//    }
//
//
//    private void getCourses(){
//        // Create a panel for the course list
//        JPanel coursePanel = new JPanel();
//        coursePanel.setLayout(new BoxLayout(coursePanel, BoxLayout.Y_AXIS));
//        coursePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add some padding
//        JLabel coursesLabel = new JLabel("Courses:");
//        coursesLabel.setAlignmentX(Component.LEFT_ALIGNMENT); // Align the label to the left
//        coursesLabel.setFont(new Font("SansSerif", Font.BOLD, 14)); // Increase font size and bold the label
//        JPanel labelPanel = new JPanel();
//        labelPanel.setLayout(new BorderLayout());
//        labelPanel.add(coursesLabel, BorderLayout.WEST); // Add the label to the left of the panel
//        coursePanel.add(labelPanel);
//        coursePanel.add(new JSeparator()); // Add a horizontal separator
//        List<Course> courses = student.getCourseList();
//        student.removeCsIppAssignment();
//
//
//        for (Course course : courses) {
//            JPanel courseRow = new JPanel(new BorderLayout());
//            JLabel label = new JLabel(String.format("%s - %s Semester: %s", course.getCourseCode(), course.getCourseName(),course.getSemester()));
//            label.setFont(new Font("SansSerif", Font.PLAIN, 12)); // Decrease font size
//            JCheckBox checkBox = new JCheckBox("", true); // set checkbox checked by default
//            courseRow.add(label, BorderLayout.WEST);
//            courseRow.add(checkBox, BorderLayout.EAST);
//            courseRow.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0)); // Add some padding
//            coursePanel.add(courseRow);
//        }
////        for (Course course : courses) {
////            JPanel courseRow = new JPanel(new BorderLayout());
////            JLabel label = new JLabel(String.format("%s - %s Semester: %s", course.getCourseCode(), course.getCourseName(),course.getSemester()));
////            label.setFont(new Font("SansSerif", Font.PLAIN, 12)); // Decrease font size
////            JCheckBox checkBox = new JCheckBox("", true); // set checkbox checked by default
////            courseRow.add(label, BorderLayout.WEST);
////            courseRow.add(checkBox, BorderLayout.EAST);
////            courseRow.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0)); // Add some padding
////            coursePanel.add(courseRow);
////        }
//
//        // Add the course panel to the center of the main panel
//        panel.add(coursePanel, BorderLayout.CENTER);
//    }
//
//    private void getPrerequisites() {
//        // Create a panel for the prerequisites list
//        JPanel prerequisitesPanel = new JPanel();
//        prerequisitesPanel.setLayout(new BoxLayout(prerequisitesPanel, BoxLayout.Y_AXIS));
//        prerequisitesPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add some padding
//        JLabel prerequisitesLabel = new JLabel("Prerequisites:");
//        prerequisitesLabel.setAlignmentX(Component.LEFT_ALIGNMENT); // Align the label to the left
//        prerequisitesLabel.setFont(new Font("SansSerif", Font.BOLD, 14)); // Increase font size and bold the label
//        JPanel labelPanel = new JPanel();
//        labelPanel.setLayout(new BorderLayout());
//        labelPanel.add(prerequisitesLabel, BorderLayout.WEST); // Add the label to the left of the panel
//        prerequisitesPanel.add(labelPanel);
//        prerequisitesPanel.add(new JSeparator()); // Add a horizontal separator
//
//        // Add your prerequisites logic here
//        // Example:
//        List<Course> prerequisites = student.getPrerequisites();
//        for (Course prerequisite : prerequisites) {
//            JPanel prerequisiteRow = new JPanel(new BorderLayout());
//            JLabel label = new JLabel(String.format("%s - %s", prerequisite.getCourseCode(), prerequisite.getCourseName()));
//            label.setFont(new Font("SansSerif", Font.PLAIN, 12)); // Decrease font size
//            JCheckBox checkBox = new JCheckBox("", false); // set checkbox unchecked by default
//            prerequisiteRow.add(label, BorderLayout.WEST);
//            prerequisiteRow.add(checkBox, BorderLayout.EAST);
//            prerequisiteRow.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0)); // Add some padding
//            prerequisitesPanel.add(prerequisiteRow);
//        }
//
//        // Add the prerequisites panel to the right of the main panel
//        panel.add(prerequisitesPanel, BorderLayout.EAST);
//    }
//
//
//    // Need to add action button listeners to this method
//    private void getStudentBox() {
//        // Create a panel for the student information
//        JPanel infoPanel = new JPanel(new GridLayout(4, 1, 0, 5)); // Update grid layout to 4 rows
//        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10)); // Add some padding
//        JLabel nameLabel = new JLabel("Student Name: " + student.getName());
//        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 14)); // Increase font size and bold the label
//        JLabel idLabel = new JLabel("Student ID: " + student.getStudentID());
//        idLabel.setFont(new Font("SansSerif", Font.PLAIN, 12)); // Decrease font size
//        JLabel semesterAdmittedLabel = new JLabel("Semester Admitted: " + student.getSemesterAdmittedToProgram());
//        semesterAdmittedLabel.setFont(new Font("SansSerif", Font.PLAIN, 12)); // Decrease font size
//        JLabel majorLabel = new JLabel("Major: " + student.getMajor());
//        majorLabel.setFont(new Font("SansSerif", Font.PLAIN, 12)); // Decrease font size
//
//        infoPanel.add(nameLabel);
//        infoPanel.add(idLabel);
//        infoPanel.add(semesterAdmittedLabel);
//        infoPanel.add(majorLabel);
//
//        // Create a panel for the checkboxes
//        JPanel checkboxPanel = new JPanel(new GridLayout(2, 1, 0, 5));
//        checkboxPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10)); // Add some padding
//        JCheckBox fastTrackCheckBox = new JCheckBox("Fast Track to Masters", student.getFastTrackCheck());
//        JCheckBox thesisMastersCheckBox = new JCheckBox("Thesis Masters", student.getThesisMastersCheck());
//        checkboxPanel.add(fastTrackCheckBox);
//        checkboxPanel.add(thesisMastersCheckBox);
//
//        // Add the panels with the student information and checkboxes to the top right of the main panel
//        JPanel topRightPanel = new JPanel(new BorderLayout());
//        topRightPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10)); // Add some padding
//        topRightPanel.add(infoPanel, BorderLayout.NORTH);
//        topRightPanel.add(checkboxPanel, BorderLayout.SOUTH);
//        panel.add(topRightPanel, BorderLayout.NORTH);
//    }



}
