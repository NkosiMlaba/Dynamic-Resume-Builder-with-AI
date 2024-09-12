package za.co.theemlaba.domain.response;

import java.util.HashMap;

/**
 * Represents a generic response object with a result and data.
 */
public abstract class Response {

    private String result;
    private HashMap data;


    /**
     * Constructs a Response object with the specified result and data.
     *
     * @param result The result of the response.
     * @param data   The data associated with the response.
     */
    public Response(String result, HashMap data) {
        this.result = result;
        this.data = data;
    }

    /**
     * Returns the result of the response.
     *
     * @return The result of the response.
     */
    public String getResult() {
        return result;
    }

    /**
     * Returns the data associated with the response.
     *
     * @return The data associated with the response.
     */
    public HashMap getData() {
        return data;
    }

    /**
     * Returns a string representation of the Response object.
     *
     * @return A string representation of the Response object.
     */
    @Override
    public String toString() {
        return "StandardResponse{" +
                "result='" + result + '\'' +
                ", data=" + data +
                '}';
    }
}
