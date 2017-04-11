package com.bykth.confdroid.confdroid_application;

import java.io.*;
/**
 * Created by Glantz on 2017-04-10.
 */
public class XML_editor {


    public void ApplyXMLSetting(String filepath, String regexCommand, String replaceWith) throws IOException {
        FileOutputStream stream = new FileOutputStream("/Users/Glantz/Desktop/KTH/projektkurs åk2/KOD/tmp.xml");

        String tmp = readFileAsString(filepath).replaceAll(regexCommand, replaceWith);

        try {
            stream.write(tmp.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            stream.close();
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
