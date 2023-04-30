import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import javax.swing.DefaultCellEditor;

public class Main {
    public static void main(String[] args) {
        SpreadsheetUI sheet = new SpreadsheetUI("Michael","20293","Fall 2020",true,true);
        sheet.addCourse("CS 4485","F21","A");

        for (int i = 0;i < 10;i++) {
            sheet.addCourse("CS 4485","F21","A");
        }

    }
}

