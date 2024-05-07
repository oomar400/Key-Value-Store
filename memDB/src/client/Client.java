package client;

import utils.ObjectMessenger;
import protocol.Request;
import protocol.Response;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

/**
 * Represents a client that communicates with a server using sockets.
 */
public class Client {
    private Socket client;
    private Semaphore semaphore;

    private ObjectMessenger messenger;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;

    /**
     * Constructs a client with the given socket.
     *
     * @param client The socket representing the client connection.
     */
    public Client(Socket client) {
        try {
            this.client = client;
            messenger = new ObjectMessenger();
            objectOutputStream = new ObjectOutputStream(client.getOutputStream());
            objectInputStream = new ObjectInputStream(client.getInputStream());
            semaphore = new Semaphore(1);
        } catch (Exception e) {
            close();
        }
    }

    /**
     * Handles user commands and sends them to the server.
     */
    public void commandHandler() {
        try {
            Scanner scanner = new Scanner(System.in);
            while (client.isConnected()) {
                semaphore.acquire();
                System.out.print(">>> ");
                String command = scanner.nextLine();
                Request request = new Request(command);
                messenger.sendObject(request, objectOutputStream);
            }
        } catch (InterruptedException | IOException e) {
            close();
        }
    }

    /**
     * Handles responses from the server.
     */
    public void respHandler(){
        new Thread(() -> {
            while (client.isConnected()) {
                try {
                    Response resp = (Response) messenger.receiveObject(objectInputStream);
                    if (resp != null) {
                        System.out.println(resp);
                        semaphore.release();
                    }
                } catch (IOException | ClassNotFoundException e) {
                    close();
                }
            }
        }).start();
    }

    /**
     * Closes the client and releases resources.
     */
    private void close() {
        try {
            if(objectInputStream != null)
                objectInputStream.close();
            if(objectOutputStream != null)
                objectOutputStream.close();
            if(semaphore != null)
                semaphore.release();
            if (client != null)
                client.close();
        } catch (IOException e) {
            throw new RuntimeException("Failed to close resources", e);
        }
    }

    public static void main(String[] args) {
        String host = args[0];
        int port = Integer.parseInt(args[1]);
        try {
            Socket socket = new Socket(host, port);
            Client client = new Client(socket);
            client.respHandler();
            client.commandHandler();
        }catch (IOException e){
            System.out.println("Failed to connect to " + host + ":" + port);
        }
    }
}
