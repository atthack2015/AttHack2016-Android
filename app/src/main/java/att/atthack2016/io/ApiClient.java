package att.atthack2016.io;

import retrofit.RestAdapter;

/**
 * Created by Edgar Salvador Maurilio on 03/01/2016.
 */
public class ApiClient {

    private static ApiService API_SERVICE;

    public static ApiService getApiService() {

        if (API_SERVICE == null) {

            RestAdapter adapter = new RestAdapter.Builder()
                    .setEndpoint(Constants.BASE_URL)
                    .setLogLevel(RestAdapter.LogLevel.FULL)
                    .build();

            API_SERVICE = adapter.create(ApiService.class);
        }

        return API_SERVICE;

    }
}
