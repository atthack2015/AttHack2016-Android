package att.atthack2016.models;

import java.util.List;

/**
 * Created by Edgar Salvador Maurilio on 03/01/2016.
 */
public class StationModelResponse {

    private List<StationModel> results;

    public StationModelResponse(List<StationModel> results) {
        this.results = results;
    }

    public List<StationModel> getResults() {
        return results;
    }

    public void setResults(List<StationModel> results) {
        this.results = results;
    }
}
