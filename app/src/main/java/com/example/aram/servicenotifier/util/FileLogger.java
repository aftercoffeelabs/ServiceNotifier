package com.example.aram.servicenotifier.util;

import android.content.Context;
import android.text.format.DateFormat;

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
    private static File mFile = null;
    private Context mContext;

    private FileOutputStream mOutStream;
    private FileInputStream mInStream;
    private BufferedReader mBuffReader;

    public FileLogger(Context context) {

        mContext = context;
        mFile = new File(mContext.getFilesDir(), FILE_NAME);
    }

    /**
     * Log
     *
     * Only call this from Service
     */
    public void log(String text){

        if (mContext != null) {

            // Append timestamp, create final formatted string
            final CharSequence dateTime = DateFormat.format("yyyy-MM-dd hh:mm:ss", new Date());
            String outStr = dateTime.toString() + ":   " + text + "\\n";

            try {
                if (mFile != null) {
                    mOutStream = mContext.openFileOutput(
                            mFile.getName(), mContext.MODE_APPEND);
                    mOutStream.write(outStr.getBytes());
                    mOutStream.close();
                } else {
                    mFile = new File(mContext.getFilesDir(), FILE_NAME);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
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
