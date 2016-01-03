package att.atthack2016.io;

import java.util.List;

import att.atthack2016.models.StationModel;
import att.atthack2016.models.StationModelResponse;
import retrofit.Callback;
import retrofit.http.GET;

/**
 * Created by Edgar Salvador Maurilio on 03/01/2016.
 */
public interface ApiServiceStations {

    @GET(Constants.PATH_SEARCH)
    public void getStations (Callback<StationModelResponse> serverResponse);




}
