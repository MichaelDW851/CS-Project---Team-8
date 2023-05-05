import java.io.*;
import java.util.List;
import javax.swing.JFileChooser;


public class StudentSaving {

    private static String filepath = "";

    //THIS IS HOW TO USE THE FUNCTIONS
//    public static void main(String args[]) {
//        StudentSaving objectIO = new StudentSaving();
//
//        Student student = new Student("John","1","aaa", "1");
//        objectIO.writeObjectToFile(student);
//
//        // Deserialize the object
//        Student deserializedStudent = objectIO.readObjectFromFile(filepath + student.getName() + student.getStudentID() + ".ser");
//
//        System.out.println("Deserialized Student: " + deserializedStudent.getName());
//    }

    public void StudentSaving(){

    }

    public void saveStudent(Student student){
        filepath = getFilePath();

        writeStudentInfoToFile(student);



    }

    public Student getSavedStudent(String filepath){
        Student deserializedStudent = readObjectFromFile(filepath);

        return deserializedStudent;
    }

    public void writeStudentInfoToFile(Student student) {
        String fileExtension = ".txt";
        String filename = student.getName() + student.getStudentID() + fileExtension;
        String fullPath = filepath + filename;

        try {
            FileWriter fileWriter = new FileWriter(fullPath);
            PrintWriter printWriter = new PrintWriter(fileWriter);

            // Write student information to the file
            printWriter.println("Name: " + student.getName());
            printWriter.println("ID: " + student.getStudentID());
            // Add additional information as needed

            printWriter.println("Courses:");
            List<Course> courses = student.getCourses();
            for (Course course : courses) {
                printWriter.println(course.toString());
            }

            printWriter.close();
            System.out.println("Student information was successfully written to the file: " + fullPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // FOR DE-SERIALIZATION:

    public Student readObjectFromFile(String filepath) {
        Student obj = null;

        try {
            FileInputStream file = new FileInputStream(filepath);
            ObjectInputStream objectIn = new ObjectInputStream(file);
            obj = (Student) objectIn.readObject();
            objectIn.close();

            System.out.println("The Object was successfully read from a file");

        } catch (Exception ex) {
            System.err.println("Failed to read " + filepath + ", " + ex);
        }

        return obj;
    }

    public String getFilePath() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile().getAbsolutePath() + "/";
        } else {
            return "";
        }
    }
}
