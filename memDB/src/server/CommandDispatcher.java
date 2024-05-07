package server;

import server.keyvalstore.DataStore;
import protocol.Command;
import protocol.Request;
import protocol.Response;
import utils.ObjectMessenger;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.SocketException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Dispatches incoming commands to their respective handlers.
 */
public class CommandDispatcher {
    private final Map<Command, CommandHandler> commandMap = new HashMap<>();
    private final ObjectMessenger messenger;
    private final ObjectOutputStream objectOutputStream;
    private final DataStore store;

    /**
     * Constructs a command dispatcher with the given dependencies.
     *
     * @param messenger The object messenger for sending responses.
     * @param objectInputStream The object input stream for receiving requests.
     * @param objectOutputStream The object output stream for sending responses.
     * @param store The data store for storing key-value pairs.
     */

    public CommandDispatcher(ObjectMessenger messenger, ObjectInputStream objectInputStream, ObjectOutputStream objectOutputStream, DataStore store) {
        this.messenger = messenger;
        this.objectOutputStream = objectOutputStream;
        this.store = store;
        initCommandMap();
    }

    private void initCommandMap(){
          commandMap.put(Command.ECHO, this::handleEcho);
          commandMap.put(Command.PING, this::handlePing);
          commandMap.put(Command.SET, this::handleSet);
          commandMap.put(Command.GET, this::handleGet);
          commandMap.put(Command.DELETE, this::handleDelete);
          commandMap.put(Command.SETX, this::handleSetx);
          commandMap.put(Command.LPUSH, this::handleLpush);
          commandMap.put(Command.POP, this::handlePop);
          commandMap.put(Command.LLEN, this::handleLlen);
          commandMap.put(Command.INCREMENT, this::handleIncrement);
          commandMap.put(Command.DECREMENT, this::handleDecrement);
          commandMap.put(Command.SAVE, this::handleSave);
          commandMap.put(Command.LOAD, this::handleLoad);
    }

    /**
     * Handles the LOAD command to load data from a file into the data store.
     *
     * @param request The request containing the LOAD command and file name.
     * @return The response indicating success or failure.
     */
    private Response handleLoad(Request request) {
        if(request != null){
            Command command = request.getCommand();
            String key = request.getKey();
            if(command == Command.LOAD && key != null){
                ObjectIO objectIO = new ObjectIO();
                store.loadDataStore(key);
                return new Response(true, store.getKeys());
            }
        }
        return new Response(false, "Failed to load keys");
    }

    /**
     * Handles the SAVE command to save data from the data store into a file.
     *
     * @param request The request containing the SAVE command and file name.
     * @return The response indicating success or failure.
     */
    private Response handleSave(Request request){
        Command command = request.getCommand();
        String key = request.getKey();
        if(command == Command.SAVE && key != null){
            ObjectIO objectIO = new ObjectIO();
            store.saveDataStore(key);
            return new Response(true, "Saved : " + store.getKeys());
        }
        return new Response(false, "Invalid command or key");
    }

    private Response handleLlen(Request request) {
        if(request != null){
            Command command = request.getCommand();
            String key = request.getKey();
            if(command == Command.LLEN && key != null){
                return new Response(store.lLen(key) > 1, store.lLen(key).toString());
            }
        }
        return new Response(false, "Invalid command or key");
    }

    private Response handlePop(Request request) {
        if(request != null){
            Command command = request.getCommand();
            String key = request.getKey();
            String val = request.getArgs().getFirst();
            if(command == Command.POP && key != null && val != null){
                return new Response(!store.get(key).equals("null"), store.pop(key));
            }
        }
        return new Response(false, "Invalid command or key");
    }

    private Response handleLpush(Request request) {
        if(request != null){
            Command command = request.getCommand();
            String key = request.getKey();
            String val = request.getArgs().getFirst();
            if(command == command.LPUSH && key != null && val != null){
                store.lPush(key, val);
                return new Response(true, store.get(key));

            }
        }
        return new Response(false, "Invalid command or key");
    }

    private Response handleDelete(Request request) {
        if(request != null){
            Command command = request.getCommand();
            String key = request.getKey();
            if(command == Command.DELETE && key != null) {
                store.delete(key);
                return new Response(true, store.get(key));
            }
        }
        return new Response(false, "Invalid command or key");
    }

    private Response handleSetx(Request request) {
        if(request != null){
            Command command = request.getCommand();
            String key = request.getKey();
            String val = request.getArgs().getFirst();
            Long time = Long.parseLong(request.getArgs().get(1));
            if(command == Command.SETX && key != null && val != null){
                store.put(key, val, time);
                return new Response(true, store.get(key));
            }
        }
        return new Response(false, "Invalid command or key");
    }

    private Response handleGet(Request request) {
        if(request != null){
            Command command = request.getCommand();
            String key = request.getKey();
            if(command == Command.GET && key != null){
                String val = store.get(key);
                return new Response(!val.equals("null"), store.get(key));
            }
        }
        return new Response(false, "Invalid command or key");
    }

    private Response handleSet(Request request) {
        if (request != null) {
            System.out.println("HANDLING SET " + request.toString());
            Command command = request.getCommand();
            String key = request.getKey();
            List<String> args = request.getArgs();

            if (command == Command.SET && !args.isEmpty()) {
                String val = args.getFirst();
                if (key != null && val != null) {
                    store.put(key, val);
                    return new Response(true, store.get(key));
                }
            }
        }
        return new Response(false, "Invalid command or value");
    }

    private Response handleEcho(Request request) {
        return new Response(true, "echo");
    }

    private Response handlePing(Request request) {
        return new Response(true, "pong");
    }

    private Response handleIncrement(Request request){
        if(request != null){
            Command command = request.getCommand();
            String key = request.getKey();
            if(command == Command.INCREMENT && key != null){
                store.increment(key);
                return new Response(true, store.get(key));
            }
        }
        return new Response(true, "Invalid command or value");
    }
    private Response handleDecrement(Request request){
        if(request != null){
            Command command = request.getCommand();
            String key = request.getKey();
            if(command == Command.DECREMENT && key != null){
                store.decrement(key);
                return new Response(true, store.get(key));
            }
        }
        return new Response(false, "Invalid command or value");
    }

    /**
     * Dispatches a request to the appropriate command handler and sends the response.
     *
     * @param request The request to dispatch.
     * @throws IOException If an I/O error occurs.
     */
    public void dispatch(Request request) throws IOException {
        Command command = request.getCommand();
        CommandHandler handler = commandMap.get(command);
        Response resp = null;
        if(handler != null){
            resp = handler.handle(request);
        }else {
            resp = handleInvalidCommand();
        }
        messenger.sendObject(resp, objectOutputStream);
    }

    private Response handleInvalidCommand() {
       return new Response(false, "Invalid command");
    }
}
