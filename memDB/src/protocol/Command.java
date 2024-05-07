package protocol;

/**
 * Enum representing the different commands supported by the server.
 */
public enum Command {
    ECHO,       // Echo the message back
    PING,       // Ping the server
    SET,        // Set a key-value pair
    SETX,       // Set a key-value pair with expiry
    GET,        // Get the value associated with a key
    DELETE,     // Delete a key-value pair
    INCREMENT,  // Increment the value associated with a key
    DECREMENT,  // Decrement the value associated with a key
    LPUSH,      // Push a value to the beginning of a list
    RPUSH,      // Push a value to the end of a list
    POP,        // Pop a value from the end of a list
    LLEN,       // Get the length of a list
    SAVE,       // Save the data store to a file
    LOAD        // Load the data store from a file
}
