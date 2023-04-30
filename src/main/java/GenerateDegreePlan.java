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
        SpreadsheetUI degreePlan = new SpreadsheetUI(student.name,student.studentID,student.semesterAdmittedToProgram,student.getFastTrackCheck(),student.getThesisMastersCheck());
        for (Course course: student.getCourses()) {
            degreePlan.addCourse(course.courseName,course.semester,course.grade);
        }
    }




}
