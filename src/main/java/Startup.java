import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.concurrent.CountDownLatch;


public class Startup {

    private String filePath;
    // latch Makes the program wait for user to select file
    private final CountDownLatch latch;


    public Startup(){
        this.latch = new CountDownLatch(1);
        initialize();

    }

    // Create initial GUI with button that opens file selector
    private void initialize(){

        JFrame frame = new JFrame("File Selector");
        JButton selectFileButton =new JButton("Click to select degree audit or student");
        selectFileButton.setBounds(50,150,300,60);
        frame.add(selectFileButton);
        frame.setBounds(500,150,400,400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        frame.setVisible(true);

        // This is the action for the select file button.
        selectFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createFileChooser(frame);
                latch.countDown();
            }
        });

        frame.getContentPane().add(selectFileButton);

//        {
//            public void actionPerformed(ActionEvent e){
//                selectFileButton.setText("Welcome to Javatpoint.");
//            }
//        });
    }

    private void createFileChooser(JFrame frame) {
        String filename = File.separator + "tmp";
        JFileChooser fileChooser = new JFileChooser(new File(filename));

        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY); // only files

        // pop up an "Open File" file chooser dialog
        int result = fileChooser.showOpenDialog(frame);

        //get filepath
        if (result == JFileChooser.APPROVE_OPTION) {

            filePath = fileChooser.getSelectedFile().getAbsolutePath();
            System.out.println("Filepath = " + filePath);
            String extension = "";
            int i = filePath.lastIndexOf('.');
            if (i >= 0) {
                extension = filePath.substring(i + 1);
            }
            System.out.println("Exe = " + extension);

//        System.out.println("File path: " + filePath);
//        System.out.println("File type: " + extension);

//        if(extension.equals("ser")) createStudentDegree();
//        else if(extension.equals("pdf")){
//            //parse pdf and create editable degree plan
//        }else if(extension.equals("docx")) {
//            //parse docx and create editable degree plan
//        }
//        else System.out.print("Incorrect file input.");

        }
        frame.setVisible(false);


    }

    public String getFilePath () throws InterruptedException {

        latch.await();
        System.out.println("Returning = " + filePath);
        return filePath;
        //Create the degree plan with a student obj

    }
}