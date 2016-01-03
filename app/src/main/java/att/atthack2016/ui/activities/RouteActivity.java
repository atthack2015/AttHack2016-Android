package att.atthack2016.ui.activities;

import android.app.Dialog;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;

import java.util.ArrayList;
import java.util.List;

import att.atthack2016.R;
import att.atthack2016.io.ApiClienM2X;
import att.atthack2016.models.IdBusModel;
import att.atthack2016.models.StationModel;
import att.atthack2016.models.StatusResponse;
import att.atthack2016.models.ValueBody;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class RouteActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, OnMapReadyCallback {

    private static final String LOG_TAG = RouteActivity.class.getSimpleName();
    private GoogleApiClient mGoogleApiClient;
    private GoogleMap mMap;
    private List<StationModel> resultsStations;


    @Bind(R.id.imageViewStation)
    CircleImageView circleImageView;

    @Bind(R.id.textViewStation)
    TextView textViewStation;

    @Bind(R.id.textViewTime)
    TextView textViewTime;
    private Marker myMarker;
    private IdBusModel idBusModel;


    private Location concurrentLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);
        ButterKnife.bind(this);
        buildGoogleApiClient();
        setupMap();
        getIdBus();
        getStations();
        setupInfoStation();

        sendId();
    }

    private void getIdBus() {
        idBusModel = EventBus.getDefault().getStickyEvent(IdBusModel.class);
    }


    private void getStations() {
        resultsStations = EventBus.getDefault().getStickyEvent(ArrayList.class);
    }

    private void setupInfoStation() {
        StationModel stationModel = EventBus.getDefault().getStickyEvent(StationModel.class);
        textViewStation.setText(stationModel.getName());
        textViewTime.setText("3:35 PM");
    }


    private void setupMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
//        mMap.setMyLocationEnabled(true);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(20.654089, -103.391776), 15));
        displayStations();
    }


    public void displayStations() {

        PolygonOptions polygonOptions = new PolygonOptions();

        for (StationModel stationModel : resultsStations) {
            mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.marker_station))
                    .title(stationModel.getName())
                    .snippet(stationModel.getName())
                    .position(new LatLng(stationModel.getLatitude(), stationModel.getLongitude())));


            polygonOptions.add(new LatLng(stationModel.getLatitude(), stationModel.getLongitude()));

        }
        polygonOptions.fillColor(getResources().getColor(R.color.colorAccent));

//        mMap.addPolyline(new PolylineOptions().ad);
    }

    @Override
    public void onResume() {
        super.onResume();
        connectGoogleApiClient();
    }


    private void connectGoogleApiClient() {
        if (!mGoogleApiClient.isConnected() && !mGoogleApiClient.isConnecting()) {
            Log.d(LOG_TAG, "connectGoogleApiClient");
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disconnectGoogleApiClient();
    }

    private void disconnectGoogleApiClient() {

        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d(LOG_TAG, "onConnected");

        startLocationUpdates();
    }

    private void startLocationUpdates() {
        LocationServices.FusedLocationApi
                .requestLocationUpdates(mGoogleApiClient, makeLocationRequest(), this);
    }

    protected LocationRequest makeLocationRequest() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(0);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return mLocationRequest;
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(LOG_TAG, "onConnectionSuspended");
        mGoogleApiClient.connect();
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(LOG_TAG, "onConnectionFailed");

        tryResolvedConnectionGPS(connectionResult);
    }


    private void chageMarker(Location location) {
        if (myMarker != null) {
            myMarker.remove();
        }

        myMarker = mMap.addMarker(new MarkerOptions().title("Salvador").snippet("12:35").position(new LatLng(location.getLatitude(), location.getLongitude()))
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.bus)));
    }

    private void tryResolvedConnectionGPS(ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(this, 1213);
            } catch (IntentSender.SendIntentException e) {
                mGoogleApiClient.connect();
            }
        } else {
            showDialogErrorPlayServices(connectionResult.getErrorCode());
        }
    }

    private void showDialogErrorPlayServices(int errorCode) {
        Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(this, errorCode,
                1213);
        dialog.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1213 && resultCode == RESULT_OK) {
            connectGoogleApiClient();
        } else {
            finish();
        }
    }

    @OnClick(R.id.floatingButtonFinishRoute)
    public void finishRoute() {
        Toast.makeText(RouteActivity.this, "Gracias!!!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(RouteActivity.this, HomeActivity.class);
        startActivity(intent);
    }


    @Override
    public void onLocationChanged(Location location) {

        chageMarker(location);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 15));


        sendLatitude(location);
        sendLongitude(location);

        if (concurrentLocation != null) {
            sendSpeed(location);
        }
        concurrentLocation = location;


    }


    private void sendId() {
        ApiClienM2X.getApiService().sendIdBus(new ValueBody(idBusModel.getUid()), new Callback<StatusResponse>() {
            @Override
            public void success(StatusResponse statusResponse, Response response) {
                Log.d("success Idbus", statusResponse.getStatus());
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("error IdBus ", error.getMessage());

            }
        });
    }

    private void sendLatitude(Location location) {
        ApiClienM2X.getApiService().sendLatitude(new ValueBody(String.valueOf(location.getLatitude())), new Callback<StatusResponse>() {
            @Override
            public void success(StatusResponse statusResponse, Response response) {
                Log.d("success Idbus", statusResponse.getStatus());

            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("error IdBus ", error.getMessage());

            }
        });
    }

    private void sendLongitude(Location location) {
        ApiClienM2X.getApiService().sendLongitude(new ValueBody(String.valueOf(location.getLongitude())), new Callback<StatusResponse>() {
            @Override
            public void success(StatusResponse statusResponse, Response response) {
                Log.d("success Idbus", statusResponse.getStatus());
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("error IdBus ", error.getMessage());

            }
        });
    }


    private void sendSpeed(Location lastLocation) {


        float distance[] = new float[2];

        Location.distanceBetween(lastLocation.getLatitude(), lastLocation.getLongitude()
                , concurrentLocation.getLatitude(), concurrentLocation.getLongitude(),
                distance);


        float speed = distance[0] / (lastLocation.getTime() - concurrentLocation.getTime());

        Log.d("SPEED", String.valueOf(speed));

        ApiClienM2X.getApiService().sendSpeed(new ValueBody(String.valueOf(speed)),
                new Callback<StatusResponse>() {
                    @Override
                    public void success(StatusResponse statusResponse, Response response) {
                        Log.d("success Speed", statusResponse.getStatus());
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.d("error Speed ", error.getMessage());

                    }
                });
    }


}
