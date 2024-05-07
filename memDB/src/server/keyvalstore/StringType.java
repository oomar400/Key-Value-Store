package server.keyvalstore;

import java.io.Serializable;

/**
 * Represents a string type that can be stored in the key-value store.
 */
public class StringType implements Type, Serializable {
    private String value;
    private Long expTime;

    /**
     * Constructor with initial value.
     * Sets the expiry time to -1 (never expires).
     *
     * @param value The initial value of the string.
     */
    public StringType(String value){
        this.value = value;
        expTime = -1L;
    }

    /**
     * Appends a string to the current value.
     *
     * @param append The string to append.
     */
    public void append(String append){
        value = value + append;
    }

    /**
     * Increments the value if it is a number.
     * If the value is not a number, prints a message to the console.
     */
    public void increment(){
        try{
            int intVal = Integer.parseInt(value);
            value = Integer.toString(intVal + 1);
        }catch(NumberFormatException e){
            try{
                float floatVal = Float.parseFloat(value);
                value = Float.toString(floatVal + 1);
            }catch (NumberFormatException ee) {
                System.out.println("Value is not a number");
            }
        }
    }

    /**
     * Decrements the value if it is a number.
     * If the value is not a number, prints a message to the console.
     */
    public void decrement(){
        try{
            int intVal = Integer.parseInt(value);
            value = Integer.toString(intVal - 1);
        }catch(NumberFormatException e){
            try{
                float floatVal = Float.parseFloat(value);
                value = Float.toString(floatVal - 1);
            }catch (NumberFormatException ee) {
                System.out.println("Value is not a number");
            }
        }
    }

    /**
     * Gets the current value of the string.
     *
     * @return The current value of the string.
     */
    @Override
    public String get() {
        return value;
    }

    /**
     * Sets the expiry time for the string.
     * Schedules an expiration check task.
     *
     * @param time The expiry time in milliseconds.
     */
    @Override
    public void setExpiry(Long time) {
        expTime = System.currentTimeMillis() + time;
        scheduleExpirationCheck();
    }

    /**
     * Schedules a task to check if the string has expired.
     * If expired, sets the value to "null".
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
            this.value = "null";
        }).start();
    }
}
