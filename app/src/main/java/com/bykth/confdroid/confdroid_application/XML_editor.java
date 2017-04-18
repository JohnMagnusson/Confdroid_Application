package com.bykth.confdroid.confdroid_application;

import android.content.Context;

import java.io.*;

/**
 * Created by Glantz on 2017-04-10.
 */
public class XML_editor {

    private Context context;

    public XML_editor(Context context) {
        this.context = context;
    }

    /**
     * @param filepath     The filepath including
     * @param regexCommand
     * @param replaceWith
     * @throws IOException
     */
    public boolean ApplyXMLSetting(String filepath, String regexCommand, String replaceWith) throws IOException {

        String tmp = readFileAsString(filepath).replaceAll(regexCommand, replaceWith);

        FileOutputStream stream = new FileOutputStream(context.getFilesDir() + "/tmp.xml");

        try {
            stream.write(tmp.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            stream.close();
        }

        return moveFileToApp(context.getFilesDir() + "/tmp.xml", filepath);


    }

    private boolean moveFileToApp(String tmpFilepath, String applicationFilepath) {
        try {
            Process proc = Runtime.getRuntime().exec("su");
            DataOutputStream os = new DataOutputStream(proc.getOutputStream());
            os.writeBytes("cat " + tmpFilepath + " > " + applicationFilepath + "\n");
            os.writeBytes("exit\n");
            os.flush();
            BufferedReader stdInput = new BufferedReader(new
                    InputStreamReader(proc.getInputStream()));

            BufferedReader stdError = new BufferedReader(new
                    InputStreamReader(proc.getErrorStream()));

            // wait for the output from the command
            String s = null;
            while ((s = stdInput.readLine()) != null) ;
            // wait for any errors from the attempted command

            while ((s = stdError.readLine()) != null) ;
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }


    }

    private String readFileAsString(String filePath) throws IOException {
        StringBuffer fileData = new StringBuffer();
        BufferedReader reader = new BufferedReader(
                new FileReader(filePath));
        char[] buf = new char[1024];
        int numRead = 0;
        while ((numRead = reader.read(buf)) != -1) {
            String readData = String.valueOf(buf, 0, numRead);
            fileData.append(readData);
        }
        reader.close();


        return fileData.toString();
    }
}
