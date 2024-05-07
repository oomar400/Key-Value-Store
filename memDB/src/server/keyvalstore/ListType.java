package server.keyvalstore;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * Represents a list type that can be stored in the key-value store.
 */
public class ListType implements Type, Serializable {
    private final List<StringType> list;
    private Long expTime;

    /**
     * Default constructor.
     * Initializes the list and sets the expiry time to -1 (never expires).
     */
    public ListType(){
        list = new LinkedList<>();
        expTime = -1L;
    }

    /**
     * Adds a new element to the beginning of the list.
     *
     * @param value The value to be added to the list.
     */
    public void lPush(String value){
        list.addFirst(new StringType(value));
    }

    /**
     * Adds a new element to the end of the list.
     *
     * @param value The value to be added to the list.
     */
    public void rPush(String value){
        list.addLast(new StringType(value));
    }

    /**
     * Removes and returns the last element of the list.
     *
     * @return The last element of the list, or "null" if the list is empty.
     */
    public String pop(){
        if(!list.isEmpty()){
            return list.removeLast().get();
        }
        return "null";
    }

    /**
     * Returns the number of elements in the list.
     *
     * @return The number of elements in the list.
     */
    public int len(){
        return list.size();
    }

    /**
     * Returns a string representation of the list.
     *
     * @return A string representation of the list.
     */
    @Override
    public String get(){
        StringBuilder sb = new StringBuilder();
        if(list.isEmpty())
            return "null";
        for(StringType str : list){
            sb.append(str.get()).append(" ");
        }
        return sb.toString();
    }

    /**
     * Sets the expiry time for the list.
     * Schedules an expiration check task.
     *
     * @param time The expiry time in milliseconds.
     */
    @Override
    public void setExpiry(Long time) {
        expTime = time;
        scheduleExpirationCheck();
    }

    @Override
    public void increment() {

    }

    @Override
    public void decrement() {

    }

    /**
     * Schedules a task to check if the list has expired.
     * If expired, clears the list.
     */
    private void scheduleExpirationCheck() {
        new Thread(() -> {
            while (System.currentTimeMillis() < expTime) {
                try {
                    Thread.sleep(expTime - System.currentTimeMillis());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException(e);
                }
            }
            this.list.clear();
        }).start();
    }
}
