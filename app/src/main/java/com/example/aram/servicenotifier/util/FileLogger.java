package com.example.aram.servicenotifier.util;

import android.text.format.DateFormat;

import com.example.aram.servicenotifier.infrastructure.MyApp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;

/**
 * Class FileLogger
 *
 * Used for logging system messages for debugging
 */
public class FileLogger {

    private final static String FILE_NAME = "service_notifier_log";
    private static File mFile = new File(MyApp.getContext().getFilesDir(), FILE_NAME);

    private FileOutputStream mOutStream;
    private FileInputStream mInStream;
    private BufferedReader mBuffReader;

    public FileLogger() {
    }

    public void log(String text){

        // Append timestamp, create final formatted string
        final CharSequence dateTime = DateFormat.format("yyyy-MM-dd hh:mm:ss", new Date());
        String outStr = dateTime.toString() + ":   " + text + "\\n";

        try {
            if (mFile != null) {
                mOutStream = MyApp.getContext().openFileOutput(
                        mFile.getName(), MyApp.getContext().MODE_APPEND);
                mOutStream.write(outStr.getBytes());
                mOutStream.close();
            } else {
                mFile = new File(MyApp.getContext().getFilesDir(), FILE_NAME);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static String openAndReadFile() {

        String fileContents = "";
        BufferedReader bufferedReader = null;

        try {
            if (mFile != null) {

                String inputLine;
                bufferedReader = new BufferedReader(new FileReader(
                        mFile.getAbsolutePath()));

                while ((inputLine = bufferedReader.readLine()) != null) {
                    fileContents += inputLine;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        return fileContents;
    }

    public static void deleteLogFile() {

        if (mFile != null) {
            if (mFile.exists()) {
                mFile.delete();
                mFile = null;
            }
        }
    }
}
