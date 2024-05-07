package server.keyvalstore;

/**
 * Interface representing a type that can be stored in the key-value store.
 */
public interface Type {
    /**
     * Gets the value of the type.
     *
     * @return The value of the type.
     */
    String get();

    /**
     * Sets the expiry time for the type.
     *
     * @param time The expiry time in milliseconds.
     */
    void setExpiry(Long time);

    /**
     * Increments the value of the type if it is a number.
     */
    void increment();

    /**
     * Decrements the value of the type if it is a number.
     */
    void decrement();
}
