import java.io.*;
import java.util.List;
import javax.swing.JFileChooser;

public class StudentSaving {

    private static String filepath = "";

    public static void main(String args[]) {
        // Create an instance of the class
        StudentSaving studentSaving = new StudentSaving();

        Student student = new Student("John Smith", "123456789", "Computer Science", "Fall 2021", true, false);

        // Save the student object to a file
        studentSaving.writeObjectToFile(student);

        // Deserialize the object
        Student deserializedStudent = studentSaving.readObjectFromFile(filepath + student.getName() + student.getStudentID() + ".ser");

        // Check if the deserializedStudent object is not null before accessing its properties
        if (deserializedStudent != null) {
            System.out.println("Deserialized Student: " + deserializedStudent.getName());
        } else {
            System.out.println("Failed to deserialize the student object.");
        }
    }

    // Constructor of the class
    public StudentSaving() {
        // Get the file path from the user
        filepath = getFilePath();


    }

    public void saveStudent(Student student) {
   //     writeStudentInfoToFile(student);

        writeObjectToFile(student);

        // Deserialize the object
        Student deserializedStudent = readObjectFromFile(filepath+ student.getName() + student.getStudentID() + ".ser");

        // Check if the deserializedStudent object is not null before accessing its properties
        if (deserializedStudent != null) {
            System.out.println("Deserialized Student: " + deserializedStudent.getName());
        } else {
            System.out.println("Failed to deserialize the student object.");
        }
    }

    public Student getSavedStudent(String filepath) {
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
            System.out.println("Failed to write student to file");
        }
    }

    private void writeObjectToFile(Student student) {
        String fileExtension = ".ser";
        String filename = student.getName() + student.getStudentID() + fileExtension;
        String fullPath = filepath + filename;

        try {
            FileOutputStream file = new FileOutputStream(fullPath);
            ObjectOutputStream objectOut = new ObjectOutputStream(file);
            objectOut.writeObject(student);
            objectOut.close();
            System.out.println("The Object was successfully written to a file: " + fullPath);

        } catch (Exception ex) {
            System.err.println("Failed to write " + fullPath + ", " + ex);
        }
    }

    public Student readObjectFromFile(String filepath) {
        Student obj = null;

        try {
            File file = new File(filepath);
            if (file.exists()) {
                FileInputStream fileIn = new FileInputStream(filepath);
                ObjectInputStream objectIn = new ObjectInputStream(fileIn);
                obj = (Student) objectIn.readObject();
                objectIn.close();
                fileIn.close();
                System.out.println("The Object was successfully read from a file");
            } else {
                System.out.println("The file " + filepath + " does not exist.");
            }
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
