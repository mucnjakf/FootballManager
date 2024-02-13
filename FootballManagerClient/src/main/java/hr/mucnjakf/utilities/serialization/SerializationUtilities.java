package hr.mucnjakf.utilities.serialization;

import java.io.*;

public class SerializationUtilities {

    public static void write(Object object, File file) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))){
            oos.writeObject(object);
        }
    }

    public static Object read(File file) throws IOException, ClassNotFoundException {
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))){
            return ois.readObject();
        }
    }
}
