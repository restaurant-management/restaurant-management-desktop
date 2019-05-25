package dao.exceptions;

import org.json.JSONException;
import org.json.JSONObject;

public class RequestFailException extends Exception {
    private int code;
    private String message;

    public RequestFailException(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        try {
            JSONObject json = new JSONObject(message);
            return json.getString("message");
        } catch (JSONException e) {
            return "Request failed " + code;
        }
    }
}
