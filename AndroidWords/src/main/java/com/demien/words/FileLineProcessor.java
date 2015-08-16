package com.demien.words;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dmitry on 24.03.15.
 */
public class FileLineProcessor {

    //static final String FILE_PATH="/home/dmitry/Developer/java_repository/AndroidWords/res/raw/words.txt";
    static final String FILE_PATH="/home/dmitry/Desktop/1000words";

    public static void main(String ars[]){
        BufferedReader br = null;
        WordTranslator translator=new WordTranslator();

        List<String> words=new ArrayList<String>();

        try {

            String sCurrentLine;

            br = new BufferedReader(new FileReader(FILE_PATH));

            String translation="";
            while ((sCurrentLine = br.readLine()) != null) {
                //System.out.println(sCurrentLine.substring(1,100));
                String[] arr=sCurrentLine.split(" ");
                for (String s:arr) {
                    int pos=s.indexOf("=");
                    if (pos>0) {
                        s=s.substring(0, s.indexOf("="));
                        //if (s.startsWith("nie")){

                        translation="";
                        try {
                            translation=translator.getTranslationAndExample(s);
                        } catch (Exception e) {
                            translation="";
                        }

                            System.out.println(s+"#"+translation);
                        //}
                    } else {
                        //System.out.println("Error with:"+s);
                    }

                    words.add(s);
                }

            }
            //System.out.println("Words count:"+words.size());

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null)br.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }




    }
}
