import javax.swing.*;
import java.awt.*;
import java.util.List;


public class GenerateDegreePlan {

    Student student;
    JFrame frame = new JFrame("Degree Plan");
    JPanel panel = new JPanel();
    public GenerateDegreePlan(Student student) {
        this.student = student;
        generateDegree();
    }

    void generateDegree() {
        // Set the panel's background color
        panel.setBackground(new Color(255, 255, 240)); // ivory color

        //Call method to get student box
        getStudentBox();

        // Call get courses
        getCourses();


        // Set the frame's look and feel to the system's default
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // Create and configure the frame
        frame.setContentPane(panel);
        frame.pack();
        frame.setLocationRelativeTo(null); // Center the frame on the screen
        frame.setVisible(true);
    }


    private void getCourses(){
        // Create a panel for the course list
        JPanel coursePanel = new JPanel();
        coursePanel.setLayout(new BoxLayout(coursePanel, BoxLayout.Y_AXIS));
        coursePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add some padding
        JLabel coursesLabel = new JLabel("Courses:");
        coursesLabel.setAlignmentX(Component.LEFT_ALIGNMENT); // Align the label to the left
        coursesLabel.setFont(new Font("SansSerif", Font.BOLD, 14)); // Increase font size and bold the label
        JPanel labelPanel = new JPanel();
        labelPanel.setLayout(new BorderLayout());
        labelPanel.add(coursesLabel, BorderLayout.WEST); // Add the label to the left of the panel
        coursePanel.add(labelPanel);
        coursePanel.add(new JSeparator()); // Add a horizontal separator
        List<Course> courses = student.getCourseList();
        student.removeCsIppAssignment();


        for (Course course : courses) {
            JPanel courseRow = new JPanel(new BorderLayout());
            JLabel label = new JLabel(String.format("%s - %s", course.getCourseCode(), course.getCourseName()));
            label.setFont(new Font("SansSerif", Font.PLAIN, 12)); // Decrease font size
            JCheckBox checkBox = new JCheckBox("", true); // set checkbox checked by default
            courseRow.add(label, BorderLayout.WEST);
            courseRow.add(checkBox, BorderLayout.EAST);
            courseRow.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0)); // Add some padding
            coursePanel.add(courseRow);
        }

        // Add the course panel to the center of the main panel
        panel.add(coursePanel, BorderLayout.CENTER);
    }

    //Try to create new colum that shows prerequisites
//    private void getPrereqs(){
//        // Create a panel for the course list
//        JPanel coursePanel = new JPanel();
//        coursePanel.setLayout(new BoxLayout(coursePanel, BoxLayout.Y_AXIS));
//        coursePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add some padding
//        JLabel coursesLabel = new JLabel("Prerequisites:");
//        coursesLabel.setAlignmentX(Component.LEFT_ALIGNMENT); // Align the label to the left
//        coursesLabel.setFont(new Font("SansSerif", Font.BOLD, 14)); // Increase font size and bold the label
//        JPanel labelPanel = new JPanel();
//        labelPanel.setLayout(new BorderLayout());
//        labelPanel.add(coursesLabel, BorderLayout.WEST); // Add the label to the left of the panel
//        coursePanel.add(labelPanel);
//        coursePanel.add(new JSeparator()); // Add a horizontal separator
//        List<Course> prereqs = student.getPrerequisites();
//        student.removeCsIppAssignment();
//
//
//        for (Course course : prereqs) {
//            JPanel courseRow = new JPanel(new BorderLayout());
//            System.out.println("adding prereq");
//            JLabel label = new JLabel(String.format("%s - %s", course.getCourseCode(), course.getCourseName()));
//            label.setFont(new Font("SansSerif", Font.PLAIN, 12)); // Decrease font size
//            JCheckBox checkBox = new JCheckBox("", true); // set checkbox checked by default
//            courseRow.add(label, BorderLayout.WEST);
//            courseRow.add(checkBox, BorderLayout.EAST);
//            courseRow.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0)); // Add some padding
//            coursePanel.add(courseRow);
//        }
//
//        // Add the course panel to the center of the main panel
//        panel.add(coursePanel, BorderLayout.CENTER);
//    }





    // Need to add action button listeners to this method
    private void getStudentBox() {
        // Create a panel for the student information
        JPanel infoPanel = new JPanel(new GridLayout(4, 1, 0, 5)); // Update grid layout to 4 rows
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10)); // Add some padding
        JLabel nameLabel = new JLabel("Student Name: " + student.getName());
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 14)); // Increase font size and bold the label
        JLabel idLabel = new JLabel("Student ID: " + student.getStudentID());
        idLabel.setFont(new Font("SansSerif", Font.PLAIN, 12)); // Decrease font size
        JLabel semesterAdmittedLabel = new JLabel("Semester Admitted: " + student.getSemesterAdmittedToProgram());
        semesterAdmittedLabel.setFont(new Font("SansSerif", Font.PLAIN, 12)); // Decrease font size
        JLabel majorLabel = new JLabel("Major: " + student.getMajor());
        majorLabel.setFont(new Font("SansSerif", Font.PLAIN, 12)); // Decrease font size

        infoPanel.add(nameLabel);
        infoPanel.add(idLabel);
        infoPanel.add(semesterAdmittedLabel);
        infoPanel.add(majorLabel);

        // Create a panel for the checkboxes
        JPanel checkboxPanel = new JPanel(new GridLayout(2, 1, 0, 5));
        checkboxPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10)); // Add some padding
        JCheckBox fastTrackCheckBox = new JCheckBox("Fast Track to Masters", student.getFastTrackCheck());
        JCheckBox thesisMastersCheckBox = new JCheckBox("Thesis Masters", student.getThesisMastersCheck());
        checkboxPanel.add(fastTrackCheckBox);
        checkboxPanel.add(thesisMastersCheckBox);

        // Add the panels with the student information and checkboxes to the top right of the main panel
        JPanel topRightPanel = new JPanel(new BorderLayout());
        topRightPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10)); // Add some padding
        topRightPanel.add(infoPanel, BorderLayout.NORTH);
        topRightPanel.add(checkboxPanel, BorderLayout.SOUTH);
        panel.add(topRightPanel, BorderLayout.NORTH);
    }



}
