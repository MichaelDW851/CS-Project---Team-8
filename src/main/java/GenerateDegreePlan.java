import java.util.ArrayList;


public class GenerateDegreePlan {


    Student student;
    public GenerateDegreePlan(Student student) {
        this.student = student;
        generateDegree();
    }

    private void generateDegree() {
        SpreadsheetUI degreePlan = new SpreadsheetUI(student.name,student.studentID,student.semesterAdmittedToProgram,student.getFastTrackCheck(),student.getThesisMastersCheck(),student.getTrack());
        System.out.println(student.name + "\n" + student.getCoreCourses());
        System.out.println( "\n" + student.getElectiveCourses());
        System.out.println("\n" + student.getLevelingCourses());
        //  System.out.println("\n" + student.getCoreCourses());


        for (Course course: student.getCourses()) {
            degreePlan.addCourse(course);
        }
    }




}