package com.example.drinkup.GestioneFile;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class LibFileExt { //memoria esterna


    public static boolean saveFile(String fileName, String data) {
        File extStore = Environment.getExternalStorageDirectory();
        // ==> /storage/emulated/0/note.txt
        String path = extStore.getAbsolutePath() + "/" + fileName;
        Log.i("ExternalStorageDemo", "Save to: " + path);


        File myFile = new File(path);
        if(myFile.exists()) //se è già presente faccio l'append
        {
            return appendF(myFile, data);
        }
        else //altrimenti creo il file
        {
            return writeF(myFile, data);
        }
    }

    public static boolean writeFile(String fileName, String data){
        File extStore = Environment.getExternalStorageDirectory();
        // ==> /storage/emulated/0/note.txt
        String path = extStore.getAbsolutePath() + "/" + fileName;

        Log.i("ExternalStorageDemo", "Save to: " + path);


        File myFile = new File(path);
        try {
            myFile.createNewFile();
            FileOutputStream fOut = new FileOutputStream(myFile);
            OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
            myOutWriter.write(data);
            myOutWriter.close();
            fOut.close();
            return true;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean appendFile(String fileName, String data){
        File extStore = Environment.getExternalStorageDirectory();
        // ==> /storage/emulated/0/note.txt
        String path = extStore.getAbsolutePath() + "/" + fileName;
        Log.i("ExternalStorageDemo", "Save to: " + path);


        File myFile = new File(path);
        if(myFile.exists()) {
            try {
                FileOutputStream fOut = new FileOutputStream(myFile, true);
                OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
                myOutWriter.append(data + "\n");
                myOutWriter.close();
                fOut.close();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        } else return false;
    }

    private static boolean writeF(File myFile, String data){
        try {
            myFile.createNewFile();
            FileOutputStream fOut = new FileOutputStream(myFile);
            OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
            myOutWriter.write(data + "\n");
            myOutWriter.close();
            fOut.close();
            return true;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static boolean appendF(File myFile, String data){
        try
        {
            FileOutputStream fOut = new FileOutputStream(myFile, true);
            OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
            myOutWriter.append(data + "\n");
            myOutWriter.close();
            fOut.close();
            return true;
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String readFile(String fileName) {

        File extStore = Environment.getExternalStorageDirectory();
        // ==> /storage/emulated/0/note.txt
        String path = extStore.getAbsolutePath() + "/" + fileName;
        Log.i("ExternalStorageDemo", "Read file: " + path);

        String s = "";
        String fileContent = "";
        try {
            File myFile = new File(path);
            if(!myFile.exists()){
                writeFile(fileName, "");
            }
            FileInputStream fIn = new FileInputStream(myFile);
            BufferedReader myReader = new BufferedReader(
                    new InputStreamReader(fIn));

            while ((s = myReader.readLine()) != null) {
                fileContent += s + "\n";
            }
            myReader.close();

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return fileContent;
    }

    public static boolean deleteContentFile(String fileName){
        File extStore = Environment.getExternalStorageDirectory();
        // ==> /storage/emulated/0/note.txt
        String path = extStore.getAbsolutePath() + "/" + fileName;
        Log.i("ExternalStorageDemo", "Save to: " + path);


        File myFile = new File(path);
        if(myFile.exists()) {
            try {
                FileOutputStream fOut = new FileOutputStream(myFile);
                OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
                myOutWriter.write("");
                myOutWriter.close();
                fOut.close();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        } else return false;
    }

}

