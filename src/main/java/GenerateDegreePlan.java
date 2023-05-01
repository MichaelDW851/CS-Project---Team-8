import java.util.ArrayList;


public class GenerateDegreePlan {


    Student student;
    public GenerateDegreePlan(Student student) {
        this.student = student;
        generateDegree();
    }

    private void generateDegree() {
        SpreadsheetUI degreePlan = new SpreadsheetUI(student.name,student.studentID,student.semesterAdmittedToProgram,student.getFastTrackCheck(),student.getThesisMastersCheck());
        System.out.println(student.name + "\n" + student.getCoreCourses());
        System.out.println( "\n" + student.getElectiveCourses());
        System.out.println("\n" + student.getLevelingCourses());
      //  System.out.println("\n" + student.getCoreCourses());
        degreePlan.addCores((ArrayList<Course>) student.getCoreCourses());
        degreePlan.addTrackElectives(new ArrayList<Course>());
        degreePlan.addApprovedElectives((ArrayList<Course>) student.getElectiveCourses());
        degreePlan.addAdditionalElectives(new ArrayList<Course>());
        degreePlan.addPreReqs((ArrayList<Course>) student.getLevelingCourses());

//        for (Course course: student.getCourses()) {
//            degreePlan.addCourse(course.courseName,course.semester,course.grade);
//        }
    }




}
