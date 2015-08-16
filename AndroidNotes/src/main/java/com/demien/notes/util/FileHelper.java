package com.demien.notes.util;

import android.content.Context;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dmitry on 19.11.14.
 */
public class FileHelper<T extends Serializable> {


    public void saveObjectListToFile(Context context, String fileName, List<T> list, Class<?> cl) throws IOException {
        //FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
        File file=new File(fileName);
        FileOutputStream fos = new FileOutputStream(file);
        ObjectOutputStream os = new ObjectOutputStream(fos);
        for (T object:list) {
            os.writeObject(object);
        }
        os.close();
    }


    public List<T> loadListFromFile(Context context, String fileName, Class<?> cl) throws IOException, ClassNotFoundException, JAXBException {
        File file=new File(fileName);
        FileInputStream fis = new FileInputStream(file);
        ObjectInputStream is = new ObjectInputStream(fis);
        List<T> result=new ArrayList<T>();
        T object=null;
        do {
            try {
                object = (T) is.readObject();
            } catch (Exception e) {
                object=null;
            }
            if (object!=null) {
                result.add(object);
            }
        } while (object!=null);
             return result;
    }

}
