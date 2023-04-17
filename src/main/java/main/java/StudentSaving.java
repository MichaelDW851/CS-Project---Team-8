package main.java;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class StudentSaving {

    private static String filepath = "C:\\Users\\maste\\Desktop\\";

    //THIS IS HOW TO USE THE FUNCTIONS
    public static void main(String args[]) {
        StudentSaving objectIO = new StudentSaving();

        Student student = new Student("John","1","aaa", "1");
        objectIO.writeObjectToFile(student);

        // Deserialize the object
        Student deserializedStudent = objectIO.readObjectFromFile(filepath + student.getName() + student.getStudentID() + ".ser");

        System.out.println("Deserialized Student: " + deserializedStudent.getName());
    }



    public void writeObjectToFile(Student serObj) {

        String fileExtension = ".ser";
        String filename = serObj.getName() + serObj.getStudentID() + fileExtension;
        String fullPath = filepath + filename;

        try {

            FileOutputStream fileOut = new FileOutputStream(fullPath);
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(serObj);
            objectOut.close();
            System.out.println("The Object  was succesfully written to a file");

        } catch (Exception ex) {
            ex.printStackTrace();
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
}