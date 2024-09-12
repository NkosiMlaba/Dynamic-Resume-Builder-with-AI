package za.co.theemlaba.domain.response;

import java.util.HashMap;


public class ApiResponse extends Response {
    
    /**
     * Constructs a new ApiResponse object with default status "OK" and an empty data map.
     */
    public ApiResponse() {
        super("OK", new HashMap<>());
    }

    /**
     * Constructs a new ApiResponse object with default status "OK" and the provided data map.
     *
     * @param data A HashMap containing the response data.
     */
    public ApiResponse(HashMap data) {
        super("OK", data);
    }

    @Override
    public String toString() {
        return "StandardResponse{" + "\n" +
                "result=" + getResult() + "\n" +
                "data=" + getData() + "\n" +
                '}';
    }
}
