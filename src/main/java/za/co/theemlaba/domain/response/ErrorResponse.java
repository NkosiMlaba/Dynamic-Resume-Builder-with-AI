package za.co.theemlaba.domain.response;

import java.util.HashMap;

/**
 * Represents an error response object with an "ERROR" status and a message.
 */
public class ErrorResponse extends Response {

    /**
     * Constructs an ErrorResponse object with the specified message.
     *
     * @param message The message associated with the error.
     */
    public ErrorResponse(String message) {
        super("ERROR", new HashMap<>() {{ put("message", message); }});
    }
}
