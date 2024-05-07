package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * A server that listens for incoming connections and handles them using client handlers.
 */
public class Server {
    private final ServerSocket server;

    /**
     * Constructs a server that listens on the specified port.
     *
     * @param port The port to listen on.
     */
    public Server(int port) {
        try {
            server = new ServerSocket(port);
            System.out.println("Server started " + server.getLocalSocketAddress());
        } catch (IOException e) {
            System.out.println("Failed to start server");
            e.printStackTrace();
        }
    }

    /**
     * Starts the server, accepting incoming connections and creating client handler threads.
     */
    public void start() {
        try {
            while (!server.isClosed()) {
                Socket conn = server.accept();
                ClientHandler client = new ClientHandler(conn);
                Thread clientThread = new Thread(client);
                clientThread.start();
                System.out.println("Connection from :  " + conn.getInetAddress().toString() + " On Port : " + conn.getPort());
            }
        } catch (IOException e) {
            e.printStackTrace();
            stop();
        }
    }

    /**
     * Stops the server, closing the server socket.
     */
    public void stop() {
        try {
            if (server != null) {
                server.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Main method to start the server on port 5001.
     *
     * @param args The command-line arguments.
     * @throws IOException If an I/O error occurs.
     */
    public static void main(String[] args) throws IOException {
        int port = Integer.parseInt(args[0]);
        Server server = new Server(port);
        server.start();
    }
}
