package com.group.MediaLibrary.data;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

public class DataLayerException extends Exception {

    public DataLayerException(String message) {
        super(message);
        writeLog(this, message);
    }

    public DataLayerException(Exception baseException) {
        super(baseException.getMessage());
        writeLog(baseException);
    }

    public DataLayerException(Exception baseException, String... values) {
        super(values[0]);
        writeLog(baseException, values);
    }

    public void writeLog(Exception baseException, String... values) {
        try {
            //create file
            File file = new File("./log.txt");
            PrintWriter pw;
            if(file.exists()) {
                pw = new PrintWriter(new FileOutputStream(file, true)); //append
            } else {
                pw = new PrintWriter(new FileOutputStream(file, false)); //overwrite
            }

            //print exception and values to log file
            baseException.printStackTrace(pw);
            for(int i = 0; i < values.length; i++) {
                pw.println(values[i]);
            }

            pw.println(); //blank line to separate entries
            pw.flush();
        } catch (FileNotFoundException fnfe) {

        }
    }

}
