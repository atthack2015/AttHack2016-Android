
package att.atthack2016.io;

import att.atthack2016.models.StatusResponse;
import att.atthack2016.models.ValueBody;
import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.Headers;
import retrofit.http.PUT;

/**
 * Created by Edgar Salvador Maurilio on 03/01/2016.
 */
public interface ApiServiceM2X {

    @Headers(Constants.HEADER_KEY)
    @PUT(Constants.PATH_LONGITUDE)
    public void sendLongitude(@Body ValueBody valueBody, Callback<StatusResponse> server);

    @Headers(Constants.HEADER_KEY)
    @PUT(Constants.PATH_LATITUDE)
    public void sendLatitude(@Body ValueBody valueBody, Callback<StatusResponse> server);

    @Headers(Constants.HEADER_KEY)
    @PUT(Constants.PATH_ID)
    public void sendIdBus(@Body ValueBody valueBody, Callback<StatusResponse> server);

    @Headers(Constants.HEADER_KEY)
    @PUT(Constants.PATH_SPEED)
    public void sendSpeed(@Body ValueBody valueBody, Callback<StatusResponse> server);


}
