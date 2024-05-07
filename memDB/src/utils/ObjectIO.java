package utils;

import server.keyvalstore.Type;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

/**
 * Utility class for reading and writing objects to files.
 */
public class ObjectIO {
    private final String dataPath = "./data";

    /**
     * Constructor that creates the data directory if it does not exist.
     */
    public ObjectIO(){
        Path path = Paths.get(dataPath);
        try{
            Files.createDirectories(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Writes a map of key-value pairs to a file.
     *
     * @param dataStore The map to write to the file.
     * @param fileName The name of the file to write to (without the extension).
     */
    public void writeObjectToFile(Map<String, Type> dataStore, String fileName) {
        String filePath = Paths.get(dataPath, fileName.concat(".ser")).toString();
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filePath))) {
            out.writeObject(dataStore);
        } catch (IOException e) {
            throw new RuntimeException("File path not found for" + dataPath + fileName + "\n" + e);
        }
    }

    /**
     * Reads a map of key-value pairs from a file.
     *
     * @param fileName The name of the file to read from (without the extension).
     * @return The map of key-value pairs read from the file.
     */
    public Map<String, Type> readObjectFromFile(String fileName) {
        String filePath = Paths.get(dataPath, fileName.concat(".ser")).toString();
        Object obj;
        try(ObjectInputStream input = new ObjectInputStream(new FileInputStream(filePath))){
            obj = input.readObject();
        }catch (IOException | ClassNotFoundException e){
            throw new RuntimeException("File path not found for" + dataPath + fileName + "\n" + e);
        }
        return (Map<String, Type>)obj;
    }
}
