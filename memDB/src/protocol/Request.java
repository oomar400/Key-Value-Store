package protocol;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Represents a request sent to the server.
 */
public class Request implements Serializable {
    private Command command;
    private String key;
    private final List<String> args;

    /**
     * Constructs a request object from raw data.
     *
     * @param rawData The raw data of the request.
     */
    public Request(String rawData){
        args = new ArrayList<>();
        command = null;
        parseData(rawData);
    }

    /**
     * Gets the command of the request.
     *
     * @return The command of the request.
     */
    public Command getCommand() {
        return command;
    }

    /**
     * Gets the key of the request.
     *
     * @return The key of the request.
     */
    public String getKey(){
        return key;
    }

    /**
     * Gets the arguments of the request.
     *
     * @return The arguments of the request.
     */
    public List<String> getArgs() {
        return args;
    }

    /**
     * Parses the raw data of the request and sets the command, key, and arguments accordingly.
     *
     * @param data The raw data of the request.
     */
    private void parseData(String data){
        String[] parts = data.split("\\s+");
        // command operation eg : delete name, len names
        if(parts.length == 2){
            command = parseCommand(parts[0]);
            key = parts[1];
        } else if(parts.length >= 3) {
            command = parseCommand(parts[0]);
            key = parts[1];
            args.addAll(Arrays.asList(parts).subList(2, parts.length));
        }else {
            command = parseCommand(parts[0]);
            key = null;
        }
    }

    /**
     * Parses a command string and returns the corresponding Command enum value.
     * If the command string is not a valid command, returns null.
     *
     * @param commandString The command string to parse.
     * @return The Command enum value corresponding to the command string, or null if the command is invalid.
     */
    private Command parseCommand(String commandString) {
        try {
            return Command.valueOf(commandString.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

}
