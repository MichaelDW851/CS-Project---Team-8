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
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Method call to get student info and courses
        getStudentInfo();
        // Method call to get fast track and thesis checks
        getChecks();

        getCourses();


    }

    void getStudentInfo(){
        //Print student name and ID
        JLabel nameLabel = new JLabel("Student Name: " + student.getName());
        panel.add(nameLabel);
        JLabel idLabel = new JLabel("Student ID: " + student.getStudentID());
        panel.add(idLabel);




    }

    private void getCourses(){
        //panel.add(Box.createRigidArea(new Dimension(0, 10))); // Add an empty space

        panel.add(new JLabel(" "));
        List<Course> courses = student.getCourseList();

        for (Course course : courses) {
            JLabel label = new JLabel(String.format("%s - %s", course.getCourseCode(), course.getCourseName()));
            panel.add(label);
        }

        frame.add(panel);
        frame.setSize(500, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    // Need to add action button listeners to this method
    private void getChecks() {
        // Fast Track to Masters selection
        panel.add(new JLabel(""));
        panel.add(new JLabel("Fast Track to Masters:"));
        JCheckBox fastTrackCheckBox = new JCheckBox("", student.getFastTrackCheck());
        panel.add(fastTrackCheckBox);

// Thesis Masters selection
        panel.add(new JLabel(""));
        panel.add(new JLabel("Thesis Masters:"));
        JCheckBox thesisMastersCheckBox = new JCheckBox("", student.getThesisMastersCheck());
        panel.add(thesisMastersCheckBox);


    }
}
