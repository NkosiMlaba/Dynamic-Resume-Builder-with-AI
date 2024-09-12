package za.co.theemlaba.domain.response;

import java.util.HashMap;


/**
 * Represents a basic response object with an "OK" status and a message.
 */
public class BasicResponse extends Response {

    /**
     * Constructs a BasicResponse object with the specified message.
     *
     * @param message The message associated with the response.
     */
    public BasicResponse(String message) {
        super("OK", new HashMap<>() {{ put("message", message); }});
    }
}
