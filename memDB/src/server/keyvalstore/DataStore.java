package server.keyvalstore;

import utils.ObjectIO;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a key-value store that supports different types of values.
 */
public class DataStore {
    /**
     * The underlying map to store key-value pairs.
     */
    public Map<String, Type> map = new HashMap<>();

    /**
     * Puts a string value into the store with the specified key.
     *
     * @param key The key of the value.
     * @param value The string value to put.
     */
    public void put(String key, String value){
        StringType t = new StringType(value);
        map.put(key, t);
    }

    /**
     * Puts a string value into the store with the specified key and expiry time.
     *
     * @param key The key of the value.
     * @param value The string value to put.
     * @param time The expiry time in milliseconds.
     */
    public void put(String key, String value, Long time){
        StringType t = new StringType(value);
        t.setExpiry(time);
        map.put(key, t);
    }

    /**
     * Gets the string value associated with the specified key.
     *
     * @param key The key of the value to get.
     * @return The string value, or "null" if the key does not exist.
     */
    public String get(String key){
        if(map.get(key) != null)
            return map.get(key).get();
        return "null";
    }

    /**
     * Deletes the value associated with the specified key.
     *
     * @param key The key of the value to delete.
     */
    public void delete(String key){
        if(map.get(key) != null){
            map.remove(key);
        }
    }

    /**
     * Increments the value associated with the specified key if it is a number.
     *
     * @param key The key of the value to increment.
     */
    public void increment(String key){
        if(map.get(key) != null){
            map.get(key).increment();
        }
    }

    /**
     * Decrements the value associated with the specified key if it is a number.
     *
     * @param key The key of the value to decrement.
     */
    public void decrement(String key){
        if(map.get(key) != null) {
            map.get(key).decrement();
        }
    }

    /**
     * Gets the length of the list associated with the specified key.
     *
     * @param key The key of the list.
     * @return The length of the list, or -1 if the key does not exist.
     */
    public Integer lLen(String key){
        if(map.get(key) != null){
            ListType list = (ListType) map.get(key);
            return list.len();
        }
        return -1;
    }

    /**
     * Pushes a value to the beginning of the list associated with the specified key.
     *
     * @param key   The key of the list.
     * @param value The value to push.
     */
    public void lPush(String key, String value){
        Type list = map.get(key);
        if(list == null){
            ListType t = new ListType();
            t.lPush(value);
            map.put(key, t);
        }else{
            ((ListType) list).lPush(value);
        }
    }

    /**
     * Pops a value from the end of the list associated with the specified key.
     *
     * @param key The key of the list.
     * @return The popped value, or "null" if the list is empty or the key does not exist.
     */
    public String pop(String key){
        if(map.get(key) != null){
            ListType t = (ListType) map.get(key);
            return t.pop();
        }
        return "null";
    }

    /**
     * Gets the keys in the store.
     *
     * @return A string representation of the keys.
     */
    public String getKeys(){
        return map.keySet().toString();
    }

    /**
     * Loads data from a file into the store.
     *
     * @param fileName The name of the file to load from.
     */
    public void loadDataStore(String fileName){
        ObjectIO objectIO = new ObjectIO();
        map = objectIO.readObjectFromFile(fileName);
    }

    /**
     * Saves data from the store to a file.
     *
     * @param fileName The name of the file to save to.
     */
    public void saveDataStore(String fileName){
        ObjectIO objectIO = new ObjectIO();
        objectIO.writeObjectToFile(map, fileName);
    }
}
