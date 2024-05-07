package utils;

import java.io.*;

/**
 * Utility class for sending and receiving objects over streams.
 */
public class ObjectMessenger {
    /**
     * Sends an object over an ObjectOutputStream.
     *
     * @param obj The object to send.
     * @param out The ObjectOutputStream to use for sending.
     * @throws IOException If an I/O error occurs while sending the object.
     */
    public void sendObject(Object obj, ObjectOutputStream out) throws IOException {
        out.writeObject(obj);
        out.flush();
    }

    /**
     * Receives an object from an ObjectInputStream.
     *
     * @param in The ObjectInputStream to receive from.
     * @return The received object.
     * @throws IOException If an I/O error occurs while receiving the object.
     * @throws ClassNotFoundException If the class of the received object cannot be found.
     */
    public Object receiveObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        return in.readObject();
    }

}
