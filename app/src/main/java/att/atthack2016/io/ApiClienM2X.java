package att.atthack2016.io;

import retrofit.RestAdapter;

/**
 * Created by Edgar Salvador Maurilio on 03/01/2016.
 */
public class ApiClienM2X {


    private static ApiServiceM2X API_SERVICE;

    public static ApiServiceM2X getApiService() {

        if (API_SERVICE == null) {

            RestAdapter adapter = new RestAdapter.Builder()
                    .setEndpoint(Constants.BASE_URL_M2X)
                    .setLogLevel(RestAdapter.LogLevel.FULL)
                    .build();

            API_SERVICE = adapter.create(ApiServiceM2X.class);
        }

        return API_SERVICE;

    }
}
