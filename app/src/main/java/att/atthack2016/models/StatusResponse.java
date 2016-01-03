package att.atthack2016.models;

import java.util.logging.StreamHandler;

/**
 * Created by Edgar Salvador Maurilio on 03/01/2016.
 */
public class StatusResponse {

    private String status;

    public StatusResponse(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
