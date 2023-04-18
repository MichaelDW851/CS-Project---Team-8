import javax.swing.*;
import java.util.List;

public class GenerateDegreePlan {
    public GenerateDegreePlan(Student student) {
        generateDegree(student);
    }

    void generateDegree(Student student){
        JFrame frame = new JFrame("Degree Plan");
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

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
}
