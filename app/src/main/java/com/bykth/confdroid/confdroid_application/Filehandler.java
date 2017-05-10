package com.bykth.confdroid.confdroid_application;

import android.content.Context;
import com.bykth.confdroid.confdroid_application.model.Authentication;

import java.io.*;

/**
 * Created by Glantz on 2017-05-05.
 */
public class Filehandler {
    private Context context;

    public Filehandler(Context context) {
        this.context = context;

    }

    public void writeJSONtoTXT(String Json, Boolean toBeHashed) {
        FileOutputStream stream = null;
        try {
            stream = new FileOutputStream(this.context.getFilesDir() + "/json.txt");
            if (toBeHashed) {
                stream.write(MD5(Json).getBytes());
            } else {
                stream.write(Json.getBytes());
            }
            stream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String MD5(String md5) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(md5.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
        }
        return null;
    }

    public String readFileAsString() {
        StringBuffer fileData = new StringBuffer();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(
                    new FileReader(this.context.getFilesDir() + "/json.txt"));
            char[] buf = new char[1024];
            int numRead = 0;

            while ((numRead = reader.read(buf)) != -1) {
                String readData = String.valueOf(buf, 0, numRead);
                fileData.append(readData);
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileData.toString();
    }

    public void WriteConfigurationFileToBinary(String URL,String Auth)  {
        Authentication auth = new Authentication(URL,Auth);
        FileOutputStream outFile;
        ObjectOutputStream outStream;
        try { // try to open and write the file
            outFile = new FileOutputStream(this.context.getFilesDir() + "/auth.bin");
            outStream = new ObjectOutputStream(outFile);
            { // write one object
                outStream.writeObject(auth); // this one line writes an entire object!!!!
            } // write one object

            outStream.close();
        } // try to open and write the file
        catch (IOException ex) { // catch
            ex.getMessage();
            ex.printStackTrace();
        } // catch
    }

    public Authentication readFromConfigurationFileBinary() {
        Authentication auth = null;
        try {
            FileInputStream fstream = new FileInputStream(this.context.getFilesDir() + "/auth.bin");
            ObjectInputStream ostream = new ObjectInputStream(fstream);
            auth = (Authentication) ostream.readObject();
            // do something with obj
        } catch (Exception e) {
            e.getMessage();
        }
        return auth;
    }

}
