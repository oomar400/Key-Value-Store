package server;

import protocol.Request;
import protocol.Response;

/**
 * Interface for handling commands received by the server.
 */
public interface CommandHandler {
    /**
     * Handles a request and returns a response.
     *
     * @param request The request to handle.
     * @return The response to the request.
     */
    Response handle(Request request);
}
