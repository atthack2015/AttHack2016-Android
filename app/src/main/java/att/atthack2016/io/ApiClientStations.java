package att.atthack2016.io;

import retrofit.RestAdapter;

/**
 * Created by Edgar Salvador Maurilio on 03/01/2016.
 */
public class ApiClientStations {

    private static ApiServiceStations API_SERVICE;

    public static ApiServiceStations getApiService() {

        if (API_SERVICE == null) {

            RestAdapter adapter = new RestAdapter.Builder()
                    .setEndpoint(Constants.BASE_URL_STATIONS)
                    .setLogLevel(RestAdapter.LogLevel.FULL)
                    .build();

            API_SERVICE = adapter.create(ApiServiceStations.class);
        }

        return API_SERVICE;

    }
}
