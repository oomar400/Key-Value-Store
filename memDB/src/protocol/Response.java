package protocol;

import java.io.Serializable;

/**
 * Represents a response from the server.
 */
public class Response implements Serializable {
    private boolean success;
    private String response;

    /**
     * Constructs a response with the given success status and response message.
     *
     * @param success Whether the operation was successful.
     * @param response The response message.
     */
    public Response(boolean success, String response) {
        this.success = success;
        this.response = response;
    }

    /**
     * Checks if the operation was successful.
     *
     * @return True if the operation was successful, false otherwise.
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * Sets the success status of the response.
     *
     * @param success The success status to set.
     */
    public void setSuccess(boolean success) {
        this.success = success;
    }

    /**
     * Gets the response message.
     *
     * @return The response message.
     */
    public String getResponse() {
        return response;
    }

    /**
     * Sets the response message.
     *
     * @param response The response message to set.
     */
    public void setResponse(String response) {
        this.response = response;
    }

    /**
     * Returns a string representation of the response.
     *
     * @return A string representation of the response.
     */
    @Override
    public String toString() {
        return "Response [success=" + success + ", response=" + response + "]";
    }
}
