package server;

import server.keyvalstore.DataStore;
import utils.ObjectMessenger;
import protocol.Request;

import java.io.*;
import java.net.Socket;

/**
 * Represents a client handler that processes requests from a client.
 */
public class ClientHandler implements Runnable{
    private Socket client;
    private ObjectMessenger messenger;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    private CommandDispatcher commandDispatcher;

    /**
     * Constructs a client handler with the given client socket.
     *
     * @param client The client socket.
     */
    public ClientHandler(Socket client)  {
        try{
            this.client = client;
            DataStore store = new DataStore();
            messenger = new ObjectMessenger();
            objectInputStream = new ObjectInputStream(client.getInputStream());
            objectOutputStream = new ObjectOutputStream(client.getOutputStream());
            commandDispatcher = new CommandDispatcher(messenger, objectOutputStream, store);
        }catch (IOException e){
            closeClient();
        }
    }

    /**
     * Continuously listens for requests from the client and dispatches them for processing.
     */
    @Override
    public void run() {
        while (client.isConnected()) {
            try {
                Request request = (Request) messenger.receiveObject(objectInputStream);
                commandDispatcher.dispatch(request);
            } catch (IOException | ClassNotFoundException e) {
                closeClient();
            }
        }
    }

    /**
     * Closes the client connection and releases resources.
     */
    private void closeClient()  {
        try{
            if(objectInputStream != null)
                objectInputStream.close();
            if(objectOutputStream != null)
                objectOutputStream.close();
            if(client != null)
                client.close();
        }catch (IOException e){
            throw new RuntimeException("Error closing connection", e);
        }
    }

}
